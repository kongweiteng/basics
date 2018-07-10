package com.enn.constant;

/**
 * 删除标识
 *
 * @Author: 张洪源
 * @Date: 2018-06-06 13:22
 */
public enum DelFlag {

    SAVE(0),
    DELETE(1);

    private Integer code;

    DelFlag(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
