package com.enn.energy.system.service.impl;

import com.enn.vo.energy.system.CusCust;
import com.enn.energy.system.dao.CusCustMapper;
import com.enn.energy.system.service.ICusCustService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 客户基本信息表 tag：CustomerInfo 服务实现类
 * </p>
 *
 * @author kongweiteng
 * @since 2018-05-29
 */
@Service
public class CusCustServiceImpl extends ServiceImpl<CusCustMapper, CusCust> implements ICusCustService {

    @Autowired
    private CusCustMapper cusCustMapper;
    @Override
    public EnergyResp<CusCust> selectOne(String id) {
        EnergyResp<CusCust> resp = new EnergyResp<>();
        CusCust cusCust = cusCustMapper.selectById(id);
        resp.ok(cusCust);
        return resp;
    }

    @Override
    public EnergyResp<ListResp<CusCust>> selectAll() {
        //定义返回实体
        EnergyResp<ListResp<CusCust>> resp = new EnergyResp<>();
        ListResp<CusCust> list= new ListResp<>();
        List<CusCust> all = cusCustMapper.getAll();
        list.setList(all);
        resp.ok(list);
        return resp;
    }
}
