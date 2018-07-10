package com.enn.vo.energy.business.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月6日 下午8:37:06
* @Description 类描述
*/
@Data
public class SteamMeterReadingHourStatisticsBo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7673412882674191240L;
	
	/**
	 * 总蒸汽量
	 */
	private BigDecimal totalSteamPower;
	
	/**
	 * 总蒸汽费用
	 */
	private BigDecimal totalSteamFees;
	

}
