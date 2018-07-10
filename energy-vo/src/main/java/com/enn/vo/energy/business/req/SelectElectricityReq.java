package com.enn.vo.energy.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 查询生产线电量(尖峰平谷)请求实体
 *
 * @Author: 肖明玉
 * @Date: 2018-06-08
 */
@Data
@ApiModel("车间用电统计查询请求实体")
public class SelectElectricityReq {


	@ApiModelProperty("开始时间")
	@NotBlank(message = "开始时间不能为空")
	//@Length(min = 7,max = 9,message = "时间格式不正确")
	private String startTime;
	@ApiModelProperty("结束时间")
	@NotBlank(message = "结束时间不能为空")
	//@Length(min = 7,max = 9,message = "时间格式不正确")
	private String endTime;
	@NotBlank(message = "时间类型不能为空")
	@ApiModelProperty("查询的类型month表示月，year表示年")
	private String downsample;
	@ApiModelProperty("表号")
	private List<String> equipID;
	@NotBlank(message = "生产线ID不能为空")
	@ApiModelProperty("生产线ID")
	private String id;


}
