package com.enn.vo.energy.app.login;

public class AppLoginParam {
    private String loginName;
    private String pwd;
    private Integer appType;
    private String channel;
    private String usToken;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getAppType() {
        return appType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUsToken() {
        return usToken;
    }

    public void setUsToken(String usToken) {
        this.usToken = usToken;
    }
}
