package com.enn.service.business;


import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.po.CustMeter;
import com.enn.vo.energy.business.po.CustWifi;
import com.enn.vo.energy.business.po.DistributionMeter;
import com.enn.vo.energy.business.req.CustMeterReq;
import com.enn.vo.energy.business.req.MeterListReq;
import com.enn.vo.energy.business.resp.MeterResp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "energy-zuul-gateway")
public interface ICustMeterService {

	/**
	 * 获取配电室下的计量点信息
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/energy-proxy/energy-provider-business/custMeter/getCustMeterByDistributionId", method = RequestMethod.POST)
	EnergyResp<List<DistributionMeter>> getCustMeterByDistributionId(@RequestBody CustMeterReq custMeterReq);

	/**
	 * 根据企业id查询所有的计量表信息
	 */
	@RequestMapping(value = "/energy-proxy/energy-provider-business/custMeter/getAllMeter", method = RequestMethod.POST)
	public EnergyResp<List<MeterResp>> getAllMeter(MeterListReq meterListReq);

	/**
	 * 保存 计量表信息
	 */
	@RequestMapping(value = "/energy-proxy/energy-provider-business/custMeter/save", method = RequestMethod.POST)
	public EnergyResp<Integer> save(CustMeter meter);


	/**
	 *  根据 计量表id  查询计量表所在的 wifi
	 */
	@RequestMapping(value = "/energy-proxy/energy-provider-business/custMeter/getWifiByMeteId", method = RequestMethod.POST)
	public EnergyResp<CustWifi> getWifiByMeteId(DefaultReq id);

	/**
	 *  根据 计量表id  查询计量表信息
	 */
	@RequestMapping(value = "/energy-proxy/energy-provider-business/custMeter/getMeterByMeterId", method = RequestMethod.POST)
	public EnergyResp<CustMeter> getMeterByMeterId(DefaultReq defaultReq);

	/**
	 * 根据配电室ID获取计量点信息
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/energy-proxy/energy-provider-business/custMeter/getCustMetersByDistributionId", method = RequestMethod.POST)
	EnergyResp<List<DistributionMeter>> getCustMetersByDistributionId(@RequestBody DefaultReq defaultReq);
}
