package com.enn.vo.energy.business.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PriceBalanceDiagram {

    private BigDecimal capacityFees;
    private BigDecimal demandFees;
    private Date blanceTime;
    private BigDecimal loadRate;

    public BigDecimal getCapacityFees() {
        return capacityFees;
    }

    public void setCapacityFees(BigDecimal capacityFees) {
        this.capacityFees = capacityFees;
    }

    public BigDecimal getDemandFees() {
        return demandFees;
    }

    public void setDemandFees(BigDecimal demandFees) {
        this.demandFees = demandFees;
    }

    public Date getBlanceTime() {
        return blanceTime;
    }

    public void setBlanceTime(Date blanceTime) {
        this.blanceTime = blanceTime;
    }

    public BigDecimal getLoadRate() {
        return loadRate;
    }

    public void setLoadRate(BigDecimal loadRate) {
        this.loadRate = loadRate;
    }
}
