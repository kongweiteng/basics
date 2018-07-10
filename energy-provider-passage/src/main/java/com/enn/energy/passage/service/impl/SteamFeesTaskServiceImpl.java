package com.enn.energy.passage.service.impl;

import com.alibaba.fastjson.JSON;
import com.enn.energy.passage.dao.*;
import com.enn.energy.passage.service.ISteamFeesTaskService;
import com.enn.energy.passage.vo.CalculateParam;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.LadderPriceUtil;
import com.enn.vo.energy.passage.req.SteamFeesCalculateReq;
import com.enn.vo.energy.passage.resp.SteamFeesCalculateResp;
import com.enn.vo.energy.passage.vo.*;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by sl
 * User: sl
 * Date: 2018/6/14
 * Time: 上午10:57
 */
@Service
public class SteamFeesTaskServiceImpl implements ISteamFeesTaskService {

    private static Logger logger = LoggerFactory.getLogger(ISteamFeesTaskService.class);

    @Autowired
    private SteamMeterReadingHourMapper steamMeterReadingHourMapper;

    @Autowired
    private SteamInfoMapper steamInfoMapper;

    @Autowired
    private SteamLadderPriceMapper steamLadderPriceMapper;

    @Autowired
    private SteamMeterReadingDayMapper steamMeterReadingDayMapper;

    @Autowired
    private SteamMeterReadingMonthMapper steamMeterReadingMonthMapper;

    @Autowired
    private SteamMeterReadingBaseMapper steamMeterReadingBaseMapper;

    /**
     * <计算蒸汽费用--hour><功能具体实现>
     *
     * @param
     * @return void
     * @create：2018/6/14 下午4:45
     * @author：sl
     */
    @Override
    public void steamFeesTaskForHour() {

        // 获取前一小时用汽情况
        List<SteamVo> steamVoList = steamMeterReadingHourMapper.steamHourData();
        logger.info("获取前一小时用汽情况: {}", JSON.toJSONString(steamVoList));

        List<SteamVo> newList = steamFeesCommon(steamVoList);

        if (newList != null && newList.size() > 0) {
            // 写库
            logger.info("》》》》》》》》》》》更新 start 《《《《《《《《《《《");
            steamMeterReadingHourMapper.updateBatch(newList);
            logger.info("》》》》》》》》》》》更新 end 《《《《《《《《《《《");
        }
    }

    /**
     * <计算蒸汽费用api><功能具体实现>
     *
     * @param req
     * @return java.math.BigDecimal
     * @create：2018/6/14 下午4:45
     * @author：sl
     */
    @Override
    public BigDecimal steamFeesApi(SteamFeesCalculateReq req) {
        return steamFees(req.getBeforeVol(), req.getUseVol(), req.getMeterNo());
    }

    /**
     *
     * <用汽-天-calculate><功能具体实现>
     *
     * @create：2018/6/18 下午2:18
     * @author：sl
     * @param
     * @return void
     */
    @Override
    public void steamFeesJobForDay() {
        // 获取前一天用汽情况
        List<SteamVo> steamVoList = steamMeterReadingDayMapper.steamDayData();
        logger.info("获取前一天用汽情况: {}", JSON.toJSONString(steamVoList));

        List<SteamVo> newList = steamFeesCommon(steamVoList);
        if (newList != null && newList.size() > 0) {
            // 写库
            logger.info("》》》》》》》》》》》更新 start 《《《《《《《《《《《");
            steamMeterReadingDayMapper.updateBatch(newList);
            logger.info("》》》》》》》》》》》更新 end 《《《《《《《《《《《");
        }
    }

    @Override
    public void steamFeesJobForDayByCondition(CalculateParam param) {
        // 获取前一天用汽情况
        List<SteamVo> steamVoList = steamMeterReadingDayMapper.steamDayDataByCondition(param);
        logger.info("获取前一天用汽情况: {}", JSON.toJSONString(steamVoList));

        List<SteamVo> newList = steamFeesCommon(steamVoList);
        if (newList != null && newList.size() > 0) {
            // 写库
            logger.info("》》》》》》》》》》》更新 start 《《《《《《《《《《《");
            steamMeterReadingDayMapper.updateBatch(newList);
            logger.info("》》》》》》》》》》》更新 end 《《《《《《《《《《《");
        }
    }

    @Override
    public void steamFeesJobForHour(CalculateParam param) {
        // 获取前一天用汽情况
        List<SteamVo> steamVoList = steamMeterReadingHourMapper.steamHourDatas(param);
        logger.info("获取符合条件用汽情况: {}", JSON.toJSONString(steamVoList));

        List<SteamVo> newList = steamFeesCommon(steamVoList);
        if (newList != null && newList.size() > 0) {
            // 写库
            logger.info("》》》》》》》》》》》更新 start 《《《《《《《《《《《");
            steamMeterReadingHourMapper.updateBatch(newList);
            logger.info("》》》》》》》》》》》更新 end 《《《《《《《《《《《");
        }
    }

    /**
     *
     * <用汽-月-calculate><功能具体实现>
     *
     * @create：2018/6/18 下午2:18
     * @author：sl
     * @param
     * @return void
     */
    @Override
    public void steamFeesJobForMonth() {
        // 获取前一天用汽情况
        List<SteamVo> steamVoList = steamMeterReadingMonthMapper.steamMonthData();
        logger.info("获取前一个月用汽情况: {}", JSON.toJSONString(steamVoList));

        List<SteamVo> newList = steamFeesCommon(steamVoList);
        if (newList != null && newList.size() > 0) {
            // 写库
            logger.info("》》》》》》》》》》》更新 start 《《《《《《《《《《《");
            steamMeterReadingMonthMapper.updateBatchFees(newList);
            logger.info("》》》》》》》》》》》更新 end 《《《《《《《《《《《");
        }
    }

    @Override
    public void steamFeesJobForMonthByCondition(CalculateParam param) {
        // 获取前一天用汽情况
        List<SteamVo> steamVoList = steamMeterReadingMonthMapper.steamMonthDataByCondition(param);
        logger.info("获取前一个月用汽情况: {}", JSON.toJSONString(steamVoList));

        List<SteamVo> newList = steamFeesCommon(steamVoList);
        if (newList != null && newList.size() > 0) {
            // 写库
            logger.info("》》》》》》》》》》》更新 start 《《《《《《《《《《《");
            steamMeterReadingMonthMapper.updateBatchFees(newList);
            logger.info("》》》》》》》》》》》更新 end 《《《《《《《《《《《");
        }
    }

    /**
     *
     * <上月同比><功能具体实现>
     *
     * @create：2018/6/18 下午3:54
     * @author：sl
     * @param
     * @return void
     */
    @Override
    public void lastMonthPercent() {
        // 上月同比
        List<SteamMeterReadingMonth> monthDatas = new ArrayList<>();
        SteamMeterReadingMonth data = null;
        // 获取上月、上上个月用汽情况
        List<SteamPercentVo> percentVos = steamMeterReadingMonthMapper.percentVos();
        for (SteamPercentVo vo : percentVos) {
            if (vo.getUseQuantity().doubleValue() != 0 && vo.getLastUseQuantity().doubleValue() != 0) {
                BigDecimal percent = vo.getUseQuantity().subtract(vo.getLastUseQuantity()).divide(vo.getLastUseQuantity());
                data = new SteamMeterReadingMonth();
                data.setId(vo.getId());
                data.setLastMonthPercent(percent);
                monthDatas.add(data);
            }
        }

        if (monthDatas != null && monthDatas.size() > 0) {
            // 写库
            logger.info("》》》》》》》》》》》上月同比更新 start 《《《《《《《《《《《");
            steamMeterReadingMonthMapper.updateBatch(monthDatas);
            logger.info("》》》》》》》》》》》上月同比更新 end 《《《《《《《《《《《");
        }
    }

    /**
     *
     * <同期环比><功能具体实现>
     *
     * @create：2018/6/18 下午3:54
     * @author：sl
     * @param
     * @return void
     */
    @Override
    public void samePeriodPercent() {
        // 同期环比
        List<SteamMeterReadingMonth> monthDatas = new ArrayList<>();
        SteamMeterReadingMonth data = null;
        // 获取上月、去年上个月用汽情况
        List<SteamPercentVo> lastPercentVos = steamMeterReadingMonthMapper.lastPercentVos();
        for (SteamPercentVo vo : lastPercentVos) {
            if (vo.getUseQuantity().doubleValue() != 0 && vo.getLastUseQuantity().doubleValue() != 0) {
                BigDecimal percent = vo.getUseQuantity().subtract(vo.getLastUseQuantity()).divide(vo.getLastUseQuantity());
                data = new SteamMeterReadingMonth();
                data.setId(vo.getId());
                data.setLastMonthPercent(percent);
                monthDatas.add(data);
            }
        }

        if (monthDatas != null && monthDatas.size() > 0) {
            // 写库
            logger.info("》》》》》》》》》》》同期环比更新 start 《《《《《《《《《《《");
            steamMeterReadingMonthMapper.updateLastBatch(monthDatas);
            logger.info("》》》》》》》》》》》同期环比更新 end 《《《《《《《《《《《");
        }
    }

    /**
     *
     * <用汽费用计算><功能具体实现>
     *
     * @create：2018/6/18 下午2:17
     * @author：sl
     * @param steamVoList
     * @return void
     */
    public List<SteamVo> steamFeesCommon(List<SteamVo> steamVoList) {
        List<SteamVo> newList = new ArrayList<>();

        if (steamVoList != null && steamVoList.size() > 0) {
            for (SteamVo vo : steamVoList) {
                if (vo.getUseQuantity().compareTo(BigDecimal.ZERO) > 0 &&
                        vo.getQuantity().subtract(vo.getUseQuantity()).compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal fees = steamFees(vo.getQuantity().subtract(vo.getUseQuantity()), vo.getUseQuantity(), vo.getMeterNo());
                    if (fees.doubleValue() >= 0) {
                        vo.setFees(fees);
                        newList.add(vo);
                    }
                }
            }
        }

        return newList;
    }

    /**
     * <汽价格><功能具体实现>
     *
     * @param beforeVol
     * @param useVol
     * @param meterNo
     * @return java.math.BigDecimal
     * @create：2018/6/14 下午4:01
     * @author：sl
     */
    public BigDecimal steamFees(BigDecimal beforeVol, BigDecimal useVol, String meterNo) {

        // fees
        BigDecimal fees = new BigDecimal("0");
        if (beforeVol.compareTo(BigDecimal.ZERO) < 0) {
            logger.error("蒸汽起始量有误：{}", beforeVol);
            throw new RuntimeException("蒸汽起始量有误 vol="+ beforeVol);
        } else {
            // 根据表号获取企业用汽标准
            SteamInfoVo steamInfoVo = steamInfoMapper.steamInfosByLoopNumber(meterNo);
            logger.info("企业用汽标准: {}", JSON.toJSONString(steamInfoVo));

            if (steamInfoVo != null) {
                // 根据表号获取本年度表初始示数
                SteamMeterReadingBase base = steamMeterReadingBaseMapper.baseVol(meterNo, DateUtil.getYear());
                BigDecimal baseVol = new BigDecimal("0");
                if (base != null) {
                    baseVol = base.getBaseQuantity();
                }

                // 判断企业用汽是否执行阶梯用汽(是否执行单一价格；0为否，1为是)
                if ("1".equals(steamInfoVo.getIsSinglePrice())) {
                    BigDecimal feesTotal = new BigDecimal("0");
                    feesTotal = useVol.multiply(steamInfoVo.getBasicPrice()).setScale(5, RoundingMode.HALF_UP);
                    // fees
                    fees = feesTotal;
                    logger.info("企业用汽单一价格：{}", feesTotal);
                } else {
                    // 根据阶梯id获取阶梯价格
                    List<SteamLadderPriceVo> steamLadderPrices = steamLadderPriceMapper.steamLadderPrices(steamInfoVo.getId());

                    List<BigDecimal> price = new LinkedList<BigDecimal>();
                    List<BigDecimal> vol = new LinkedList<BigDecimal>();

                    for (SteamLadderPriceVo o : steamLadderPrices) {
                        // 阶梯价格
                        price.add(o.getSteamPrice());

                        // 阶梯量
                        if (o.getStartValue().doubleValue() != 0) {
                            vol.add(new BigDecimal(o.getStartValue()));
                        }
                    }
                    logger.info("企业阶梯价格：{}", JSON.toJSONString(price));
                    logger.info("企业阶梯量：{}", JSON.toJSONString(vol));

                    BigDecimal bvol = beforeVol.subtract(baseVol);
                    if (bvol.compareTo(BigDecimal.ZERO) < 0) {
                        fees = new BigDecimal("-1");
                    } else {
                        fees = LadderPriceUtil.getMoney(beforeVol.subtract(baseVol), useVol, price, vol).setScale(5, RoundingMode.HALF_UP);
                    }
                }
            } else {
                fees = new BigDecimal("-1");
            }
        }
        return fees;
    }

	@Override
	public List<SteamFeesCalculateResp> steamFeesApi(List<SteamFeesCalculateReq> req) {
		
		List<SteamFeesCalculateResp> calculateResps=Lists.newArrayList();
		try {
        	ForkJoinPool pool = new ForkJoinPool();
        	Integer taskSize=req.size();
//        	System.out.println("   size:===="+taskSize);
        	if(CollectionUtils.isNotEmpty(req)){
        		SteamFeesForkJoinService task = new SteamFeesForkJoinService(req,this,1,taskSize);
        		calculateResps=pool.invoke(task);
        	}
//            System.out.println("task size:===="+calculateResps.size());
        }catch (Exception e) {
        	logger.error("【生产在线监测】,method:[steamFeesApi],查询[蒸汽minute]realTime采样数据异常,异常原因reason:{}",e.getMessage());
        }
		return calculateResps;
	}
}
