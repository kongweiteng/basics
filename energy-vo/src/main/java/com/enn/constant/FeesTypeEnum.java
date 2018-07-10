package com.enn.constant;

/**
 * 电费上升或下降类型
 *
 * @Author: 张洪源
 * @Date: 2018-06-09 18:00
 */
public enum FeesTypeEnum {

    UP("UP"),
    DOWN("DOWN");

    private String code;

    FeesTypeEnum(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
