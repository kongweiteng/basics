package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("产品单耗生产线列表")
public class ProduceLineResp {

    @ApiModelProperty("产品列表ID")
    private Long productDataId;

    @ApiModelProperty("生产线ID")
    private Long lineId;

    @ApiModelProperty("生产线名称")
    private String lineName;

    @ApiModelProperty("生产线表号")
    private String lineMetric;

    @ApiModelProperty("统计维度")
    private String countDimensions;

    @ApiModelProperty("综合能源费用")
    private BigDecimal sumEnergyFees;

    @ApiModelProperty("总用电量")
    private BigDecimal countElectricity;

    @ApiModelProperty("总电费")
    private BigDecimal countElectricityFees;

    @ApiModelProperty("总蒸汽量")
    private BigDecimal countSteam;

    @ApiModelProperty("总蒸汽费用")
    private BigDecimal countSteamFees;

    @ApiModelProperty("生产产量")
    private BigDecimal produceNumber;

    @ApiModelProperty("生产单耗（电）")
    private BigDecimal produceConsumptionElectricity;

    @ApiModelProperty("生产单耗（蒸汽）")
    private BigDecimal produceConsumptionSteam;

    @ApiModelProperty("是否选中 ")
    private Boolean select;

    @ApiModelProperty("站ID")
    private String staId;

    public Long getProductDataId() {
        return productDataId;
    }

    public void setProductDataId(Long productDataId) {
        this.productDataId = productDataId;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineMetric() {
        return lineMetric;
    }

    public void setLineMetric(String lineMetric) {
        this.lineMetric = lineMetric;
    }

    public String getCountDimensions() {
        return countDimensions;
    }

    public void setCountDimensions(String countDimensions) {
        this.countDimensions = countDimensions;
    }

    public BigDecimal getSumEnergyFees() {
        return sumEnergyFees;
    }

    public void setSumEnergyFees(BigDecimal sumEnergyFees) {
        this.sumEnergyFees = sumEnergyFees;
    }

    public BigDecimal getCountElectricity() {
        return countElectricity;
    }

    public void setCountElectricity(BigDecimal countElectricity) {
        this.countElectricity = countElectricity;
    }

    public BigDecimal getCountElectricityFees() {
        return countElectricityFees;
    }

    public void setCountElectricityFees(BigDecimal countElectricityFees) {
        this.countElectricityFees = countElectricityFees;
    }

    public BigDecimal getCountSteam() {
        return countSteam;
    }

    public void setCountSteam(BigDecimal countSteam) {
        this.countSteam = countSteam;
    }

    public BigDecimal getCountSteamFees() {
        return countSteamFees;
    }

    public void setCountSteamFees(BigDecimal countSteamFees) {
        this.countSteamFees = countSteamFees;
    }

    public BigDecimal getProduceNumber() {
        return produceNumber;
    }

    public void setProduceNumber(BigDecimal produceNumber) {
        this.produceNumber = produceNumber;
    }

    public BigDecimal getProduceConsumptionElectricity() {
        return produceConsumptionElectricity;
    }

    public void setProduceConsumptionElectricity(BigDecimal produceConsumptionElectricity) {
        this.produceConsumptionElectricity = produceConsumptionElectricity;
    }

    public BigDecimal getProduceConsumptionSteam() {
        return produceConsumptionSteam;
    }

    public void setProduceConsumptionSteam(BigDecimal produceConsumptionSteam) {
        this.produceConsumptionSteam = produceConsumptionSteam;
    }

    public Boolean getSelect() {
        return select;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }

    public String getStaId() {
        return staId;
    }

    public void setStaId(String staId) {
        this.staId = staId;
    }
}
