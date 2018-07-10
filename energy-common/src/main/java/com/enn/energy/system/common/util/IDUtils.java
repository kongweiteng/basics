package com.enn.energy.system.common.util;

import java.util.Random;

/**
 * Created by enn on 2018/1/12.
 */
public class IDUtils {

    public static String genId() {

        //取当前时间长整形
        long currentTimeMillis = System.currentTimeMillis();
        //加上三位随机数
        Random random = new Random();
        int nextInt = random.nextInt(999);
        String str = currentTimeMillis + String.format("%03d", nextInt);
        long id = new Long(str);
        return String.valueOf(id);
    }
}
