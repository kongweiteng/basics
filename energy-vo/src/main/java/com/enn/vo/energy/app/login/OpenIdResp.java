package com.enn.vo.energy.app.login;

public class OpenIdResp {
    private  Integer retCode;
    private  String msg;
    private  String token;
    private  String openid;


    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }


    @Override
    public String toString() {
        return "OpenIdResp{" +
                "retCode=" + retCode +
                ", msg='" + msg + '\'' +
                ", token='" + token + '\'' +
                ", openid='" + openid + '\'' +
                '}';
    }
}
