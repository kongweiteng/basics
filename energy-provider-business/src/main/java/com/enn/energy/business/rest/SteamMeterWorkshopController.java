package com.enn.energy.business.rest;

import com.enn.constant.StatusCode;
import com.enn.energy.business.service.ISteamMeterReadingService;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.req.SteamMeterDayReq;
import com.enn.vo.energy.business.resp.SteamMeterReadingDetailResp;
import com.enn.vo.energy.business.resp.SteamMeterReadingResp;
import com.enn.vo.energy.business.upload.UploadResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 车间用汽统计-月/年Controller
 *
 * @Author: sl
 * @Date: 2018-06-07 16:39
 */
@Api(tags = {"车间用汽统计-月/年"})
@RestController
@RequestMapping("/steamMeter/workshop")
public class SteamMeterWorkshopController {

    @Autowired
    private ISteamMeterReadingService steamMeterReadingService;

    @ApiOperation(value = "车间用汽统计-用汽曲线day")
    @RequestMapping(value = "/curveDataForDay", method = RequestMethod.POST)
    public EnergyResp<List<SteamMeterReadingResp>> curveDataForDay(@RequestBody @Valid SteamMeterDayReq req, BindingResult result) {

        // response
        EnergyResp<List<SteamMeterReadingResp>> resp = new EnergyResp<List<SteamMeterReadingResp>>();

        // param 校验，error back
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        resp.ok(steamMeterReadingService.getSteamMeterForDay(req));
        return resp;
    }

    @ApiOperation(value = "车间用汽统计-车间用汽详情day")
    @RequestMapping(value = "/detailDataForDay", method = RequestMethod.POST)
    public EnergyResp<PagedList<SteamMeterReadingResp>> detailDataForDay(@RequestBody @Valid SteamMeterDayReq req, BindingResult result) {

        // response
        EnergyResp<PagedList<SteamMeterReadingResp>> resp = new EnergyResp<PagedList<SteamMeterReadingResp>>();

        // param 校验，error back
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        resp.ok(steamMeterReadingService.getSteamMeterDetailForDay(req));
        return resp;
    }

    @ApiOperation(value = "车间用汽统计-用汽曲线year")
    @RequestMapping(value = "/curveDataForYear", method = RequestMethod.POST)
    public EnergyResp<List<SteamMeterReadingResp>> curveDataForYear(@RequestBody @Valid SteamMeterDayReq req, BindingResult result) {

        // response
        EnergyResp<List<SteamMeterReadingResp>> resp = new EnergyResp<List<SteamMeterReadingResp>>();

        // param 校验，error back
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        // 判断是否大于等于当月
        if (DateUtil.isGtThisMonth(req.getEndTime()) || DateUtil.isGtThisMonth(req.getStartTime())) {
            req.setFlag(1);
        } else {
            req.setFlag(0);
        }
        resp.ok(steamMeterReadingService.getSteamMeterForYear(req));
        return resp;
    }

    @ApiOperation(value = "车间用汽统计-车间用汽详情year")
    @RequestMapping(value = "/detailDataForYear", method = RequestMethod.POST)
    public EnergyResp<PagedList<SteamMeterReadingDetailResp>> detailDataForYear(@RequestBody @Valid SteamMeterDayReq req, BindingResult result) {

        // response
        EnergyResp<PagedList<SteamMeterReadingDetailResp>> resp = new EnergyResp<PagedList<SteamMeterReadingDetailResp>>();

        // param 校验，error back
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }

        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        // 判断是否大于等于当月
        if (DateUtil.isGtThisMonth(req.getEndTime()) || DateUtil.isGtThisMonth(req.getStartTime())) {
            req.setFlag(1);
        } else {
            req.setFlag(0);
        }
        resp.ok(steamMeterReadingService.getSteamMeterDetailForYear(req));
        return resp;
    }

    @ApiOperation(value = "车间用汽统计-车间用汽详情导出excel-day")
    @RequestMapping(value = "/detailDataForExcelDay", method = RequestMethod.POST)
    public EnergyResp<UploadResp> detailDataForExcelDay(@RequestBody @Valid SteamMeterDayReq req, BindingResult result) {

        // response
        EnergyResp<UploadResp> resp = new EnergyResp<>();

        // param 校验，error back
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        resp = steamMeterReadingService.exportDetailDataForExcelDay(req);
        return resp;
    }

    @ApiOperation(value = "车间用汽统计-车间用汽详情excel-year")
    @RequestMapping(value = "/detailDataForExcelYear", method = RequestMethod.POST)
    public EnergyResp<UploadResp> detailDataForExcelYear(@RequestBody @Valid SteamMeterDayReq req, BindingResult result) {

        // response
        EnergyResp<UploadResp> resp = new EnergyResp<>();

        // param 校验，error back
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }

        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        // 判断是否大于等于当月
        if (DateUtil.isGtThisMonth(req.getEndTime()) || DateUtil.isGtThisMonth(req.getStartTime())) {
            req.setFlag(1);
        } else {
            req.setFlag(0);
        }
        resp = steamMeterReadingService.detailDataForExcelYear(req);
        return resp;
    }

}
