package com.enn.energy.business.rest;


import com.enn.energy.business.service.IElectricityDistributionRoomService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.req.CustReq;
import com.enn.vo.energy.business.resp.ElectricityDistributionRoom;
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

@RestController
@RequestMapping("/electricityDistributionRoom")
@Api(value = "配电室信息信息接口", description = "配电室信息信息接口", tags = "配电室信息")
public class ElectricityDistributionRoomController {
	private static final Logger logger = LoggerFactory.getLogger(ElectricityDistributionRoomController.class);

	@Autowired
	private IElectricityDistributionRoomService electricityDistributionRoomService;

	/**
	 * 根据客户ID查询配电室信息
	 *
	 * @param custReq
	 * @return
	 */
	@RequestMapping(value = "/getElectricityDistributionRoomByCustID", method = RequestMethod.POST)
	@ApiOperation(value = "查询配电室信息", notes = "根据客户ID查询配电室信息")
	public EnergyResp<List<ElectricityDistributionRoom>> getElectricityDistributionRoomByCustID(@RequestBody CustReq custReq) {
		EnergyResp<List<ElectricityDistributionRoom>> electricityDistributionRoomEnergyResp = new EnergyResp<>();
		try {
			List<ElectricityDistributionRoom> electricityDistributionRooms =
					electricityDistributionRoomService.getElectricityDistributionRoomByCustID(custReq.getCustID());
			if (electricityDistributionRooms.size()==0){
				electricityDistributionRoomEnergyResp.null_obj(null ,"企业下未找到配电室");
			}else {
				electricityDistributionRoomEnergyResp.ok(electricityDistributionRooms);
			}
		} catch (Exception e) {
			logger.error("根据客户ID查询配电室信息异常，请求参数{}", custReq.getCustID() + "异常信息" + e);
		}

		return electricityDistributionRoomEnergyResp;
	}


	/**
	 * 根据客户ID查询配电室信息
	 *
	 * @param custReq
	 * @return
	 */
	@RequestMapping(value = "/getDistributionRoomByCustID", method = RequestMethod.POST)
	@ApiOperation(value = "查询所有配电室信息", notes = "根据客户ID查询所有配电室信息")
	public EnergyResp<List<ElectricityDistributionRoom>> getDistributionRoomByCustID(@RequestBody CustReq custReq) {
		EnergyResp<List<ElectricityDistributionRoom>> electricityDistributionRoomEnergyResp = new EnergyResp<>();
		try {
			List<ElectricityDistributionRoom> electricityDistributionRooms =
					electricityDistributionRoomService.getDistributionRoomByCustID(custReq.getCustID());
			if (electricityDistributionRooms.size()==0){
				electricityDistributionRoomEnergyResp.null_obj(null ,"企业下未找到配电室");
			}else {
				electricityDistributionRoomEnergyResp.ok(electricityDistributionRooms);
			}
		} catch (Exception e) {
			logger.error("根据客户ID查询配电室信息异常，请求参数{}", custReq.getCustID() + "异常信息" + e);
		}

		return electricityDistributionRoomEnergyResp;
	}

	/**
	 *根据配电室查询变压器
	 * @param
	 * @author 肖明玉
	 * @return
	 */


}
