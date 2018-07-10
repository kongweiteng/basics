package com.enn.vo.energy.app;

import com.enn.vo.energy.business.resp.StatisticsDataResp;

import java.util.List;

public class EnergyTrend {
    private String name;
    private List<StatisticsDataResp> dataResp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDataResp(List<StatisticsDataResp> dataResp) {
        this.dataResp = dataResp;
    }

}
