package com.enn.energy.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.system.CompanyCust;
import com.enn.vo.energy.system.SysLogError;
import com.enn.energy.system.dao.SysLogErrorMapper;
import com.enn.energy.system.service.ISysLogErrorService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.enn.vo.energy.system.req.SysLogCondition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kongweiteng
 * @since 2018-06-27
 */
@Service
public class SysLogErrorServiceImpl extends ServiceImpl<SysLogErrorMapper, SysLogError> implements ISysLogErrorService {

    @Resource
    private  SysLogErrorMapper sysLogErrorMapper;


    @Override
    public EnergyResp<ListResp<SysLogError>> select() {
        EnergyResp<ListResp<SysLogError>> resp = new EnergyResp<>();
        ListResp<SysLogError> sysLogErrorListResp = new ListResp<>();
        EntityWrapper<SysLogError> wrapper = new EntityWrapper<>();
        wrapper.orderBy("time",false);
        List<SysLogError> sysLogErrors = sysLogErrorMapper.selectList(wrapper);
        sysLogErrorListResp.setList(sysLogErrors);
        resp.ok(sysLogErrorListResp);
        return resp;
    }

    @Override
    public EnergyResp<ListResp<SysLogError>> selectByCondition(SysLogCondition sysLogError) {
        EnergyResp<ListResp<SysLogError>> resp= new EnergyResp<>();
        ListResp<SysLogError> respppp = new ListResp<>();
        EntityWrapper<SysLogError> wrapper = new EntityWrapper<>();
        wrapper.eq("code",sysLogError.getCode()).like("msg",sysLogError.getMsg()).like("project",sysLogError.getProject()).orderBy("time",false);
        List<SysLogError> sysLogErrors = sysLogErrorMapper.selectList(wrapper);
        respppp.setList(sysLogErrors);
        resp.ok(respppp);
        return resp;
    }

    @Override
    public EnergyResp<Integer> insertErrorLog(SysLogError sysLogError) {
        EnergyResp<Integer> resp= new EnergyResp<>();
        Integer insert = sysLogErrorMapper.insert(sysLogError);
        resp.ok(insert);
        return resp;
    }
}
