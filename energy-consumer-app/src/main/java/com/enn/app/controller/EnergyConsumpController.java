package com.enn.app.controller;

import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.service.business.ICustElectricCountService;
import com.enn.service.business.ICustSteamCountService;
import com.enn.service.business.ISteamMeterReadingService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.app.FeeCount;
import com.enn.vo.energy.app.HistoricalDataResp;
import com.enn.vo.energy.app.YesterdayDataResp;
import com.enn.vo.energy.business.bo.SteamMeterReadingDayBo;
import com.enn.vo.energy.business.req.CompanyMeteReq;
import com.enn.vo.energy.business.req.CustReq;
import com.enn.vo.energy.business.req.ElectricMeterReadingReq;
import com.enn.vo.energy.business.req.StatisticsDataReq;
import com.enn.vo.energy.business.resp.MeterResp;
import com.enn.vo.energy.business.resp.StatisticsDataResp;
import com.enn.vo.energy.business.resp.SteamDayAve;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/energyConsumption")
@Api(tags = {"昨日能源消耗"})
public class EnergyConsumpController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(EnergyConsumpController.class);

    @Autowired
    private ICustElectricCountService custElectricCountService;

    @Autowired
    private ICustSteamCountService custSteamCountService;

    @Autowired
    private ISteamMeterReadingService steamMeterReadingService;

    @RequestMapping(value = "/findLastFeesPie", method = RequestMethod.POST)
    @ApiOperation(value = "获取昨日费用饼形图", notes = "获取昨日费用饼形图")
    public EnergyResp<FeeCount> findLastFees(@RequestBody @Valid CustReq custReq, BindingResult result) {
        logger.info("获取昨日能源费用");
        EnergyResp<FeeCount> resp = new EnergyResp();
        FeeCount feeCount = new FeeCount();
        if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
        BigDecimal steamSum = null;
        BigDecimal elecSum = null;
        //获取昨日用电费用
        logger.info("获取昨日用电费用");
        EnergyResp<List<String>> meterNoResp = findElecNosByCust(custReq.getCustID());
        if (!StatusCode.SUCCESS.getCode().equals(meterNoResp.getCode())) {
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), "获取企业主电表错误！");
        }
        if (meterNoResp.getData() != null && meterNoResp.getData().size() > 0) {
            List<String> equipIDs = meterNoResp.getData();
            ElectricMeterReadingReq electricMeterReadingReq = new ElectricMeterReadingReq();
            String start = DateUtil.getLastDay(DateUtil.getNowDay());
            String end = DateUtil.getNowDay();
            electricMeterReadingReq.setStartTime(DateUtil.format(start, "yyyy-MM-dd"));
            electricMeterReadingReq.setEndTime(DateUtil.format(end, "yyyy-MM-dd"));
            electricMeterReadingReq.setMeterNoList(equipIDs);
            EnergyResp<List<StatisticsDataResp>> statistics = custElectricCountService.getElecSumByDay(electricMeterReadingReq);
            if (!StatusCode.SUCCESS.getCode().equals(statistics.getCode())) {
                logger.info("查询昨日用电费用错误");
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), statistics.getError());
            }
            if (statistics.getData() != null && statistics.getData().size() > 0) {
                List<StatisticsDataResp> statisticsDataRespList = statistics.getData();
                elecSum = statisticsDataRespList.get(0).getSumFees();
                elecSum = MathUtils.twoDecimal(MathUtils.divide(elecSum, new BigDecimal(10000)));
                feeCount.setElecFee(elecSum.toString());
                logger.info("昨日电费:" + elecSum);
            } else {
                logger.info("获取昨日用电费用为空");
            }
        } else {
            logger.info("企业下无主电表");
        }
        //获取昨日用气费用
        logger.info("获取昨日用汽费用");
        EnergyResp<List<String>> steamNoResp = findSteamNosByCust(custReq.getCustID());
        if (!StatusCode.SUCCESS.getCode().equals(steamNoResp.getCode())) {
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), "获取企业主蒸汽表错误！");
        }
        if (steamNoResp.getData() != null && steamNoResp.getData().size() > 0) {
            List<String> equipIDs = steamNoResp.getData();
            SteamMeterReadingDayBo steamMeterReadingDayBo = new SteamMeterReadingDayBo();
            String start = DateUtil.getLastDay(DateUtil.getNowDay());
            String end = DateUtil.getNowDay();
            steamMeterReadingDayBo.setStart(DateUtil.format(start, "yyyy-MM-dd"));
            steamMeterReadingDayBo.setEnd(DateUtil.format(end, "yyyy-MM-dd"));
            steamMeterReadingDayBo.setEquipID(equipIDs);
            EnergyResp<SteamDayAve> steamYesterday = steamMeterReadingService.getSteamYesterday(equipIDs);
            if (!StatusCode.SUCCESS.getCode().equals(steamYesterday.getCode())) {
                logger.info("查询昨日用电费用错误");
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), steamYesterday.getError());
            }
            if (steamYesterday.getData() != null) {
                SteamDayAve steamDayAve = steamYesterday.getData();
                BigDecimal fees = steamDayAve.getYesterdayMonth();
                if (fees != null) {
                    steamSum = MathUtils.twoDecimal(MathUtils.divide(fees, new BigDecimal(10000)));
                    feeCount.setSteamFee(steamSum.toString());
                }
                logger.info("昨日汽费:" + steamSum);
            } else {
                logger.info("获取昨日用电费用为空");
            }
        } else {
            logger.info("企业下无主蒸汽表");
        }
        BigDecimal totalSum = MathUtils.add(steamSum, elecSum);
        logger.info("昨日能源总费用：" + totalSum);
        if (totalSum != null) {
            feeCount.setTotalFee(MathUtils.towDecimal(totalSum).toString());
            BigDecimal divide = MathUtils.divide(elecSum, totalSum);

            BigDecimal elecProportion = MathUtils.mul(divide, new BigDecimal(100), 2);
            feeCount.setElecProportion(elecProportion.toString());
            feeCount.setSteamProportion(MathUtils.sub(new BigDecimal(100), elecProportion).toString());
        }
        resp.ok(feeCount);
        return resp;
    }


    @RequestMapping(value = "/findLastElecInfo", method = RequestMethod.POST)
    @ApiOperation(value = "获取昨日用电信息", notes = "获取昨日用电信息")
    public EnergyResp<YesterdayDataResp> findLastElecData(@RequestBody @Valid CustReq custReq, BindingResult result) {
        EnergyResp<YesterdayDataResp> energyResp = new EnergyResp<>();

        EnergyResp<List<String>> meterNoResp = findElecNosByCust(custReq.getCustID());

        if (!meterNoResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), meterNoResp.getError());
        }

        //使用电表获取用电量
        if (meterNoResp.getData() != null && meterNoResp.getData().size() > 0) {
            List<String> meters = meterNoResp.getData();
            //查询电量
            ElectricMeterReadingReq req = new ElectricMeterReadingReq();
            String start = DateUtil.getLastDay(DateUtil.getNowDay());
            String end = DateUtil.getNowDay();
            req.setStartTime(DateUtil.format(start, "yyyy-MM-dd"));
            req.setEndTime(DateUtil.format(end, "yyyy-MM-dd"));
            req.setMeterNoList(meters);
            EnergyResp<List<StatisticsDataResp>> elecSumByDay = custElectricCountService.getElecSumByDay(req);
            if (!elecSumByDay.getCode().equals(StatusCode.SUCCESS.getCode())) {
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), elecSumByDay.getError());
            }
            YesterdayDataResp yesterdayDataResp = new YesterdayDataResp();
            if (elecSumByDay.getData() != null && elecSumByDay.getData().size() > 0) {
                StatisticsDataResp statisticsDataResp = elecSumByDay.getData().get(0);
                //封装数据

                BigDecimal sumQuantity1 = statisticsDataResp.getSumQuantity();
                if (sumQuantity1 != null) {
                    yesterdayDataResp.setQuantity(MathUtils.towDecimal(sumQuantity1).toString());
                }
                BigDecimal sumFee1 = statisticsDataResp.getSumFees();
                //计算环比
                //1.获取昨日的昨日的数据
                req.setStartTime(DateUtil.format(DateUtil.getLastDay(start), "yyyy-MM-dd"));
                req.setEndTime(DateUtil.format(DateUtil.getLastDay(end), "yyyy-MM-dd"));
                req.setMeterNoList(meters);
                EnergyResp<List<StatisticsDataResp>> elecyestyest = custElectricCountService.getElecSumByDay(req);
                if (!elecyestyest.getCode().equals(StatusCode.SUCCESS.getCode())) {
                    throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), elecyestyest.getError());
                }


                if (elecyestyest.getData() != null && elecyestyest.getData().size() > 0) {
                    StatisticsDataResp elecyestyestResp = elecyestyest.getData().get(0);
                    //2.计算环比--昨日环比（昨日-前日）/前日*100%
                    BigDecimal sumQuantity = elecyestyestResp.getSumQuantity();
                    BigDecimal sumFee = elecyestyestResp.getSumFees();

                    if (sumQuantity != null && sumQuantity1 != null) {
                        BigDecimal sub = MathUtils.sub(sumQuantity1, sumQuantity);
                        if (sub != null) {
                            BigDecimal divide = MathUtils.divide(sub, sumQuantity);
                            yesterdayDataResp.setQuantityRatio(MathUtils.mul(divide,new BigDecimal(100),2).toString());
                        }
                    }
                    if (sumFee != null && sumFee1 != null) {
                        BigDecimal sub = MathUtils.sub(sumFee1, sumFee);
                        if (sub != null) {
                            BigDecimal divide = MathUtils.divide(sub, sumFee);
                            yesterdayDataResp.setFeeRatio(MathUtils.mul(divide,new BigDecimal(100),2).toString());
                        }
                    }
                }
                energyResp.ok(yesterdayDataResp);
            } else {
                energyResp.setCode(StatusCode.SUCCESS.getCode());
                energyResp.setData(yesterdayDataResp);
                energyResp.setMsg("昨日用电信息为空");
            }
        }
        return energyResp;
    }

    @RequestMapping(value = "/findLastSteamInfo", method = RequestMethod.POST)
    @ApiOperation(value = "获取昨日用汽信息", notes = "获取昨日用汽信息")
    public EnergyResp<YesterdayDataResp> findLastSteamData(@RequestBody @Valid CustReq custReq, BindingResult result) {
        EnergyResp<YesterdayDataResp> energyResp = new EnergyResp<>();
        EnergyResp<List<String>> steamNoResp = findSteamNosByCust(custReq.getCustID());
        YesterdayDataResp yesterdayDataResp = new YesterdayDataResp();
        if (!steamNoResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), steamNoResp.getError());
        }
        if (steamNoResp.getData() != null && steamNoResp.getData().size() > 0) {
            List<String> meters = steamNoResp.getData();

            EnergyResp<SteamDayAve> steamDayAve = steamMeterReadingService.getSteamYesterday(meters);
            if (!steamDayAve.getCode().equals(StatusCode.SUCCESS.getCode())) {
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), steamDayAve.getError());
            }
            BigDecimal lastQuantity = steamDayAve.getData().getDayAve();
            BigDecimal quantityRatio = steamDayAve.getData().getDayDeviation();
            BigDecimal feeRatio = steamDayAve.getData().getMonthRatio();

            if (lastQuantity != null) {
                yesterdayDataResp.setQuantity(lastQuantity.toString());
            }
            if (quantityRatio != null) {
                yesterdayDataResp.setQuantityRatio(quantityRatio.toString());
            }
            if (feeRatio != null) {
                yesterdayDataResp.setFeeRatio(feeRatio.toString());
            }
            energyResp.ok(yesterdayDataResp);
        } else {
            energyResp.setCode(StatusCode.SUCCESS.getCode());
            energyResp.setMsg("该企业无主蒸汽表");
            energyResp.setData(yesterdayDataResp);
        }
        return energyResp;
    }

}
