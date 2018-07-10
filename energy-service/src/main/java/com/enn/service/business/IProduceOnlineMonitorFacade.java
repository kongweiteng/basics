package com.enn.service.business;


import java.util.List;

import javax.validation.Valid;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.dto.DepartUnitDto;
import com.enn.vo.energy.business.dto.EnergyCostDto;
import com.enn.vo.energy.business.dto.ProduceOnlineElectricRealtimeSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineElectricSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineStreamRealtimeSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineStreamSampleDto;
import com.enn.vo.energy.business.vo.ProduceOnlineMonitorParams;

import org.springframework.web.bind.annotation.RequestParam;


/**
 * 能用计划管理页面
 * @author kai.guo
 *
 */
@FeignClient(value = "energy-zuul-gateway")
public interface IProduceOnlineMonitorFacade {
	
	/**
	 * 查询指定企业id的生产车间列表
	 * @param companyId
	 */
	@RequestMapping(value="/energy-proxy/energy-provider-business/produce/queryProductionDepartment",method=RequestMethod.POST)
	public EnergyResp<List<DepartUnitDto>> queryProductionDepartment(@RequestParam("companyId") Long companyId);
	
	/**
	 * 查询指定部门的电力采样数据
	 * @param onlineMonitorParams
	 * @return
	 */
	@RequestMapping( value="/energy-proxy/energy-provider-business/produce/queryPowerCurveForWebElectricSystem",method=RequestMethod.POST)
	public EnergyResp<ProduceOnlineElectricSampleDto> queryPowerCurveForWebElectricSystem(@RequestBody @Valid ProduceOnlineMonitorParams onlineMonitorParams);
	
	/**
	 * 查询指定部门的蒸汽采样数据
	 * @param onlineMonitorParams
	 * @return
	 */
	@RequestMapping( value="/energy-proxy/energy-provider-business/produce/queryFlowCurveForWebThermodynamicSystem",method=RequestMethod.POST)
	public EnergyResp<ProduceOnlineStreamSampleDto> queryFlowCurveForWebThermodynamicSystem(@RequestBody @Valid ProduceOnlineMonitorParams onlineMonitorParams);
	
	
	/**
	 * 查询指定部门的电力实时用电情况
	 * @param onlineMonitorParams
	 * @return
	 */
	@RequestMapping( value="/energy-proxy/energy-provider-business/produce/queryRealTimeElectricityStatus",method=RequestMethod.POST)
	public EnergyResp<ProduceOnlineElectricRealtimeSampleDto> queryRealTimeElectricityStatus(@RequestBody @Valid ProduceOnlineMonitorParams onlineMonitorParams);
	
	
	/**
	 * 查询指定部门的蒸汽实时使用情况
	 * @param onlineMonitorParams
	 * @return
	 */
	@RequestMapping( value="/energy-proxy/energy-provider-business/produce/queryRealTimeStreamStatus",method=RequestMethod.POST)
	public EnergyResp<ProduceOnlineStreamRealtimeSampleDto> queryRealTimeStreamStatus(@RequestBody @Valid ProduceOnlineMonitorParams onlineMonitorParams);
	
	
	/**
	 * 查询指定部门的能源费用占比
	 * @param onlineMonitorParams
	 * @return
	 */
	@RequestMapping( value="/energy-proxy/energy-provider-business/produce/queryEnergyCostData",method=RequestMethod.POST)
	public EnergyResp<EnergyCostDto> queryEnergyCostData(@RequestBody @Valid ProduceOnlineMonitorParams onlineMonitorParams);
	
}
