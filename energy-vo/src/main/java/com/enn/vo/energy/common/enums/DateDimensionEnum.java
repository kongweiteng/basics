package com.enn.vo.energy.common.enums;

import lombok.Getter;

/**
* @author kai.guo
* @version 创建时间：2018年6月11日 下午2:51:36
* @Description 时间维度
*/
@Getter
public enum DateDimensionEnum {
	
	YEAR_DATE_DIMENSION("1","年"),
	MONTH_DATE_DIMENSION("2","月"),
	DAY_DATE_DIMENSITON("3","日");
	
	/**
	 * 时间维度
	 */
	private String timeDemension;
	
	/**
	 * 时间维度描述
	 */
	private String desc;
	

	private DateDimensionEnum(String timeDemension, String desc) {
		this.timeDemension = timeDemension;
		this.desc = desc;
	}
	

}
