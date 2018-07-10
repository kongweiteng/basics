package com.enn.energy.business.service.impl;

import com.enn.energy.business.dao.PowerFactorMapper;
import com.enn.energy.business.service.IPowerFactorService;
import com.enn.vo.energy.business.bo.StatisticsDataBo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class PowerFactorServiceImpl implements IPowerFactorService {

    @Resource
    private PowerFactorMapper powerFactorMapper;

    @Override
    public BigDecimal findPowerFactorByDay(StatisticsDataBo statisticsDataBo) {
        return powerFactorMapper.findPowerFactorByDay(statisticsDataBo.getStart(),statisticsDataBo.getEnd(),statisticsDataBo.getMeters());
    }

    @Override
    public BigDecimal findPowerFactorByHour(StatisticsDataBo statisticsDataBo) {
        return powerFactorMapper.findPowerFactorByHour(statisticsDataBo.getStart(),statisticsDataBo.getEnd(),statisticsDataBo.getMeters());
    }
}
