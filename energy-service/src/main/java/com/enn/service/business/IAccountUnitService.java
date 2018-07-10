package com.enn.service.business;

import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.req.AccountUnitReq;
import com.enn.vo.energy.business.req.CompanyMeteReq;
import com.enn.vo.energy.business.req.UnitReq;
import com.enn.vo.energy.business.resp.MeterResp;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.system.NodeTree;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "energy-zuul-gateway")
public interface IAccountUnitService {

    /**
     * 获取企业下一级核算单元
     *
     * @param de
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/accountingUnit/parentUnit", method = RequestMethod.POST)
    public EnergyResp<ListResp<AccountingUnit>> getParentEqu(DefaultReq de);

    /**
     * 功能：
     * * 1.根据企业id获取该企业下核算单元（以类型区分是车间还是生产线，类型参数为空时返回全部）
     * * 2.根据核算单元id获取该核算单元下的子级核算单元（以类型区分是什么东西，类型参数为空时返回全部）
     * @param accountUnitReq
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/accountingUnit/unitListByCondition", method = RequestMethod.POST)
    public EnergyResp<List<UnitResp>> queryAccountList(AccountUnitReq accountUnitReq);

    /**
     * 核算单元列表(分页)
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/accountingUnit/unitList", method = RequestMethod.POST)
    public EnergyResp<List<UnitResp>> queryList(UnitReq unitReq);


    /**
     * 保存核算单元
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/accountingUnit/save", method = RequestMethod.POST)
    public EnergyResp<Integer> save(AccountingUnit ac);

    /**
     * 禁用核算单元
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/accountingUnit/disable", method = RequestMethod.POST)
    public EnergyResp<Integer> disable(DefaultReq de);

    /**
     * 核算单元树
     * @param de
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/accountingUnit/equipTree", method = RequestMethod.POST)
    public EnergyResp<NodeTree> getUnitTree(DefaultReq de);

    /**
     * 根据核算单元id获取所关联的计量表
     *
     * @param de
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/accountingUnit/queryMeterListByAccount", method = RequestMethod.POST)
    public EnergyResp<List<MeterResp>> queryMeterListByAccount(DefaultReq de);


    /**
     * 根据核算单元id获取所关联的计量表--根据参数类型不同
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/accountingUnit/queryMeterListByAccountAndType", method = RequestMethod.POST)
    public EnergyResp<List<MeterResp>> queryMeterListByAccountAndType(@RequestBody CompanyMeteReq de);


    /**\
     *  根据企业id查询计量表（根据参数类型查不同表计）
     * @param de
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/accountingUnit/queryMeterListByCompanyAndType", method = RequestMethod.POST)
    public EnergyResp<List<MeterResp>> queryMeterListByCompanyAndType(@RequestBody  CompanyMeteReq de);

    /**
     * 根据车间id查询 计量表（没有就不返回）
     * @param defaultReq
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/accountingUnit/queryStaMeterByCompany", method = RequestMethod.POST)
    public EnergyResp<MeterResp> queryStaMeterByCompany(DefaultReq defaultReq);

    @RequestMapping(value = "/energy-proxy/energy-provider-business/accountingUnit/getOne", method = RequestMethod.POST)
    public EnergyResp<AccountingUnit> getOne(DefaultReq d);

    @RequestMapping(value = "/energy-proxy/energy-provider-business/accountingUnit/getUnitByMete", method = RequestMethod.POST)
    public EnergyResp<List<AccountingUnit>> getUnitByMete(@RequestParam("meter") String meter);
}
