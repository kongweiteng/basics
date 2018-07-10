package com.enn.energy.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.enn.constant.StatusCode;
import com.enn.vo.energy.system.CompanyCust;
import com.enn.energy.system.dao.CompanyCustMapper;
import com.enn.energy.system.service.ICompanyCustService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.NodeTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kongweiteng
 * @since 2018-06-05
 */
@Service
public class CompanyCustServiceImpl extends ServiceImpl<CompanyCustMapper, CompanyCust> implements ICompanyCustService {
    @Autowired
    private CompanyCustMapper companyCustMapper;

    /**
     * 根据id查询单条数据
     *
     * @param id
     * @return
     */
    @Override
    public EnergyResp<CompanyCust> getOne(Long id) {
        EnergyResp<CompanyCust> energyResp = new EnergyResp<>();
        CompanyCust companyCust = new CompanyCust();
        companyCust.setId(id);
        companyCust.setDelFlag("0");
        energyResp.ok(companyCustMapper.selectOne(companyCust));
        return energyResp;
    }

    @Override
    public EnergyResp<List<CompanyCust>> getNodeCompany(Long parentId) {
        EnergyResp<List<CompanyCust>> energyResp = new EnergyResp<>();
        EntityWrapper<CompanyCust> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", "0");
        wrapper.eq("parent_id", parentId);
        List<CompanyCust> companyCusts = companyCustMapper.selectList(wrapper);
        energyResp.ok(companyCusts);
        return energyResp;
    }

    @Override
    public EnergyResp<NodeTree> getCompanyTree(Long id) {
        EnergyResp<NodeTree> energyResp = new EnergyResp();
        //根据id查询一级企业
        EnergyResp<CompanyCust> oneCompany = getOne(id);
        if (oneCompany.getData() == null) {
            energyResp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), "该id对应的企业不存在！！");
            return energyResp;
        }
        NodeTree nodeTree = new NodeTree();
        nodeTree.setNodeName(oneCompany.getData().getCompanyName());
        nodeTree.setId(oneCompany.getData().getId());
        nodeTree.setType("company");
        NodeTree tree = getTree(nodeTree);
        energyResp.ok(tree);
        return energyResp;
    }

    /**
     * 查询企业以及其下所有子公司信息
     *
     * @param id
     * @return
     */
    @Override
    public EnergyResp<List<CompanyCust>> getAllCompany(Long id) {
        EnergyResp<List<CompanyCust>> resp = new EnergyResp<>();
        //根据企业id查询企业信息
        List<CompanyCust> allChildCompany =new ArrayList<>();
                EnergyResp<CompanyCust> one = getOne(id);
        if (one.getData() != null) {
            allChildCompany.add(one.getData());
            allChildCompany = getAllChildCompany(id,allChildCompany);
        }
        resp.ok(allChildCompany);

        return resp;
    }

    /**
     * 根据企业id查询旗下所有子公司
     *
     * @return
     */
    private List<CompanyCust> getAllChildCompany(Long id,List<CompanyCust> re) {
        EnergyResp<List<CompanyCust>> nodeCompany = getNodeCompany(id);
        if (nodeCompany.getData() != null && nodeCompany.getData().size() > 0) {
            for (CompanyCust co : nodeCompany.getData()) {
                //将子节点放入list中
                if(co!=null){
                    re.add(co);
                    getAllChildCompany(co.getId(),re);
                }else {
                    continue;
                }
            }
        }
        return re;
    }


    /**
     * 组装树
     *
     * @return
     */
    private NodeTree getTree(NodeTree nodeTree) {
        //查询该企业下的字公司
        EnergyResp<List<CompanyCust>> nodeCompany = getNodeCompany(nodeTree.getId());

        if (nodeCompany.getData() != null && nodeCompany.getData().size() > 0) {//该公司下有子节点
            //先循环子公司
            List<NodeTree> nodeTrees = new ArrayList<>();
            for (CompanyCust co : nodeCompany.getData()) {
                NodeTree com = new NodeTree();
                com.setId(co.getId());
                com.setNodeName(co.getCompanyName());
                com.setType("childCompany");
                nodeTrees.add(getTree(com));
            }
            nodeTree.setNode(nodeTrees);
        } else {//该公司下没有子公司
            return nodeTree;
        }
        return nodeTree;
    }
}
