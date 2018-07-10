package com.enn.vo.energy.app;

import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;

@ApiModel("昨日数据返回实体")
public class YesterdayDataResp {
    /**
     * 能源用量
     */
    private String quantity;


    /**
     * 能源用量环比
     */
    private String quantityRatio;


    /**
     *  该能源费用环比
     */
    private String feeRatio;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityRatio() {
        return quantityRatio;
    }

    public void setQuantityRatio(String quantityRatio) {
        this.quantityRatio = quantityRatio;
    }

    public String getFeeRatio() {
        return feeRatio;
    }

    public void setFeeRatio(String feeRatio) {
        this.feeRatio = feeRatio;
    }
}
