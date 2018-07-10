package com.enn.energy.business.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.enn.energy.business.dao.CustEquipMapper;
import com.enn.energy.business.service.ICustEquipService;
import com.enn.service.system.ICompanyService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.CustEquip;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.system.NodeTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class CustEquipServiceImpl extends ServiceImpl<CustEquipMapper, CustEquip> implements ICustEquipService {
    @Autowired
    private CustEquipMapper custEquipMapper;

    @Autowired
    private ICompanyService companyService;

    @Override
    public EnergyResp<List<CustEquip>> getParentEqu(Long companyId) {
        EnergyResp<List<CustEquip>> resp = new EnergyResp<>();
        EntityWrapper<CustEquip> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", "0").eq("cust_id", companyId).isNull("parent_id");
        List<CustEquip> custEquips = custEquipMapper.selectList(wrapper);
        resp.ok(custEquips);
        return resp;
    }

    @Override
    public EnergyResp<List<CustEquip>> getChildEqu(Long parentId) {
        EnergyResp<List<CustEquip>> resp = new EnergyResp<>();
        EntityWrapper<CustEquip> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", "0").eq("parent_id", parentId);
        List<CustEquip> custEquips = custEquipMapper.selectList(wrapper);
        resp.ok(custEquips);
        return resp;
    }

    @Override
    public EnergyResp<NodeTree> getEquipTree(DefaultReq defaultReq) {
        //获取到企业机构树
        EnergyResp<NodeTree> companyTree = companyService.getCompanyTree(defaultReq);
        //往组织机构树中添加核算单元节点
        equipTree(companyTree.getData());
        return companyTree;
    }

    @Override
    public EnergyResp<UnitResp> queryList(Long id) {
        return null;
    }

    /**
     * 递归组织结构树，添加相应的核算单元
     */
    private NodeTree equipTree(NodeTree nodeTree){
        //子节点下的
        List<NodeTree> node = nodeTree.getNode();
        for (NodeTree nodeTree1 : node) {
            EnergyResp<List<CustEquip>> parentNode = null;
            Long id = nodeTree1.getId();
            //如果该节点不是核算单元，就要查询顶级节点，如果是核算单元，就查询子节点
            if (nodeTree1.getType().equals("equip")) {//是核算单元
                parentNode = getChildEqu(id);
            } else {//不是核算单元
                parentNode = getParentEqu(id);
            }
            List<NodeTree> nodeTrees = nodeTree1.getNode();
            if (parentNode.getData() != null && parentNode.getData().size() > 0) {
                for (CustEquip custEquip : parentNode.getData()) {
                    NodeTree comp = new NodeTree();
                    comp.setType("equip");
                    comp.setId(custEquip.getId());
                    comp.setNodeName(custEquip.getName());
                    nodeTrees.add(equipTree(comp));
                }
                nodeTree1.setNode(nodeTrees);
            }
        }
        nodeTree.setNode(node);


        EnergyResp<List<CustEquip>> parentNode = null;
        Long id = nodeTree.getId();
        //如果该节点不是核算单元，就要查询顶级节点，如果是核算单元，就查询子节点
        if(nodeTree.getType().equals("equip")){
            parentNode=getChildEqu(id);
        }else{
            parentNode = getParentEqu(id);
        }
        List<NodeTree> nodeTrees = nodeTree.getNode();
        if(parentNode.getData()!=null&&parentNode.getData().size()>0){
            for(CustEquip custEquip:parentNode.getData()){
                NodeTree comp= new NodeTree();
                comp.setType("equip");
                comp.setId(custEquip.getId());
                comp.setNodeName(custEquip.getName());
                nodeTrees.add(equipTree(comp));
            }
            nodeTree.setNode(nodeTrees);
        }
        return nodeTree;
    }
}
