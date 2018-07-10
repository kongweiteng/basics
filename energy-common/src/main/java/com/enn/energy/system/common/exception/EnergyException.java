package com.enn.energy.system.common.exception;

/**
 * @author kongweiteng
 * 使用方式示例：throw new EnergyException(EnergyExceptionEnum.FILE_READING_ERROR);
 * @Description 业务异常的封装
 */
public class EnergyException extends RuntimeException {

    //友好提示的code码
    private Integer code;

    //友好提示
    private String msg;

    //业务异常跳转的页面
    private String error;

    public EnergyException() {

    }

    public EnergyException(String msg, String error) {
        this.msg = msg;
        this.error = error;
    }

    public EnergyException(Integer code, String msg, String error) {
        this.code = code;
        this.msg = msg;
        this.error = error;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
