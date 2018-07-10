package com.enn.vo.energy.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ApiModel("企业用汽统计返回")
public class EnterpriseSteam {

    @ApiModelProperty(value="总用汽量")
    private BigDecimal countPows;

    @ApiModelProperty(value="企业用汽详情")
    private List<SteamMonthNum> steamMonthNumList;

}
