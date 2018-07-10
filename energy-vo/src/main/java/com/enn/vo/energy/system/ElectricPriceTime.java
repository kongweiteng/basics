package com.enn.vo.energy.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("电基础信息价格起始时间")
public class ElectricPriceTime {

    private long id;
    @ApiModelProperty("电基础信息id")
    private long electricInfoId;

    @ApiModelProperty("类型(尖峰平谷)")
    @NotBlank
    private String type;

    @ApiModelProperty("时刻开始时间")
    @Length(min = 5, max = 5, message = "时刻开始时间格式为：HH:mm！")
    private String startDate;

    @ApiModelProperty("时刻结束时间")
    @Length(min = 5, max = 5, message = "时刻结束时间格式为：HH:mm！")
    private String endDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getElectricInfoId() {
        return electricInfoId;
    }

    public void setElectricInfoId(long electricInfoId) {
        this.electricInfoId = electricInfoId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
