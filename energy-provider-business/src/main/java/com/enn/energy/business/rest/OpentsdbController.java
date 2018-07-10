package com.enn.energy.business.rest;

import com.enn.constant.StatusCode;
import com.enn.energy.business.service.IOpentsdbService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.req.LastReq;
import com.enn.vo.energy.business.req.LastStaReq;
import com.enn.vo.energy.business.req.SamplDataReq;
import com.enn.vo.energy.business.req.SamplDataStaReq;
import com.enn.vo.energy.business.resp.LastResp;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/opentsdb")
@Api(tags = {"查询大数据平台"})
public class OpentsdbController {


    @Resource
    private IOpentsdbService opentsdbService;

    /*
     * 获取采样数据
     */
    @ApiOperation(value = "采样查询", notes = "sampl")
    @RequestMapping(value = "/sampl/downsample", method = RequestMethod.POST)
    public EnergyResp<ListResp<RmiSamplDataResp>> getSamplData(@RequestBody @Valid SamplDataReq samplDataReq, BindingResult result) {
        EnergyResp energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        return opentsdbService.getSamplData(samplDataReq);
    }


    /*
     * 获取采样数据--不同站点
     */
    @ApiOperation(value = "采样查询--不同站点", notes = "sampl")
    @RequestMapping(value = "/getSamplDataStaReq", method = RequestMethod.POST)
    public EnergyResp<ListResp<RmiSamplDataResp>> getSamplDataStaReq(@RequestBody @Valid SamplDataStaReq samplDataReq, BindingResult result) {
        EnergyResp energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        return opentsdbService.getSamplData(samplDataReq);
    }

    /**
     * 获取最新数据-设备的最后一条--不同站点
     */
    @ApiOperation(value = "最后一条数据--不同站点", notes = "last")
    @RequestMapping(value = "/staLast", method = RequestMethod.POST)
    public EnergyResp<ListResp<LastResp>> getStaLast(@RequestBody @Valid LastStaReq last, BindingResult result) {
        EnergyResp energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        EnergyResp<ListResp<LastResp>> lastDatas = opentsdbService.getLastDatas(last);
        return lastDatas;
    }


    /**
     * 获取最新数据-设备的最后一条
     */
    @ApiOperation(value = "最后一条数据", notes = "last")
    @RequestMapping(value = "/last", method = RequestMethod.POST)
    public EnergyResp<ListResp<LastResp>> getLast(@RequestBody @Valid LastReq last, BindingResult result) {
        EnergyResp energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        EnergyResp<ListResp<LastResp>> lastDatas = opentsdbService.getLastDatas(last);
        return lastDatas;
    }





    /**
     * 获取电表的
     */

}
