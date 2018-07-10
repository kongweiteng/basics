package com.enn.energy.business.service;

import com.baomidou.mybatisplus.service.IService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.CustMeter;
import com.enn.vo.energy.business.req.AccountUnitReq;
import com.enn.vo.energy.business.req.UnitReq;
import com.enn.vo.energy.business.resp.MeterResp;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.business.vo.SteamUnitVo;
import com.enn.vo.energy.system.NodeTree;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kongweiteng
 * @since 2018-06-06
 */
public interface IAccountingUnitService extends IService<AccountingUnit> {

    /**
     * 根据核算单元id获取核算单元信息
     */
    public EnergyResp<AccountingUnit> getOneUnit(Long id);

    /**
     * 根据企业id获取子级的核算单元
     */
    public EnergyResp<List<AccountingUnit>> getParentUnit(Long companyId);

    /**
     * 根据企业id查询核算单元树
     */
    public EnergyResp<NodeTree> getUnitTree(DefaultReq defaultReq);

    /**
     * 根据父级id 查询子核算单元
     */
    public EnergyResp<List<AccountingUnit>> getChildUnit(Long parentId);


    /**
     * 核算单元列表查询 -分页
     */
    public EnergyResp<List<AccountingUnit>> queryList( UnitReq unitReq);
    /**
     * 核算单元列表查询
     */
    public EnergyResp<List<AccountingUnit>> queryAccountList( AccountUnitReq accountUnitReq);
    /**
     * 核算单元列表查询
     */
    public EnergyResp<List<UnitResp>> queryAccountListT( AccountUnitReq accountUnitReq);

    /**
     * 根据企业ids查询 这些企业下的核算单元
     */
    public EnergyResp<List<AccountingUnit>> getUnitByCompanyisd(List<Long> ids);
    /**
     * 根据企业ids查询 这些企业下的核算单元
     */
    public EnergyResp<List<AccountingUnit>> getPageUnitByCompanyisd(List<Long> ids,Integer pageNo,Integer pageSize);

    /**
     * 根据核算单元id 查询该核算单元以及旗下所有的核算单元
     */
    public EnergyResp<List<AccountingUnit>> getUnitByParentUnit(Long id);

    /**
     * 根据核算单元id 查询该核算单元以及旗下所有的核算单元
     */
    public EnergyResp<List<AccountingUnit>> getPageUnitByParentUnit(Long id,Integer pageNo,Integer pageSize);


    /**
     * 查询核算单元下的表
     * @param id
     * @return
     */
    public List<CustMeter> getAccountMeter(Long id);

    /**
     * 查询核算单元下的表--根据参数类型查不同表计
     * @param id
     * @return
     */
    public List<CustMeter> getAccountEleMeter(Long id,String energyType);

    /**
     * 根据 核算单元ids 查询计量表信息列表
     * @param ids
     * @return
     */
    public List<CustMeter> getAccountMeter(List<Long> ids);
    /**
     * 根据 核算单元ids 查询计量表 --根据参数类型查不同表计
     * @param ids
     * @return
     */
    public List<CustMeter> getAccountEleMeter(List<Long> ids,String energyType);


    /**
     * 根据 核算单元ids 查询计量表 --蒸汽表
     * @param ids
     * @return
     */
    public List<CustMeter> getAccountSteaMeter(List<Long> ids);

    public List<MeterResp> getAccountMeterT(Long id);


    public List<MeterResp> getAccountEleMeterT(Long id,String energyType);

    /**
     * 根据 用电企业id 查询 企业下一级核算单元的 计量表信息
     * 如果一级核算单元没有设备 则查询下一级
     * 0.根据企业id查询子公司的ids
     * 1.根据子公司的ids 查询分别下的一级核算单元 ids
     * 2.根据所有的一级核算单元ids查询
     * 3.根据核算单元ids查询 旗下计量表，如果旗下没有则查找下一级
     */
    public List<MeterResp> queryMeterListByCompany(Long id);


    /**
     * 功能步骤通上，根据参数类型查不同表计
     * @param id
     * @return
     */
    public List<MeterResp> queryEleMeterListByCompany(Long id,String energyType);


    /**
     * 保存核算单元信息接口
     */
    EnergyResp<Integer> save(AccountingUnit ac);


    /*(*
      禁用核算单元
     */
    public EnergyResp<Integer> disable(DefaultReq de);


    /**
     * 查询车间下的 蒸汽表
     */

    public EnergyResp<MeterResp> queryStaMeterByCompany(DefaultReq de);


    /**
     * 根据主建查询
     */
    public EnergyResp<AccountingUnit> getOne(DefaultReq de);

    public EnergyResp<List<AccountingUnit>> getUnitByMete(String de);


    public List<AccountingUnit> getUnitByCompanyisd(String companyId,String accountingType);


    /**
     * 根据表号查询核算单元信息
     * @param meterNo
     * @return
     */
    public SteamUnitVo getUnitName(String meterNo);
}
