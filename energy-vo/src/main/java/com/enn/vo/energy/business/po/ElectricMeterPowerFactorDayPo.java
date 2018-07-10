package com.enn.vo.energy.business.po;

import java.io.Serializable;
import java.math.BigDecimal;

public class ElectricMeterPowerFactorDayPo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String meterNo;
    private String readTime;
    private BigDecimal powerFactor;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public BigDecimal getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(BigDecimal powerFactor) {
        this.powerFactor = powerFactor;
    }
}
