package com.enn.energy.business.dao;

import java.util.List;

import com.enn.vo.energy.business.condition.EnergyPlanCondition;
import com.enn.vo.energy.business.po.EnergyPlanPo;

public interface EnergyPlanPoMapper {
	
	int deleteByPrimaryKey(Long id);

    int insert(EnergyPlanPo record);

    int insertSelective(EnergyPlanPo record);

    EnergyPlanPo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyPlanPo record);

    int updateByPrimaryKey(EnergyPlanPo record);
    
    /**
     * 根据制定条件查询能源计划
     * @return
     */
    List<EnergyPlanPo> queryEnergyPlanByCondition(EnergyPlanCondition condition);
}