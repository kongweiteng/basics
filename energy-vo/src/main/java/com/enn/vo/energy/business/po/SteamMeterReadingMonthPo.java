package com.enn.vo.energy.business.po;

import java.math.BigDecimal;
import java.util.Date;

public class SteamMeterReadingMonthPo {

    private Long id;

    private String meterNo;

    private String metric;

    private String readTime;

    private BigDecimal useQuantity;

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
