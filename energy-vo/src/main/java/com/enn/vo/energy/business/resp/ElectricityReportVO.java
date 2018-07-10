package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/*
 *用户年度申报电量VO
 */
@ApiModel("年度申报电量信息实体")
public class ElectricityReportVO {
    @ApiModelProperty("用电时间")
    private String elecDate;

    @ApiModelProperty("计划用电量")
    private String declareQuantity;

    @ApiModelProperty("实际用电量")
    private String accountQuantity;

    @ApiModelProperty("偏差电量")
    private String offsetQuantity;

    @ApiModelProperty("偏差率")
    private String rate;

    @ApiModelProperty("申报pdf地址,为时")
    private String imgUrl;

    public String getElecDate() {
        return elecDate;
    }

    public void setElecDate(String elecDate) {
        this.elecDate = elecDate;
    }

    public String getDeclareQuantity() {
        return declareQuantity;
    }

    public void setDeclareQuantity(String declareQuantity) {
        this.declareQuantity = declareQuantity;
    }

    public String getAccountQuantity() {
        return accountQuantity;
    }

    public void setAccountQuantity(String accountQuantity) {
        this.accountQuantity = accountQuantity;
    }

    public String getOffsetQuantity() {
        return offsetQuantity;
    }

    public void setOffsetQuantity(String offsetQuantity) {
        this.offsetQuantity = offsetQuantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
