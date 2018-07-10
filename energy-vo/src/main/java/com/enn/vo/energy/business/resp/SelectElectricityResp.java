package com.enn.vo.energy.business.resp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * 查询生产线电量(尖峰平谷)返回实体
 *
 * @Author: 肖明玉
 * @Date: 2018-06-08
 */
@Data
@Api("车间用电统计查询响应实体")
public class SelectElectricityResp {

	@ApiModelProperty("客户id")
	private String custId;

	@ApiModelProperty("day表示天，month表示月，year表示年")
	private String downsample;

	@ApiModelProperty(value = "表号", name = "equipID", example = "['METE0026066784']")
	private List<String> equipID;

	@ApiModelProperty("尖峰平谷实体")
	private ElectricMeterReadingResp electricMeterReadingResp;

}
