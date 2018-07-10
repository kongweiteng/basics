package com.enn.vo.energy.business.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月6日 下午3:29:26
* @Description 类描述
*/
@Data
public class ProduceOnlineElectricRealtimeSampleDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6508547283583972698L;
	
	/**
	 * 电力实时功率
	 */
	private BigDecimal electricRealTimePower;
	
	/**
	 * 电力24小时总用电功率
	 */
	private BigDecimal electric24HourTotalPower;
	
	/**
	 * 设备实时功率
	 */
	private List<DevicePowerDto> devicePowerDtos;
	

}
