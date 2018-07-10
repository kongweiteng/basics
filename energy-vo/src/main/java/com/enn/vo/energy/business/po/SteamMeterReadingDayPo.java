package com.enn.vo.energy.business.po;

import java.math.BigDecimal;
import java.util.Date;

public class SteamMeterReadingDayPo {
    private Long id;

    private String meterNo;

    private String metric;

    private Date readTime;

    private BigDecimal useQuantity;

    private BigDecimal quantity;

    private BigDecimal fees;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo == null ? null : meterNo.trim();
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric == null ? null : metric.trim();
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public BigDecimal getUseQuantity() {
        return useQuantity;
    }

    public void setUseQuantity(BigDecimal useQuantity) {
        this.useQuantity = useQuantity;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }
}