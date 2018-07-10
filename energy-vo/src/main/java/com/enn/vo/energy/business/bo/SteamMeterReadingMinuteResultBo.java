package com.enn.vo.energy.business.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 蒸汽分钟统计
 * @author kai.guo
 *
 */
@Data
public class SteamMeterReadingMinuteResultBo {

    private String meterNo;

    private String metric;

    private String readTime;

    private BigDecimal useQuantity;

    private BigDecimal quantity;

    private BigDecimal fees;

}