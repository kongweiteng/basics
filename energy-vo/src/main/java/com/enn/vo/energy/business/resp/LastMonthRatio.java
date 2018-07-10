package com.enn.vo.energy.business.resp;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LastMonthRatio {

    //当月
    private BigDecimal whenMonth;
    //上月同期
    private BigDecimal lastMonth;
    //环比
    private BigDecimal ratio;
}
