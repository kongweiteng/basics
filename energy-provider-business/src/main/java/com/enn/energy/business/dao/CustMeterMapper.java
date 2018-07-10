package com.enn.energy.business.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.enn.vo.energy.business.po.CustMeter;
import com.enn.vo.energy.business.vo.SteamUnitVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustMeterMapper extends BaseMapper<CustMeter> {


    List<String> loopNumbersForProductLine(@Param("workShopId") Integer workShopId, @Param("energyType") String energyType);

    SteamUnitVo custMeterByLoopNumber(String meterNo);

    BigDecimal getCapacity(String loopNumber);
}
