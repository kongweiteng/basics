package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 企业用电量及费用实体
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 15:20
 */
@Data
@ApiModel("企业用电及费用")
public class ElectricMeterReadingResp {

    /**
     * 总电量
     */
    @ApiModelProperty("总用电量")
    private BigDecimal sumQuantity = new BigDecimal(0);

    /**
     * 尖电量
     */
    @ApiModelProperty("尖电量")
    private BigDecimal tipQuantity = new BigDecimal(0);

    /**
     * 峰电量
     */
    @ApiModelProperty("峰电量")
    private BigDecimal peakQuantity = new BigDecimal(0);

    /**
     * 平电量
     */
    @ApiModelProperty("平电量")
    private BigDecimal flatQuantity = new BigDecimal(0);

    /**
     * 谷电量
     */
    @ApiModelProperty("谷电量")
    private BigDecimal valleyQuantity = new BigDecimal(0);

    /**
     * 总费用
     */
    @ApiModelProperty("总用电费用")
    private BigDecimal sumFees = new BigDecimal(0);

    /**
     * 尖费用
     */
    @ApiModelProperty("尖费用")
    private BigDecimal tipFees = new BigDecimal(0);

    /**
     * 峰费用
     */
    @ApiModelProperty("峰费用")
    private BigDecimal peakFees = new BigDecimal(0);

    /**
     * 平费用
     */
    @ApiModelProperty("平费用")
    private BigDecimal flatFees = new BigDecimal(0);

    /**
     * 谷费用
     */
    @ApiModelProperty("谷费用")
    private BigDecimal valleyFees = new BigDecimal(0);

    /**
     * 计算总用电量
     *
     * @return
     */
    public BigDecimal sumQuantity() {
        return tipQuantity.add(peakQuantity).add(flatQuantity).add(valleyQuantity);
    }

    /**
     * 计算总费用
     *
     * @return
     */
    public BigDecimal sumFees() {
        return tipFees.add(peakFees).add(flatFees).add(valleyFees);
    }
}
