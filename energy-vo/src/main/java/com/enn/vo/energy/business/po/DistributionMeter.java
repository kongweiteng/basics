package com.enn.vo.energy.business.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("配电室计量点的信息")
public class DistributionMeter {

	@ApiModelProperty("计量仪表id")
	private Long meterId;
	@ApiModelProperty("回路编号")
	private String loopNumber;
	@ApiModelProperty("设备名称")
	private String meterName;
	@ApiModelProperty("")
	private Long distributionId;
	@ApiModelProperty("是否参数核算表")
	private String isAccoun;
	@ApiModelProperty("归属的无线模块id")
	private Long wifiId;
	@ApiModelProperty("能源类型")
	private String energyType;
	@ApiModelProperty("站点ID")
	private String staId;
	@ApiModelProperty("设备类型")
	private String equipMK;

}
