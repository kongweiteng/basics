package com.enn.energy.system.rest;


import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.PagedList;
import com.enn.energy.system.service.ElectricInfoService;
import com.enn.energy.system.service.SteamInfoService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.ElectricInfo;
import com.enn.vo.energy.system.SteamInfo;
import com.enn.vo.energy.system.req.ElectricInfoReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 能源基础信息管理接口
 * @author zxj
 * @since 2018-06-05
 */
@RestController
@RequestMapping("/energyBasics")
@Api(value = "能源基础信息管理接口",tags = {"能源基础信息管理"})
public class EnergyBasicsController {

    private static final Logger logger = LoggerFactory.getLogger(EnergyBasicsController.class);

    @Resource
    private ElectricInfoService electricInfoService;
    @Resource
    private SteamInfoService steamInfoService;

    @PostMapping("/addElectric")
    @ApiOperation(value = "新增或修改电基础信息", notes = "新增或修改电基础信息")
    public EnergyResp<Integer> addElectric(@RequestBody ElectricInfo electricInfo){
        EnergyResp<Integer> energyResp = new EnergyResp<>();
        try {
            energyResp = electricInfoService.addElectric(electricInfo);
        }catch (Exception e){
            logger.info(electricInfo.getCustId()+"----新增或修改电基础信息出错");
            throw new EnergyException(StatusCode.ERROR.getCode(), "新增或修改电基础信息出现异常！", e.getMessage());
        }
        return energyResp;
    }

    @PostMapping("/delElectric")
    @ApiOperation(value = "删除电基础信息", notes = "删除电基础信息")
    public EnergyResp<Integer> delElectric(@RequestBody String id){
        EnergyResp<Integer> energyResp = new EnergyResp<>();
        try {
            energyResp = electricInfoService.delElectric(id);
        }catch (Exception e){
            logger.info(id+"----删除电基础信息出错");
            throw new EnergyException(StatusCode.ERROR.getCode(), "删除电基础信息出现异常！", e.getMessage());
        }
        return energyResp;
    }

    @PostMapping("/getElectricOne")
    @ApiOperation(value = "查询一条电基础信息", notes = "查询一条电基础信息")
    public EnergyResp<ElectricInfo> getElectricOne(@RequestBody String id){
        EnergyResp<ElectricInfo> energyResp = new EnergyResp<>();
        try{
            energyResp = electricInfoService.getElectricOne(id);
        }catch (Exception e){
            logger.info(id+"----查询一条电基础信息");
            throw new EnergyException(StatusCode.ERROR.getCode(), "查询一条电基础信息出现异常！", e.getMessage());
        }
        return energyResp;
    }

    @PostMapping("/getElectricList")
    @ApiOperation(value = "查询多条电基础信息", notes = "查询多条电基础信息")
    public EnergyResp<PagedList<ElectricInfo>> getElectricList(@RequestBody ElectricInfoReq infoReq){
        EnergyResp<PagedList<ElectricInfo>> energyResp = new EnergyResp<>();
        try{
            energyResp = electricInfoService.getElectricList(infoReq);
        }catch (Exception e){
            logger.info(infoReq.getCustId()+"----查询多条电基础信息");
            throw new EnergyException(StatusCode.ERROR.getCode(), "查询多条电基础信息出现异常！", e.getMessage());
        }
        return energyResp;
    }

    @PostMapping("/getElectricInfo")
    @ApiOperation(value = "根据客户id及日期，查询电基础信息", notes = "根据客户id及日期，查询电基础信息")
    public EnergyResp<ElectricInfo> getElectricInfo(@RequestBody ElectricInfoReq infoReq){
        return electricInfoService.getElectricInfo(infoReq);
    }


    @PostMapping("/addSteam")
    @ApiOperation(value = "新增或修改蒸汽基础信息", notes = "新增或修改蒸汽基础信息")
    public EnergyResp<Integer> addSteam(@RequestBody SteamInfo steamInfo){
        EnergyResp<Integer> energyResp = new EnergyResp<>();
        try {
            energyResp = steamInfoService.addSteam(steamInfo);
        }catch (Exception e){
            logger.info(steamInfo.getCustId()+"----新增或修改蒸汽基础信息出错");
            throw new EnergyException(StatusCode.ERROR.getCode(), "新增或修改蒸汽基础信息出现异常！", e.getMessage());
        }
        return energyResp;
    }

    @PostMapping("/delSteam")
    @ApiOperation(value = "删除蒸汽基础信息", notes = "删除蒸汽基础信息")
    public EnergyResp<Integer> delSteam(@RequestBody String id){
        EnergyResp<Integer> energyResp = new EnergyResp<>();
        try {
            energyResp = steamInfoService.delSteam(id);
        }catch (Exception e){
            logger.info(id+"----删除蒸汽基础信息出错");
            throw new EnergyException(StatusCode.ERROR.getCode(), "删除蒸汽基础信息出现异常！", e.getMessage());
        }
        return energyResp;
    }

    @PostMapping("/getSteamOne")
    @ApiOperation(value = "查询一条蒸汽基础信息", notes = "查询一条蒸汽基础信息")
    public EnergyResp<SteamInfo> getSteamOne(@RequestBody String id){
        EnergyResp<SteamInfo> energyResp = new EnergyResp<>();
        try{
            energyResp = steamInfoService.getSteamOne(id);
        }catch (Exception e){
            logger.info(id+"----查询一条蒸汽基础信息");
            throw new EnergyException(StatusCode.ERROR.getCode(), "查询一条蒸汽基础信息出现异常！", e.getMessage());
        }
        return energyResp;
    }

    @PostMapping("/getSteamInfoList")
    @ApiOperation(value = "查询多条蒸汽基础信息", notes = "查询多条蒸汽基础信息")
    public EnergyResp<PagedList<SteamInfo>> getSteamInfoList(@RequestBody ElectricInfoReq infoReq){
        EnergyResp<PagedList<SteamInfo>> energyResp = new EnergyResp<>();
        try{
            energyResp = steamInfoService.getSteamInfoList(infoReq);
        }catch (Exception e){
            logger.info(infoReq.getCustId()+"----查询多条蒸汽基础信息");
            throw new EnergyException(StatusCode.ERROR.getCode(), "查询多条蒸汽基础信息出现异常！", e.getMessage());
        }
        return energyResp;
    }

}
