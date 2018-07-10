package com.enn.vo.energy.web;


import com.enn.vo.energy.business.resp.DataResp;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

public class ElectricityCostOptimization {

    /*
     *用量
     */
    @ApiModelProperty(value="能源用量",name="quantity",example="100.00")
    private List<DataResp> dataResp;//散点图

    private BigDecimal loadDemand;//容需平衡负荷需量

    private BigDecimal loadRate;//负荷概率

    private BigDecimal capacityPrice;//容量基本电价
    private BigDecimal demandPrice;//需量基本电价（暂时未用）
    private String advice;//推荐基本电价方案

    public List<DataResp> getDataResp() {
        return dataResp;
    }

    public void setDataResp(List<DataResp> dataResp) {
        this.dataResp = dataResp;
    }

    public BigDecimal getLoadDemand() {
        return loadDemand;
    }

    public void setLoadDemand(BigDecimal loadDemand) {
        this.loadDemand = loadDemand;
    }

    public BigDecimal getLoadRate() {
        return loadRate;
    }

    public void setLoadRate(BigDecimal loadRate) {
        this.loadRate = loadRate;
    }

    public BigDecimal getCapacityPrice() {
        return capacityPrice;
    }

    public void setCapacityPrice(BigDecimal capacityPrice) {
        this.capacityPrice = capacityPrice;
    }

    public BigDecimal getDemandPrice() {
        return demandPrice;
    }

    public void setDemandPrice(BigDecimal demandPrice) {
        this.demandPrice = demandPrice;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
