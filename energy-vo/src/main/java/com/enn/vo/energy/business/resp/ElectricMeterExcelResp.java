package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
@ApiModel("车间用电统计表格")
public class ElectricMeterExcelResp {
	/**
	 * 查询的日期
	 */
	@ApiModelProperty("查询的日期")
	private String dateTime;
	/**
	 * 封装返回实体
	 */
	private ElectricMeterReadingResp electricMeterReadingResp;
}
