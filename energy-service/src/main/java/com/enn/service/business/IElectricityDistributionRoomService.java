package com.enn.service.business;


import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.req.CustReq;
import com.enn.vo.energy.business.resp.ElectricityDistributionRoom;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by zxj on 2018/6/5
 *
 * @Author xmy
 */
@FeignClient(value = "energy-zuul-gateway")
public interface IElectricityDistributionRoomService {

	/**
	 * 获取企业下的配电室
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/energy-proxy/energy-provider-business/electricityDistributionRoom/getElectricityDistributionRoomByCustID", method = RequestMethod.POST)
	EnergyResp<List<ElectricityDistributionRoom>> getElectricityDistributionRoomByCustID(@RequestBody CustReq custReq);

	/**
	 * 获取企业下的所有配电室
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/energy-proxy/energy-provider-business/electricityDistributionRoom/getDistributionRoomByCustID", method = RequestMethod.POST)
	EnergyResp<List<ElectricityDistributionRoom>> getDistributionRoomByCustID(@RequestBody CustReq custReq);

}
