package com.enn.energy.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.enn.energy.system.dao.SysUserMapper;
import com.enn.energy.system.entity.SysUser;
import com.enn.energy.system.service.ISysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysUserServiceImpl implements ISysUserService {
    @Resource
    private SysUserMapper sysUserMapper;


    @Override

    public List<SysUser> selectUserPage() {
        List<SysUser> bypage = sysUserMapper.findBypage();
        return bypage;
    }
}
