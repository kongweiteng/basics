package com.enn.web.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EnergyQuantityAndFee {
    /**
     * 能源用量
     */
    private BigDecimal quantity;


    /**
     * 能源用量环比
     */
    private BigDecimal quantityRatio;


    /**
     *  该能源费用
     */
    private BigDecimal fee;

}
