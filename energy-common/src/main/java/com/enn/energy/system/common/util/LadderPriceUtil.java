package com.enn.energy.system.common.util;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class LadderPriceUtil {

    /**
     * @param cycNum 起始周期累积量
     * @param useNum 用气量
     * @param price  价格信息， 1,1.2,2
     * @param vol    阶梯分界    100 200
     * @return
     */
    public static BigDecimal
    getMoney(BigDecimal cycNum, BigDecimal useNum, List<BigDecimal> price, List<BigDecimal> vol) {
        return getMoney(cycNum.add(useNum), price, vol)
                .subtract(getMoney(cycNum, price, vol));
    }

    /**
     * 获得0到指定量之间的金额
     *
     * @param num
     * @param price
     * @param vol1
     * @return
     */
    private static BigDecimal
    getMoney(BigDecimal num, List<BigDecimal> price, List<BigDecimal> vol1) {
        BigDecimal money = new BigDecimal("0");
        List<BigDecimal> vol = new LinkedList<BigDecimal>();
        vol.addAll(vol1);
        vol.add(0, new BigDecimal("0"));
        vol.add(new BigDecimal(Integer.MAX_VALUE));
        //i用于确定当前的气量在哪个区间,单价的话i是1  
        for (int i = 0; i < vol.size(); i++) {
            if (num.compareTo(vol.get(i)) >= 0 && num.compareTo(vol.get(i + 1)) < 0) {
                //找到指定的区间了  
                for (int j = 0; j <= i; j++) {

                    if (j != i) {
                        money = money.add((vol.get(j + 1).subtract(vol.get(j))).multiply(price.get(j)));
                    } else {
                        money = money.add((num.subtract(vol.get(j))).multiply(price.get(j)));
                    }

                }

                break;
            }
        }

        return money;
    }

    public static void main(String[] args) {

        List<BigDecimal> price = new LinkedList<BigDecimal>();
        List<BigDecimal> vol = new LinkedList<BigDecimal>();

        price.add(new BigDecimal("1.0"));
        price.add(new BigDecimal("1.2"));
        price.add(new BigDecimal("2.0"));

        vol.add(new BigDecimal("100"));
        vol.add(new BigDecimal("200"));

        System.out.println(getMoney(new BigDecimal("100"), new BigDecimal("150"), price, vol));

    }
}  