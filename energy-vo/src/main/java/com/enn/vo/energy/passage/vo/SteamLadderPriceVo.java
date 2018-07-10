package com.enn.vo.energy.passage.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class SteamLadderPriceVo implements Serializable {

    private Integer id;

    private Integer steamId;

    private Integer startValue;

    private Integer endValue;

    private BigDecimal steamPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSteamId() {
        return steamId;
    }

    public void setSteamId(Integer steamId) {
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

    public BigDecimal getSteamPrice() {
        return steamPrice;
    }

    public void setSteamPrice(BigDecimal steamPrice) {
        this.steamPrice = steamPrice;
    }
}
