package com.enn.vo.energy.common.enums;

import lombok.Getter;

/**
* @author kai.guo
* @version 创建时间：2018年6月11日 下午8:14:39
* @Description 蒸汽测点枚举
*/
@Getter
public enum SteamMetricEnum {
	
	STEAM_TOTAL_FLOW("EMS.FsIntLP","蒸汽累计流量"),
	STEAM_FLOW("EMS.FsLP","蒸汽流量"),
	STEAM_PRESSURE("EMS.PAsLP","蒸汽压力"),
	STEAM_TEMPERATURE("EMS.TsLP","蒸汽温度"),
	EMS_P("EMS.P","功率");

	private String metric;
	
	private String desc;

	private SteamMetricEnum(String metric, String desc) {
		this.metric = metric;
		this.desc = desc;
	}

	

}
