package com.enn.vo.energy.business.bo;

import java.io.Serializable;

import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月6日 下午7:29:50
* @Description 生产在线监测  Service层BO实体类
*/
@Data
public class ProduceOnlineMonitorBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8791970459474533180L;
	
	/**
	 * 在线监测采样时间
	 */
	private String sampleEndTime;
	
	/**
	 * 在线监测采样开始时间
	 */
	private String sampleStartTime;
	
	/**
	 * 生产在线监测部门id
	 */
	private Long departmentId;

}
