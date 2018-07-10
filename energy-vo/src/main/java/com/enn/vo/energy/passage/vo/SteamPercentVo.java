package com.enn.vo.energy.passage.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by sl
 * User: sl
 * Date: 2018/6/14
 * Time: 下午2:08
 */
public class SteamPercentVo implements Serializable {

    private Integer id;

    private String meterNo;

    private BigDecimal useQuantity;

    private BigDecimal lastUseQuantity;

    private String readTime;

    private String lastReadTime;


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

    public BigDecimal getLastUseQuantity() {
        return lastUseQuantity;
    }

    public void setLastUseQuantity(BigDecimal lastUseQuantity) {
        this.lastUseQuantity = lastUseQuantity;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(String lastReadTime) {
        this.lastReadTime = lastReadTime;
    }
}
