package com.enn.vo.energy.business.req;

import com.enn.vo.energy.DefaultReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("核算单元查询请求参数")
public class AccountUnitReq extends DefaultReq {
    @ApiModelProperty(value="是否是核算单元id（是true，否false）",name="isAccount",example="true")
    @NotNull(message="isAccount 不能为空！")
    private Boolean isAccount;//true表示是核算单元

    @ApiModelProperty(value="核算单元类型",name="accountingType",example="01")
    @NotNull(message="accountingType 不能为空！")
    private String accountingType;


}
