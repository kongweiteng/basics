package com.kongweiteng.basics.enu;


import java.util.Arrays;

public enum CountEnum {
    qi(1, " qi"),
    chu(2, " chu"),
    yan(3, " yan"),
    zhao(4, " zhao"),
    han(5, " han"),
    wei(6, " wei");


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
