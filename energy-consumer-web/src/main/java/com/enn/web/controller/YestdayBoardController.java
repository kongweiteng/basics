package com.enn.web.controller;

import com.alibaba.fastjson.JSON;
import com.enn.constant.Constant;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.service.business.IAccountUnitService;
import com.enn.service.passage.ISteamFeesCalculateService;
import com.enn.service.system.ICompanyService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.req.AccountUnitReq;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.system.CompanyCust;
import com.enn.web.redis.RedisLogService;
import com.enn.web.service.RedisService;
import com.enn.web.service.YestdayBoardService;
import com.enn.web.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/yestdayBoard")
@Api(tags = {"昨日看板"})
@Slf4j
public class YestdayBoardController {

    @Autowired
    private YestdayBoardService yestdayBoardService;
    @Autowired
    private IAccountUnitService accountUnitService;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private ISteamFeesCalculateService steamFeesCalculateService;

    @Autowired
    private RedisService redisService;

    /**
     * 昨日看板---车间tab
     */
    @ApiOperation(value = "车间---tab")
    @RequestMapping(value = "/getYesterdayUnitTab", method = RequestMethod.POST)
    public EnergyResp<List<EnergyBoardResp>> getYesterdayUnitTab(@RequestBody @Valid DefaultReq defaultReq, BindingResult result) {
        EnergyResp<List<EnergyBoardResp>> yesterdayCompanyTab = null;
        yesterdayCompanyTab = yestdayBoardService.getYesterdayCompanyTab(defaultReq);
        return yesterdayCompanyTab;
    }

    /**
     * 昨日看板---企业tab
     */
    @ApiOperation(value = "企业---tab")
    @RequestMapping(value = "getCompanyYesterdayUnitTab", method = RequestMethod.POST)
    //@RedisLogService(group = {Constant.ENERGY_GROUP}, key = "#defaultReq.id",expire = Constant.ENERGY_TIME)
    public EnergyResp<EnergyBoardResp> getCompanyYesterdayUnitTab(@RequestBody @Valid DefaultReq defaultReq, BindingResult result) {
        EnergyResp<EnergyBoardResp> resp = new EnergyResp();
        EnergyBoardResp data = new EnergyBoardResp();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        //根据企业id 查询企业信息
        EnergyResp<CompanyCust> one = companyService.getOne(defaultReq);
        EnergyResp<EnergyBoardResp> companyYesterdayUnitTab = yestdayBoardService.getCompanyYesterdayUnitTab(defaultReq);

        if (one.getData() != null && companyYesterdayUnitTab.getData() != null) {
            data = companyYesterdayUnitTab.getData();
            data.setName(one.getData().getCompanyName());
            data.setId(one.getData().getId());
        }
        //如果有报警信息
        if (!data.getDayEleAlarm()) {
            //查询报警信息
            //分析那个车间的总量大
            BigDecimal big = new BigDecimal(0);
            BigDecimal steamBig = new BigDecimal(0);
            String name = "";
            String steamName = "";
            EnergyResp<List<EnergyBoardResp>> yesterdayCompanyTab = yestdayBoardService.getYesterdayCompanyTab(defaultReq);
            if (yesterdayCompanyTab.getData() != null && yesterdayCompanyTab.getData().size() > 0) {
                for (EnergyBoardResp energy : yesterdayCompanyTab.getData()) {
                    String dayEleQuantity = energy.getDayEleQuantity();
                    if (dayEleQuantity != null && MathUtils.compare(new BigDecimal(dayEleQuantity), big)) {
                        big = new BigDecimal(dayEleQuantity);
                        name = energy.getName();
                    }
                }
            }
            data.setDayEleAlarmName(name);
        }

        if (!data.getDaySeatAlarm()) {
            //查询报警信息
            //分析那个车间的总量大
            BigDecimal big = new BigDecimal(0);
            BigDecimal steamBig = new BigDecimal(0);
            String name = "";
            String steamName = "";
            EnergyResp<List<EnergyBoardResp>> yesterdayCompanyTab = yestdayBoardService.getYesterdayCompanyTab(defaultReq);
            if (yesterdayCompanyTab.getData() != null && yesterdayCompanyTab.getData().size() > 0) {
                for (EnergyBoardResp energy : yesterdayCompanyTab.getData()) {

                    String daySteamQuantity = energy.getDaySeatQuantity();
                    if (daySteamQuantity != null && MathUtils.compare(new BigDecimal(daySteamQuantity), steamBig)) {
                        steamBig = new BigDecimal(daySteamQuantity);
                        steamName = energy.getName();
                    }
                }
            }
            data.setDaySeatAlarmName(steamName);
        }


        resp.ok(data);
        return resp;

    }

    /**
     * 企业下 功率曲线
     */
    @ApiOperation(value = "企业---展开曲线")
    @RequestMapping(value = "getEnergyBoardLineResp", method = RequestMethod.POST)
    //@RedisLogService(group = {Constant.ENERGY_GROUP}, key = "#defaultReq.id",expire = Constant.ENERGY_TIME)
    public EnergyResp<List<EnergyBoardLineResp>> getEnergyBoardLineResp(@RequestBody @Valid DefaultReq defaultReq, BindingResult result) {
        EnergyResp<List<EnergyBoardLineResp>> resp = new EnergyResp();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }

        return yestdayBoardService.getEnergyBoardLineResp(defaultReq);

    }

    /**
     * 车间---展开图
     */

    @ApiOperation(value = "车间---展开图(参数是车间id)")
    @RequestMapping(value = "getUnitOpen", method = RequestMethod.POST)
    public EnergyResp<EnergyUnitOutResp> getUnitOpen(@RequestBody @Valid DefaultReq defaultReq, BindingResult result) {
        EnergyResp<EnergyUnitOutResp> resp = new EnergyResp<>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }

        EnergyUnitOutResp outResp = new EnergyUnitOutResp();
        //根据车间id查询车间详细信息
        EnergyResp<AccountingUnit> one = accountUnitService.getOne(defaultReq);
        AccountingUnit data = new AccountingUnit();
        if (one.getData() != null) {
            data = one.getData();
        }


        //根据车间id查询用能明细
        UnitEnergyInfoResp yesterdayBoardUnit = yestdayBoardService.getYesterdayBoardUnit(defaultReq);
        yesterdayBoardUnit.setSteamTitle(data.getName() + "用汽明细");
        yesterdayBoardUnit.setElectricTitle(data.getName() + "用电明细");

        outResp.setInfo(yesterdayBoardUnit);
        //根据车间id查询用电统计图
        List<ElectricTypeQuantityResp> electricTypeQuantityResp = yestdayBoardService.getElectricTypeQuantityResp(defaultReq);

        outResp.setElecStatistics(electricTypeQuantityResp);
        //根据车间id查询用汽折线图
        EnergyResp<List<EnergyBoardLineResp>> getmmp = yestdayBoardService.getmmp(defaultReq);
        LineResp lineResp = new LineResp();
        lineResp.setSeatStatistics(getmmp.getData());
        lineResp.setTile(data.getName() + "蒸汽负荷情况");
        outResp.setSeatStatistics(lineResp);
        resp.ok(outResp);
        return resp;
    }

}
