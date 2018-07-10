package com.enn.vo.energy.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Setter
@Getter
@ApiModel("查询车间下生产线的请求实体")
public class ElectricityReq {
	@ApiModelProperty("车间ID")
	@NotBlank( message = "车间ID不能为空")
	private String id;
}
