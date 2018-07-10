package com.enn.vo.energy.business.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 上一个月申报状态
 */
@ApiModel("申报状态请求实体")
public class ElectricityReportStatusReq {
    @ApiModelProperty("年月yyyy-MM")
    @NotBlank( message = "yearMonth不能为空")
    private String yearMonth;

    @ApiModelProperty("用电企业id")
    @NotBlank( message = "custId不能为空")
    private String custId;

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
}
