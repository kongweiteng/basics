package com.enn.vo.energy.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("历史数据请求实体")
public class HistoricalDataReq {


    @ApiModelProperty("客户id")
    @NotBlank(message = "客户ID不能为空")
    private String custId;

    @ApiModelProperty("day表示天，month表示月，year表示年")
    @NotBlank(message = "时间类型不能为空")
    private String downsample;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getDownsample() {
        return downsample;
    }

    public void setDownsample(String downsample) {
        this.downsample = downsample;
    }
}
