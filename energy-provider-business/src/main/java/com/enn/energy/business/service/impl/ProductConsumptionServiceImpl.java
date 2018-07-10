package com.enn.energy.business.service.impl;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.enn.constant.StatusCode;
import com.enn.energy.business.dao.*;
import com.enn.energy.business.service.IProductConsumptionService;
import com.enn.energy.system.common.util.CommonConverter;
import com.enn.service.passage.ISteamFeesCalculateService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.bo.*;
import com.enn.vo.energy.business.condition.ElectricMeterReadingMinuteCondition;
import com.enn.vo.energy.business.condition.SteamMeterReadingDayCondition;
import com.enn.vo.energy.business.condition.SteamMeterReadingMinuteCondition;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.ProductDataPo;
import com.enn.vo.energy.business.po.SteamMeterReadingMinutePo;
import com.enn.vo.energy.common.enums.SteamMetricEnum;
import com.enn.vo.energy.passage.req.SteamFeesCalculateReq;
import com.enn.vo.energy.passage.resp.SteamFeesCalculateResp;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品单耗
 */
@Service
public class ProductConsumptionServiceImpl implements IProductConsumptionService {

    @Autowired
    private ProductDataMapper productDataMapper;
    @Autowired
    private ElectricMeterReadingDayPoMapper meterReadingDayPoMapper;
    @Autowired
    private SteamMeterReadingDayPoMapper steamMeterReadingDayPoMapper;
    @Autowired
    private AccountingUnitMapper accountingUnitMapper;
    @Autowired
    private ElectricMeterReadingMinutePoMapper meterReadingMinutePoMapper;
    @Autowired
    private SteamMeterReadingMinutePoMapper steamMeterReadingMinutePoMapper;
    @Autowired
    private ISteamFeesCalculateService steamFeesCalculateService;


    //根据产品id查询时间区间的数据
    @Override
    public List<ProductDataPo> findLineByProductId(String startTime, String endTime, long id) {
        return productDataMapper.findLineByProductId(startTime, endTime, id);
    }

    /**
     * 时间段内的电量和天
     */
    @Override
    public ElecMeterReadingDayStatisticsBo countElecMeterReadingDayByAssignedConditon(ElecMeterReadingDayBo elecMeterReadingDayBo) {
        ElecMeterReadingDayStatisticsBo elecMeterReadingDayStatisticsBo = meterReadingDayPoMapper.countElecMeterReadingDayByAssignedConditon(elecMeterReadingDayBo);
        return elecMeterReadingDayStatisticsBo;
    }

    /**
     * 查询指定条件下的Minute用电统计
     *
     * @param meterReadingMinuteBo
     * @return
     */
    @Override
    public ElectricMeterReadingMinuteStatisticsBo countElectricMeterReadingMinuteByAssignedConditon(
            ElectricMeterReadingMinuteBo meterReadingMinuteBo) {

        ElectricMeterReadingMinuteCondition electricMeterReadingMinuteCondition = CommonConverter.map(meterReadingMinuteBo, ElectricMeterReadingMinuteCondition.class);

        ElectricMeterReadingMinuteStatisticsBo readingMinuteStatisticsBo = meterReadingMinutePoMapper.countElectricMeterReadingMinuteByAssignedConditon(electricMeterReadingMinuteCondition);

        return readingMinuteStatisticsBo;
    }

    /**
     * 查询指定条件下的Day蒸汽统计
     *
     * @param meterReadingDayBo
     * @return
     */
    @Override
    public SteamMeterReadingDayStatisticsBo countSteamMeterReadingDayByAssignedConditon(SteamMeterReadingDayBo meterReadingDayBo) {

        SteamMeterReadingDayCondition steamMeterReadingDayCondition = CommonConverter.map(meterReadingDayBo, SteamMeterReadingDayCondition.class);

        SteamMeterReadingDayStatisticsBo steamMeterReadingDayStatisticsBo = steamMeterReadingDayPoMapper.countSteamMeterReadingDayByAssignedConditon(steamMeterReadingDayCondition);

        return steamMeterReadingDayStatisticsBo;
    }

    /**
     * 获取指定条件下的蒸汽统计信息
     *
     * @param meterReadingMinuteBo
     * @return
     */
    @Override
    public SteamMeterReadingMinuteStatisticsBo countSteamMeterReadingMinuteByAssignedConditon(
            SteamMeterReadingMinuteBo meterReadingMinuteBo) {

        SteamMeterReadingMinuteCondition steamMeterReadingMinuteCondition = CommonConverter.map(meterReadingMinuteBo, SteamMeterReadingMinuteCondition.class);
        steamMeterReadingMinuteCondition.setMetric(SteamMetricEnum.STEAM_TOTAL_FLOW.getMetric());
        SteamMeterReadingMinuteStatisticsBo meterReadingMinuteStatisticsBo = steamMeterReadingMinutePoMapper.countSteamMeterReadingMinuteByAssignedConditon(steamMeterReadingMinuteCondition);

        return meterReadingMinuteStatisticsBo;
    }

    /**
     * 获取指定条件下指定设备集合的蒸汽信息
     *
     * @param meterReadingDayBo
     * @return
     */
    @Override
    public BigDecimal querySteamMeterReadingMinuteByAssignedConditon(
            SteamMeterReadingMinuteBo meterReadingDayBo) {

        SteamMeterReadingMinuteCondition condition = CommonConverter.map(meterReadingDayBo, SteamMeterReadingMinuteCondition.class);
        condition.setMetric(SteamMetricEnum.STEAM_TOTAL_FLOW.getMetric());

        List<SteamMeterReadingMinutePo> meterReadingMinutePos = steamMeterReadingMinutePoMapper.querySteamMeterReadingMinuteByAssignedConditon(condition);

        List<SteamMeterReadingMinuteResultBo> minuteResultBos = CommonConverter.mapList(meterReadingMinutePos, SteamMeterReadingMinuteResultBo.class);

        BigDecimal equipTotalFees = BigDecimal.ZERO;

        SteamMeterReadingMinuteFeesBo minuteFeesBo = new SteamMeterReadingMinuteFeesBo();

        if (CollectionUtils.isNotEmpty(minuteResultBos)) {
            List<SteamFeesCalculateReq> calculateReqs = this.querySteamFeesByAssignedConditions(minuteResultBos);
            EnergyResp<List<SteamFeesCalculateResp>> energyResp = steamFeesCalculateService.calculateCollectionApi(calculateReqs);
            if (energyResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
                List<SteamFeesCalculateResp> calculateResps = energyResp.getData();
                if (CollectionUtils.isNotEmpty(calculateResps)) {
                    minuteFeesBo.setMeterMinuteFees(calculateResps);
                    equipTotalFees = calculateResps.stream().filter(p -> p.getFees() != null).map(m -> m.getFees()).reduce(BigDecimal.ZERO, BigDecimal::add);
                }
            }
        }

        return equipTotalFees;

    }

    /**
     * 获取指定集合的蒸汽计算费用
     *
     * @param resultBos
     * @return
     */
    private List<SteamFeesCalculateReq> querySteamFeesByAssignedConditions(List<SteamMeterReadingMinuteResultBo> resultBos) {

        List<SteamFeesCalculateReq> calculateResps = Lists.newArrayList();

        resultBos.stream().forEach(p -> {
            try {
                SteamFeesCalculateReq req = this.buildSteamFeesCalculateReq(p);

                calculateResps.add(req);
            } catch (Exception e) {
            }
        });
        return calculateResps;
    }

    /**
     * 构建蒸汽算费请求对象
     */
    private SteamFeesCalculateReq buildSteamFeesCalculateReq(SteamMeterReadingMinuteResultBo p) {
        SteamFeesCalculateReq req = new SteamFeesCalculateReq();
        req.setMeterNo(p.getMeterNo());
        req.setUseVol(p.getUseQuantity());
        req.setBeforeVol(p.getQuantity().subtract(p.getUseQuantity()));
        return req;
    }

    @Override
    public List<AccountingUnit> findLineNameByLineId(List<Long> lineIdList) {
        return accountingUnitMapper.findLineNameByLineId(lineIdList);
    }
}
