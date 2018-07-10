package com.enn.service.business;

import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.ProblemInfo;
import com.enn.vo.energy.business.req.StatisticsDataReq;
import com.enn.vo.energy.business.resp.DataResp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;

@FeignClient(value = "energy-zuul-gateway")
public interface IPowerFactorService {

    @RequestMapping(value = "/energy-proxy/energy-provider-business/powerFactor/getPowerFactorByDay", method = RequestMethod.POST)
    EnergyResp<BigDecimal> getPowerFactorByDay(@RequestBody StatisticsDataReq statisticsDataReq);

    @RequestMapping(value = "/energy-proxy/energy-provider-business/powerFactor/getPowerFactorByHour", method = RequestMethod.POST)
    EnergyResp<BigDecimal> getPowerFactorByHour(@RequestBody StatisticsDataReq statisticsDataReq);

    @RequestMapping(value = "/energy-proxy/energy-provider-business/powerFactor/min", method = RequestMethod.POST)
    DataResp getMin(@RequestBody StatisticsDataReq statisticsDataReq);
}
