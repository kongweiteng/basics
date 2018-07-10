package com.enn.vo.energy.business.dto;

import java.io.Serializable;

import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;

import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月6日 下午4:30:54
* @Description 生产在线监测 实体DTO类
*/
@Data
public class ProductOnlineMonitorDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4325048399650478930L;
	
	/**
	 * 电力系统24小时负荷数据
	 */
	private ListResp<RmiSamplDataResp> electricSampleData;
	
	/**
	 * 热力系统24小时流量数据
	 */
	private ListResp<RmiSamplDataResp> streamSampleData;
	
	/**
	 * 电力实时用电情况
	 */
	private ProduceOnlineElectricRealtimeSampleDto electricRealtimeSampleDto;
	
	/**
	 * 热力实时用汽情况
	 */
	private ProduceOnlineStreamRealtimeSampleDto streamRealtimeSampleDto;
	
	/**
	 * 能源费用占比对象
	 */
	private EnergyCostDto energyCostDto;

}
