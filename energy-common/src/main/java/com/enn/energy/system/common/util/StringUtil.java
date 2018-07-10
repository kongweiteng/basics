package com.enn.energy.system.common.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liuch
 * @create 2017年03月20日 14:32
 */

public final class StringUtil {
    private static final String regValidatorIp = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";

    private static AtomicInteger atomicInteger =new AtomicInteger(1);

    /**
     * 检查字符串是否为空
     * <p>为null或者长度为0视为空字符串
     * @param value 要检查的字符串
     * @param trim 是否去掉头尾的特定字符
     * @param trimChars 要去掉的特定字符
     * @return
     */
    public static boolean isEmpty(String value, boolean trim, char... trimChars) {
        if (trim)
            return value == null || trim(value, trimChars).length() <= 0;
        return value == null || value.length() <= 0;
    }

    /**
     * 检查字符串是否为空
     * <p>为null或者长度为0视为空字符串
     * @param value 要检查的字符串
     * @param trim 是否去掉头尾的空格
     * @return
     */
    public static boolean isEmpty(String value, boolean trim) {
        return isEmpty(value, trim, ' ');
    }

    /**
     * 检查字符串是否为空
     * <p>为null或者长度为0视为空字符串
     * @param value 要检查的字符串
     * @return
     */
    public static boolean isEmpty(String value) {
        return isEmpty(value, false);
    }

    /**
     * 如果为null，转换为""
     * @param value
     * @return
     */
    public static String nullSafeString(String value) {
        return value == null ? "" : value;
    }

    /**
     * 确保存入数据库的string值不会引起数据库报错。
     * <p>
     * 1. 数据库不允许为null，value为nul时返回""；<br />
     * 2. 超过最大长度时截断字符串。
     * @param value 要存入数据库的字符串值。
     * @param nullable 是否允许为null。
     * @param maxLength 最大长度。
     * @return
     */
    public static String dbSafeString(String value, boolean nullable, int maxLength) {
        if (value == null) {
            if (nullable)
                return null;
            return nullSafeString(value);
        }
        if (value.length() > maxLength)
            return value.substring(0, maxLength);
        return value;
    }

    /**
     * 获取uuid
     * @param isLine  是否需要中划线
     * @return uuid字符串
     */
    public static  String getUUID(boolean isLine){
        if(isLine) {
            return UUID.randomUUID().toString();
        }
        return UUID.randomUUID().toString().replace("-","");
    }
    /**
     * 去掉头尾空格字符
     * @param value 待处理的字符串
     * @return
     */
    public static String trim(String value) {
        return trim(3, value, ' ');
    }

    /**
     * 去除字符串头尾的特定字符
     *
     * @param value 待处理的字符串
     * @param chars 需要去掉的特定字符
     * @return
     */
    public static String trim(String value, char... chars) {
        return trim(3, value, chars);
    }

    /**
     * 去除字符串头部的特定字符
     *
     * @param value 待处理的字符串
     * @param chars 需要去掉的特定字符
     * @return
     */
    public static String trimStart(String value, char... chars) {
        return trim(1, value, chars);
    }

    /**
     * 去除字符串尾部的特定字符
     * @param value 待处理的字符串
     * @param chars 需要去掉的特定字符
     * @return
     */
    public static String trimEnd(String value, char... chars) {
        return trim(2, value, chars);
    }

    /**
     * 去掉字符串头尾特定字符
     * @param mode
     * <li>1: 去掉头部特定字符；
     * <li>2: 去掉尾部特定字符；
     * <li>3: 去掉头尾特定字符；
     * @param value 待处理的字符串
     * @param chars 需要去掉的特定字符
     * @return
     */
    private static String trim(int mode, String value, char... chars) {
        if (value == null || value.length() <= 0)
            return value;

        int startIndex = 0, endIndex = value.length(), index = 0;
        if (mode == 1 || mode == 3) {
            // trim头部
            while (index < endIndex) {
                if (contains(chars, value.charAt(index++))) {
                    startIndex++;
                    continue;
                }
                break;
            }
        }

        if (startIndex >= endIndex)
            return "";

        if (mode == 2 || mode == 3) {
            // trim尾部
            index = endIndex - 1;
            while (index >= 0) {
                if (contains(chars, value.charAt(index--))) {
                    endIndex--;
                    continue;
                }
                break;
            }
        }

        if (startIndex >= endIndex)
            return "";
        if (startIndex == 0 && endIndex == value.length() - 1)
            return value;

        return value.substring(startIndex, endIndex);
    }

    private static boolean contains(char[] chars, char chr) {
        if (chars == null || chars.length <= 0)
            return false;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == chr)
                return true;
        }
        return false;
    }

    /**
     * 判断是否是有效的IP地址。
     * @param value
     * @return
     */
    public static boolean isIp(String value) {
        if (isEmpty(value))
            return false;
        return value.matches(regValidatorIp);
    }

    /**
     * 方法描述：rowId 序列
     * StringUtil.getRowIdSequence
     * <P>Author :  liuch </P>
     * <P>Date : 2017/3/20 </P>
     * <P>Params :  </P>
     * @return
     */
    public static String getRowIdSequence() {
        //7位序列号构造
        NumberFormat formatter = NumberFormat.getNumberInstance();

        if(atomicInteger.get()==1){
            Long l =new Date().getTime();
            StringBuffer stringBuffer =new StringBuffer(l.toString());
            stringBuffer.reverse();
            stringBuffer = new StringBuffer(stringBuffer.substring(0,5));
            atomicInteger.set(Integer.valueOf(stringBuffer.reverse().toString()));
        }
        Integer begin =1000000+atomicInteger.getAndIncrement();
        formatter.setMinimumIntegerDigits(7);
        formatter.setMaximumIntegerDigits(7);
        formatter.setGroupingUsed(false);
        return formatter.format(begin);
    }

    public static String getNumDouble(double dou, int num) {
        String retValue = null;
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(0);
        df.setMaximumFractionDigits(num);
        retValue = df.format(dou);
        retValue = retValue.replaceAll(",", "");
        return retValue;
    }
    /**
     * 判断返回json是不是jsonarray，如果是返回true
     */
    public static boolean va(String str) {
        boolean b = str.startsWith("[");
        return b;
    }
}
