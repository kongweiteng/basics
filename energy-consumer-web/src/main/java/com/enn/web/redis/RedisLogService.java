package com.enn.web.redis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RedisLogService {


    enum CACHE_OPERATION {
        FIND, // 查询缓存操作
        UPDATE, // 需要执行修改缓存的操作
        INSERT; // 需要执行新增缓存的操作
    }

    /**
     * 存储的分组
     */
    String[] group();

    /**
     * 当前缓存操作类型
     */
    CACHE_OPERATION cacheOperation() default CACHE_OPERATION.FIND;

    /**
     * 存储的Key 默认加入类名跟方法名
     */
    String key() default "";

    /**
     * 是否使用缓存
     */
    boolean use() default true;

    /**
     * 超时时间
     */
    int expire() default 0;

    enum LOG_OPERATION {
        ON, // 开启日志记录
        OFF, // 关闭日志记录
    }

    /**
     * 当前缓存操作类型
     */
    LOG_OPERATION logOperation() default LOG_OPERATION.ON;

    /**
     * 操作名称
     */
    String name() default "";

    /**
     * 操作参数
     */
    String param() default "";

    /**
     * 日志参数 操作人操作IP,操作IP归属地
     */
    String logParam() default "";
}