package com.enn.vo.energy.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
@ApiModel("能源费用返回实体")
public class FeeCount {
    /*
     *总费用
     */
    @ApiModelProperty(value="总费用",name="totalFee",example="100.00")
    private String totalFee;
    /*
     *用电费用
     */
    @ApiModelProperty(value="用电费用",name="elecFee",example="100.00")
    private String elecFee;
    /*
     *用汽费用
     */
    @ApiModelProperty(value="用汽费用",name="steamFee",example="100.00")
    private String steamFee;

    /*
     *用电环比
     */
    @ApiModelProperty(value="用电环比",name="elecProportion",example="100.00")
    private String elecProportion;
    /*
     *用汽环比
     */
    @ApiModelProperty(value="用汽环比",name="steamProportion",example="100.00")
    private String steamProportion;

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getElecFee() {
        return elecFee;
    }

    public void setElecFee(String elecFee) {
        this.elecFee = elecFee;
    }

    public String getSteamFee() {
        return steamFee;
    }

    public void setSteamFee(String steamFee) {
        this.steamFee = steamFee;
    }


    public String getElecProportion() {
        return elecProportion;
    }

    public void setElecProportion(String elecProportion) {
        this.elecProportion = elecProportion;
    }

    public String getSteamProportion() {
        return steamProportion;
    }

    public void setSteamProportion(String steamProportion) {
        this.steamProportion = steamProportion;
    }
}
