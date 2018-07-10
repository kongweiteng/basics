package com.enn.energy.business.service;


import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.business.bo.EnergyPlanBo;
import com.enn.vo.energy.business.dto.EnergyPlanDto;

/**
 * 能用计划管理页面
 * @author kai.guo
 *
 */
public interface IEnergyProgramManageService {
	
	/**
	 * 按照能源类型，年度信息查询能源计划
	 * @return
	 */
	public PagedList<EnergyPlanDto> queryEnergyProgramsByAssignedCondition(EnergyPlanBo energyPlanBo);
	
	/**
	 * 根据主键查询能源计划
	 * @param id
	 * @return
	 */
	public EnergyPlanDto queryEnergyProgramsByPrimaryKey(Long id);
	
	/**
	 * 新增能源计划
	 * @return
	 */
	public Boolean addEnergyProgram(EnergyPlanBo energyPlanBo);
	
	/**
	 * 修改能源计划
	 * @return
	 */
	public Boolean modifyEnergyProgram(EnergyPlanBo energyPlanBo);
	
}
