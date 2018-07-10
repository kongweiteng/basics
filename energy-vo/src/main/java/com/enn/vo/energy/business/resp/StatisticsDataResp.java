package com.enn.vo.energy.business.resp;

import java.math.BigDecimal;

public class StatisticsDataResp {

    /**
     * 总量
     */
    private BigDecimal sumQuantity;

    /**
     * 总费用
     */
    private BigDecimal sumFees;

    /**
     * 采集时间
     */
    private String readTime;

    public BigDecimal getSumQuantity() {
        return sumQuantity;
    }

    public void setSumQuantity(BigDecimal sumQuantity) {
        this.sumQuantity = sumQuantity;
    }

    public BigDecimal getSumFees() {
        return sumFees;
    }

    public void setSumFees(BigDecimal sumFees) {
        this.sumFees = sumFees;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }
}
