package com.enn.vo.energy.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel("实时参数返回实体")
public class RealTimeParameterResp {

    /**
     *最大需量
     */
    @ApiModelProperty(value="最大需量",name="maxDemand",example="8096.90")
    private String maxDemand;

    /**
     *功率因数
     */
    @ApiModelProperty(value="实时功率因数",name="powerFactor",example="0.69")
    private String powerFactor;

    public String getMaxDemand() {
        return maxDemand;
    }

    public void setMaxDemand(String maxDemand) {
        this.maxDemand = maxDemand;
    }

    public String getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(String powerFactor) {
        this.powerFactor = powerFactor;
    }
}
