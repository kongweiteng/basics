package com.enn.vo.energy.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("用户信息")
public class CustUser {

    @ApiModelProperty("客户id")
    private String custId;
    @ApiModelProperty("真实姓名")
    private String realname;
    @ApiModelProperty("企业名称")
    private String entName;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }
}
