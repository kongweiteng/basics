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
public class ElectricMeterReadingMinuteStatisticsBo implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -1344824619357064532L;
	 
	/**
	 * 总电量
	 */
	private BigDecimal totalElectricPower;
	
	/**
	 * 总费用
	 */
	private BigDecimal totalElectricFees;

}
