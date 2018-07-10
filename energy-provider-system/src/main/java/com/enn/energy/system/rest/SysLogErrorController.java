package com.enn.energy.system.rest;


import com.enn.energy.system.service.ISysLogErrorService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.system.SysLogError;
import com.enn.vo.energy.system.req.SysLogCondition;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kongweiteng
 * @since 2018-06-27
 */
@RestController
@RequestMapping("/sysLogError")
@Api(tags = {"日志管理"})
public class SysLogErrorController {
    @Autowired
    private ISysLogErrorService sysLogErrorService;

    @ApiOperation(value = "查询所有错误信息")
    @RequestMapping(value = "/selectAll",method = RequestMethod.POST)
    public EnergyResp<ListResp<SysLogError>> select() {
       return sysLogErrorService.select();
    }

    @ApiOperation(value = "插入单条错误信息")
    @RequestMapping(value = "/insertOne",method = RequestMethod.POST)
    public EnergyResp<Integer> insertOne(@RequestBody  SysLogError sysLogError) {
        return sysLogErrorService.insertErrorLog(sysLogError);
    }

    /**
     * 条件查询错误日志信息
     */
    @ApiOperation(value = "条件查询日志信息")
    @RequestMapping(value = "/selectByCondition",method = RequestMethod.POST)
    public EnergyResp<ListResp<SysLogError>> selectByCondition(@RequestBody SysLogCondition sysLogCondition) {
        return sysLogErrorService.selectByCondition(sysLogCondition);
    }

}
