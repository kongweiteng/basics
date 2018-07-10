package com.enn.vo.energy.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("蒸汽基础信息")
public class SteamInfo {

    private long id;
    @ApiModelProperty("企业用户id")
    @NotNull(message="custId不能为空！")
    private long custId;

    @ApiModelProperty("能源单位")
    @NotBlank(message="能源单位不能为空！")
    private String unit;

    @ApiModelProperty("基本价格")
    @Min(value=0,message = "基本价格必须大于0！")
    private double basicPrice;

    @ApiModelProperty("开始执行日期")
    @NotBlank(message="开始执行日期不能为空！")
    @Length(min = 10, max = 10, message = "开始执行日期格式为：yyyy-MM-dd！")
    private String startExecuteDate;

    @ApiModelProperty("是否为单一价格")
    @NotNull(message="是否为单一价格 不能为空！")
    private char isSinglePrice;

    @ApiModelProperty("蒸汽基础信息价格阶梯")
    private List<SteamLadderPrice> steamLadderPrices;


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

    public double getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(double basicPrice) {
        this.basicPrice = basicPrice;
    }

    public String getStartExecuteDate() {
        return startExecuteDate;
    }

    public void setStartExecuteDate(String startExecuteDate) {
        this.startExecuteDate = startExecuteDate;
    }

    public char getIsSinglePrice() {
        return isSinglePrice;
    }

    public void setIsSinglePrice(char isSinglePrice) {
        this.isSinglePrice = isSinglePrice;
    }

    public List<SteamLadderPrice> getSteamLadderPrices() {
        return steamLadderPrices;
    }

    public void setSteamLadderPrices(List<SteamLadderPrice> steamLadderPrices) {
        this.steamLadderPrices = steamLadderPrices;
    }

}
