package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
@ApiModel("表格返回实体")
public class WorkshopResp {

	/**
	 * 查询的日期
	 */
	@ApiModelProperty("查询的日期")
	private String dateTime;

	/**
	 * 总电量
	 */
	@ApiModelProperty("总用电量")
	private BigDecimal sumQuantity;

	/**
	 * 尖电量
	 */
	@ApiModelProperty("尖电量")
	private BigDecimal tipQuantity;

	/**
	 * 峰电量
	 */
	@ApiModelProperty("峰电量")
	private BigDecimal peakQuantity;

	/**
	 * 平电量
	 */
	@ApiModelProperty("平电量")
	private BigDecimal flatQuantity;

	/**
	 * 谷电量
	 */
	@ApiModelProperty("谷电量")
	private BigDecimal valleyQuantity;

	/**
	 * 总费用
	 */
	@ApiModelProperty("总用电费用")
	private BigDecimal sumFees;

	/**
	 * 尖费用
	 */
	@ApiModelProperty("尖费用")
	private BigDecimal tipFees;

	/**
	 * 峰费用
	 */
	@ApiModelProperty("峰费用")
	private BigDecimal peakFees;

	/**
	 * 平费用
	 */
	@ApiModelProperty("平费用")
	private BigDecimal flatFees;

	/**
	 * 谷费用
	 */
	@ApiModelProperty("谷费用")
	private BigDecimal valleyFees;


}
