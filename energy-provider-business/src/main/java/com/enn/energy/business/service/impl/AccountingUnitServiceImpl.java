package com.enn.energy.business.service.impl;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.enn.energy.business.dao.AccountingUnitMapper;
import com.enn.energy.business.dao.CustMeterMapper;
import com.enn.energy.business.dao.CustWifiMapper;
import com.enn.energy.business.service.IAccountingUnitService;
import com.enn.service.system.ICompanyService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.CustMeter;
import com.enn.vo.energy.business.po.CustWifi;
import com.enn.vo.energy.business.req.AccountUnitReq;
import com.enn.vo.energy.business.req.UnitReq;
import com.enn.vo.energy.business.resp.MeterResp;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.business.vo.SteamUnitVo;
import com.enn.vo.energy.system.CompanyCust;
import com.enn.vo.energy.system.NodeTree;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kongweiteng
 * @since 2018-06-06
 */
@Service
public class AccountingUnitServiceImpl extends ServiceImpl<AccountingUnitMapper, AccountingUnit> implements IAccountingUnitService {

    @Autowired
    private ICompanyService companyService;

    @Resource
    private CustMeterMapper custMeterMapper;

    @Autowired
    private AccountingUnitMapper accountingUnitMapper;
    @Autowired
    private CustWifiMapper custWifiMapper;

    @Override
    public EnergyResp<AccountingUnit> getOneUnit(Long id) {
        EnergyResp<AccountingUnit> resp = new EnergyResp<>();
        //根据id查询信息
        AccountingUnit accountingUnit = accountingUnitMapper.selectById(id);
        resp.ok(accountingUnit);
        return resp;
    }

    @Override
    public EnergyResp<List<AccountingUnit>> getParentUnit(Long companyId) {
        EnergyResp<List<AccountingUnit>> resp = new EnergyResp<>();
        EntityWrapper<AccountingUnit> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", "0").eq("cust_id", companyId).isNull("parent_id");
        List<AccountingUnit> custEquips = accountingUnitMapper.selectList(wrapper);
        resp.ok(custEquips);
        return resp;
    }

    @Override
    public EnergyResp<NodeTree> getUnitTree(DefaultReq defaultReq) {
        //获取到企业机构树
        EnergyResp<NodeTree> companyTree = companyService.getCompanyTree(defaultReq);
        //往组织机构树中添加核算单元节点
        unitTree(companyTree.getData());
        return companyTree;
    }

    @Override
    public EnergyResp<List<AccountingUnit>> getChildUnit(Long parentId) {
        EnergyResp<List<AccountingUnit>> resp = new EnergyResp<>();
        EntityWrapper<AccountingUnit> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", "0").eq("parent_id", parentId);
        List<AccountingUnit> custEquips = accountingUnitMapper.selectList(wrapper);
        resp.ok(custEquips);
        return resp;
    }

    /**
     * 查询核算单元列表-- 分页
     * 如果拿到的id是企业，先查询到该企业以及该企业下所有子公司的list
     * 如果拿到的id是核算单元，查询该核算单元以及旗下所有的子核算单元
     *
     * @param unitReq
     * @return
     */
    @Override
    public EnergyResp<List<AccountingUnit>> queryList(UnitReq unitReq) {
        EnergyResp<List<UnitResp>> unitByCompanyisd = new EnergyResp<>();
        UnitResp unitResp = null;
        List<UnitResp> unitResps = new ArrayList<>();
        //判断类型
        EnergyResp<List<AccountingUnit>> units = new EnergyResp<>();
        if (unitReq.getNodeType().equals("unit")) {//是核算单元
            units = getPageUnitByParentUnit(unitReq.getId(), unitReq.getPageNo(), unitReq.getPageSize());
        } else {//是企业
            //根据企业id获取到list
            DefaultReq defaultReq = new DefaultReq();
            defaultReq.setId(unitReq.getId());
            EnergyResp<ListResp<CompanyCust>> allCompany = companyService.getAllCompany(defaultReq);
            //根据企业查询所有的核算单元
            List<Long> ids = new ArrayList<>();
            if (allCompany.getData() != null) {
                List<CompanyCust> list = allCompany.getData().getList();
                for (CompanyCust companyCust : list) {
                    ids.add(companyCust.getId());
                }
            }
            //根据ids查询出所有的核算单元列表
            units = getPageUnitByCompanyisd(ids, unitReq.getPageNo(), unitReq.getPageSize());
        }

        unitByCompanyisd.ok(unitResps);
        return units;
    }


    /**
     * 查询核算单元列表-- 不分页
     * 如果拿到的id是企业，先查询到该企业以及该企业下所有子公司的list
     * 如果拿到的id是核算单元，查询该核算单元以及旗下所有的子核算单元
     *
     * @param
     * @return
     */
    @Override
    public EnergyResp<List<AccountingUnit>> queryAccountList(AccountUnitReq accountUnitReq) {
        EnergyResp<List<UnitResp>> unitByCompanyisd = new EnergyResp<>();
        UnitResp unitResp = null;
        List<UnitResp> unitResps = new ArrayList<>();
        //判断类型
        EnergyResp<List<AccountingUnit>> units = new EnergyResp<>();
        if (accountUnitReq.getIsAccount()) {//是核算单元
            units = getUnitByParentUnit(accountUnitReq.getId());
        } else {//是企业
            //根据企业id获取到list
            DefaultReq defaultReq = new DefaultReq();
            defaultReq.setId(accountUnitReq.getId());
            EnergyResp<ListResp<CompanyCust>> allCompany = companyService.getAllCompany(defaultReq);
            //根据企业查询所有的核算单元
            List<Long> ids = new ArrayList<>();
            if (allCompany.getData() != null) {
                List<CompanyCust> list = allCompany.getData().getList();
                for (CompanyCust companyCust : list) {
                    ids.add(companyCust.getId());
                }
            }
            //根据ids查询出所有的核算单元列表
            if(ids.size()>0){
                units = getUnitByCompanyisd(ids);
            }
        }

        return units;
    }

    @Override
    public EnergyResp<List<UnitResp>> queryAccountListT(AccountUnitReq accountUnitReq) {
        EnergyResp<List<UnitResp>> energyResp = new EnergyResp();
        List<UnitResp> unitResps = new ArrayList<>();
        UnitResp unitResp = null;
        //获取核算单元数据
        EnergyResp<List<AccountingUnit>> listEnergyResp = queryAccountList(accountUnitReq);
        //筛选数据
        if (listEnergyResp.getData() != null) {
            for (AccountingUnit accountingUnit : listEnergyResp.getData()) {
                if (accountUnitReq.getAccountingType() != null) {
                    if (accountUnitReq.getAccountingType().equals(accountingUnit.getAccountingType())) {
                        unitResp = new UnitResp();
                        unitResp.setId(accountingUnit.getId());
                        unitResp.setName(accountingUnit.getName());
                        unitResp.setFormula(accountingUnit.getFormula());
                        unitResp.setAccountingType(accountingUnit.getAccountingType());
                        //根据id查找计量表信息
                        List<CustMeter> accountMeter = getAccountMeter(accountingUnit.getId());
                        MeterResp meterResp = null;
                        List<MeterResp> meterResps = new ArrayList<>();
                        if (accountMeter != null) {
                            for (CustMeter custMeter : accountMeter) {
                                Long wifiId = custMeter.getWifiId();
                                //根据网关id查询网关信息
                                CustWifi custWifi = custWifiMapper.selectById(wifiId);
                                meterResp = new MeterResp();
                                meterResp.setMeterName(custMeter.getName());
                                meterResp.setMeterId(custMeter.getId());
                                if(custMeter.getEnergyType().equals("01")){
                                    meterResp.setLoopNumber(custMeter.getLoopNumber());
                                }else {
                                    meterResp.setLoopNumber(custMeter.getLoopNumber());
                                }
                                meterResp.setIsAccoun(custMeter.getIsAccoun());
                                meterResp.setEnergyType(custMeter.getEnergyType());
                                if(custWifi!=null){
                                    meterResp.setStaId(custWifi.getStaId());
                                }
                                meterResps.add(meterResp);
                            }
                            unitResp.setMeters(meterResps);
                        }
                        unitResps.add(unitResp);
                    }
                } else {
                    unitResp = new UnitResp();
                    unitResp.setId(accountingUnit.getId());
                    unitResp.setName(accountingUnit.getName());
                    unitResp.setFormula(accountingUnit.getFormula());
                    unitResp.setAccountingType(accountingUnit.getAccountingType());
                    //根据id查找计量表信息
                    List<CustMeter> accountMeter = getAccountMeter(accountingUnit.getId());
                    MeterResp meterResp = null;
                    List<MeterResp> meterResps = new ArrayList<>();
                    if (accountMeter != null) {
                        for (CustMeter custMeter : accountMeter) {
                            Long wifiId = custMeter.getWifiId();
                            //根据网关id查询网关信息
                            CustWifi custWifi = custWifiMapper.selectById(wifiId);
                            meterResp = new MeterResp();
                            meterResp.setMeterName(custMeter.getName());
                            meterResp.setMeterId(custMeter.getId());
                            if(custMeter.getEnergyType().equals("01")){
                                meterResp.setLoopNumber(custMeter.getLoopNumber());
                            }else {
                                meterResp.setLoopNumber(custMeter.getLoopNumber());
                            }
                            meterResp.setIsAccoun(custMeter.getIsAccoun());
                            meterResp.setEnergyType(custMeter.getEnergyType());
                            if(custWifi!=null){
                                meterResp.setStaId(custWifi.getStaId());
                            }
                            meterResps.add(meterResp);
                        }
                        unitResp.setMeters(meterResps);
                    }
                    unitResps.add(unitResp);
                }
            }
        }
        energyResp.ok(unitResps);
        return energyResp;
    }


    @Override
    public EnergyResp<List<AccountingUnit>> getUnitByCompanyisd(List<Long> ids) {
        EnergyResp<List<AccountingUnit>> resp = new EnergyResp<>();
        EntityWrapper<AccountingUnit> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", "0").in("cust_id", ids).eq("is_temp","0");
        List<AccountingUnit> custEquips = accountingUnitMapper.selectList(wrapper);
        resp.ok(custEquips);
        return resp;
    }

    @Override
    public EnergyResp<List<AccountingUnit>> getPageUnitByCompanyisd(List<Long> ids, Integer pageNo, Integer pageSize) {
        EnergyResp<List<AccountingUnit>> resp = new EnergyResp<>();
        EntityWrapper<AccountingUnit> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", "0").in("cust_id", ids);
        Page<AccountingUnit> page = new Page<AccountingUnit>(pageNo, pageSize);
        List<AccountingUnit> custEquips = accountingUnitMapper.selectPage(page, wrapper);
        resp.ok(custEquips);
        return resp;
    }

    @Override
    public EnergyResp<List<AccountingUnit>> getUnitByParentUnit(Long id) {
        EnergyResp<List<AccountingUnit>> resp = new EnergyResp<>();
        //根据id查询核算单元信息
        List<AccountingUnit> custEquips = new ArrayList<>();
        EnergyResp<AccountingUnit> oneUnit = getOneUnit(id);
        String param = null;
        if (oneUnit.getData() != null) {
            if (oneUnit.getData().getParentId() != null && oneUnit.getData().getId()!=null) {//如果父级id存在，则使用父级ids模糊查询
                param = oneUnit.getData().getParentIds()+oneUnit.getData().getId()+",";
            } else {//父级id不存在，则使用本核算单元的id进行模糊查询
                custEquips.add(oneUnit.getData());
                param = oneUnit.getData().getId().toString()+",";
            }
        }else{
            return resp;
        }
        EntityWrapper<AccountingUnit> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", "0").like("parent_ids", param, SqlLike.RIGHT).eq("is_temp","0");
        for (AccountingUnit a : accountingUnitMapper.selectList(wrapper)) {
            custEquips.add(a);
        }
        resp.ok(custEquips);
        return resp;
    }

    @Override
    public EnergyResp<List<AccountingUnit>> getPageUnitByParentUnit(Long id, Integer pageNo, Integer pageSize) {
        //根据id查询核算单元信息
        List<AccountingUnit> custEquips = new ArrayList<>();
        EnergyResp<AccountingUnit> oneUnit = getOneUnit(id);
        String param = null;
        if (oneUnit.getData() != null) {
            if (oneUnit.getData().getParentId() != null) {//如果父级id存在，则使用父级ids模糊查询
                param = oneUnit.getData().getParentIds();
            } else {//父级id不存在，则使用本核算单元的id进行模糊查询
                //custEquips.add(oneUnit.getData());
                param = oneUnit.getData().getId().toString();
            }
        }

        EnergyResp<List<AccountingUnit>> resp = new EnergyResp<>();
        EntityWrapper<AccountingUnit> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", "0").like("parent_ids", param, SqlLike.RIGHT);
        Page<AccountingUnit> page = new Page<AccountingUnit>(pageNo, pageSize);
        for (AccountingUnit a : accountingUnitMapper.selectPage(page, wrapper)) {
            custEquips.add(a);
        }
        resp.ok(custEquips);
        return resp;
    }

    /**
     * 递归组织结构树，添加相应的核算单元
     */
    private NodeTree unitTree(NodeTree nodeTree) {
        //子节点下的
        List<NodeTree> node = nodeTree.getNode();
        for (NodeTree nodeTree1 : node) {
            EnergyResp<List<AccountingUnit>> parentNode = null;
            Long id = nodeTree1.getId();
            //如果该节点不是核算单元，就要查询顶级节点，如果是核算单元，就查询子节点
            if (nodeTree1.getType().equals("unit")) {//是核算单元
                parentNode = getChildUnit(id);
            } else {//不是核算单元
                parentNode = getParentUnit(id);
            }
            List<NodeTree> nodeTrees = nodeTree1.getNode();
            if (parentNode.getData() != null && parentNode.getData().size() > 0) {
                for (AccountingUnit accountingUnit : parentNode.getData()) {
                    NodeTree comp = new NodeTree();
                    comp.setType("unit");
                    comp.setId(accountingUnit.getId());
                    comp.setNodeName(accountingUnit.getName());
                    nodeTrees.add(unitTree(comp));
                }
                nodeTree1.setNode(nodeTrees);
            }
        }
        nodeTree.setNode(node);


        EnergyResp<List<AccountingUnit>> parentNode = null;
        Long id = nodeTree.getId();
        //如果该节点不是核算单元，就要查询顶级节点，如果是核算单元，就查询子节点
        if (nodeTree.getType().equals("unit")) {
            parentNode = getChildUnit(id);
        } else {
            parentNode = getParentUnit(id);
        }
        List<NodeTree> nodeTrees = nodeTree.getNode();
        if (parentNode.getData() != null && parentNode.getData().size() > 0) {
            for (AccountingUnit accountingUnit : parentNode.getData()) {
                NodeTree comp = new NodeTree();
                comp.setType("unit");
                comp.setId(accountingUnit.getId());
                comp.setNodeName(accountingUnit.getName());
                nodeTrees.add(unitTree(comp));
            }
            nodeTree.setNode(nodeTrees);
        }
        return nodeTree;
    }

    @Override
    public List<CustMeter> getAccountMeter(Long id) {
        EntityWrapper<CustMeter> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", "0").eq("accounting_id", id);
        return custMeterMapper.selectList(wrapper);
    }
    @Override
    public List<CustMeter> getAccountEleMeter(Long id,String energyType) {
        EntityWrapper<CustMeter> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", "0").eq("accounting_id", id).eq("energy_type",energyType).eq("is_accoun","1");
        return custMeterMapper.selectList(wrapper);
    }


    /**
     * 根据 核算单元ids 查询计量表信息列表
     * @param ids
     * @return
     */
    @Override
    public List<CustMeter> getAccountMeter(List<Long> ids) {
        EntityWrapper<CustMeter> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", "0").in("accounting_id",ids);
        return custMeterMapper.selectList(wrapper);
    }



    /**
     * 根据 核算单元ids 查询计量表信息----电表
     * @param ids
     * @return
     */
    @Override
    public List<CustMeter> getAccountEleMeter(List<Long> ids,String energyType) {
        EntityWrapper<CustMeter> wrapper = new EntityWrapper<>();
        if(ids.size()>0) {
            wrapper.eq("del_flag", "0").in("accounting_id", ids).eq("energy_type", energyType);
            return custMeterMapper.selectList(wrapper);
        }
        return new ArrayList<>();
    }

    @Override
    public List<CustMeter> getAccountSteaMeter(List<Long> ids) {
        EntityWrapper<CustMeter> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", "0").in("accounting_id",ids).eq("energy_type","02");
        return custMeterMapper.selectList(wrapper);
    }


    @Override
    public List<MeterResp> getAccountMeterT(Long id) {
        List<CustMeter> accountMeter = getAccountMeter(id);
        if(accountMeter.size()<=0){
            //查询该核算单元下一级核算单元
            EnergyResp<List<AccountingUnit>> childUnit = getChildUnit(id);
            if(childUnit.getData()!=null && childUnit.getData().size()>0){
                List<Long> ids = new ArrayList<>();
                 for(AccountingUnit acc:childUnit.getData()){
                     ids.add(acc.getId());
                }
                //使用该级别核算单元获取表号
                accountMeter= getAccountMeter(ids);
                 if(accountMeter.size()<=0){
                     //根据核算单元ids查询出下级别的所有核算单元
                     List<Long> idss = new ArrayList<>();
                     for(Long i:ids){
                         //根据核算单元id查询核算单元s
                         EnergyResp<List<AccountingUnit>> childUnit1 = getChildUnit(id);
                            for(AccountingUnit ac:childUnit1.getData()){
                                idss.add(ac.getId());
                            }
                     }
                     //根据idss查询 表号
                     accountMeter= getAccountMeter(idss);
                 }
            }
        }
        List<MeterResp> meterResps = new ArrayList<>();
        MeterResp meterResp = null;
        for (CustMeter custMeter:accountMeter){
            Long wifiId = custMeter.getWifiId();
            //根据网关id查询网关信息
            CustWifi custWifi = custWifiMapper.selectById(wifiId);
            meterResp = new MeterResp();
            meterResp.setMeterId(custMeter.getId());
            meterResp.setIsAccoun(custMeter.getIsAccoun());
            if(custMeter.getEnergyType().equals("01")){
                meterResp.setLoopNumber(custMeter.getLoopNumber());
            }else {
                meterResp.setLoopNumber(custMeter.getLoopNumber());
            }
            meterResp.setMeterName(custMeter.getName());
            meterResp.setEnergyType(custMeter.getEnergyType());
            if(custWifi!=null){
                meterResp.setStaId(custWifi.getStaId());
            }
            meterResps.add(meterResp);
        }
        return meterResps;
    }


    /**
     * 该方法是权宜之计
     * @param id
     * @param energyType
     * @return
     */
    @Override
    public List<MeterResp> getAccountEleMeterT(Long id,String energyType) {
        List<CustMeter> accountMeter = getAccountEleMeter(id,energyType);
        if(accountMeter.size()<=0){
            //查询该核算单元下一级核算单元
            EnergyResp<List<AccountingUnit>> childUnit = getChildUnit(id);
            if(childUnit.getData()!=null && childUnit.getData().size()>0){
                List<Long> ids = new ArrayList<>();
                for(AccountingUnit acc:childUnit.getData()){
                    ids.add(acc.getId());
                }
                //使用该级别核算单元获取表号
                accountMeter= getAccountEleMeter(ids, energyType);
                if(accountMeter.size()<=0){
                    //根据核算单元ids查询出下级别的所有核算单元
                    List<Long> idss = new ArrayList<>();
                    for(Long i:ids){
                        //根据核算单元id查询核算单元s
                        EnergyResp<List<AccountingUnit>> childUnit1 = getChildUnit(i);
                        for(AccountingUnit ac:childUnit1.getData()){
                            idss.add(ac.getId());
                        }
                    }
                    //根据idss查询 表号
                    accountMeter= getAccountEleMeter(idss, energyType);

                    if(accountMeter.size()<=0){
                        //根据核算单元ids查询出下级别的所有核算单元
                        List<Long> idsss = new ArrayList<>();
                        for(Long i:idss){
                            //根据核算单元id查询核算单元s
                            EnergyResp<List<AccountingUnit>> childUnit1 = getChildUnit(i);
                            for(AccountingUnit ac:childUnit1.getData()){
                                idsss.add(ac.getId());
                            }
                        }
                        //根据idsss查询 表号
                        accountMeter= getAccountEleMeter(idsss, energyType);
                    }
                }
            }
        }
        List<MeterResp> meterResps = new ArrayList<>();
        MeterResp meterResp = null;
        for (CustMeter custMeter:accountMeter){
            Long wifiId = custMeter.getWifiId();
            //根据网关id查询网关信息
            CustWifi custWifi = custWifiMapper.selectById(wifiId);
            meterResp = new MeterResp();
            meterResp.setMeterId(custMeter.getId());
            meterResp.setIsAccoun(custMeter.getIsAccoun());
            if(custMeter.getEnergyType().equals("01")){
                meterResp.setLoopNumber(custMeter.getLoopNumber());
            }else {
                meterResp.setLoopNumber(custMeter.getLoopNumber());
            }
            meterResp.setMeterName(custMeter.getName());
            meterResp.setEnergyType(custMeter.getEnergyType());
            if(custWifi!=null){
                meterResp.setStaId(custWifi.getStaId());
            }
            meterResps.add(meterResp);
        }
        return meterResps;
    }


    /**
     * 根据 用电企业id 查询 企业下一级核算单元的 计量表信息
     * 如果一级核算单元没有设备 则查询下一级
     * 0.根据企业id查询子公司的ids
     * 1.根据子公司的ids 查询分别下的一级核算单元 ids
     * 2.根据一级核算单元ids查询 旗下计量表，如果旗下没有则查找下一级
     */
    @Override
    public List<MeterResp> queryMeterListByCompany(Long id) {
        List<MeterResp> resps= new ArrayList<>();
        //0.根据企业id查询子公司的ids
        DefaultReq de = new DefaultReq();
        de.setId(id);
        EnergyResp<ListResp<CompanyCust>> allCompany = companyService.getAllCompany(de);
        List<Long> companyIds = new ArrayList<>();
        if(allCompany.getData()!=null){
            for(CompanyCust cus:allCompany.getData().getList()){
                companyIds.add(cus.getId());
            }
        }
        if(companyIds.size()>0){
            //1.根据子公司的ids 查询分别下的一级核算单元 ids
            List<Long> panertUnitIds = new ArrayList<>();
            for (Long companyId:companyIds){
                EnergyResp<List<AccountingUnit>> parentUnit = getParentUnit(companyId);
                if(parentUnit.getData()!=null){
                    for(AccountingUnit unit:parentUnit.getData()){
                        panertUnitIds.add(unit.getId());
                    }
                }
            }
            //2.根据一级核算单元ids查询 旗下计量表，如果旗下没有则查找下一级
            if(panertUnitIds.size()>0){
                for (Long unitId :panertUnitIds){
                    List<MeterResp> accountMeterT = getAccountMeterT(unitId);
                    for (MeterResp meterResp:accountMeterT){
                        resps.add(meterResp);
                    }
                }
            }
        }
        return resps;
    }

    @Override
    public List<MeterResp> queryEleMeterListByCompany(Long id,String energyType) {
        List<MeterResp> resps= new ArrayList<>();
        //0.根据企业id查询子公司的ids
        DefaultReq de = new DefaultReq();
        de.setId(id);
        EnergyResp<ListResp<CompanyCust>> allCompany = companyService.getAllCompany(de);
        List<Long> companyIds = new ArrayList<>();
        if(allCompany.getData()!=null){
            for(CompanyCust cus:allCompany.getData().getList()){
                companyIds.add(cus.getId());
            }
        }
        if(companyIds.size()>0){
            //1.根据子公司的ids 查询分别下的一级核算单元 ids
            List<Long> panertUnitIds = new ArrayList<>();
            for (Long companyId:companyIds){
                EnergyResp<List<AccountingUnit>> parentUnit = getParentUnit(companyId);
                if(parentUnit.getData()!=null){
                    for(AccountingUnit unit:parentUnit.getData()){
                        panertUnitIds.add(unit.getId());
                    }
                }
            }
            //2.根据一级核算单元ids查询 旗下计量表，如果旗下没有则查找下一级
            if(panertUnitIds.size()>0){
                for (Long unitId :panertUnitIds){
                    List<MeterResp> accountMeterT = getAccountEleMeterT(unitId,energyType);
                    for (MeterResp meterResp:accountMeterT){
                        resps.add(meterResp);
                    }
                }
            }
        }
        return resps;
    }

    /**
     * 保存核算单元
     * 当 id为空时为inser  id不为空位updata
     *
     * @param ac
     * @return
     */
    @Override
    public EnergyResp<Integer> save(AccountingUnit ac) {
        EnergyResp<Integer> en = new EnergyResp<>();
        Integer insert =null;
        if (ac.getId() == null) {//insert
            ac.setDelFlag("0");
             insert = accountingUnitMapper.insert(ac);
        } else {//updata
            if(ac.getDelFlag()==null){
                ac.setDelFlag("0");
            }
            insert = accountingUnitMapper.updateById(ac);
        }
        en.ok(insert);
        return en;
    }

    @Override
    public EnergyResp<Integer> disable(DefaultReq de) {
        EnergyResp<Integer> en = new EnergyResp<>();
        AccountingUnit ac = new AccountingUnit();
        ac.setIsUse("0");
        ac.setId(de.getId());
        Integer integer = accountingUnitMapper.updateById(ac);
        en.ok(integer);
        return en;
    }

    @Override
    public EnergyResp<MeterResp> queryStaMeterByCompany(DefaultReq de) {
        EnergyResp<MeterResp> respEnergyResp= new EnergyResp<>();
        CustMeter me = new CustMeter();
        me.setDelFlag("0");
        me.setAccountingId(de.getId());
        me.setEnergyType("02");
        CustMeter custMeter = custMeterMapper.selectOne(me);
        MeterResp meterResp= new MeterResp();
        meterResp.setMeterId(custMeter.getId());
        //查询网关信息
        Long wifiId = custMeter.getWifiId();
        //根据网关id查询网关信息
        CustWifi custWifi = custWifiMapper.selectById(wifiId);
        if(custWifi!=null){
            meterResp.setStaId(custWifi.getStaId());
        }
        meterResp.setIsAccoun(custMeter.getIsAccoun());
        if(custMeter.getEnergyType().equals("01")){
            meterResp.setLoopNumber(custMeter.getLoopNumber());
        }else {
            meterResp.setLoopNumber(custMeter.getLoopNumber());
        }
        meterResp.setMeterName(custMeter.getName());
        meterResp.setEnergyType(custMeter.getEnergyType());
        respEnergyResp.ok(meterResp);
        return respEnergyResp;
    }

    @Override
    public EnergyResp<AccountingUnit> getOne(DefaultReq de) {
        EnergyResp<AccountingUnit> energyResp= new EnergyResp<>();
        AccountingUnit unit = new AccountingUnit();
        unit.setId(de.getId());
        unit.setDelFlag("0");
        AccountingUnit accountingUnit = accountingUnitMapper.selectOne(unit);
        energyResp.ok(accountingUnit);
        return energyResp;
    }

    @Override
    public EnergyResp<List<AccountingUnit>> getUnitByMete(String de) {
        EnergyResp<List<AccountingUnit>> resp = new EnergyResp<>();
        List<AccountingUnit> unitByMete = accountingUnitMapper.getUnitByMete(de);
        resp.ok(unitByMete);
        return resp;
    }

    @Override
    public List<AccountingUnit> getUnitByCompanyisd(String companyId, String accountingType) {
        EntityWrapper<AccountingUnit> wrapper = new EntityWrapper<>();
        if(StringUtils.isBlank(accountingType)) {
            wrapper.eq("del_flag", "0").eq("cust_id", companyId);
        }else{
            wrapper.eq("del_flag", "0").eq("cust_id", companyId).eq("accounting_type",accountingType);
        }
        return accountingUnitMapper.selectList(wrapper);
    }

    @Override
    public SteamUnitVo getUnitName(String meterNo) {

        return custMeterMapper.custMeterByLoopNumber(meterNo);
    }
}
