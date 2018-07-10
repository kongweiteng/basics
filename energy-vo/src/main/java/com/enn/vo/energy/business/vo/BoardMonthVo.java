package com.enn.vo.energy.business.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by sl
 * User: sl
 * Date: 2018/6/12
 * Time: 下午8:19
 */
public class BoardMonthVo implements Serializable, Comparable<BoardMonthVo> {
    private BigDecimal fees;

    private BigDecimal useQuantity;

    private BigDecimal percent;

    private Integer unitId;

    private String meterNo;

    private String unitName;

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public BigDecimal getUseQuantity() {
        return useQuantity;
    }

    public void setUseQuantity(BigDecimal useQuantity) {
        this.useQuantity = useQuantity;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BoardMonthVo() {
    }

    public BoardMonthVo(BigDecimal fees, BigDecimal useQuantity, BigDecimal percent, Integer unitId, String meterNo, String unitName) {
        this.fees = fees;
        this.useQuantity = useQuantity;
        this.percent = percent;
        this.unitId = unitId;
        this.meterNo = meterNo;
        this.unitName = unitName;
    }

    //覆盖Comparable接口里的compareTo方法
    @Override
    public int compareTo(BoardMonthVo o) {
        // 以useQuantity进行比较
        return useQuantity.compareTo(o.getUseQuantity());
    }
}
