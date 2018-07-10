package com.enn.energy.system.common.util;


import org.apache.commons.lang3.StringUtils;

/**
 * 数据校验
 * @author kongweiteng
 */
public abstract class AssertUtil {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            //throw new EnergyException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            //throw new EnergyException(message);
        }
    }
}
