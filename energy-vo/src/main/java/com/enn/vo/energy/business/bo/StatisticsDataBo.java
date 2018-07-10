package com.enn.vo.energy.business.bo;

import java.util.List;

public class StatisticsDataBo {
    private List<String> meters;
    private String start;
    private String end;

    public List<String> getMeters() {
        return meters;
    }

    public void setMeters(List<String> meters) {
        this.meters = meters;
    }

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
}
