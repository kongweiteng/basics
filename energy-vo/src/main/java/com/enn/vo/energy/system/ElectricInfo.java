package com.enn.vo.energy.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("电基础信息")
public class ElectricInfo {

    private long id;
    @ApiModelProperty("企业用户id")
    @NotNull(message="custId不能为空！")
    private long custId;

    @ApiModelProperty("能源单位")
    @NotBlank(message="能源单位不能为空！")
    private String unit;

    @ApiModelProperty("基本容量")
    @Min(value=0,message = "基本容量必须大于0！")
    private double basicCapacity;

    @ApiModelProperty("核定最大需量")
    @Min(value=0,message = "核定最大需量必须大于0！")
    private double ratifiedMaxDemand;

    @ApiModelProperty("基本电价变压器容量")
    @Min(value=0,message = "基本电价变压器容量必须大于0！")
    private double basicTransCapacity;

    @ApiModelProperty("基本电价最大需量")
    @Min(value=0,message = "基本电价最大需量必须大于0！")
    private double basicElePriceMaxCapacity;

    @ApiModelProperty("开始执行日期")
    @NotBlank(message="开始执行日期不能为空！")
    @Length(min = 10, max = 10, message = "开始执行日期格式为：yyyy-MM-dd！")
    private String startExecuteDate;

    @ApiModelProperty("执行特殊电价开始时间")
    private String exeSpecialPriceStartDate;

    @ApiModelProperty("执行特殊电价结束时间")
    private String exeSpecialPriceEndEate;

    @ApiModelProperty("尖时刻电价")
    @Min(value=0,message = "尖时刻电价必须大于0！")
    private double tipElePrice;

    @ApiModelProperty("峰时刻电价")
    @Min(value=0,message = "峰时刻电价必须大于0！")
    private double peakElePrice;

    @ApiModelProperty("谷时刻电价")
    @Min(value=0,message = "谷时刻电价必须大于0！")
    private double valleyElePrice;

    @ApiModelProperty("平时刻电价")
    @Min(value=0,message = "平时刻电价必须大于0！")
    private double flatElePrice;

    @ApiModelProperty("价格起始时间")
    private List<ElectricPriceTime> electricPriceTimes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustId() {
        return custId;
    }

    public void setCustId(long custId) {
        this.custId = custId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getBasicCapacity() {
        return basicCapacity;
    }

    public void setBasicCapacity(double basicCapacity) {
        this.basicCapacity = basicCapacity;
    }

    public double getRatifiedMaxDemand() {
        return ratifiedMaxDemand;
    }

    public void setRatifiedMaxDemand(double ratifiedMaxDemand) {
        this.ratifiedMaxDemand = ratifiedMaxDemand;
    }

    public double getBasicTransCapacity() {
        return basicTransCapacity;
    }

    public void setBasicTransCapacity(double basicTransCapacity) {
        this.basicTransCapacity = basicTransCapacity;
    }

    public double getBasicElePriceMaxCapacity() {
        return basicElePriceMaxCapacity;
    }

    public void setBasicElePriceMaxCapacity(double basicElePriceMaxCapacity) {
        this.basicElePriceMaxCapacity = basicElePriceMaxCapacity;
    }

    public String getStartExecuteDate() {
        return startExecuteDate;
    }

    public void setStartExecuteDate(String startExecuteDate) {
        this.startExecuteDate = startExecuteDate;
    }

    public String getExeSpecialPriceStartDate() {
        return exeSpecialPriceStartDate;
    }

    public void setExeSpecialPriceStartDate(String exeSpecialPriceStartDate) {
        this.exeSpecialPriceStartDate = exeSpecialPriceStartDate;
    }

    public String getExeSpecialPriceEndEate() {
        return exeSpecialPriceEndEate;
    }

    public void setExeSpecialPriceEndEate(String exeSpecialPriceEndEate) {
        this.exeSpecialPriceEndEate = exeSpecialPriceEndEate;
    }

    public double getTipElePrice() {
        return tipElePrice;
    }

    public void setTipElePrice(double tipElePrice) {
        this.tipElePrice = tipElePrice;
    }

    public double getPeakElePrice() {
        return peakElePrice;
    }

    public void setPeakElePrice(double peakElePrice) {
        this.peakElePrice = peakElePrice;
    }

    public double getValleyElePrice() {
        return valleyElePrice;
    }

    public void setValleyElePrice(double valleyElePrice) {
        this.valleyElePrice = valleyElePrice;
    }

    public double getFlatElePrice() {
        return flatElePrice;
    }

    public void setFlatElePrice(double flatElePrice) {
        this.flatElePrice = flatElePrice;
    }

    public List<ElectricPriceTime> getElectricPriceTimes() {
        return electricPriceTimes;
    }

    public void setElectricPriceTimes(List<ElectricPriceTime> electricPriceTimes) {
        this.electricPriceTimes = electricPriceTimes;
    }
}
