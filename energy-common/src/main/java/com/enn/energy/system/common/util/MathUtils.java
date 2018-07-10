package com.enn.energy.system.common.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by enn on 2018/1/8.
 */
public class MathUtils{
    /*
     *偏差量：实际量-申报量,
     */
    public static String offsetQuantity(String accountQuantity,String declareQuantity){
        if(StringUtils.isBlank(accountQuantity)|| StringUtils.isBlank(declareQuantity)){
            return "- -";
        }
        BigDecimal accountDecimal=new BigDecimal(accountQuantity);
        BigDecimal declareDecimal=new BigDecimal(declareQuantity);
        BigDecimal sub = sub(accountDecimal, declareDecimal);

        return String.valueOf(towDecimal(sub));
    }

    //偏差率：（实际量-申报量）/申报*100%
    public static String rate(String accountQuantity,String declareQuantity){
        if(StringUtils.isBlank(accountQuantity)|| StringUtils.isBlank(declareQuantity)){
            return "- -";
        }
        String offsetQuantity = offsetQuantity(accountQuantity, declareQuantity);
        if(!StringUtils.equalsIgnoreCase("- -",offsetQuantity)){
            BigDecimal offsetDecimal=new BigDecimal(offsetQuantity);
            BigDecimal declarQuantity=new BigDecimal(declareQuantity);
            BigDecimal divide = divide(offsetDecimal, declarQuantity);
            BigDecimal mul = mul(divide, new BigDecimal(100));
            return String.valueOf(towDecimal(mul));
        }
        return null;
    }

    /**
     * 提供精确的减法运算。  BigDecimal
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static BigDecimal sub(BigDecimal v1,BigDecimal v2){
        if(v1!=null&&v2!=null){
            return v1.subtract(v2);
        }
        return null;
    }

    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal v1,BigDecimal v2){
        if(v1!=null&&v2!=null){
            return v1.multiply(v2);
        }
        return null;
    }

    /**
     * 乘法运算  返回值保留小数（参数）
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal mul(BigDecimal v1,BigDecimal v2,int point){
        if(v1!=null&&v2!=null){
            return point(v1.multiply(v2),point);
        }
        return null;
    }
    /**
     * 保留两位小数  BigDecimal
     * @param v1
     * @return
     */
    public static BigDecimal towDecimal(BigDecimal v1){
        if(v1!=null){
            BigDecimal decimal = v1.setScale(2, BigDecimal.ROUND_HALF_UP);
            return decimal;
        }else {
            return null;
        }
    }

    /**
     * 保留两位小数  BigDecimal
     * @param value
     * @return
     */
    public static BigDecimal twoDecimal(BigDecimal value){
        if(null == value){
            value = new BigDecimal(0);
        }
        return value.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    /**
     * 保留两位小数  BigDecimal
     * @param value NULL保留null
     * @return
     */
    public static BigDecimal twoDecimalNullToNull(BigDecimal value){
        if(null == value){
            value = null;
        }
        return value.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    /**
     * 提供精确的除法运算。  BigDecimal
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal divide(BigDecimal v1,BigDecimal v2){
        if(v1!=null&&v2!=null){
            if(compare(v2,new BigDecimal(0))){
                return v1.divide(v2,4,BigDecimal.ROUND_HALF_UP);
            }
        }
        return null;
    }

    /**
     * 计算环比
     * @param v1
     * @param v2
     * @return
     */
    public static double divideMul(BigDecimal v1,BigDecimal v2){
        if(v1!=null&&v2!=null){
            BigDecimal v3 = sub(v1,v2);
            if(compare(v2,new BigDecimal(0))){
                BigDecimal v4 = v3.divide(v2,4,BigDecimal.ROUND_HALF_UP);
                return mul(v4,new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
        return 0;
    }

    /**
     * 提供精确的加法运算。
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static BigDecimal add(BigDecimal v1,BigDecimal v2){
        if(v1!=null&&v2!=null){
            return v1.add(v2);
        }else {
            if(v1!=null){
                return v1;
            }
            if(v2!=null){
                return v2;
            }
        }
        return null;
    }


    /**
     * 比较大小
     */
    public  static boolean compare(BigDecimal b1,BigDecimal b2){
        if (b1==null || b2==null){
            return false;
        }
        int i = b1.compareTo(b2);
        if(i==-1){
            //= -1,表示bigdemical小于bigdemical2；
            return false;
        }else if (i==1){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 保留 n 位小数  BigDecimal
     * @param v1
     * @return
     */
    public static BigDecimal point(BigDecimal v1,int n){
        if(v1!=null){
            BigDecimal decimal = v1.setScale(n, BigDecimal.ROUND_HALF_UP);
            return decimal;
        }else {
            return null;
        }


    }

    /**
     * 转为万千瓦时，保留四位小数
     * @param v1
     * @return
     */
    public static BigDecimal thousand(BigDecimal v1){
        if(v1!=null){
            BigDecimal decimal = divide(v1,new BigDecimal(10000));
            return decimal.setScale(4, BigDecimal.ROUND_HALF_UP);
        }else {
            return new BigDecimal(0);
        }
    }



    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, Object> sortMap = new TreeMap<String, Object>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }


}
