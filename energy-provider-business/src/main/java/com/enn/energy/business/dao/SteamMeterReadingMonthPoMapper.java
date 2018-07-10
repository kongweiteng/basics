package com.enn.energy.business.dao;

import com.enn.vo.energy.business.bo.SteamMeterReadingMonthStatisticsBo;
import com.enn.vo.energy.business.condition.SteamMeterReadingMonthCondition;
import com.enn.vo.energy.business.po.SteamMeterReadingMonthPo;
import com.enn.vo.energy.business.resp.StatisticsDataResp;
import com.enn.vo.energy.business.vo.BoardMonthVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SteamMeterReadingMonthPoMapper {


    List<SteamMeterReadingMonthPo> querySteamMeterReadingMonthByAssignedConditon(SteamMeterReadingMonthCondition condition);

    List<SteamMeterReadingMonthPo> querySteamMonthGroup(SteamMeterReadingMonthCondition condition);

    /**
     * 获取指定条件下指定设备集合的蒸汽信息
     * @param condition
     * @return
     */
    SteamMeterReadingMonthStatisticsBo countSteamMeterReadingMonthByAssignedConditon(SteamMeterReadingMonthCondition condition);

    List<StatisticsDataResp> getSteamByMonth(SteamMeterReadingMonthCondition condition);

    List<BoardMonthVo> steamMonthData(@Param("loopNumbers") List<String> loopNumbers);

    BoardMonthVo steamMonthTotal(@Param("loopNumbers") List<String> loopNumbers, @Param("dateFlag") Integer dateFlag);

    BoardMonthVo steamMonthTotalByWorkShop(@Param("workShopId") Integer workShopId, @Param("dateFlag") Integer dateFlag);

    BoardMonthVo monthTotalVo(@Param("loopNumber") String loopNumber, @Param("dateFlag") Integer dateFlag);
}
