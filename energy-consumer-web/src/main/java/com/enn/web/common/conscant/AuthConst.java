package com.enn.web.common.conscant;

import com.alibaba.fastjson.JSONObject;


/**
 * 授权过程中涉及到的常量字符串
 */
public class AuthConst {
    // 会话是否授权标志
    public static String IS_LOGIN = "isLogin";
    // 跳转参数
    public static String REDIRECT_URI = "redirect_uri";
    // session中用户key
    public static String USER = "user";
    public static String cusId = "cusId";
    public static String cusCust = "cus_cust";
    // 用户授权令牌
    public static String TICKET = "ticket";
    public static String user_name = "user_name";
    public static String user_mobile = "user_mobile";
    // 应用授权令牌
    public static JSONObject ACCESS_TOKEN = new JSONObject();
}
