package com.enn.vo.energy.business.upload;

import java.io.Serializable;

public class UploadResp implements Serializable{

    private Integer retCode;
    private String msg;
    private String path;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
