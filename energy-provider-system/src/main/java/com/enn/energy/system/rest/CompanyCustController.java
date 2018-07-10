package com.enn.energy.system.rest;


import com.enn.constant.StatusCode;
import com.enn.energy.system.service.ICompanyCustService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.system.CompanyCust;
import com.enn.vo.energy.system.NodeTree;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  企业管理
 * </p>
 *
 * @author kongweiteng
 * @since 2018-06-05
 */
@RestController
@RequestMapping("/companyCust")
@Api(tags = {"企业管理"})
public class CompanyCustController {
    @Autowired
    private ICompanyCustService companyCustService;

    @ApiOperation(value = "根据id查询单条信息", notes = "one")
    @RequestMapping(value = "/one", method = RequestMethod.POST)
    public EnergyResp<CompanyCust> getOne(@RequestBody @Valid DefaultReq de, BindingResult result){
        EnergyResp energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        return companyCustService.getOne(de.getId());
    }

    /**
     * 查询机构树
     */
    @ApiOperation(value = "根据企业id查询组织树", notes = "tree")
    @RequestMapping(value = "/tree", method = RequestMethod.POST)
    public EnergyResp<NodeTree> getCompanyTree(@RequestBody @Valid DefaultReq de, BindingResult result){
        EnergyResp<NodeTree> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        return companyCustService.getCompanyTree(de.getId());
    }


    /**
     * 根据企业id查询企业下所有的子公司的id
     */
    @ApiOperation(value = "根据企业id获取所有子公司信息", notes = "getAllCompany")
    @RequestMapping(value = "/getAllCompany", method = RequestMethod.POST)
    public EnergyResp<ListResp<CompanyCust>> getAllCompany(@RequestBody @Valid DefaultReq de, BindingResult result){
        EnergyResp<ListResp<CompanyCust>> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        EnergyResp<List<CompanyCust>> allCompany = companyCustService.getAllCompany(de.getId());
        ListResp<CompanyCust> companyCustListResp= new ListResp<>();
        companyCustListResp.setList(allCompany.getData());
        energyResp.ok(companyCustListResp);
        return energyResp;
    }


}
