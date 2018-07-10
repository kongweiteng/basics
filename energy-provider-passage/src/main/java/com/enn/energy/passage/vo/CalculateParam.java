package com.enn.energy.passage.vo;

import java.io.Serializable;

/**
 * Created by sl
 * User: sl
 * Date: 2018/7/3
 * Time: 下午4:19
 */
public class CalculateParam implements Serializable {

    private String meterNo;

    private String startTime;

    private String endTime;

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
