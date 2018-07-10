package com.enn.vo.energy.passage.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SteamMeterReadingBase implements Serializable {

    private Integer id;

    private String meterNo;

    private String metric;

    private String readTime;

    private BigDecimal baseQuantity;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public BigDecimal getBaseQuantity() {
        return baseQuantity;
    }

    public void setBaseQuantity(BigDecimal baseQuantity) {
        this.baseQuantity = baseQuantity;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
