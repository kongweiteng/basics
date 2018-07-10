package com.enn.energy.business.dao;

import com.enn.vo.energy.business.bo.SteamMeterReadingDayStatisticsBo;
import com.enn.vo.energy.business.condition.SteamMeterReadingDayCondition;
import com.enn.vo.energy.business.po.SteamMeterReadingDayPo;
import com.enn.vo.energy.business.req.SteamMeterDayReq;
import com.enn.vo.energy.business.resp.StatisticsDataResp;
import com.enn.vo.energy.business.resp.SteamMeterReadingDetailResp;
import com.enn.vo.energy.business.resp.SteamMeterReadingResp;
import com.enn.vo.energy.business.resp.YesterdayBoardUnitResp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SteamMeterReadingDayPoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SteamMeterReadingDayPo record);

    int insertSelective(SteamMeterReadingDayPo record);

    SteamMeterReadingDayPo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SteamMeterReadingDayPo record);

    int updateByPrimaryKey(SteamMeterReadingDayPo record);


    List<SteamMeterReadingDayPo> querySteamMeterReadingDayByAssignedConditon(SteamMeterReadingDayCondition condition);

    List<SteamMeterReadingDayPo> querySteamDayGroup(SteamMeterReadingDayCondition condition);

    /**
     * 获取指定条件下指定设备集合的蒸汽信息
     * @param condition
     * @return
     */
    SteamMeterReadingDayStatisticsBo countSteamMeterReadingDayByAssignedConditon(SteamMeterReadingDayCondition condition);


    public List<StatisticsDataResp> getSteamByDay(SteamMeterReadingDayCondition condition);


    /**
     * 根据 表号、时间  查询能源用量和  用费和
     */
    public YesterdayBoardUnitResp getYesterdayBoardUnit(@Param("time") String time, @Param("meters") List<String> meters);

    List<SteamMeterReadingResp> getSteamMeterForDay(SteamMeterDayReq req);

    List<SteamMeterReadingResp> getSteamMeterForYear(SteamMeterDayReq req);

    List<SteamMeterReadingDetailResp> getSteamMeterDetailForYear(SteamMeterDayReq req);

    List<SteamMeterReadingResp> boderSteamCurve(@Param("loopNumber") String loopNumber);
}