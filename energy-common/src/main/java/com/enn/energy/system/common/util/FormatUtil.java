package com.enn.energy.system.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 项目用到的时间和数字格式化工具类
 *
 * @author kai.guo
 */
public class FormatUtil {
    private static NumberFormat NF = new DecimalFormat("###,##0.00");
    private static NumberFormat NF_ONE = new DecimalFormat("###,##0.0");
    private static NumberFormat NF_INT = new DecimalFormat("###,##0");

    /**
     * 格式化金钱
     * eg.
     * 123456.88 -->  123,456.88
     * 123456.00 -->  123,456.00
     *
     * @param amount
     * @return
     */
    public static String formatMoney(BigDecimal amount) {
        String amountStr = "";
        if (amount != null) {
            amountStr = NF.format(amount);
        } else {
            amountStr = "0.00";
        }

        return amountStr;
    }

    /**
     * 格式化金钱
     * eg.
     * 123456.88 -->  123,456.88
     * 123456.00 -->  123,456.00
     *
     * @param amount
     * @return
     */
    public static String formatMoneyInChinese(BigDecimal amount) {
        String amountStr = "";
        if (amount != null) {
            amountStr = NF.format(amount);
        } else {
            amountStr = "0.00";
        }

        return "￥" + amountStr;
    }

    /**
     * 格式化金钱  保留以为小数
     * eg.
     * 123456.88 -->  123,456.8
     * 123456.00 -->  123,456.0
     *
     * @param amount
     * @return
     */
    public static String formatMoneyInChineseOne(BigDecimal amount) {
        String amountStr = "";
        if (amount != null) {
            amountStr = NF_ONE.format(amount);
        } else {
            amountStr = "0.0";
        }

        return "￥" + amountStr;
    }
    
    /**
     * 格式化金钱  保留以为小数
     * eg.
     * 123456.88 -->  123,456.8
     * 123456.00 -->  123,456
     *
     * @param amount
     * @return
     */
    public static String formatMoneyInChineseTwo(BigDecimal amount) {
        String amountStr = "";
        if (amount != null) {
            amountStr = NF_INT.format(amount);
        } else {
            amountStr = "0";
        }

        return "￥" + amountStr;
    }
    

    public static String formatMoneyInChineseFu(BigDecimal amount) {
        String amountStr = "";
        if (amount != null) {
            amountStr = NF.format(amount);
        } else {
            amountStr = "0.00";
        }

        return "￥ -" + amountStr;
    }

    /**
     * 格式化金钱
     * eg.
     * 123456.88 -->  123,456.88
     * 123456.00 -->  123,456.00
     *
     * @param amount
     * @return
     */
    public static String formatMoney(int amount) {
        BigDecimal bigDecimal = new BigDecimal(amount);
        String amountStr = "";
        if (amount > 0) {
            amountStr = NF_INT.format(bigDecimal);
        } else {
            amountStr = "0";
        }
        return amountStr;
    }

    public static String formatMoney(Long amount) {
        BigDecimal bigDecimal = new BigDecimal(amount);
        String amountStr = "";
        if (amount > 0) {
            amountStr = NF_INT.format(bigDecimal);
        } else {
            amountStr = "0";
        }
        return amountStr;
    }
    
    /**
     * 格式化数值
     * eg.
     * 123456.88 -->  123,456
     * 123456.00 -->  123,456
     *
     * @param amount
     * @return
     */
    public static String formatNum(BigDecimal amount,String numFormat) {
        String amountStr = "";
        if (amount != null) {
            NumberFormat format = new DecimalFormat(numFormat);
            amountStr = format.format(amount);
        } else {
            amountStr = "0.00";
        }
        return amountStr;
    }
    
    /**
     * 格式化bigDecimal数值，并保留小数点后两位
     * @param amount
     * @return
     */
    public static String formatNum(BigDecimal amount){
    	String amountStr = "";
        if (amount != null) {
        	amountStr =  formatNum(amount, "##0.00");
        } else {
            amountStr = "0.00";
        }
        return amountStr;
    }
    
    /**
     * 保留到小数点后几位        显示：47.00%
     * 求数值subA在总值中所占比例
     * @param subA
     * @param sumB
     * @return
     */
    public static String formatPercert(BigDecimal subA,BigDecimal sumB){
    	
    	BigDecimal result=BigDecimal.ZERO;
    	if(subA!=null&&sumB!=null){
            if(sumB.compareTo(BigDecimal.ZERO)>0){
            	result=subA.divide(sumB,4,BigDecimal.ROUND_HALF_UP);
            }
        }
    	NumberFormat nf   =   NumberFormat.getPercentInstance();     
    	nf.setMinimumFractionDigits(2);   
    	return nf.format(result);
    }
    
    /**
     * 将result转换为%比
     * @param result
     * @return
     */
    public static String formatPercert(BigDecimal result){
    	NumberFormat nf   =   NumberFormat.getPercentInstance();     
    	nf.setMinimumFractionDigits(2);   
    	return nf.format(result);
    }
    
    public static void main(String[] args) {
		/*BigDecimal amount =new BigDecimal("12334523467.0000");
		String msg= formatNum(amount, "##0.00");*/
    	
//    	String msg= formatPercert(new BigDecimal(100), new BigDecimal(100));
    	
    	String msg=formatPercert(new BigDecimal("0.4734"));
    	
		System.out.println("msg:==="+msg);
	}

}
