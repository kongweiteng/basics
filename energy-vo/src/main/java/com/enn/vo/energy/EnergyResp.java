package com.enn.vo.energy;


import com.enn.constant.StatusCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 接口返回实体
 */
@ApiModel("com.enn.vo.energy.EtspResp")
public class EnergyResp<T>  implements Serializable {
    @ApiModelProperty(value="接口返回信息",name="msg",example="请求成功！！！")
    private String msg;
    @ApiModelProperty(value = "接口返回码",name="code",example="200")
    private Integer code;

    private T data;
    @ApiModelProperty(value = "接口返回错误信息",name="error",example="参数错误！！！")
    private String error;

    public void ok(T obj) {
        this.msg = StatusCode.SUCCESS.getMsg();
        this.code = StatusCode.SUCCESS.getCode();
        this.data = obj;
    }
    public void ok(T obj, String msg) {
        this.msg = msg;
        this.code = StatusCode.SUCCESS.getCode();
        this.data = obj;
    }

    public void null_obj(T obj) {
        this.code = StatusCode.E_B.getCode();
        this.msg = StatusCode.E_B.getMsg();
        this.data = obj;
    }
    public void null_obj(T obj,String error) {
        this.code = StatusCode.E_B.getCode();
        this.msg = StatusCode.E_B.getMsg();
        this.data = obj;
        this.error=error;
    }

    public void faile(T obj, String msg, Integer code) {
        this.setCode(code);
        this.setMsg(msg);
        this.setData(obj);
    }

    public void faile(Integer code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public void faile(Integer code, String msg, String error) {
        this.msg = msg;
        this.code = code;
        this.error = error;
    }

    public static <T> EnergyResp getRespObj(T param) {
        EnergyResp<T> obj = new EnergyResp<>();
        if (param == null) {
            obj.null_obj(param);
        } else {
            obj.ok(param, StatusCode.SUCCESS.getMsg());
        }
        return obj;
    }

    public static <T> EnergyResp<T> getRespList(T param) {
        List<T> list = (List) param;
        EnergyResp<T> resp = new EnergyResp<>();

        if (list != null && list.size() > 0) {
            resp.ok(param, StatusCode.SUCCESS.getMsg());
        } else {
            resp.null_obj(param);
        }
        return resp;
    }

    public EnergyResp(){}

    public EnergyResp(String msg, Integer code, String error) {
        this.msg = msg;
        this.code = code;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EnergyResp{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }
}
