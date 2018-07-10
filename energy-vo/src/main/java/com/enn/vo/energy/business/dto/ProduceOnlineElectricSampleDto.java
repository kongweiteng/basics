package com.enn.vo.energy.business.dto;

import java.io.Serializable;

import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;

import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月12日 下午3:12:34
* @Description 电力曲线采样数据
*/
@Data
public class ProduceOnlineElectricSampleDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5449138778918103960L;
	
	
	/**
	 * 电力系统24小时负荷数据
	 */
	private ListResp<RmiSamplDataDto> electricSampleData;

}
