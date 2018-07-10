package com.enn.vo.energy.business.condition;


import java.util.List;

import com.enn.vo.energy.business.vo.Base;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 能源计划管理查询条件Conditon
 * @author kai.guo
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class EnergyPlanCondition extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2989541237804576617L;

	/**
	 * 能源类型集合
	 */
	private List<String> energyTypes;
	
	/**
	 * 能源 类型
	 */
	private String energyType;

    /**
     * 查询年度
     */
    private String yearPeriod;
    
    /**
     * 企业id
     */
    private Long custId;
    

}
