package com.enn.web.controller;


import com.alibaba.fastjson.JSON;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.PagedList;
import com.enn.service.business.ISteamMeterReadingService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.req.AccountUnitReq;
import com.enn.vo.energy.business.req.CustReq;
import com.enn.vo.energy.business.req.SteamMeterDayReq;
import com.enn.vo.energy.business.resp.SteamMeterReadingDetailResp;
import com.enn.vo.energy.business.resp.SteamMeterReadingResp;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.business.upload.UploadResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/steamMeter")
@RestController
@Api(value = "车间用汽统计-月/年", description = "车间用汽统计-月/年", tags = "车间用汽统计-月/年")
public class SteamMeterWorkshopStatisticsController extends BaseController {

    @Value("${company.steam.count.day}")
    private Integer day;
    @Value("${company.steam.count.month}")
    private Integer month;
    @Value("${company.steam.count.year}")
    private Integer year;

    @Value("${file.domain}")
    protected String baseFileUrl;

    @Autowired
    private ISteamMeterReadingService steamMeterReadingService;

	@RequestMapping(value = "/getWorkshop", method = RequestMethod.POST)
	@ApiOperation(value = "获取车间下拉列表", notes = "获取车间下拉列表")
	public EnergyResp<List<UnitResp>> getWorkshop(@RequestBody @Valid CustReq custReq, BindingResult result) {

        logger.info("获取车间下拉列表 param: {}", JSON.toJSONString(custReq));
		EnergyResp<List<UnitResp>> energyResp = new EnergyResp<>();
		if (result.hasErrors()) {
			energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
			return energyResp;
		}
		AccountUnitReq accountUnitReq = new AccountUnitReq();
		accountUnitReq.setId(Long.parseLong(custReq.getCustID()));
		accountUnitReq.setAccountingType("02");
		accountUnitReq.setIsAccount(false);
		logger.info("调取车间的下拉列表，请求参数{}", getJsonString(accountUnitReq));

		EnergyResp<List<UnitResp>> listEnergyResp = accountUnitService.queryAccountList(accountUnitReq);
		if (!listEnergyResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
			energyResp.faile(listEnergyResp.getCode(), listEnergyResp.getMsg(), listEnergyResp.getError());
			return energyResp;
		}
		energyResp.faile(listEnergyResp.getCode(), listEnergyResp.getMsg());
		energyResp.setData(listEnergyResp.getData());

		logger.info("获取车间下拉列表 response: {}", JSON.toJSONString(energyResp));
		return energyResp;
	}


    @ApiOperation(value = "车间用汽统计-用汽曲线")
    @RequestMapping(value = "/curveData", method = RequestMethod.POST)
    public EnergyResp<List<SteamMeterReadingResp>> curveData(@RequestBody @Valid SteamMeterDayReq req, BindingResult result) {

	    logger.info("车间用汽统计-用汽曲线 param: {}", JSON.toJSONString(req));

	    // response
        EnergyResp<List<SteamMeterReadingResp>> resp = new EnergyResp<List<SteamMeterReadingResp>>();

        if (req.getDateType() == null) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "时间维度不能为空");
            return resp;
        }

        if (req.getUnitId() == null) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "车间id不能为空");
            return resp;
        }

        // param 校验，error back
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }

        // time check
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        switch (req.getDateType()) {
            //月统计
            case 2:
                if (!DateUtil.isValidDate(req.getStartTime()) || !DateUtil.isValidDate(req.getEndTime())) {
                    resp.faile(StatusCode.C.getCode(), "月统计时间格式不正确");
                    return resp;
                }
                // 时间跨度
                if (DateUtil.getDaySub(req.getStartTime(), req.getEndTime()) > day) {
                    resp.faile(StatusCode.C.getCode(), "查询月统计，时间跨度不能大于"+ day + "天");
                    return resp;
                }

                // data
                resp = steamMeterReadingService.curveDataForDay(req);

                break;
            //年统计
            case 3:
                if (!DateUtil.yearMonth(req.getStartTime()) || !DateUtil.yearMonth(req.getEndTime())) {
                    resp.faile(StatusCode.C.getCode(), "年统计时间格式不正确");
                    return resp;
                }
                // 时间跨度
                if (DateUtil.getMonthSpaceExt(req.getStartTime(), req.getEndTime()) > month) {
                    resp.faile(StatusCode.C.getCode(), "查询年统计，时间跨度不能大于" + month +"个月");
                    return resp;
                }

                // data
                resp = steamMeterReadingService.curveDataForYear(req);

                break;
            default:
                resp.faile(StatusCode.C.getCode(), "时间维度(dateType)参数错误");
                break;
        }
        return resp;
    }

    @ApiOperation(value = "车间用汽统计-车间用汽详情day")
    @RequestMapping(value = "/detailDataForDay", method = RequestMethod.POST)
    public EnergyResp<PagedList<SteamMeterReadingResp>> detailDataForDay(@RequestBody @Valid SteamMeterDayReq req, BindingResult result) {

        logger.info("车间用汽统计-车间用汽详情day param: {}", JSON.toJSONString(req));

        // response
        EnergyResp<PagedList<SteamMeterReadingResp>> resp = new EnergyResp<PagedList<SteamMeterReadingResp>>();

        if (req.getUnitId() == null) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "车间id不能为空");
            return resp;
        }

        // param 校验，error back
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }

        // time check
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        if (!DateUtil.isValidDate(req.getStartTime()) || !DateUtil.isValidDate(req.getEndTime())) {
            resp.faile(StatusCode.C.getCode(), "月统计时间格式不正确");
            return resp;
        }
        // 时间跨度
        if (DateUtil.getDaySub(req.getStartTime(), req.getEndTime()) > day) {
            resp.faile(StatusCode.C.getCode(), "查询月统计，时间跨度不能大于"+ day + "天");
            return resp;
        }

        // data
        return steamMeterReadingService.detailDataForDay(req);
    }

    @ApiOperation(value = "车间用汽统计-车间用汽详情year")
    @RequestMapping(value = "/detailDataForYear", method = RequestMethod.POST)
    public EnergyResp<PagedList<SteamMeterReadingDetailResp>> detailDataForYear(@RequestBody @Valid SteamMeterDayReq req, BindingResult result) {

        logger.info("车间用汽统计-车间用汽详情year param: {}", JSON.toJSONString(req));

        // response
        EnergyResp<PagedList<SteamMeterReadingDetailResp>> resp = new EnergyResp<PagedList<SteamMeterReadingDetailResp>>();

        if (req.getUnitId() == null) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "车间id不能为空");
            return resp;
        }

        // param 校验，error back
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }

        // time check
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        if (!DateUtil.yearMonth(req.getStartTime()) || !DateUtil.yearMonth(req.getEndTime())) {
            resp.faile(StatusCode.C.getCode(), "年统计时间格式不正确");
            return resp;
        }
        // 时间跨度
        if (DateUtil.getMonthSpaceExt(req.getStartTime(), req.getEndTime()) > month) {
            resp.faile(StatusCode.C.getCode(), "查询年统计，时间跨度不能大于" + month +"个月");
            return resp;
        }

        // data
        return steamMeterReadingService.detailDataForYear(req);
    }

    @ApiOperation(value = "车间用汽统计-车间用汽详情-excel-day")
    @RequestMapping(value = "/detailDataForExcelDay", method = RequestMethod.POST)
    public EnergyResp<UploadResp> detailDataForExcelDay(@RequestBody @Valid SteamMeterDayReq req, BindingResult result) {

        logger.info("车间用汽统计-车间用汽详情day param: {}", JSON.toJSONString(req));

        // response
        EnergyResp<UploadResp> resp = new EnergyResp<UploadResp>();

        if (req.getUnitId() == null) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "车间id不能为空");
            return resp;
        }

        // param 校验，error back
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }

        // time check
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        if (!DateUtil.isValidDate(req.getStartTime()) || !DateUtil.isValidDate(req.getEndTime())) {
            resp.faile(StatusCode.C.getCode(), "月统计时间格式不正确");
            return resp;
        }
        // 时间跨度
        if (DateUtil.getDaySub(req.getStartTime(), req.getEndTime()) > day) {
            resp.faile(StatusCode.C.getCode(), "查询月统计，时间跨度不能大于"+ day + "天");
            return resp;
        }

        resp = steamMeterReadingService.detailDataForExcelDay(req);
        if (resp != null && StringUtils.isNotBlank(resp.getData().getPath())) {
            String path = baseFileUrl + resp.getData().getPath() + "?filename=" + "用汽报表-车间用汽(天)" +
                    req.getStartTime() + "-" + req.getEndTime() + ".xls";
            resp.getData().setPath(path);
        }

        // data
        return resp;
    }

    @ApiOperation(value = "车间用汽统计-车间用汽详情-excel-year")
    @RequestMapping(value = "/detailDataForExcelYear", method = RequestMethod.POST)
    public EnergyResp<UploadResp> detailDataForExcelYear(@RequestBody @Valid SteamMeterDayReq req, BindingResult result) {

        logger.info("车间用汽统计-车间用汽详情-excel-year param: {}", JSON.toJSONString(req));

        // response
        EnergyResp<UploadResp> resp = new EnergyResp<UploadResp>();

        if (req.getUnitId() == null) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "车间id不能为空");
            return resp;
        }

        // param 校验，error back
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }

        // time check
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        if (!DateUtil.yearMonth(req.getStartTime()) || !DateUtil.yearMonth(req.getEndTime())) {
            resp.faile(StatusCode.C.getCode(), "年统计时间格式不正确");
            return resp;
        }
        // 时间跨度
        if (DateUtil.getMonthSpaceExt(req.getStartTime(), req.getEndTime()) > month) {
            resp.faile(StatusCode.C.getCode(), "查询年统计，时间跨度不能大于" + month +"个月");
            return resp;
        }

        resp = steamMeterReadingService.detailDataForExcelYear(req);
        if (resp != null && StringUtils.isNotBlank(resp.getData().getPath())) {
            String path = baseFileUrl + resp.getData().getPath() + "?filename=" + "用汽报表-车间用汽(年)" +
                    req.getStartTime() + "-" + req.getEndTime() + ".xls";
            resp.getData().setPath(path);
        }

        // data
        return resp;
    }

}
