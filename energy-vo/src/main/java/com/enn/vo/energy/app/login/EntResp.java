package com.enn.vo.energy.app.login;

import java.util.List;

public class EntResp {
    private Boolean exit;
    private String entName;
    private String realName;
    private String accType;
    private String entFaxNum;
    private String msg;
    private Integer retCode;
    private List<AccRole> accRoleList;

    public List<AccRole> getAccRoleList() {
        return accRoleList;
    }

    public void setAccRoleList(List<AccRole> accRoleList) {
        this.accRoleList = accRoleList;
    }

    public Boolean getExit() {
        return exit;
    }

    public void setExit(Boolean exit) {
        this.exit = exit;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getEntFaxNum() {
        return entFaxNum;
    }

    public void setEntFaxNum(String entFaxNum) {
        this.entFaxNum = entFaxNum;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }


    @Override
    public String toString() {
        return "EntResp{" +
                "exit=" + exit +
                ", entName='" + entName + '\'' +
                ", realName='" + realName + '\'' +
                ", accType='" + accType + '\'' +
                ", entFaxNum='" + entFaxNum + '\'' +
                ", msg='" + msg + '\'' +
                ", retCode=" + retCode +
                '}';
    }
}
