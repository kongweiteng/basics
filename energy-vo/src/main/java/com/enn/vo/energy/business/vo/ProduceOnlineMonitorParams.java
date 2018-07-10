package com.enn.vo.energy.business.vo;

import java.io.Serializable;


import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月6日 下午7:29:50
* @Description 生产在线监测  rest层VO实体类
*/
@Data
@ApiModel("生产在线监测")
public class ProduceOnlineMonitorParams implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1193110042695361325L;

	/**
	 * 生产在线监测部门id
	 */
	@ApiModelProperty("部门id")
	@NotBlank( message = "部门id不能为空")
	private String departmentId;
	
	
	/**
	 * 在线监测采样开始时间
	 * 
	 */
	@ApiModelProperty(name="sampleCurrentTime", value="采样时间(格式：yyyy-MM-dd HH:mm:00)", example="2018-06-13 11:09:00")
	@NotBlank( message = "采样时间不能为空")
	private String sampleCurrentTime;

}
