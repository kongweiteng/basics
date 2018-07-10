package com.enn.energy.business.service.impl;

import com.enn.vo.energy.business.CusContact;
import com.enn.energy.business.dao.CusContactMapper;
import com.enn.energy.business.service.ICusContactService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.enn.vo.energy.EnergyResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户联系信息表，（基本信息表同客户联系表是1对多关系） tag：ConsContact 服务实现类
 * </p>
 *
 * @author kongweiteng
 * @since 2018-05-30
 */
@Service
public class CusContactServiceImpl extends ServiceImpl<CusContactMapper, CusContact> implements ICusContactService {
	@Autowired
    private  CusContactMapper cusContactMapper;


    @Override
    public EnergyResp<CusContact> getOne(String id) {
        EnergyResp<CusContact> resp = new EnergyResp<>();
        CusContact cusContact = cusContactMapper.selectById(id);
        resp.ok(cusContact);
        return resp;
    }
}
