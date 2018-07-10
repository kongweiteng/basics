package com.enn.vo.energy.user;

import lombok.Getter;
import lombok.Setter;

/**
 * 请求基础服务返回基类
 * @param <T>
 */
@Getter
@Setter
public class EnnBosstResp<T> {

    private String msg;
    private Integer code;
    private T entity;
}
