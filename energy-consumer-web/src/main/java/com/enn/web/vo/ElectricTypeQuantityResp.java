package com.enn.web.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel("昨日看板尖峰平谷电量统计")
@Getter
@Setter
public class ElectricTypeQuantityResp implements Serializable {
    @ApiModelProperty("生产线名称")
    private String unitName;
    @ApiModelProperty("生产线id")
    private Long unitId;

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


}
