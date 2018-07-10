package com.enn.energy.business.dao;

import java.util.List;

import com.enn.vo.energy.business.bo.SteamMeterReadingMinuteStatisticsBo;
import com.enn.vo.energy.business.condition.SteamMeterReadingMinuteCondition;
import com.enn.vo.energy.business.po.SteamMeterReadingMinutePo;
import org.springframework.web.bind.annotation.ResponseBody;

public interface SteamMeterReadingMinutePoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SteamMeterReadingMinutePo record);

    int insertSelective(SteamMeterReadingMinutePo record);

    SteamMeterReadingMinutePo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SteamMeterReadingMinutePo record);

    int updateByPrimaryKey(SteamMeterReadingMinutePo record);
    
    /** 获取指定条件下的蒸汽统计信息
     * @param condition
     * @return
     */
    List<SteamMeterReadingMinutePo> querySteamMeterReadingMinuteByAssignedConditon(SteamMeterReadingMinuteCondition  condition);
    
    /**
     * 获取指定条件下指定设备集合的蒸汽信息
     * @param condition
     * @return
     */
    SteamMeterReadingMinuteStatisticsBo countSteamMeterReadingMinuteByAssignedConditon(SteamMeterReadingMinuteCondition condition);
}