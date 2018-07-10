package com.enn.energy.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.CompanyCust;
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
public interface ICompanyCustService extends IService<CompanyCust> {
    public EnergyResp<CompanyCust> getOne(Long id);

    /**
     * 根据父级id查询子公司信息
     * @param parentId
     * @return
     */
    public EnergyResp<List<CompanyCust>> getNodeCompany(Long parentId);


    /**
     * 组装机构树结构
     */
    public EnergyResp<NodeTree> getCompanyTree(Long id);

    /**
     * 查询企业下所有的子公司id
     */
    EnergyResp<List<CompanyCust>> getAllCompany(Long id);

}
