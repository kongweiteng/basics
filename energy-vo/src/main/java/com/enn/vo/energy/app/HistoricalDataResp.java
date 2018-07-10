package com.enn.vo.energy.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("历史数据返回实体")
public class HistoricalDataResp {

    @ApiModelProperty(value="数据类型",name="type",example="day")
    private String type;

    /**
     *day/month/year
     */
    @ApiModelProperty(value="用能类型",name="type",example="day")
    private String energyType;

    /*8
     *用电量
     */
    @ApiModelProperty(value="能源用量",name="electricity",example="100.00")
    private String electricity;

    /**
     *上期用电量
     */
    @ApiModelProperty(value="同期能源用量",name="lastElectricity",example="100.00")
    private String lastElectricity;

    /**
     *环比
     */
    @ApiModelProperty(value="环比值",name="mom",example="0.00")
    private String mom;

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

    public String getLastElectricity() {
        return lastElectricity;
    }

    public void setLastElectricity(String lastElectricity) {
        this.lastElectricity = lastElectricity;
    }

    public String getMom() {
        return mom;
    }

    public void setMom(String mom) {
        this.mom = mom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
