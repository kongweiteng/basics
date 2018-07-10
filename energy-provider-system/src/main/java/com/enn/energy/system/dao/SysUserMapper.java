package com.enn.energy.system.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.enn.energy.system.entity.SysUser;

import java.util.List;

public interface SysUserMapper {

    List<SysUser> findBypage();
}
