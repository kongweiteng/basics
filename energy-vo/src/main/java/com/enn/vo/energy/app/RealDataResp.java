package com.enn.vo.energy.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("实时数据返回实体")
public class RealDataResp {

    /**
     *最大需量
     */
    @ApiModelProperty(value="最大需量",name="averagePower",example="8096.90")
    private String averagePower;

    /**
     *月平均功率因数
     */
    @ApiModelProperty(value="实时功率因数",name="monthAverage",example="0.69")
    private String powerFactor;

    public String getAveragePower() {
        return averagePower;
    }

    public void setAveragePower(String averagePower) {
        this.averagePower = averagePower;
    }

    public String getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(String powerFactor) {
        this.powerFactor = powerFactor;
    }
}
