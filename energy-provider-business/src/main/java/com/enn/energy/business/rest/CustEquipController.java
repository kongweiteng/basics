package com.enn.energy.business.rest;


import com.enn.constant.StatusCode;
import com.enn.energy.business.service.ICustEquipService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.CustEquip;
import com.enn.vo.energy.business.req.UnitReq;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.system.NodeTree;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 核算单元管理
 * </p>
 *
 * @author kongweiteng
 * @since 2018-06-05
 */
@ApiIgnore
@RestController
@RequestMapping("/custEquip")
@Api(tags = {"核算单元管理"})
public class CustEquipController {
    @Autowired
    private ICustEquipService custEquipService;

    @ApiOperation(value = "根据企业id一级核算单元", notes = "parentEquip")
    @RequestMapping(value = "/parentEquip", method = RequestMethod.POST)
    public EnergyResp<ListResp<CustEquip>> getParentEqu(@RequestBody @Valid DefaultReq de, BindingResult result) {
        EnergyResp<ListResp<CustEquip>> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        EnergyResp<List<CustEquip>> parentEqu = custEquipService.getParentEqu(de.getId());
        ListResp<CustEquip> listResp = new ListResp<>();
        listResp.setList(parentEqu.getData());
        energyResp.ok(listResp);
        return energyResp;
    }


    /**
     * 查询组织结构下的核算单元树结构
     */
    @ApiOperation(value = "核算单元树", notes = "查询组织结构下的核算单元树结构")
    @RequestMapping(value = "/equipTree", method = RequestMethod.POST)
    public EnergyResp<NodeTree> getEquipTree(@RequestBody @Valid DefaultReq de, BindingResult result){
        EnergyResp<NodeTree> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }

        return custEquipService.getEquipTree(de);
    }

    /**
     * 核算单元列表查询
     */
    public EnergyResp<UnitResp> queryList(@RequestBody @Valid UnitReq unitReq, BindingResult result){
        EnergyResp<UnitResp> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }

        return  null;
    }
}
