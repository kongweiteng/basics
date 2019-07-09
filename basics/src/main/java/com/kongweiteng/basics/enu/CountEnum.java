package com.kongweiteng.basics.enu;


import java.util.Arrays;

public enum CountEnum {
    one(1, " qi"),
    two(2, " chu"),
    t(3, " yan"),
    f(4, " zhao"),
    g(5, " han"),
    e(6, " wei");


    private Integer retCode;
    private String reMessage;

    CountEnum(Integer retCode, String reMessage) {
        this.retCode = retCode;
        this.reMessage = reMessage;
    }

    public static CountEnum forEach(int index) {
        CountEnum countEnum = Arrays.stream(CountEnum.values()).filter(e -> e.getRetCode() == index).findAny().orElse(null);
        return countEnum;

    }


    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public String getReMessage() {
        return reMessage;
    }

    public void setReMessage(String reMessage) {
        this.reMessage = reMessage;
    }

}
