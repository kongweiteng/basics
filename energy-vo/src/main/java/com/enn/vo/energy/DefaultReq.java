package com.enn.vo.energy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel("请求基类")
public class DefaultReq {
    @ApiModelProperty(value="id",name="id",example="123")
    @NotNull(message="id 不能为空！")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
