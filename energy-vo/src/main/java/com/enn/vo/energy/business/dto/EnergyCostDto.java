package com.enn.vo.energy.business.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月7日 下午2:09:43
* @Description 生产在线监测  蒸汽，电量费用占比统计
*/
@Data
public class EnergyCostDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8441762583781552246L;
	
	/**
	 * 能源总费用
	 */
	private BigDecimal energyFees=BigDecimal.ZERO;
	
	
	/**
	 * 用电总费用
	 */
	private BigDecimal totalElectricFees=BigDecimal.ZERO;
	
	/**
	 * 电量百分比
	 */
	private String electricFeesPercert;
	
	/**
	 * 总蒸汽费用
	 */
	private BigDecimal totalSteamFees=BigDecimal.ZERO;
	
	/**
	 * 蒸汽百分比
	 */
	private String steamFeesPercent;

}
