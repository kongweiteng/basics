package com.enn.vo.energy.business.req;

import java.util.List;

/**
 * 统计电量请求参数
 */
public class StatisticsDataReq {
    private List<String> meters;
    private String start;
    private String end;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public List<String> getMeters() {
        return meters;
    }

    public void setMeters(List<String> meters) {
        this.meters = meters;
    }


}
