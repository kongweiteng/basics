package com.enn.energy.system.common.util;

import java.util.UUID;

/**
 * @author kongweiteng
 * @Description 生成ID工具
 */
public class IdGenerator {

    /**
     * 生成uuid
     * @return
     */
    public static String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
