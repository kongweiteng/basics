package com.enn.vo.energy.passage.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ApiModel("蒸汽费用计算参数")
public class SteamFeesCalculateReq implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6682435485228684170L;

	@ApiModelProperty(value = "当前蒸汽表使用之前用量", name = "beforeVol", example = "100.00")
    @NotNull(message = "beforeVol 不能为空！")
    private BigDecimal beforeVol;

    @ApiModelProperty(value = "当前蒸汽表使用量", name = "useVol", example = "10.00")
    @NotNull(message = "useVol 不能为空！")
    private BigDecimal useVol;

    @ApiModelProperty(value = "蒸汽表号", name = "meterNo", example = "11")
    @NotNull(message = "meterNo 不能为空！")
    private String meterNo;

}
