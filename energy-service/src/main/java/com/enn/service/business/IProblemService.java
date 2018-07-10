package com.enn.service.business;

import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.ProblemInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "energy-zuul-gateway")
public interface IProblemService {


	@RequestMapping(value = "/energy-proxy/energy-provider-business/problem/saveProblemInfo", method = RequestMethod.POST)
	EnergyResp saveProblemInfo(@RequestBody ProblemInfo problemInfo);
}