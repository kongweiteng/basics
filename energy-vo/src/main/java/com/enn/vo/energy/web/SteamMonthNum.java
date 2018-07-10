package com.enn.vo.energy.web;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@ApiModel("核算单元用汽统计")
public class SteamMonthNum {

    @ApiModelProperty(value="核算单元类型,02车间，03生产线")
    private String accountingType;

    @ApiModelProperty(value="核算单元名称")
    private String name;

    @ApiModelProperty(value="蒸汽费用")
    private BigDecimal steamMonth;

    @ApiModelProperty(value="蒸汽量")
    private BigDecimal steamAmount;

}
