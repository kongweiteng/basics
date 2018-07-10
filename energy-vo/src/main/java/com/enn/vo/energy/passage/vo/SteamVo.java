package com.enn.vo.energy.passage.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by sl
 * User: sl
 * Date: 2018/6/14
 * Time: 下午2:08
 */
public class SteamVo implements Serializable {

    private Integer id;

    private String meterNo;

    private BigDecimal useQuantity;

    private BigDecimal quantity;

    private BigDecimal fees;

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
