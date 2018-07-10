package com.enn.vo.energy.business.dto;

import java.io.Serializable;

import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;

import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月12日 下午3:14:11
* @Description 类描述
*/
@Data
public class ProduceOnlineStreamSampleDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2814007784604171960L;
	
	
	/**
	 * 热力系统24小时流量数据
	 */
	private ListResp<RmiSamplDataDto> streamFlowSampleData;
	
	
	/**
	 * 热力系统24小时流量数据
	 */
	private ListResp<RmiSamplDataDto> streamPressureSampleData;

}
