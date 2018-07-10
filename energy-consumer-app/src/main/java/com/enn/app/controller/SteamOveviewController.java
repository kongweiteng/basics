package com.enn.app.controller;

import com.enn.constant.Constant;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.service.business.IAccountUnitService;
import com.enn.service.business.ICustElectricCountService;
import com.enn.service.business.ICustMeterService;
import com.enn.service.business.IOpentsdbService;
import com.enn.service.system.ICompanyService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.app.EnergyTrend;
import com.enn.vo.energy.app.EnergyTrendResp;
import com.enn.vo.energy.app.HistoricalDataResp;
import com.enn.vo.energy.business.req.*;
import com.enn.vo.energy.business.resp.MeterResp;
import com.enn.vo.energy.business.resp.StatisticsDataResp;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.system.CompanyCust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/steamView")
@Api(tags = {"用汽总览"})
public class SteamOveviewController extends BaseController{
    @Autowired
    private ICustElectricCountService custElectricCountService;
    @Autowired
    private IOpentsdbService opentsdbService;
    @Autowired
    private IAccountUnitService accountUnitService;
    @Autowired
    private ICustMeterService custMeterService;
    @Autowired
    private ICompanyService companyService;

    /**
     * 历史运行数据 -采集统计用汽量
     * /index/historicalOperation*/


    @ApiOperation(value = "用汽总览-历史运行数据", notes = "日、月、年历史数据")
    @RequestMapping(value = "/historicalOperation", method = RequestMethod.POST)
    public EnergyResp<ListResp<HistoricalDataResp>> getOperation(@RequestBody @Valid CustReq custReq, BindingResult result) {
        EnergyResp<ListResp<HistoricalDataResp>> energyResp = new EnergyResp<ListResp<HistoricalDataResp>>();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        ListResp<HistoricalDataResp> data = new ListResp<>();
        List<HistoricalDataResp> historicalDataResps = new ArrayList<>();
        EnergyResp<List<String>> custMeterNosResp = findElecNosByCust(custReq.getCustID());

        if (StatusCode.SUCCESS.getCode().equals(custMeterNosResp.getCode())) {
            List<String> custMeterNos = custMeterNosResp.getData();
            /**
             * 获取日tab中数据*/
            StatisticsDataReq statisticsDataReq = new StatisticsDataReq();
            String end = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
            String nowD = DateUtil.getNowDay();//获取当天0点0分0秒
            statisticsDataReq.setStart(nowD);
            statisticsDataReq.setEnd(end);
            statisticsDataReq.setMeters(custMeterNos);
            EnergyResp<StatisticsDataResp> dayElectricity = custElectricCountService.getDayElectricity(statisticsDataReq);//当日

            String lastDayStart = DateUtil.getLastDay(nowD);
            String lastDayEnd = DateUtil.getLastDay(end);
            statisticsDataReq.setStart(lastDayStart);
            statisticsDataReq.setEnd(lastDayEnd);
            EnergyResp<StatisticsDataResp> yesterday = custElectricCountService.getDayElectricity(statisticsDataReq);//昨日同期

            //将日数据封装
            HistoricalDataResp returnDay = new HistoricalDataResp();
            returnDay.setType("day");
            returnDay.setEnergyType("01");
            if (StatusCode.SUCCESS.getCode().equals(dayElectricity.getCode())) {
                returnDay.setElectricity(MathUtils.towDecimal(dayElectricity.getData().getSumQuantity()).toString());
            } else {
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), "etsp-provider-rmi server find exception!!!" + dayElectricity.getError());
            }
            if (StatusCode.SUCCESS.getCode().equals(yesterday.getCode())) {
                returnDay.setLastElectricity(MathUtils.towDecimal(yesterday.getData().getSumQuantity()).toString());
            } else {
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), "etsp-provider-rmi server find exception!!!" + yesterday.getError());
            }
            //计算环比
            BigDecimal sub = MathUtils.sub(dayElectricity.getData().getSumQuantity(), yesterday.getData().getSumQuantity());
            BigDecimal divide = MathUtils.divide(sub, yesterday.getData().getSumQuantity());
            returnDay.setMom(divide == null ? null : MathUtils.point(divide, 4).toString());

            historicalDataResps.add(returnDay);

            /**
             * 获取月tab中数据*/
            //获取当月数据
            String startM = DateUtil.formatDate(DateUtil.getOneDay(),"yyyy-MM-dd HH:mm:ss");//获取本月1号0点0分0秒
            statisticsDataReq.setStart(startM);
            statisticsDataReq.setEnd(end);

            EnergyResp<StatisticsDataResp> monthElectricity = custElectricCountService.getMonthElectricity(statisticsDataReq);
            String lastMonthStart = DateUtil.getLastMonth(startM);//获取上月1号0点0分0秒
            String lastMonthEnd = DateUtil.getLastMonth(end);
            statisticsDataReq.setStart(lastMonthStart);
            statisticsDataReq.setEnd(lastMonthEnd);
            //获取上月同期数据
            EnergyResp<StatisticsDataResp> lastMonthElectricity = custElectricCountService.getMonthElectricity(statisticsDataReq);
            sub = MathUtils.sub(monthElectricity.getData().getSumQuantity(), lastMonthElectricity.getData().getSumQuantity());
            divide = MathUtils.divide(sub, lastMonthElectricity.getData().getSumQuantity());
            HistoricalDataResp returnMonth = new HistoricalDataResp();
            returnMonth.setType("month");
            returnMonth.setEnergyType("01");
            returnMonth.setElectricity(MathUtils.divide(monthElectricity.getData().getSumQuantity(), new BigDecimal(10000)).toString());
            returnMonth.setLastElectricity(MathUtils.divide(lastMonthElectricity.getData().getSumQuantity(), new BigDecimal(10000)).toString());
            returnMonth.setMom(divide == null ? null : MathUtils.point(divide, 4).toString());
            historicalDataResps.add(returnMonth);
            /**
             * 获取年tab中数据*/
            String nowY = DateUtil.formatDate(DateUtil.getYearDay(),"yyyy-MM-dd HH:mm:ss");//获取当年1号0点0分0秒
            statisticsDataReq.setStart(nowY);
            statisticsDataReq.setEnd(end);
            EnergyResp<StatisticsDataResp> yearElectricity = custElectricCountService.getYearElectricity(statisticsDataReq);
            //上年同期
            String lastYearStart = DateUtil.getLastYear(nowY);
            String lastYearEnd = DateUtil.getLastYear(end);
            statisticsDataReq.setStart(lastYearStart);
            statisticsDataReq.setEnd(lastYearEnd);
            EnergyResp<StatisticsDataResp> lastYearElectricity = custElectricCountService.getYearElectricity(statisticsDataReq);
            //计算环比
            sub = MathUtils.sub(yearElectricity.getData().getSumQuantity(), lastYearElectricity.getData().getSumQuantity());
            divide = MathUtils.divide(sub, lastYearElectricity.getData().getSumQuantity());
            HistoricalDataResp returnYear = new HistoricalDataResp();
            returnYear.setType("year");
            returnYear.setEnergyType("01");
            returnYear.setElectricity(MathUtils.divide(yearElectricity.getData().getSumQuantity(), new BigDecimal(10000)).toString());
            returnYear.setLastElectricity(MathUtils.divide(lastYearElectricity.getData().getSumQuantity(), new BigDecimal(10000)).toString());
            returnYear.setMom(divide == null ? null : MathUtils.point(divide, 4).toString());
            historicalDataResps.add(returnYear);
            data.setList(historicalDataResps);
            energyResp.ok(data);
        } else {
            energyResp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), "该用户下无电表！！");
        }
        return energyResp;
    }

    /**
     * 用电总览-全厂连续15日用电趋势
     * /index/realData
     */
    @ApiOperation(value = "用汽总览-厂连续15日用电趋势", notes = "用汽总览-厂连续15日用电趋势")
    @RequestMapping(value = "/elecTrend", method = RequestMethod.POST)
    public EnergyResp findElecTrend(@RequestBody @Valid CustReq custReq, BindingResult result) {
        EnergyResp<EnergyTrendResp> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        List<String> equips = new ArrayList<>();
        EnergyResp<List<StatisticsDataResp>> resp = new EnergyResp<List<StatisticsDataResp>>();
        EnergyTrendResp trendResp = new EnergyTrendResp();
        List<EnergyTrend> trendList = new ArrayList<>();
        EnergyTrend energyTrend = new EnergyTrend();
        String end = DateUtil.getLastDay(DateUtil.formatDateTime(new Date()));
        String start = DateUtil.getAddDay(end, -15);

        //获取企业信息
        DefaultReq defaultReq = new DefaultReq();
        defaultReq.setId(Long.parseLong(custReq.getCustID()));
        EnergyResp<CompanyCust> companyResp = companyService.getOne(defaultReq);
        if (StatusCode.SUCCESS.getCode().equals(companyResp.getCode())) {
            String companyName = companyResp.getData().getCompanyName();
            energyTrend.setName(companyName);
        } else {
            energyResp.setMsg(companyResp.getMsg());
            energyResp.setCode(companyResp.getCode());
            return energyResp;
        }

        //查询客户下所有电能表号
        MeterListReq meterListReq = new MeterListReq();
        meterListReq.setEnergyType("02");
        meterListReq.setId(Long.parseLong(custReq.getCustID()));
        EnergyResp<List<MeterResp>> meterListResp = custMeterService.getAllMeter(meterListReq);
        List<MeterResp> meterRespList = new ArrayList<>();
        if (StatusCode.SUCCESS.getCode().equals(meterListResp.getCode())) {
            meterRespList = meterListResp.getData();
            for (MeterResp meterResp : meterRespList) {
                equips.add(meterResp.getLoopNumber());
            }
        } else {
            energyResp.setMsg(meterListResp.getMsg());
            energyResp.setCode(meterListResp.getCode());
            return energyResp;
        }
        //查询全厂连续15日用电量
        ElectricMeterReadingReq readingReq = new ElectricMeterReadingReq();
        readingReq.setStartTime(start);
        readingReq.setEndTime(DateUtil.format(end, "yyyy-MM-dd"));
        readingReq.setMeterNoList(equips);
        resp = custElectricCountService.getElecSumByDay(readingReq);
        if (StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
            energyTrend.setDataResp(resp.getData());
        } else {
            energyResp.setMsg(resp.getMsg());
            energyResp.setCode(resp.getCode());
            return energyResp;
        }
        trendResp.setEnergyTrend(energyTrend);
        //------全厂趋势查询end------------
        //查询企业下所有的车间列表
        AccountUnitReq req = new AccountUnitReq();
        req.setId(Long.parseLong(custReq.getCustID()));
        req.setAccountingType(Constant.ACCOUNTING_TYPE_02);
        req.setIsAccount(false);
        EnergyResp<List<UnitResp>> unitListResp = accountUnitService.queryAccountList(req);
        if (StatusCode.SUCCESS.getCode().equals(unitListResp.getCode())) {
            List<UnitResp> unitList = unitListResp.getData();
            //查询每个车间连续15日用电量
            for (UnitResp unitResp : unitList) {
                energyTrend = new EnergyTrend();
                energyTrend.setName(unitResp.getName());
                equips = new ArrayList<>();
                meterRespList = unitResp.getMeters();
                for (MeterResp meterResp : meterRespList) {
                    equips.add(meterResp.getLoopNumber());
                }
                readingReq.setMeterNoList(equips);
                resp = custElectricCountService.getElecSumByDay(readingReq);
                if (StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
                    energyTrend.setDataResp(resp.getData());
                    trendList.add(energyTrend);

                } else {
                    energyResp.setMsg(resp.getMsg());
                    energyResp.setCode(resp.getCode());
                    return energyResp;
                }
            }
            trendResp.setEnergyTrendList(trendList);
        } else {
            energyResp.setMsg(unitListResp.getMsg());
            energyResp.setCode(unitListResp.getCode());
            return energyResp;
        }
        energyResp.ok(trendResp);

        return energyResp;
    }
}
