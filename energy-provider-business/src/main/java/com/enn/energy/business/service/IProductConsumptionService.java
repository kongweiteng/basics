package com.enn.energy.business.service;

import com.enn.vo.energy.business.bo.*;
import com.enn.vo.energy.business.condition.ElectricMeterReadingMinuteCondition;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.ProductDataPo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品单耗Service
 */
public interface IProductConsumptionService {

    /**
     * 根据产品id查询时间区间的数据
     *
     * @param startTime
     * @param endTime
     * @param id
     * @return
     */
    List<ProductDataPo> findLineByProductId(String startTime, String endTime, long id);

    /**
     * 时间段内的电量和 天
     */
    public ElecMeterReadingDayStatisticsBo countElecMeterReadingDayByAssignedConditon(ElecMeterReadingDayBo elecMeterReadingDayBo);

    /**
     * 查询指定条件下的Minute用电统计
     *
     * @return
     */
    public ElectricMeterReadingMinuteStatisticsBo countElectricMeterReadingMinuteByAssignedConditon(ElectricMeterReadingMinuteBo meterReadingMinuteBo);

    /**
     * 查询指定条件下的Day蒸汽统计
     *
     * @param meterReadingDayBo
     * @return
     */
    public SteamMeterReadingDayStatisticsBo countSteamMeterReadingDayByAssignedConditon(SteamMeterReadingDayBo meterReadingDayBo);

    /**
     * 查询指定条件下的Minute蒸汽统计
     * @return
     */
    public SteamMeterReadingMinuteStatisticsBo countSteamMeterReadingMinuteByAssignedConditon(SteamMeterReadingMinuteBo meterReadingMinuteBo );

    /**
     * 获取指定条件下的蒸汽统计信息
     * @param meterReadingDayBo
     * @return
     */
    public BigDecimal querySteamMeterReadingMinuteByAssignedConditon(SteamMeterReadingMinuteBo meterReadingDayBo);
    /**
     * 查询核算生产线名称
     *
     * @param lineIdList
     */
    List<AccountingUnit> findLineNameByLineId(List<Long> lineIdList);
}
