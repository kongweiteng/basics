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
public class SteamMeterReadingDayStatisticsBo implements Serializable {


    private static final long serialVersionUID = 2584650365668775009L;

    /**
     * 总蒸汽量
     */
    private BigDecimal totalSteamPower;

    /**
     * 总蒸汽费用
     */
    private BigDecimal totalSteamFees;
}
