package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
 *用户年度申报电量VO
 */
@ApiModel("用户年度申报电量实体")
public class ElectricityReportStatus {
    //
    @ApiModelProperty("申报状态 0未申报,1已申报")
    private String status;

    @ApiModelProperty("年月yyyy-MM")
    private String yearMonth;

    @ApiModelProperty("用电企业id")
    private String custId;

    private String declareQuantityId;

    private String imgUrl;

    private String declareQuantity;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getDeclareQuantityId() {
        return declareQuantityId;
    }

    public void setDeclareQuantityId(String declareQuantityId) {
        this.declareQuantityId = declareQuantityId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDeclareQuantity() {
        return declareQuantity;
    }

    public void setDeclareQuantity(String declareQuantity) {
        this.declareQuantity = declareQuantity;
    }
}
