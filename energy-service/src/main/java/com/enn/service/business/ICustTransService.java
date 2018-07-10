package com.enn.service.business;

import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.req.CustMeterReq;
import com.enn.vo.energy.business.resp.CustTransResp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "energy-zuul-gateway")
public interface ICustTransService {

	/**
	 * 获取配电室下的变压器
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/energy-proxy/energy-provider-business/custTrans/getCustTransByDistributionId", method = RequestMethod.POST)
	EnergyResp<ListResp<CustTransResp>> getCustTransByDistributionId(@RequestBody CustMeterReq custMeterReq);

}
