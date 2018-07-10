package com.enn.service.business;

import com.enn.vo.energy.EnergyResp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "energy-zuul-gateway")
public interface ICustService {


    @RequestMapping(value = "/energy-proxy/energy-provider-business/cust/getCustId", method = RequestMethod.POST)
    public EnergyResp<String> getCustId(@RequestBody String relationId);
}
