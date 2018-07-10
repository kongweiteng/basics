package com.enn.vo.energy.business.bo;

import java.io.Serializable;

import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月6日 下午7:29:50
* @Description 生产在线监测  Service层BO实体类
*/
@Data
public class ProduceOnlineMonitorTransferBo implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2657630319888309175L;
	/**
	 * 生产在线监测部门id
	 */
	private Long departmentId;
	
	/**
	 * 能源类型名称
	 */
	private String energyTypeLabel;

}
