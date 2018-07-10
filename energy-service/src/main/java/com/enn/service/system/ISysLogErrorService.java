package com.enn.service.system;

import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.system.CompanyCust;
import com.enn.vo.energy.system.NodeTree;
import com.enn.vo.energy.system.SysLogError;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 错误日志
 */
@FeignClient(value = "energy-zuul-gateway")
public interface ISysLogErrorService {


    /**
     * 查询错误信息
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/sysLogError/selectAll")
    public EnergyResp<ListResp<SysLogError>> select();


    /**
     * 插入错误信息
     * @param sysLogError
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/sysLogError/insertOne")
    public EnergyResp<Integer> insertOne(SysLogError sysLogError);
}
