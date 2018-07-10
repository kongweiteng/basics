package com.enn.vo.energy.passage.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class SteamMeterReadingMonth implements Serializable {

    private Integer id;

    private String meterNo;

    private String metric;

    private String readTime;

    private BigDecimal useQuantity;

    private BigDecimal fees;

    private BigDecimal lastMonthPercent;

    private BigDecimal samePeriodPercent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getLastMonthPercent() {
        return lastMonthPercent;
    }

    public void setLastMonthPercent(BigDecimal lastMonthPercent) {
        this.lastMonthPercent = lastMonthPercent;
    }

    public BigDecimal getSamePeriodPercent() {
        return samePeriodPercent;
    }

    public void setSamePeriodPercent(BigDecimal samePeriodPercent) {
        this.samePeriodPercent = samePeriodPercent;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public BigDecimal getUseQuantity() {
        return useQuantity;
    }

    public void setUseQuantity(BigDecimal useQuantity) {
        this.useQuantity = useQuantity;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }
}
