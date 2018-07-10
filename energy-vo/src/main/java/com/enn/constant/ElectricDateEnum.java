package com.enn.constant;

/**
 * 用电量时间类型枚举
 *
 * @Author: 张洪源
 * @Date: 2018-05-04 19:05
 */
public enum ElectricDateEnum {

    DAY(0),
    MONTH(1);

    private int code;

    ElectricDateEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
