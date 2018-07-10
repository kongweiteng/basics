package com.enn.web.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 功能：获取配置配置文件中以auth开头配置
 * @author kongweiteng
 */
@Component
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    //appid
    private String appid;
    //秘钥
    private String appsecret;
    //单点url
    private String uac_url;
    //登出冲定向地址
    private String logout_redirect_url;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getUac_url() {
        return uac_url;
    }

    public void setUac_url(String uac_url) {
        this.uac_url = uac_url;
    }

    public String getLogout_redirect_url() {
        return logout_redirect_url;
    }

    public void setLogout_redirect_url(String logout_redirect_url) {
        this.logout_redirect_url = logout_redirect_url;
    }
}
