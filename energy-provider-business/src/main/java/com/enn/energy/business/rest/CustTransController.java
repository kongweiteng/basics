package com.enn.energy.business.rest;


import com.enn.energy.business.service.ICustTransService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.req.CustMeterReq;
import com.enn.vo.energy.business.resp.CustTransResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/custTrans")
@RestController
@Api(value = "变压器信息", description = "变压器信息接口", tags = "变压器信息")
public class CustTransController {

	private static final Logger logger = LoggerFactory.getLogger(ElectricityDistributionRoomController.class);

	@Autowired
	private ICustTransService custTransService;

	@RequestMapping(value = "/getCustTransByDistributionId", method = RequestMethod.POST)
	@ApiOperation(value = "查询变压器信息", notes = "根据配电室ID查询变压器信息")
	public EnergyResp<ListResp<CustTransResp>> getCustTransByDistributionId(@RequestBody CustMeterReq custMeterReq) {
		EnergyResp<ListResp<CustTransResp>> energyResp = new EnergyResp<>();
		Long[] ids = new Long[custMeterReq.getIds().size()];
		custMeterReq.getIds().toArray(ids);
		try {
			List<CustTransResp> custTransResps = custTransService.getCustTransByDistributionId(ids);
			if (custTransResps.size() == 0) {
				energyResp.null_obj(null, "配电室下未找到变压器");
			} else {
				ListResp<CustTransResp> listResp=new ListResp<>();
				listResp.setList(custTransResps);
				energyResp.ok(listResp);
			}
		} catch (Exception e) {
			logger.error("根据配电室ID查询变压器信息接口异常,请求参数{}", ids + "异常信息" + e);
		}
		return energyResp;
	}


}
