package com.enn.energy.system.common.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 功能：http请求工具类
 * @author kongweiteng
 */
public class HttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * post 请求  传入json对象
     * @param url
     * @param jsonParam
     * @return
     */
    public static JSONObject httpPost(String url, JSONObject jsonParam) {
        String result = httpPostStr(url, jsonParam.toJSONString());
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(result);
        } catch (JSONException e) {
            logger.info("json 数据格式化异常：{}",e);
            return new JSONObject();
        }
        return jsonObject;
    }

    /**
     * post 请求  传入json 字符串
     * @param url
     * @param strParam
     * @return
     */
    public static String httpPostStr(String url, String strParam) {
        return  httpPostStr(url, strParam, null);
    }

    /**
     * post 请求  传入json 对象
     * @param url
     * @param jsonParam
     * @return
     */
    public static String httpPostStr(String url, JSONObject jsonParam) {
        return  httpPostStr(url, jsonParam.toJSONString(), null);
    }


    /**
     * post 请求  传入json 对象
     * @param url
     * @param jsonParam
     * @return
     */
    public static String httpPostStr(String url, JSONObject jsonParam, Header header) {
        return  httpPostStr(url, jsonParam.toJSONString(), header);
    }


    /**
     * post 请求  传入json 字符串  有header
     * @param url
     * @param strParam
     * @return
     */
    public static String httpPostStr(String url, String strParam,Header header) {
        // post请求返回结果
        String  jsonResult = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        if (header != null) {
            httpPost.setHeader(header);
        }
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(6000).setConnectTimeout(6000).build();
        httpPost.setConfig(requestConfig);
        try {
            if (null != strParam) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(strParam,"utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse result = httpClient.execute(httpPost);
            //请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                try {
                    //读取服务器返回过来的json字符串数据
                    jsonResult = EntityUtils.toString(result.getEntity(), "utf-8");
                    return jsonResult;
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + url, e);
                    throw new EnergyException(StatusCode.E_C.getCode(),StatusCode.E_C.getMsg(),"执行http请求失败！！"+e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new EnergyException(StatusCode.E_C.getCode(),StatusCode.E_C.getMsg(),"执行http请求失败！！"+e.getMessage());
        } finally {
            httpPost.releaseConnection();
        }

        return null;
    }



    public static JSONObject doGet(String url) {
        // get请求返回结果
        JSONObject jsonResult = null;

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000).setConnectTimeout(2000).build();
        request.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = client.execute(request);

            //请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //读取服务器返回过来的json字符串数据
                HttpEntity entity = response.getEntity();
                String strResult = EntityUtils.toString(entity, "utf-8");
                //把json字符串转换成json对象
                jsonResult = JSONObject.parseObject(strResult);
            } else {
                logger.error("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            logger.error("get请求提交失败:" + url, e);
        } finally {
            request.releaseConnection();
        }
        return jsonResult;
    }
}
