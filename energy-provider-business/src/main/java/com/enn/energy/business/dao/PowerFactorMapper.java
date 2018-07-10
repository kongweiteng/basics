package com.enn.energy.business.dao;

import com.enn.vo.energy.business.bo.StatisticsDataBo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PowerFactorMapper {
    BigDecimal findPowerFactorByDay(@Param("startTime")String startTime, @Param("endTime")String endTime, @Param("meterNoList")List<String> meterNoList);

    BigDecimal findPowerFactorByHour(@Param("startTime")String startTime, @Param("endTime")String endTime, @Param("meterNoList")List<String> meterNoList);

}
