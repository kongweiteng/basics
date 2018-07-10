package com.enn.vo.energy.business.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zxj
 * @version 创建时间：2018年6月9日
 * @Description 类描述
 */
@Data
public class ElecMeterReadingDayStatisticsBo implements Serializable {


    private static final long serialVersionUID = 2584650365668775009L;

    /**
     * 总dian量
     */
    private BigDecimal totalElecPower;

    /**
     * 总dian费用
     */
    private BigDecimal totalElecFees;


    public BigDecimal getTotalElecPower() {
        return totalElecPower;
    }

    public void setTotalElecPower(BigDecimal totalElecPower) {
        this.totalElecPower = totalElecPower;
    }

    public BigDecimal getTotalElecFees() {
        return totalElecFees;
    }

    public void setTotalElecFees(BigDecimal totalElecFees) {
        this.totalElecFees = totalElecFees;
    }
}
