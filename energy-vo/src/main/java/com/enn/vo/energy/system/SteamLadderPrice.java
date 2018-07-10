package com.enn.vo.energy.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("蒸汽基础信息价格阶梯")
public class SteamLadderPrice {

    private long id;
    @ApiModelProperty("蒸汽基础信息id")
    private long steamId;
    @ApiModelProperty("开始值")
    private Integer startValue;
    @ApiModelProperty("结算值")
    private Integer endValue;
    @ApiModelProperty("单价")
    private double steamPrice;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSteamId() {
        return steamId;
    }

    public void setSteamId(long steamId) {
        this.steamId = steamId;
    }

    public Integer getStartValue() {
        return startValue;
    }

    public void setStartValue(Integer startValue) {
        this.startValue = startValue;
    }

    public Integer getEndValue() {
        return endValue;
    }

    public void setEndValue(Integer endValue) {
        this.endValue = endValue;
    }

    public double getSteamPrice() {
        return steamPrice;
    }

    public void setSteamPrice(double steamPrice) {
        this.steamPrice = steamPrice;
    }
}
