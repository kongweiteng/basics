package com.enn.vo.energy.app;

import com.enn.vo.energy.business.resp.StatisticsDataResp;

import java.util.List;

public class EnergyTrendResp {
   private EnergyTrend energyTrend;
   private List<EnergyTrend> energyTrendList;

    public EnergyTrend getEnergyTrend() {
        return energyTrend;
    }

    public void setEnergyTrend(EnergyTrend energyTrend) {
        this.energyTrend = energyTrend;
    }

    public List<EnergyTrend> getEnergyTrendList() {
        return energyTrendList;
    }

    public void setEnergyTrendList(List<EnergyTrend> energyTrendList) {
        this.energyTrendList = energyTrendList;
    }
}
