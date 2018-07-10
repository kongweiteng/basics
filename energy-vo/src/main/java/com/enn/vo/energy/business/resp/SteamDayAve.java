package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SteamDayAve {

    //用汽日均量
    @ApiModelProperty("用能日均量、昨日用能量")
    private BigDecimal dayAve;

    //日偏差量
    @ApiModelProperty("日偏差量、昨日环比")
    private BigDecimal dayDeviation;

    @ApiModelProperty("昨日用汽费用")
    private BigDecimal yesterdayMonth;

    @ApiModelProperty("昨日蒸汽量最大的表号")
    private String meterNo;

    @ApiModelProperty("昨日费用环比")
    private BigDecimal monthRatio;
}
