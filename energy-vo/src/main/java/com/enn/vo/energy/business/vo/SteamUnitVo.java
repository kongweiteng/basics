package com.enn.vo.energy.business.vo;

import java.io.Serializable;

/**
 * Created by sl
 * User: sl
 * Date: 2018/6/12
 * Time: 下午8:19
 */
public class SteamUnitVo implements Serializable {

    private String unitName;

    private Integer unitId;

    private String loopNumber;

    private String energyType;

    private String unitType;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getLoopNumber() {
        return loopNumber;
    }

    public void setLoopNumber(String loopNumber) {
        this.loopNumber = loopNumber;
    }

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }
}
