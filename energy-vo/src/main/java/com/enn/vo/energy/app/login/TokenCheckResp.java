package com.enn.vo.energy.app.login;

public class TokenCheckResp {
    private Integer retCode;
    private String msg;
    private Boolean exist;

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

    public Boolean getExist() {
        return exist;
    }

    public void setExist(Boolean exist) {
        this.exist = exist;
    }


    @Override
    public String toString() {
        return "TokenCheckResp{" +
                "retCode=" + retCode +
                ", msg='" + msg + '\'' +
                ", exist=" + exist +
                '}';
    }
}
