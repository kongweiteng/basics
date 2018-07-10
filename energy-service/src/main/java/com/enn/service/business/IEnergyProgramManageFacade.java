package com.enn.service.business;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.dto.EnergyPlanDto;
import com.enn.vo.energy.business.vo.EnergyPlanEntityParams;
import com.enn.vo.energy.business.vo.EnergyPlanQueryParams;
import com.enn.vo.energy.business.vo.EnergyPlanSavingEntityParams;


/**
 * 能用计划管理页面
 * @author kai.guo
 *
 */
@FeignClient(value = "energy-zuul-gateway")
public interface IEnergyProgramManageFacade {
	
	/**
	 * 查询指定条件的能源管理计划
	 * @param energyPlanParams
	 * @return
	 */
	@RequestMapping(value="/energy-proxy/energy-provider-business/energyProgramManage/queryEnergyPrograms",method=RequestMethod.POST)
	public EnergyResp<PagedList<EnergyPlanDto>> queryEnergyProgramsByAssignedCondition(@RequestBody EnergyPlanQueryParams energyPlanParams);
	
	
	@RequestMapping(value="/energy-proxy/energy-provider-business/energyProgramManage/addEnergyProgram",method=RequestMethod.POST)
	public EnergyResp<Boolean> addEnergyProgram(@RequestBody EnergyPlanSavingEntityParams energyPlanParams);
	
	
	@RequestMapping(value="/energy-proxy/energy-provider-business/energyProgramManage/mofigyEnergyProgram",method=RequestMethod.POST)
	public EnergyResp<Boolean> modifyEnergyProgram(@RequestBody EnergyPlanEntityParams energyPlanParams);
	
	
	@RequestMapping(value="/energy-proxy/energy-provider-business/energyProgramManage/queryEnergyProgramById",method=RequestMethod.POST)
	public EnergyResp<EnergyPlanDto> queryEnergyProgramById(@RequestParam("id") Long id);

}
