package com.enn.web.controller;

import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.PagedList;
import com.enn.service.system.IEnergyBasicsService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.ElectricInfo;
import com.enn.vo.energy.system.SteamInfo;
import com.enn.vo.energy.system.req.ElectricInfoReq;
import com.enn.web.vo.WebReqId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * 能源基础信息管理接口
 * @author zxj
 * @since 2018-06-06
 *
 */
@RestController
@RequestMapping("/energyBasics")
@Api(value = "能源信息管理接口",tags = {"能源信息管理"})
public class EnergyBasicsController {

    private static final Logger logger = LoggerFactory.getLogger(EnergyBasicsController.class);

    @Autowired
    private IEnergyBasicsService energyBasicsService;

    @PostMapping("/addElectric")
    @ApiOperation(value = "电--新增或修改电基础信息", notes = "新增或修改电基础信息")
    public EnergyResp<Integer> addElectric(@RequestBody @Valid ElectricInfo electricInfo,BindingResult result){
        EnergyResp<Integer> energyResp = new EnergyResp<>();
        if(result.hasErrors()){
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        return energyBasicsService.addElectric(electricInfo);
    }

    @PostMapping("/delElectric")
    @ApiOperation(value = "电--删除电基础信息", notes = "删除电基础信息")
    public EnergyResp<Integer> delElectric(@RequestBody @Valid WebReqId reqId,BindingResult result){
        EnergyResp<Integer> energyResp = new EnergyResp<>();
        if(result.hasErrors()){
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        return energyBasicsService.delElectric(reqId.getId());
    }

    @PostMapping("/getElectricOne")
    @ApiOperation(value = "电--查询一条电基础信息", notes = "查询一条电基础信息")
    public EnergyResp<ElectricInfo> getElectricOne(@RequestBody @Valid WebReqId reqId,BindingResult result){
        EnergyResp<ElectricInfo> energyResp = new EnergyResp<>();
        if(result.hasErrors()){
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        return energyBasicsService.getElectricOne(reqId.getId());
    }

    @PostMapping("/getElectricList")
    @ApiOperation(value = "电--查询多条电基础信息", notes = "查询多条电基础信息")
    public EnergyResp<PagedList<ElectricInfo>> getElectricList(@RequestBody @Valid ElectricInfoReq infoReq,BindingResult result){
        EnergyResp<PagedList<ElectricInfo>> energyResp = new EnergyResp<>();
        if(result.hasErrors()){
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        if(infoReq.getPageNum() == null || infoReq.getPageNum() < 1){
            infoReq.setPageNum(1);
        }
        if(infoReq.getPageSize() == null || infoReq.getPageSize() < 1){
            infoReq.setPageSize(10);
        }
        return energyBasicsService.getElectricList(infoReq);
    }

    @PostMapping("/addSteam")
    @ApiOperation(value = "蒸汽--新增或修改蒸汽基础信息", notes = "新增或修改蒸汽基础信息")
    public EnergyResp<Integer> addSteam(@RequestBody @Valid SteamInfo steamInfo,BindingResult result){
        EnergyResp<Integer> energyResp = new EnergyResp<>();
        if(result.hasErrors()){
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        return energyBasicsService.addSteam(steamInfo);
    }

    @PostMapping("/delSteam")
    @ApiOperation(value = "蒸汽--删除蒸汽基础信息", notes = "删除蒸汽基础信息")
    public EnergyResp<Integer> delSteam(@RequestBody @Valid WebReqId reqId,BindingResult result){
        EnergyResp<Integer> energyResp = new EnergyResp<>();
        if(result.hasErrors()){
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        return energyBasicsService.delSteam(reqId.getId());
    }

    @PostMapping("/getSteamOne")
    @ApiOperation(value = "蒸汽--查询一条蒸汽基础信息", notes = "查询一条蒸汽基础信息")
    public EnergyResp<SteamInfo> getSteamOne(@RequestBody @Valid WebReqId reqId,BindingResult result){
        EnergyResp<SteamInfo> energyResp = new EnergyResp<>();
        if(result.hasErrors()){
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        energyResp = energyBasicsService.getSteamOne(reqId.getId());
//        if(StatusCode.SUCCESS.getCode().equals(energyResp.getCode()) && null != energyResp.getData()){
//            if(energyResp.getData().getSteamLadderPrices().size()>0){
//                energyResp.getData().getSteamLadderPrices().remove(0);
//            }
//        }
        return energyResp;
    }

    @PostMapping("/getSteamInfoList")
    @ApiOperation(value = "蒸汽--查询多条蒸汽基础信息", notes = "查询多条蒸汽基础信息")
    public EnergyResp<PagedList<SteamInfo>> getSteamInfoList(@RequestBody @Valid ElectricInfoReq infoReq,BindingResult result){
        EnergyResp<PagedList<SteamInfo>> energyResp = new EnergyResp<>();
        if(result.hasErrors()){
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        energyResp = energyBasicsService.getSteamInfoList(infoReq);
//        if(StatusCode.SUCCESS.getCode().equals(energyResp.getCode()) && null != energyResp.getData()){
//            List<SteamInfo> steamInfos = energyResp.getData().getData();
//            for (SteamInfo steamInfo:steamInfos){
//                if(steamInfo.getSteamLadderPrices().size()>0){
//                    steamInfo.getSteamLadderPrices().remove(0);
//                }
//            }
//        }
        return energyResp;
    }
}
