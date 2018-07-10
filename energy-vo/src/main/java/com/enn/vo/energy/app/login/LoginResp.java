package com.enn.vo.energy.app.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 登录返回
 */
@ApiModel("登录返回实体")
public class LoginResp {
    @ApiModelProperty(value="token令牌",name="token",example="1377a9f5b83e4aa5808cb5493bb590e8")
    private String token;
    @ApiModelProperty(value="联系方式",name="contact",example="400-888-888")
    private String contact;
    @ApiModelProperty(value="企业信息",name="msg")
    private CusInfo cusInfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public CusInfo getCusInfo() {
        return cusInfo;
    }

    public void setCusInfo(CusInfo cusInfo) {
        this.cusInfo = cusInfo;
    }

}
