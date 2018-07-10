package com.enn.vo.energy.passage.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@ApiModel("蒸汽费用计算返回")
public class SteamFeesCalculateResp {

    @ApiModelProperty(value="蒸汽费用",name="fees",example="100.00")
    private BigDecimal fees;
    
    @ApiModelProperty(value = "蒸汽表号", name = "meterNo", example = "11")
    private String meterNo;

}
