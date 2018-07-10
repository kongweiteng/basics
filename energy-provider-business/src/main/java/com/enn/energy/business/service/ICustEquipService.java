package com.enn.energy.business.service;

import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.CustEquip;
import com.baomidou.mybatisplus.service.IService;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.system.NodeTree;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kongweiteng
 * @since 2018-06-05
 */
public interface ICustEquipService extends IService<CustEquip> {
    /**
     * 根据企业id获取等级的核算单元
     */
    public EnergyResp<List<CustEquip>> getParentEqu(Long companyId);

    /**
     * 根据父级id 查询自己核算单元
     */
    public EnergyResp<List<CustEquip>> getChildEqu(Long parentId);


    /**
     * 根据企业id查询核算单元树
     */
    public EnergyResp<NodeTree> getEquipTree(DefaultReq defaultReq);

    /**
     * 根据核算单元id 查询旗下的核算单元信息
     * @param id
     */
    public EnergyResp<UnitResp> queryList(Long id);

}
