package com.enn.vo.energy.business.dto;

import java.io.Serializable;

import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月6日 下午3:35:28
* @Description 类描述
*/
@Data
public class DevicePowerDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5371736193567880145L;
	
	/**
	 * 设备id
	 */
	private String equipID;
	
	/**
	 * 设备名称
	 */
	private String equipName;

	/**
	 * 设备功率值
	 */
    private String value;
    
    /**
     * 设备功率百分比
     */
    private String percent;

}
