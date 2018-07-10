package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("计量表信息")
@Getter
@Setter
public class MeterResp {
    @ApiModelProperty("计量表ID")
    private Long meterId;

    @ApiModelProperty("设备编号")
    private String loopNumber;

    @ApiModelProperty("计量表名称")
    private String meterName;

    @ApiModelProperty("是否参与核算表(0:否，1是)")
    private String isAccoun;

    @ApiModelProperty("能源类型")
    private String energyType;

    @ApiModelProperty("站点id")
    private String staId;

}
