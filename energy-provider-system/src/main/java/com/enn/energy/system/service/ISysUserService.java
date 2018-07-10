package com.enn.energy.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.enn.energy.system.entity.SysUser;

import java.util.List;

public interface ISysUserService {

    public List<SysUser> selectUserPage();
}
