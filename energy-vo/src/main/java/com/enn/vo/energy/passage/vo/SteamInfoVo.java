package com.enn.vo.energy.passage.vo;

import java.math.BigDecimal;

public class SteamInfoVo {

    private Integer id;

    private Integer custId;

    private BigDecimal basicPrice;

    private String startExecuteDate;

    private String isSinglePrice;

    private long diffTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public BigDecimal getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(BigDecimal basicPrice) {
        this.basicPrice = basicPrice;
    }

    public String getStartExecuteDate() {
        return startExecuteDate;
    }

    public void setStartExecuteDate(String startExecuteDate) {
        this.startExecuteDate = startExecuteDate;
    }

    public String getIsSinglePrice() {
        return isSinglePrice;
    }

    public void setIsSinglePrice(String isSinglePrice) {
        this.isSinglePrice = isSinglePrice;
    }

    public long getDiffTime() {
        return diffTime;
    }

    public void setDiffTime(long diffTime) {
        this.diffTime = diffTime;
    }
}
