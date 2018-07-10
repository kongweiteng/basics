package com.enn.vo.energy.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 查询客户信息请求参数
 */
@ApiModel("查询申报电量请求实体")
public class ElectricityReportReq {
    @ApiModelProperty("要查询的年份")
    @NotBlank( message = "查询年份不能为空")
    private String year;

    @ApiModelProperty("用电企业id")
    @NotBlank( message = "custId不能为空")
    private String custId;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }
}
