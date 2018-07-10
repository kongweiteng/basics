package com.enn.vo.energy.business.resp;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductionLineResp {

    /**
     * 生产线名称
     */
    private String lineName;

    /**
     * 总蒸汽量
     */
    private BigDecimal totalSteamPower;

    /**
     * 总蒸汽费用
     */
    private BigDecimal totalSteamFees;
}
