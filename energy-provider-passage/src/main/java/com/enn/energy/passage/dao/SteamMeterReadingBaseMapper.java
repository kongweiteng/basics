package com.enn.energy.passage.dao;

import com.enn.vo.energy.passage.vo.SteamMeterReadingBase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SteamMeterReadingBaseMapper {

    SteamMeterReadingBase baseVol(@Param("meterNo") String meterNo, @Param("year") String year);
}