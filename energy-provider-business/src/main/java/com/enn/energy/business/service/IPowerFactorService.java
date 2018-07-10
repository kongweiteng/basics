package com.enn.energy.business.service;

import com.enn.vo.energy.business.bo.StatisticsDataBo;

import java.math.BigDecimal;

public interface IPowerFactorService {

    BigDecimal findPowerFactorByDay(StatisticsDataBo statisticsDataBo);

    BigDecimal findPowerFactorByHour(StatisticsDataBo statisticsDataBo);
}
