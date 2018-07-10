package com.enn.web.util;

import com.alibaba.fastjson.JSON;
import com.enn.web.common.config.AuthProperties;
import com.enn.web.common.conscant.AuthConst;
import com.enn.web.vo.DataResult;
import com.enn.web.vo.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginUtil {

    private static Log logger = LogFactory.getLog(LoginUtil.class);

    @Autowired
    protected AuthProperties authProperties;

    private static LoginUtil loginUtil;

    @PostConstruct //@PostConstruct修饰的方法会在服务器加载Servle的时候运行，并且只会被服务器执行一次。PostConstruct在构造函数之后执行,init()方法之前执行
    public void init() {
        loginUtil = this;
        loginUtil.authProperties = this.authProperties;
    }


    /**
     * 获取统一认证中心的accessToken
     * @return
     */
    public static String accessToken() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appid", loginUtil.authProperties.getAppid());
        params.put("appscrect", loginUtil.authProperties.getAppsecret());
        DataResult rtn = null;
        try {
            rtn = HTTPUtil.post(loginUtil.authProperties.getUac_url() + "/token", JSON.toJSONString(params));

            if (rtn != null && rtn.getErrno() == 0) {
                AuthConst.ACCESS_TOKEN = JSON.parseObject(JSON.toJSONString(rtn.getData()));
                AuthConst.ACCESS_TOKEN.put("date", System.currentTimeMillis());
                return AuthConst.ACCESS_TOKEN.getString("access_token");
            } else {
                logger.info("获取统一认证中心accessToken失败:"+ rtn.toString());
                return null;
            }
        } catch (Exception e) {
            logger.info("获取统一认证中心accessToken失败:"+ rtn.toString());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取统一用户中心用户信息
     * @param access_token
     * @param ticket
     * @return
     */
    public static User getUserInfo(String access_token, String ticket) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("ticket", ticket);
        params.put("access_token", access_token);
        DataResult rtn =null;
        try {
            rtn = HTTPUtil.post(loginUtil.authProperties.getUac_url() + "/userinfo", JSON.toJSONString(params));
            if (rtn != null && rtn.getErrno() == 0) {
                return JSON.parseObject(JSON.toJSONString(rtn.getData()), User.class);
            } else {
                logger.info("获取统一认证中心accessToken失败:"+ rtn.toString());
                return null;
            }
        } catch (Exception e) {
            logger.info("获取统一认证中心accessToken失败:"+ rtn.toString());
            e.printStackTrace();
        }
        return null;
    }

}
