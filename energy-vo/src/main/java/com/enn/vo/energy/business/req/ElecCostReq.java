package com.enn.vo.energy.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel("用电成本优化请求实体")
public class ElecCostReq {
    @ApiModelProperty("年月yyyy-MM-dd")
    @NotBlank( message = "时间不能为空")
    private String time;

    @ApiModelProperty("用电企业id")
    @NotNull( message = "custId不能为空")
    private Long custId;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }
}
