package com.enn.service.passage;

import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.passage.req.SteamFeesCalculateReq;
import com.enn.vo.energy.passage.resp.SteamFeesCalculateResp;

import java.util.List;

import javax.validation.Valid;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 蒸汽费用计算
 * @author sl
 * @since 2018-06-11
 */
@FeignClient(value = "energy-zuul-gateway")
//@FeignClient(name = "energy-provider-passage-debug", url = "http://127.0.0.1:8089/")
public interface ISteamFeesCalculateService {

    /**
     * 计算蒸汽费用api
     * @param req
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-passage/steamFees/calculateApi")
//	 @RequestMapping(method = RequestMethod.POST, value = "/steamFees/calculateApi")
    public EnergyResp<SteamFeesCalculateResp> calculateApi(@RequestBody SteamFeesCalculateReq req);
	 
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-passage/steamFees/calculateCollectionApi")
//	 @RequestMapping(value = "/steamFees/calculateCollectionApi", method = RequestMethod.POST)
	 public EnergyResp<List<SteamFeesCalculateResp>> calculateCollectionApi(@RequestBody @Valid List<SteamFeesCalculateReq> req);

}
