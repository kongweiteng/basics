package com.enn.vo.energy.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel("历史数据实体")
public class HistoryDataResp {

    /*
     *用量
     */
    @ApiModelProperty(value="能源用量",name="quantity",example="100.00")
    private String quantity;

    /**
     *上期用量
     */
    @ApiModelProperty(value="同期能源用量",name="lastQuantity",example="100.00")
    private String lastQuantity;

    /**
     *环比
     */
    @ApiModelProperty(value="环比值",name="quantityRatio",example="0.00")
    private String quantityRatio;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLastQuantity() {
        return lastQuantity;
    }

    public void setLastQuantity(String lastQuantity) {
        this.lastQuantity = lastQuantity;
    }

    public String getQuantityRatio() {
        return quantityRatio;
    }

    public void setQuantityRatio(String quantityRatio) {
        this.quantityRatio = quantityRatio;
    }
}
