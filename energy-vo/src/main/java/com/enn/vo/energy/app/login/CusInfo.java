package com.enn.vo.energy.app.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("登录返回企业实体")
public class CusInfo {
    @ApiModelProperty(value="企业id",name="cusId",example="1377a9f5b83e4aa5808cb5493bb590e8")
    private String cusId;
    @ApiModelProperty(value="企业名称",name="cusName",example="广东达利园")
    private String cusName;

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }
}
