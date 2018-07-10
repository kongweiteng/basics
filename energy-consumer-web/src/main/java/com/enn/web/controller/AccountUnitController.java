package com.enn.web.controller;

import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.service.business.IAccountUnitService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.req.UnitReq;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.system.NodeTree;
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

@RestController
@RequestMapping("/accountUnit")
@Api(tags = {"核算单元管理"})
public class AccountUnitController {
    @Autowired
    private IAccountUnitService accountUnitService;

    /**
     * 查询组织结构下的核算单元树结构
     */
    @ApiOperation(value = "核算单元树", notes = "查询组织结构下的核算单元树结构")
    @RequestMapping(value = "/unitTree", method = RequestMethod.POST)
    public EnergyResp<NodeTree> getUnitTree(@RequestBody @Valid DefaultReq de, BindingResult result) {
        EnergyResp<NodeTree> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }

        return accountUnitService.getUnitTree(de);
    }

    /**
     * 核算单元列表
     */
    @ApiOperation(value = "核算列表(分页)", notes = "企业下或者核算单元下的子集")
    @RequestMapping(value = "/unitList", method = RequestMethod.POST)
    public EnergyResp<List<UnitResp>> queryList(@RequestBody @Valid UnitReq unitReq, BindingResult result){
        EnergyResp<List<UnitResp>> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        EnergyResp<List<UnitResp>> listEnergyResp = accountUnitService.queryList(unitReq);
        return listEnergyResp;
    }

}
