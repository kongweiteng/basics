package com.enn.energy.business.rest;

import com.enn.constant.StatusCode;
import com.enn.energy.business.service.IAccountingUnitService;
import com.enn.energy.business.service.ICustElectricCountService;
import com.enn.energy.business.service.IElectricMeterReadingService;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.bo.ElecMeterReadingDayBo;
import com.enn.vo.energy.business.bo.ElecMeterReadingDayStatisticsBo;
import com.enn.vo.energy.business.po.*;
import com.enn.vo.energy.business.po.DataResp;
import com.enn.vo.energy.business.req.AccountUnitReq;
import com.enn.vo.energy.business.req.ElectricMeterReadingReq;
import com.enn.vo.energy.business.req.StatisticsDataReq;
import com.enn.vo.energy.business.resp.*;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 企业用电统计Controller
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 16:39
 */
@Api(tags = {"用电报表--企业用电"})
@RestController
@RequestMapping("/custElectricCount")
public class CustElectricCountController {
    private static Logger logger = LoggerFactory.getLogger(CustElectricCountController.class);
    @Autowired
    private ICustElectricCountService custElectricCountService;
    @Autowired
    private IElectricMeterReadingService electricMeterReadingService;
    @Autowired
    private IAccountingUnitService accountingUnitService;

    @ApiOperation(value = "查询企业分时电费占比(分钟)")
    @RequestMapping(value = "/getSumByMinute", method = RequestMethod.POST)
    public EnergyResp<ElectricMeterReadingResp> getSumByMinute(@RequestBody @Valid ElectricMeterReadingReq req, BindingResult result) {
        EnergyResp<ElectricMeterReadingResp> resp = new EnergyResp<ElectricMeterReadingResp>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        resp.ok(custElectricCountService.getSumByMinute(req));
        return resp;
    }

    @ApiOperation(value = "查询企业分时电费占比(日)")
    @RequestMapping(value = "/getSumByDay", method = RequestMethod.POST)
    public EnergyResp<ElectricMeterReadingResp> getSumByDay(@RequestBody @Valid ElectricMeterReadingReq req, BindingResult result) {
        EnergyResp<ElectricMeterReadingResp> resp = new EnergyResp<ElectricMeterReadingResp>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        resp.ok(custElectricCountService.getSumByDay(req));
        return resp;
    }

    @ApiOperation(value = "查询企业分时电费占比(月)")
    @RequestMapping(value = "/getSumByMonth", method = RequestMethod.POST)
    public EnergyResp<ElectricMeterReadingResp> getSumByMonth(@RequestBody @Valid ElectricMeterReadingReq req, BindingResult result) {
        EnergyResp<ElectricMeterReadingResp> resp = new EnergyResp<ElectricMeterReadingResp>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        resp.ok(custElectricCountService.getSumByMonth(req));
        return resp;
    }

    @ApiOperation(value = "车间用电曲线(月)")
    @RequestMapping(value = "/getMeterByDay", method = RequestMethod.POST)
    public EnergyResp<MeterReadingResp> getMeterByDay(@RequestBody @Valid ElectricMeterReadingReq req, BindingResult result) {
        EnergyResp<MeterReadingResp> energyResp = new EnergyResp<>();
        List<DataResp> dataRespList = null;
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            energyResp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return energyResp;
        }
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        MeterReadingResp meterReadingResp = custElectricCountService.getMeterByDay(req);
        energyResp.ok(meterReadingResp);
        return energyResp;
    }

    @ApiOperation(value = "车间用电曲线(年)")
    @RequestMapping(value = "/getMeterByMonth", method = RequestMethod.POST)
    public EnergyResp<MeterReadingResp> getMeterByMonth(@RequestBody @Valid ElectricMeterReadingReq req, BindingResult result) {
        EnergyResp<MeterReadingResp> energyResp = new EnergyResp<>();
        List<DataResp> dataRespList = null;
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            energyResp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return energyResp;
        }
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        MeterReadingResp meterReadingResp = custElectricCountService.getMeterByMonth(req);
        energyResp.ok(meterReadingResp);
        return energyResp;
    }


    @ApiOperation(value = "查询企业每日电量、电费总计")
    @RequestMapping(value = "getElecSumByDay", method = RequestMethod.POST)
    public EnergyResp<List<StatisticsDataResp>> getElecSumByDay(@RequestBody @Valid ElectricMeterReadingReq req, BindingResult result) {
        EnergyResp<List<StatisticsDataResp>> resp = new EnergyResp<List<StatisticsDataResp>>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        resp.ok(custElectricCountService.getElecSumByDay(req));
        return resp;
    }

    @ApiOperation(value = "查询企业没有月电量、电费总计")
    @RequestMapping(value = "getElecSumByMonth", method = RequestMethod.POST)
    public EnergyResp<List<StatisticsDataResp>> getElecSumByMonth(@RequestBody @Valid ElectricMeterReadingReq req, BindingResult result) {
        EnergyResp<List<StatisticsDataResp>> resp = new EnergyResp<List<StatisticsDataResp>>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        resp.ok(custElectricCountService.getElecSumByMonth(req));
        return resp;
    }


    /**
     * 根据车间id查询各个生产线的用电情况（昨日数值）
     */
    @ApiOperation(value = "（昨日数值）根据车间id查询各个生产线的用电情况")
    @RequestMapping(value = "getYesterdayBoardUnit", method = RequestMethod.POST)
    public EnergyResp<List<YesterdayBoardUnitResp>> getYesterdayBoardUnit(@RequestBody @Valid DefaultReq defaultReq, BindingResult result) {
        EnergyResp<List<YesterdayBoardUnitResp>> resp = new EnergyResp<List<YesterdayBoardUnitResp>>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        EnergyResp<List<YesterdayBoardUnitResp>> yesterdayBoardUnit = electricMeterReadingService.getYesterdayBoardUnit(defaultReq);
        return yesterdayBoardUnit;
    }

    @ApiOperation(value = "统计当月电量、电费")
    @RequestMapping(method = RequestMethod.POST, value = "/month")
    public EnergyResp<StatisticsDataResp> getMonthElectricity(@RequestBody @Valid StatisticsDataReq req, BindingResult result) {
        EnergyResp<StatisticsDataResp> resp = new EnergyResp<>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        StatisticsDataResp statistics = new StatisticsDataResp();
        //获取到日表中的数据-1号到昨天
        String start = req.getStart();
        String end = req.getEnd();
        String startD = DateUtil.format(start, "yyyy-MM-dd");
        String endD = DateUtil.format(end, "yyyy-MM-dd");
        EnergyResp<StatisticsDataResp> monthElectricity = null;
        try {
            monthElectricity = custElectricCountService.getMonthElectricity(startD, endD, req.getMeters());
        } catch (Exception e) {
            logger.info("当月电量统计---计算1号到昨天电量出现异常！！！");
            throw new EnergyException(StatusCode.ERROR.getCode(), "当月电量统计---计算1号到昨天电量出现异常！！！", e.getMessage());
        }
        //获取到小时表中的数据-0点到现在
//        String startH = DateUtil.format(end,"yyyy-MM-dd")+" 00:00:00";
//        String endH = DateUtil.format(end, "yyyy-MM-dd HH") + ":00:00";
//        EnergyResp<StatisticsDataResp> dayElectricity = null;
//        try {
//            dayElectricity = custElectricCountService.getDayElectricity(startH, endH, req.getMeters());
//        } catch (Exception e) {
//            logger.info("当月电量统计---计算本天0点到现在电量出现异常！！！");
//            throw new EnergyException(StatusCode.ERROR.getCode(), "当月电量统计---计算本天0点到现在电量出现异常！！！", e.getMessage());
//        }

        //获取到分钟表中的数据-0点到现在
//        String startM = DateUtil.format(end, "yyyy-MM-dd HH") + ":00:01";
//        String endM = end;
//        EnergyResp<StatisticsDataResp> hourElectricity = null;
//        try {
//            hourElectricity = custElectricCountService.getHourElectricity(startM, endM, req.getMeters());
//        } catch (Exception e) {
//            logger.info("当月电量统计---计算本天当前小时点到现在电量出现异常！！！");
//            throw new EnergyException(StatusCode.ERROR.getCode(), "当月电量统计---计算本天0点到现在电量出现异常！！！", e.getMessage());
//        }
//        BigDecimal quantitySum = new BigDecimal(0);
//        BigDecimal feeSum = new BigDecimal(0);
        BigDecimal quantityMonth = monthElectricity.getData().getSumQuantity();
//        BigDecimal quantityDay = dayElectricity.getData().getSumQuantity();
//        BigDecimal quantityHour = hourElectricity.getData().getSumQuantity();
//        quantitySum = MathUtils.add(MathUtils.add(quantityMonth, quantityDay), quantityHour);
        BigDecimal feeMonth = monthElectricity.getData().getSumFees();
//        BigDecimal feeDay = dayElectricity.getData().getSumFees();
//        BigDecimal feeHour = hourElectricity.getData().getSumFees();
//        feeSum = MathUtils.add(MathUtils.add(feeMonth, feeDay), feeHour);
//        statistics.setSumQuantity(quantitySum);
//        statistics.setSumFees(feeSum);
        statistics.setSumQuantity(quantityMonth);
        statistics.setSumFees(feeMonth);
        resp.ok(statistics);
        return resp;
    }


    /**
     * 上月同期电量统计 -
     *
     * @return
     */
//    @RequestMapping(value = "/lastMonth", method = RequestMethod.POST)
//    @ApiOperation(value = "统计上月同期电量、电费")
//    public EnergyResp<StatisticsDataResp> getLastMonthElectricity(@RequestBody StatisticsDataReq statisticsDataReq) {
//
//        EnergyResp<StatisticsDataResp> resp = new EnergyResp<>();
//        StatisticsDataResp statistics = new StatisticsDataResp();
//        String now = DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
//        String last = DateUtil.getLastMonth(now);
//        String startD = DateUtil.format(DateUtil.getLastOneDay(),"yyyy-MM-dd");
//        String endD =  DateUtil.format(DateUtil.getLastDay(last),"yyyy-MM-dd");
//        //获取到日表中的数据-1号到昨天
//        EnergyResp<StatisticsDataResp> monthElectricity = custElectricCountService.getMonthElectricity(startD,
//                endD, statisticsDataReq.getMeters());
//        //获取到小时表中的数据-0点到现在
//        String startH = DateUtil.format(last,"yyyy-MM-dd")+" 00:00:00";//当天 00：00：00
//        String endH = DateUtil.format(last, "yyyy-MM-dd HH") + ":00:00";
//        EnergyResp<StatisticsDataResp> dayElectricity = custElectricCountService.getDayElectricity(startH, endH, statisticsDataReq.getMeters());
//        String startM = DateUtil.format(last, "yyyy-MM-dd HH") + ":00:01";
//        String endM = last;
//        EnergyResp<StatisticsDataResp> hourElectricity = custElectricCountService.getHourElectricity(startM, endM, statisticsDataReq.getMeters());
//        BigDecimal quantitySum = new BigDecimal(0);
//        BigDecimal feeSum = new BigDecimal(0);
//        BigDecimal quantityMonth = monthElectricity.getData().getSumQuantity();
//        BigDecimal quantityDay = dayElectricity.getData().getSumQuantity();
//        BigDecimal quantityHour = hourElectricity.getData().getSumQuantity();
//        quantitySum = MathUtils.add(MathUtils.add(quantityMonth, quantityDay), quantityHour);
//        BigDecimal feeMonth = monthElectricity.getData().getSumFees();
//        BigDecimal feeDay = dayElectricity.getData().getSumFees();
//        BigDecimal feeHour = hourElectricity.getData().getSumFees();
//        feeSum = MathUtils.add(MathUtils.add(feeMonth, feeDay), feeHour);
//        statistics.setSumQuantity(quantitySum);
//        statistics.setSumFees(feeSum);
//        resp.ok(statistics);
//
//        return resp;
//    }


    /**
     * 统计日电量
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/day")
    @ApiOperation(value = "统计日电量、电费")
    public EnergyResp<StatisticsDataResp> getDayElectricity(StatisticsDataReq req) {
        EnergyResp<StatisticsDataResp> resp = new EnergyResp<>();
        StatisticsDataResp statistics = new StatisticsDataResp();
        logger.info("请求方法/custElectricCount/day");
        EnergyResp<StatisticsDataResp> dayElectricity = null;
        String start = req.getStart();
        String end = req.getEnd();
        try {
            String startH = start;//当天 00：00：00
            String endH = DateUtil.format(end, "yyyy-MM-dd HH") + ":00:00";
            dayElectricity = custElectricCountService.getDayElectricity(startH, endH, req.getMeters());
            logger.info("请求数据库");
        } catch (Exception e) {
            logger.info("计算日统计电量出现异常！！！");
            throw new EnergyException(StatusCode.ERROR.getCode(), "计算日统计电量出现异常！！！", e.getMessage());
        }
        //获取到分钟表中的数据-0点到现在
        String startM = DateUtil.format(end, "yyyy-MM-dd HH") + ":00:01";
        String endM = end;
        EnergyResp<StatisticsDataResp> hourElectricity = null;
        try {
            hourElectricity = custElectricCountService.getHourElectricity(startM, endM, req.getMeters());
        } catch (Exception e) {
            logger.info("当月电量统计---计算本天当前小时点到现在电量出现异常！！！");
            throw new EnergyException(StatusCode.ERROR.getCode(), "当月电量统计---计算本天0点到现在电量出现异常！！！", e.getMessage());
        }
        BigDecimal quantitySum = MathUtils.add(dayElectricity.getData().getSumQuantity(), hourElectricity.getData().getSumQuantity());
        BigDecimal feeSum = MathUtils.add(dayElectricity.getData().getSumFees(), hourElectricity.getData().getSumFees());
        statistics.setSumQuantity(quantitySum);
        statistics.setSumFees(feeSum);
        resp.ok(statistics);
        return resp;
    }

    /**
     * 统计昨日同期
     * @return
     */
//    @RequestMapping(method = RequestMethod.POST, value = "/yesterday")
//    @ApiOperation(value = "统计昨日同期电量、电费")
//    public EnergyResp<StatisticsDataResp> getYesterdayElectricity(StatisticsDataReq statisticsDataReq){
//        EnergyResp<StatisticsDataResp> resp= new EnergyResp<>();
//        StatisticsDataResp statistics = new StatisticsDataResp();
//        EnergyResp<StatisticsDataResp> dayElectricity = null;
//        String now = DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
//        String last = DateUtil.getLastDay(now);
//        try {
//            String startH = DateUtil.format(last,"yyyy-MM-dd")+" 00:00:00";//当天 00：00：00
//            String endH = DateUtil.format(last, "yyyy-MM-dd HH") + ":00:00";
//            dayElectricity = custElectricCountService.getDayElectricity(startH, endH, statisticsDataReq.getMeters());
//
//        } catch (Exception e) {
//            logger.info("计算昨日统计电量出现异常！！！");
//            throw new EnergyException(StatusCode.ERROR.getCode(), "计算昨日统计电量出现异常！！！", e.getMessage());
//        }
//
//        //获取到分钟表中的数据-0点到现在
//        String startM = DateUtil.format(last, "yyyy-MM-dd HH") + ":00:01";
//        String endM = DateUtil.format(last, "yyyy-MM-dd HH:mm:ss");
//        EnergyResp<StatisticsDataResp> hourElectricity = null;
//        try {
//            hourElectricity = custElectricCountService.getHourElectricity(startM, endM, statisticsDataReq.getMeters());
//        } catch (Exception e) {
//            logger.info("当月电量统计---计算本天当前小时点到现在电量出现异常！！！");
//            throw new EnergyException(StatusCode.ERROR.getCode(), "当月电量统计---计算本天0点到现在电量出现异常！！！", e.getMessage());
//        }
//
//        BigDecimal quantitySum = MathUtils.add(dayElectricity.getData().getSumQuantity(), hourElectricity.getData().getSumQuantity());
//        BigDecimal feeSum = MathUtils.add(dayElectricity.getData().getSumFees(), hourElectricity.getData().getSumFees());
//        statistics.setSumQuantity(quantitySum);
//        statistics.setSumFees(feeSum);
//        resp.ok(statistics);
//        return resp;
//
//    }


    /**
     * 统计当年电量
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/year")
    @ApiOperation(value = "统计当年电量、电费")
    public EnergyResp<StatisticsDataResp> getYearElectricity(StatisticsDataReq req) {
        EnergyResp<StatisticsDataResp> retuSum = new EnergyResp<>();
        StatisticsDataResp statistics = new StatisticsDataResp();
        EnergyResp<StatisticsDataResp> yearElectricity = null;
        String start = req.getStart();
        String end = req.getEnd();
        try {
            //获取到月表中的数据--1月到上月的数据
            String startY = DateUtil.format(start, "yyyy-MM");
            String endY = DateUtil.format(DateUtil.getLastMonth(end), "yyyy-MM");
            yearElectricity = custElectricCountService.getYearElectricity(startY, endY, req.getMeters());
        } catch (Exception e) {
            logger.info("计算从1月到上月统计电量出现异常！！！");
            throw new EnergyException(StatusCode.ERROR.getCode(), "计算从1月到上月统计电量出现异常！！！", e.getMessage());
        }
        //获取到日表中的数据-1号到昨天
        String startD = DateUtil.format(end, "yyyy-MM") + "-01";
        String endD = DateUtil.format(DateUtil.getLastDay(end), "yyyy-MM-dd");
        EnergyResp<StatisticsDataResp> monthElectricity = null;
        try {
            monthElectricity = custElectricCountService.getMonthElectricity(startD, endD, req.getMeters());
        } catch (Exception e) {
            logger.info("当月电量统计---计算1号到昨天电量出现异常！！！");
            throw new EnergyException(StatusCode.ERROR.getCode(), "当月电量统计---计算1号到昨天电量出现异常！！！", e.getMessage());
        }
        //获取到小时表中的数据-0点到现在
        String startH = DateUtil.format(end, "yyyy-MM-dd") + " 00:00:00";//当天 00：00：00
        String endH = DateUtil.format(end, "yyyy-MM-dd HH") + ":00:00";
        EnergyResp<StatisticsDataResp> dayElectricity = null;
        try {
            dayElectricity = custElectricCountService.getDayElectricity(startH, endH, req.getMeters());
        } catch (Exception e) {
            logger.info("当月电量统计---计算本天0点到现在电量出现异常！！！");
            throw new EnergyException(StatusCode.ERROR.getCode(), "当月电量统计---计算本天0点到现在电量出现异常！！！", e.getMessage());
        }

        //获取到分钟表中的数据-0点到现在
        String startM = DateUtil.format(end, "yyyy-MM-dd HH") + ":00:01";
        String endM = end;
        EnergyResp<StatisticsDataResp> hourElectricity = null;
        try {
            hourElectricity = custElectricCountService.getHourElectricity(startM, endM, req.getMeters());
        } catch (Exception e) {
            logger.info("当月电量统计---计算本天当前小时点到现在电量出现异常！！！");
            throw new EnergyException(StatusCode.ERROR.getCode(), "当月电量统计---计算本天0点到现在电量出现异常！！！", e.getMessage());
        }
        BigDecimal quantitySum = MathUtils.add(yearElectricity.getData().getSumQuantity(), monthElectricity.getData().getSumQuantity());
        quantitySum = MathUtils.add(quantitySum, dayElectricity.getData().getSumQuantity());
        quantitySum = MathUtils.add(quantitySum, hourElectricity.getData().getSumQuantity());
        BigDecimal feeSum = MathUtils.add(yearElectricity.getData().getSumFees(), monthElectricity.getData().getSumFees());
        feeSum = MathUtils.add(feeSum, dayElectricity.getData().getSumFees());
        feeSum = MathUtils.add(feeSum, hourElectricity.getData().getSumFees());
        statistics.setSumQuantity(quantitySum);
        statistics.setSumFees(feeSum);
        retuSum.ok(statistics);
        return retuSum;
    }

    /**
     * 统计去年同期
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/lastYear")
    @ApiOperation(value = "统计去年同期电量、电费")
    public EnergyResp<StatisticsDataResp> getLastYearElectricity(StatisticsDataReq statisticsDataReq) {
        EnergyResp<StatisticsDataResp> retuSum = new EnergyResp<>();
        StatisticsDataResp statistics = new StatisticsDataResp();
        String now = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        String last = DateUtil.getLastYear(now);
        EnergyResp<StatisticsDataResp> yearElectricity = null;
        try {
            //获取到月表中的数据--1月到上月的数据
            String startY = DateUtil.format(last, "yyyy") + "-01";
            String endY = DateUtil.format(last, "yyyy-MM");
            yearElectricity = custElectricCountService.getYearElectricity(startY, endY, statisticsDataReq.getMeters());
        } catch (Exception e) {
            logger.info("计算从1月到上月统计电量出现异常！！！");
            throw new EnergyException(StatusCode.ERROR.getCode(), "计算从1月到上月统计电量出现异常！！！", e.getMessage());
        }
        //获取到日表中的数据-1号到昨天
        String startD = DateUtil.format(last, "yyyy-MM-dd");
        String endD = DateUtil.format(last, "yyyy-MM-dd");
        EnergyResp<StatisticsDataResp> monthElectricity = null;
        try {
            monthElectricity = custElectricCountService.getMonthElectricity(startD, endD, statisticsDataReq.getMeters());
        } catch (Exception e) {
            logger.info("当月电量统计---计算1号到昨天电量出现异常！！！");
            throw new EnergyException(StatusCode.ERROR.getCode(), "当月电量统计---计算1号到昨天电量出现异常！！！", e.getMessage());
        }
        //获取到小时表中的数据-0点到现在
        //获取到小时表中的数据-0点到现在
        String startH = DateUtil.format(last, "yyyy-MM-dd") + " 00:00:00";//当天 00：00：00
        String endH = DateUtil.format(last, "yyyy-MM-dd HH") + ":00:00";
        EnergyResp<StatisticsDataResp> dayElectricity = null;
        try {
            dayElectricity = custElectricCountService.getDayElectricity(startH, endH, statisticsDataReq.getMeters());
        } catch (Exception e) {
            logger.info("当月电量统计---计算本天0点到现在电量出现异常！！！");
            throw new EnergyException(StatusCode.ERROR.getCode(), "当月电量统计---计算本天0点到现在电量出现异常！！！", e.getMessage());
        }

        //获取到分钟表中的数据-0点到现在
        String startM = DateUtil.format(last, "yyyy-MM-dd HH") + ":00:01";
        String endM = last;
        EnergyResp<StatisticsDataResp> hourElectricity = null;
        try {
            hourElectricity = custElectricCountService.getHourElectricity(startM, endM, statisticsDataReq.getMeters());
        } catch (Exception e) {
            logger.info("当月电量统计---计算本天当前小时点到现在电量出现异常！！！");
            throw new EnergyException(StatusCode.ERROR.getCode(), "当月电量统计---计算本天0点到现在电量出现异常！！！", e.getMessage());
        }
        BigDecimal quantitySum = MathUtils.add(yearElectricity.getData().getSumQuantity(), monthElectricity.getData().getSumQuantity());
        quantitySum = MathUtils.add(quantitySum, dayElectricity.getData().getSumQuantity());
        quantitySum = MathUtils.add(quantitySum, hourElectricity.getData().getSumQuantity());
        BigDecimal feeSum = MathUtils.add(yearElectricity.getData().getSumFees(), monthElectricity.getData().getSumFees());
        feeSum = MathUtils.add(feeSum, dayElectricity.getData().getSumFees());
        feeSum = MathUtils.add(feeSum, hourElectricity.getData().getSumFees());
        statistics.setSumQuantity(quantitySum);
        statistics.setSumFees(feeSum);
        retuSum.ok(statistics);
        return retuSum;
    }


    @ApiOperation(value = "用电日均量（7天的平均数）、日偏差量")
    @RequestMapping(value = "getElecDayAve", method = RequestMethod.POST)
    public EnergyResp<ElecDayAve> getElecDayAve(@RequestBody List<String> meters) {
        if (meters.size()<0){
            throw new EnergyException(StatusCode.C.getCode(), StatusCode.C.getMsg(), "表号不能为空！！！");
        }
        EnergyResp<ElecDayAve> resp = new EnergyResp<>();
        ElecDayAve elecDayAve = new ElecDayAve();
        String end = DateUtil.getLastOneday();
        String start = DateUtil.getDay(DateUtil.add(new Date(), Calendar.DATE, -7));
        ElecMeterReadingDayBo elecMeterReadingDayBo = new ElecMeterReadingDayBo();
        elecMeterReadingDayBo.setStart(start);
        elecMeterReadingDayBo.setEnd(end);
        elecMeterReadingDayBo.setEquipID(meters);
        //查询数据
        ElecMeterReadingDayStatisticsBo elec = electricMeterReadingService.countElecMeterReadingDayByAssignedConditon(elecMeterReadingDayBo);//七天数据
        BigDecimal ave = null;
        if (elec != null) {
            //查询该时间段内的数据条数
            int size = meters.size();
            Integer count = electricMeterReadingService.getCount(elecMeterReadingDayBo);
            //计算7天的平均数
            ave = MathUtils.divide(elec.getTotalElecPower(), MathUtils.divide(new BigDecimal(count),new BigDecimal(size))); //用电日均量
            elecDayAve.setDayAve(ave);
        }
        //获取前日数据

        elecMeterReadingDayBo.setStart(DateUtil.getLastTwoday());
        elecMeterReadingDayBo.setEnd(DateUtil.getLastTwoday());
        ElecMeterReadingDayStatisticsBo eleYest = electricMeterReadingService.countElecMeterReadingDayByAssignedConditon(elecMeterReadingDayBo);

        BigDecimal deviation = null;
        if (eleYest != null) {
            deviation = MathUtils.divide(MathUtils.sub(eleYest.getTotalElecPower(), ave), ave);  //日偏差量
        }
        elecDayAve.setDayDeviation(deviation);

        String meterNo = null;
        deviation = new BigDecimal(0);

        /**
         * 根据表号拿到该表所在核算单元的， 下级车间
         */
        List<String> metes = new ArrayList<>();
        String type = "02";//车间
        if (meters.size() > 0) {
            String s = meters.get(0);
            //根据总表号获取核算单元信息
            EnergyResp<List<AccountingUnit>> unitByMete = accountingUnitService.getUnitByMete(s);
            //根据核算单元信息查询该核算单元下的车间
            if (unitByMete.getData() != null && unitByMete.getData().size() > 0) {
                AccountingUnit accountingUnit = unitByMete.getData().get(0);
                //如果该核算单元是车间了，则查下边的生产线
                if(accountingUnit.getAccountingType().equals("03")){//生产线
                    metes=meters;
                }else if(accountingUnit.getAccountingType().equals("02")){//车间
                    type="03";
                    metes= get(type,accountingUnit.getId());
                }else {
                    metes= get(type,accountingUnit.getId());
                }
            }
        }
        for (String meter : metes) {
            List<String> mes = new ArrayList<>();
            mes.add(meter);
            elecMeterReadingDayBo.setEquipID(mes);
            elecMeterReadingDayBo.setStart(DateUtil.getLastOneday());
            elecMeterReadingDayBo.setEnd(DateUtil.getLastOneday());
            ElecMeterReadingDayStatisticsBo statisticsBo2 = electricMeterReadingService.countElecMeterReadingDayByAssignedConditon(elecMeterReadingDayBo);
            if (statisticsBo2 != null) {
                if (statisticsBo2.getTotalElecPower().compareTo(deviation) == 1) {
                    meterNo = meter;
                    deviation = statisticsBo2.getTotalElecPower();
                }
            }
        }
        elecDayAve.setMeterNo(meterNo);
        resp.ok(elecDayAve);
        return resp;
    }


    /**
     *
     * @param type
     * @param id
     * @return
     */
    public List<String> get(String type,Long id){
        List<String> meterssss= new ArrayList<>();

        AccountUnitReq accountUnitReq = new AccountUnitReq();
        accountUnitReq.setIsAccount(true);
        accountUnitReq.setAccountingType(type);
        accountUnitReq.setId(id);
        EnergyResp<List<UnitResp>> listEnergyResp = accountingUnitService.queryAccountListT(accountUnitReq);
        //拿到车间的ids
        List<Long> unids = new ArrayList<>();
        if (listEnergyResp.getData() != null) {
            for (UnitResp unitResp : listEnergyResp.getData()) {
                unids.add(unitResp.getId());
            }
        }
        //根据ids查询表号
        List<CustMeter> accountEleMeter = null;
        if (unids.size() > 0) {

            accountEleMeter = accountingUnitService.getAccountEleMeter(unids, "01");
        }
        //表号s

        if (accountEleMeter != null && accountEleMeter.size() > 0) {
            for (CustMeter mete : accountEleMeter) {
                meterssss.add(mete.getLoopNumber());
            }
        }
        return meterssss;
    }

    @ApiOperation(value = "查询日总用电量和总费用")
    @RequestMapping(value = "/getElecSumByD", method = RequestMethod.POST)
    public EnergyResp<ElectricMeterReadingDayPo> getElecSumByD(@RequestBody @Valid ElectricMeterReadingReq req, BindingResult result) {
        EnergyResp<ElectricMeterReadingDayPo> resp = new EnergyResp<ElectricMeterReadingDayPo>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        resp.ok(electricMeterReadingService.getElecSumByD(req));
        return resp;
    }

    @ApiOperation(value = "查询月总用电量和总费用")
    @RequestMapping(value = "/getElecSumByM", method = RequestMethod.POST)
    public EnergyResp<ElectricMeterReadingMonthPo> getElecSumByM(@RequestBody @Valid ElectricMeterReadingReq req, BindingResult result) {
        EnergyResp<ElectricMeterReadingMonthPo> resp = new EnergyResp<ElectricMeterReadingMonthPo>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        resp.ok(electricMeterReadingService.getElecSumByM(req));
        return resp;
    }
}
