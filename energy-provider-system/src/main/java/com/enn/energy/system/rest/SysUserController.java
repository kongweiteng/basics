package com.enn.energy.system.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.enn.energy.system.entity.SysUser;
import com.enn.energy.system.service.ISysUserService;
import com.enn.vo.energy.EnergyResp;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private ISysUserService sysUserService;

    @ApiOperation(value = "获取用户信息列表", notes = "")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public EnergyResp<Page<SysUser>> generate() {
        EnergyResp<Page<SysUser>> res = new EnergyResp<>();
        Page<SysUser> page = new Page<>();
        page.setCurrent(1);
        page.setSize(2);
        PageHelper.setPagination(page);
        page.setRecords(sysUserService.selectUserPage());
        page.setTotal(PageHelper.freeTotal());//获取总数并释放资源 也可以 PageHelper.getTotal()
        res.ok(page);
        return res;
    }
}
