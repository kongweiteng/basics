package com.enn.vo.energy.system.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@ApiModel("客户信息请求实体")
public class WebReq {

    @ApiModelProperty("客户id")
    @NotNull(message = "custId 不能为空")
    private Long custId;

    @ApiModelProperty("产品名称")
    private String name;

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
