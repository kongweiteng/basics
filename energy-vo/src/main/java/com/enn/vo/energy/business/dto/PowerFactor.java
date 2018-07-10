package com.enn.vo.energy.business.dto;

import com.enn.vo.energy.business.resp.DataResp;
import com.enn.vo.energy.business.resp.MetricAnalysisResp;

import java.math.BigDecimal;
import java.util.List;

public class PowerFactor {
    private BigDecimal lastValue;
    private BigDecimal avgValue;

    private List<MetricAnalysisResp> dataResp;

    private BigDecimal minValue;
    private String time;

    public BigDecimal getLastValue() {
        return lastValue;
    }

    public void setLastValue(BigDecimal lastValue) {
        this.lastValue = lastValue;
    }

    public BigDecimal getAvgValue() {
        return avgValue;
    }

    public void setAvgValue(BigDecimal avgValue) {
        this.avgValue = avgValue;
    }

    public List<MetricAnalysisResp> getDataResp() {
        return dataResp;
    }

    public void setDataResp(List<MetricAnalysisResp> dataResp) {
        this.dataResp = dataResp;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
