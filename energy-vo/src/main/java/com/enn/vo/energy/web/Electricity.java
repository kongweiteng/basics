package com.enn.vo.energy.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@ApiModel("企业用汽统计查询")
public class Electricity {

    @ApiModelProperty("客户id")
    @NotBlank(message = "客户ID不能为空")
    private String custId;

    @ApiModelProperty("1=day，2=month，3=year")
    @NotNull(message = "时间维度不能为空")
    private Integer downsample;

    @ApiModelProperty("开始时间")
    private String startDate;

    @ApiModelProperty("结束时间")
    private String endDate;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public Integer getDownsample() {
        return downsample;
    }

    public void setDownsample(Integer downsample) {
        this.downsample = downsample;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
