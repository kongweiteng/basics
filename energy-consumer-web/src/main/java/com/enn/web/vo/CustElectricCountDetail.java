package com.enn.web.vo;

import com.enn.vo.energy.business.resp.ElectricMeterReadingResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 企业用电详情实体
 *
 * @Author: 张洪源
 * @Date: 2018-06-09 16:48
 */
@Data
@ApiModel("企业用电详情")
public class CustElectricCountDetail {

    /**
     * 核算单元
     */
    @ApiModelProperty("核算单元名称")
    private String accountingUnitName;

    /**
     * 总用电量
     */
    @ApiModelProperty("总用电量")
    private BigDecimal sumQuantity;

    /**
     * 价格上升 up /下降 down
     */
    @ApiModelProperty("价格上升 up /下降 down")
    private String feesType;

    /**
     * 总电费
     */
    @ApiModelProperty("总电费")
    private BigDecimal sumFees;

    /**
     * 尖电费
     */
    @ApiModelProperty("尖电费")
    private BigDecimal sumTipFees;

    /**
     * 峰电费
     */
    @ApiModelProperty("峰电费")
    private BigDecimal sumPeakFees;

    /**
     * 平电费
     */
    @ApiModelProperty("平电费")
    private BigDecimal sumFlatFees;

    /**
     * 谷电费
     */
    @ApiModelProperty("谷电费")
    private BigDecimal sumValleyFees;

    /**
     * 实体转换
     *
     * @param resp
     * @return
     */
    public static CustElectricCountDetail trans(ElectricMeterReadingResp resp) {
        if (null == resp) {
            return null;
        }
        CustElectricCountDetail detail = new CustElectricCountDetail();
        detail.setSumQuantity(resp.getSumQuantity());
        detail.setSumFees(resp.getSumFees());
        detail.setSumTipFees(resp.getTipFees());
        detail.setSumPeakFees(resp.getPeakFees());
        detail.setSumFlatFees(resp.getFlatFees());
        detail.setSumValleyFees(resp.getValleyFees());
        return detail;
    }
}
