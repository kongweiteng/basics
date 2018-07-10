package com.enn.energy.business.dao;

import java.util.List;

import com.enn.vo.energy.business.bo.SteamMeterReadingHourStatisticsBo;
import com.enn.vo.energy.business.condition.SteamMeterReadingHourCondition;
import com.enn.vo.energy.business.po.SteamMeterReadingHourPo;

public interface SteamMeterReadingHourPoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SteamMeterReadingHourPo record);

    int insertSelective(SteamMeterReadingHourPo record);

    SteamMeterReadingHourPo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SteamMeterReadingHourPo record);

    int updateByPrimaryKey(SteamMeterReadingHourPo record);
    
    
    /** 获取指定条件下的蒸汽统计信息
     * @param condition
     * @return
     */
    List<SteamMeterReadingHourPo> querySteamMeterReadingHourByAssignedConditon(SteamMeterReadingHourCondition  condition);
    
    /**
     * 获取指定条件下指定设备集合的蒸汽信息
     * @param condition
     * @return
     */
    SteamMeterReadingHourStatisticsBo countSteamMeterReadingHourByAssignedConditon(SteamMeterReadingHourCondition condition);
}