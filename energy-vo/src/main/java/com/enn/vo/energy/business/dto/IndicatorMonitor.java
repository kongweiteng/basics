package com.enn.vo.energy.business.dto;

public class IndicatorMonitor {
    private PowerFactor powerFactor;
    private MaxDemand maxDemand;
    private PriceBalanceDiagram priceBalanceDiagram;

    public PowerFactor getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(PowerFactor powerFactor) {
        this.powerFactor = powerFactor;
    }

    public MaxDemand getMaxDemand() {
        return maxDemand;
    }

    public void setMaxDemand(MaxDemand maxDemand) {
        this.maxDemand = maxDemand;
    }

    public PriceBalanceDiagram getPriceBalanceDiagram() {
        return priceBalanceDiagram;
    }

    public void setPriceBalanceDiagram(PriceBalanceDiagram priceBalanceDiagram) {
        this.priceBalanceDiagram = priceBalanceDiagram;
    }
}
