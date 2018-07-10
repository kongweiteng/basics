package com.enn.energy.business.rest;


import com.enn.constant.StatusCode;
import com.enn.energy.business.service.IAccountingUnitService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.CustMeter;
import com.enn.vo.energy.business.req.AccountUnitReq;
import com.enn.vo.energy.business.req.CompanyMeteReq;
import com.enn.vo.energy.business.req.UnitReq;
import com.enn.vo.energy.business.resp.MeterResp;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.system.NodeTree;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 核算单元管理
 * </p>
 *
 * @author kongweiteng
 * @since 2018-06-06
 */
@RestController
@RequestMapping("/accountingUnit")
@Api(tags = {"核算单元管理"})
public class AccountingUnitController {
    @Autowired
    private IAccountingUnitService accountingUnitService;


    @ApiOperation(value = "获取企业下一级核算单元", notes = "parentUnit")
    @RequestMapping(value = "/parentUnit", method = RequestMethod.POST)
    public EnergyResp<ListResp<AccountingUnit>> getParentEqu(@RequestBody @Valid DefaultReq de, BindingResult result) {
        EnergyResp<ListResp<AccountingUnit>> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        EnergyResp<List<AccountingUnit>> parentUnit = accountingUnitService.getParentUnit(de.getId());
        ListResp<AccountingUnit> listResp = new ListResp<>();
        listResp.setList(parentUnit.getData());
        energyResp.ok(listResp);
        return energyResp;
    }


    /**
     * 查询组织结构下的核算单元树结构
     */
    @ApiOperation(value = "核算单元树", notes = "查询组织结构下的核算单元树结构")
    @RequestMapping(value = "/equipTree", method = RequestMethod.POST)
    public EnergyResp<NodeTree> getEquipTree(@RequestBody @Valid DefaultReq de, BindingResult result) {
        EnergyResp<NodeTree> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }

        return accountingUnitService.getUnitTree(de);
    }


    /**
     * 核算单元列表查询
     */
    @ApiOperation(value = "核算列表(分页)", notes = "企业下或者核算单元下的子集")
    @RequestMapping(value = "/unitList", method = RequestMethod.POST)
    public EnergyResp<List<UnitResp>> queryList(@RequestBody @Valid UnitReq unitReq, BindingResult result) {
        EnergyResp<List<UnitResp>> energyResp = new EnergyResp();
        UnitResp unitResp = null;
        List<UnitResp> unitResps = new ArrayList<>();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        EnergyResp<List<AccountingUnit>> units = accountingUnitService.queryList(unitReq);

        //将数据封装返回
        for (AccountingUnit accountingUnit : units.getData()) {

            unitResp = new UnitResp();
            unitResp.setName(accountingUnit.getName());
            unitResp.setFormula(accountingUnit.getFormula());
            unitResp.setId(accountingUnit.getId());
            unitResp.setAccountingType(accountingUnit.getAccountingType());
            //根据id查找计量表信息
            List<CustMeter> accountMeter = accountingUnitService.getAccountMeter(accountingUnit.getId());
            MeterResp meterResp = null;
            List<MeterResp> meterResps = new ArrayList<>();
            if (accountMeter != null) {
                for (CustMeter custMeter : accountMeter) {
                    meterResp = new MeterResp();
                    meterResp.setMeterName(custMeter.getName());
                    meterResp.setMeterId(custMeter.getId());
                    meterResp.setLoopNumber(custMeter.getLoopNumber());
                    meterResp.setIsAccoun(custMeter.getIsAccoun());
                    meterResp.setEnergyType(custMeter.getEnergyType());
                    meterResps.add(meterResp);
                }
                unitResp.setMeters(meterResps);
            }
            unitResps.add(unitResp);
        }
        energyResp.ok(unitResps);
        return energyResp;
    }


    /**
     * 功能：
     * 1.根据企业id获取该企业下核算单元（以类型区分是车间还是生产线，类型参数为空时返回全部）
     * 2.根据核算单元id获取该核算单元下的子级核算单元（以类型区分是什么东西，类型参数为空时返回全部）
     */
    @ApiOperation(value = "查询核算单元", notes = "根据企业id或者核算单元id查询特定类型的核算单元")
    @RequestMapping(value = "/unitListByCondition", method = RequestMethod.POST)
    public EnergyResp<List<UnitResp>> queryAccountList(@RequestBody @Valid AccountUnitReq accountUnitReq, BindingResult result) {
        EnergyResp<List<UnitResp>> energyResp = new EnergyResp();
        List<UnitResp> unitResps = new ArrayList<>();
        UnitResp unitResp = null;
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }

        return accountingUnitService.queryAccountListT(accountUnitReq);
    }


    /**
     * 根据核算单元id获取所关联的计量表
     */
    @ApiOperation(value = "根据核算单元id查询一级计量表（电表、汽表）", notes = "如果一级核算单元下没有计量表，则查询下一级")
    @RequestMapping(value = "/queryMeterListByAccount", method = RequestMethod.POST)
    public EnergyResp<List<MeterResp>> queryMeterListByAccount(@RequestBody @Valid DefaultReq de, BindingResult result) {
        EnergyResp<List<MeterResp>> resp = new EnergyResp<>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        List<MeterResp> meterResps = new ArrayList<>();
        List<MeterResp> account2 = accountingUnitService.getAccountEleMeterT(de.getId(), "01");
        for (MeterResp meterResp : account2) {
            meterResps.add(meterResp);
        }

        List<MeterResp> account1 = accountingUnitService.getAccountEleMeterT(de.getId(), "02");
        for (MeterResp meterResp : account1) {
            meterResps.add(meterResp);
        }

        resp.ok(meterResps);
        return resp;
    }


    /**
     * 根据核算单元id获取所关联的计量表
     */
    @ApiOperation(value = "根据核算单元id查询一级计量表（根据参数类型查不同表计）", notes = "如果一级核算单元下没有计量表，则查询下一级")
    @RequestMapping(value = "/queryMeterListByAccountAndType", method = RequestMethod.POST)
    public EnergyResp<List<MeterResp>> queryMeterListByAccountAndType(@RequestBody @Valid CompanyMeteReq de, BindingResult result) {
        EnergyResp<List<MeterResp>> resp = new EnergyResp<>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        List<MeterResp> accountMeterT = accountingUnitService.getAccountEleMeterT(de.getId(), de.getEnergyType());
        resp.ok(accountMeterT);
        return resp;
    }


    /**
     * 保存核算单元
     * 当 id为空时为inser  id不为空位updata
     */
    @ApiOperation(value = "保存核算单元", notes = "保存核算单元 (id为空时 insert，id不为空时 update)")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public EnergyResp<Integer> save(@RequestBody @Valid AccountingUnit ac, BindingResult result) {
        EnergyResp<Integer> resp = new EnergyResp<>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        resp = accountingUnitService.save(ac);
        return resp;
    }

    /**
     * 禁用核算单元
     */
    @ApiOperation(value = "禁用核算单元", notes = "")
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    public EnergyResp<Integer> disable(@RequestBody @Valid DefaultReq de, BindingResult result) {
        EnergyResp<Integer> resp = new EnergyResp<>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        resp = accountingUnitService.disable(de);
        return resp;
    }


    /**
     * 根据 用电企业id 查询 企业下一级核算单元的 计量表信息
     * 如果一级核算单元没有设备 则查询下一级
     * 0.根据企业id查询子公司的ids
     * 1.根据子公司的ids 查询分别下的一级核算单元 ids
     * 2.根据所有的一级核算单元ids查询
     * 3.根据核算单元ids查询 旗下计量表，如果旗下没有则查找下一级
     */
    @ApiOperation(value = "根据企业id查询计量表（电表、汽表）", notes = "根据企业id查询计量表")
    @RequestMapping(value = "/queryMeterListByCompany", method = RequestMethod.POST)
    public EnergyResp<List<MeterResp>> queryMeterListByCompany(@RequestBody @Valid DefaultReq de, BindingResult result) {
        EnergyResp<List<MeterResp>> resp = new EnergyResp<>();
        List<MeterResp> meters = new ArrayList<>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }

        //查询电表
        List<MeterResp> mete1 = accountingUnitService.queryEleMeterListByCompany(de.getId(), "01");
        for (MeterResp m : mete1) {
            meters.add(m);
        }
        //查询蒸汽表、
        List<MeterResp> meter2 = accountingUnitService.queryEleMeterListByCompany(de.getId(), "02");
        for (MeterResp m : meter2) {
            meters.add(m);
        }
        resp.ok(meters);
        return resp;
    }


    /**
     * 根据 用电企业id 查询 企业下一级核算单元的 计量表信息
     * 如果一级核算单元没有设备 则查询下一级
     * 0.根据企业id查询子公司的ids
     * 1.根据子公司的ids 查询分别下的一级核算单元 ids
     * 2.根据所有的一级核算单元ids查询
     * 3.根据核算单元ids查询 旗下计量表，如果旗下没有则查找下一级
     */
    @ApiOperation(value = "根据企业id查询计量表（根据参数类型查不同表计）", notes = "根据参数类型查不同表计")
    @RequestMapping(value = "/queryMeterListByCompanyAndType", method = RequestMethod.POST)
    public EnergyResp<List<MeterResp>> queryMeterListByCompanyAndType(@RequestBody @Valid CompanyMeteReq de, BindingResult result) {
        EnergyResp<List<MeterResp>> resp = new EnergyResp<>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        List<MeterResp> meteResps = accountingUnitService.queryEleMeterListByCompany(de.getId(), de.getEnergyType());
        resp.ok(meteResps);
        return resp;
    }


    /**
     * 查询车间下的计量表信息
     */

    @ApiOperation(value = "根据车间id查询计量表（没有返回空）", notes = "根据车间id查询计量表（没有返回空）")
    @RequestMapping(value = "/queryStaMeterByCompany", method = RequestMethod.POST)
    public EnergyResp<MeterResp> queryStaMeterByCompany(@RequestBody @Valid DefaultReq de, BindingResult result) {
        EnergyResp<MeterResp> resp = new EnergyResp<>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        EnergyResp<MeterResp> respEnergyResp = accountingUnitService.queryStaMeterByCompany(de);

        return respEnergyResp;
    }


    /**
     * 根据id查询 核算单元信息
     */
    @ApiOperation(value = "根据主键查询", notes = "")
    @RequestMapping(value = "/getOne", method = RequestMethod.POST)
    public EnergyResp<AccountingUnit> getOne(@RequestBody @Valid DefaultReq de, BindingResult result) {
        EnergyResp<AccountingUnit> resp = new EnergyResp<>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        EnergyResp<AccountingUnit> one = accountingUnitService.getOne(de);
        return one;

    }


    /**
     * 根据表号查询核算单元信息
     */
    @ApiOperation(value = "根据表号查询核算单元信息", notes = "")
    @RequestMapping(value = "/getUnitByMete", method = RequestMethod.POST)
    public EnergyResp<List<AccountingUnit>> getUnitByMete(@RequestParam("meter") String meter) {
        EnergyResp<List<AccountingUnit>> resp = new EnergyResp<>();
        EnergyResp<List<AccountingUnit>> unitByMete = accountingUnitService.getUnitByMete(meter);
        return unitByMete;
    }

}
