package com.enn.vo.energy.business.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月6日 下午3:29:26
* @Description 实时用汽情况 DTO
*/
@Data
public class ProduceOnlineStreamRealtimeSampleDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6508547283583972698L;
	
	/**
	 * 蒸汽实时功率
	 */
	private BigDecimal streamRealTimePower;
	
	/**
	 * 蒸汽24小时总用电功率
	 */
	private BigDecimal stream24HourTotalPower;
	
	/**
	 * 设备实时功率
	 */
	private List<DevicePowerDto> devicePowerDtos;
	

}
