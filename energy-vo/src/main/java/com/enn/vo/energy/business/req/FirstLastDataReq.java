package com.enn.vo.energy.business.req;

import java.util.List;

/**
 * 获取时间段内的第一条和最后一条数据 -请求实体
 */
public class FirstLastDataReq {
    private String start;
    private String end;
    private List<String> equipID;
    private String metric;
    private String staId;//站点id

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

    public List<String> getEquipID() {
        return equipID;
    }

    public void setEquipID(List<String> equipID) {
        this.equipID = equipID;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getStaId() {
        return staId;
    }

    public void setStaId(String staId) {
        this.staId = staId;
    }
}
