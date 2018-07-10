package com.enn.energy.passage.dao;

import com.enn.energy.passage.vo.CalculateParam;
import com.enn.vo.energy.business.po.SteamMeterReadingDayPo;
import com.enn.vo.energy.passage.vo.SteamVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SteamMeterReadingDayMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SteamMeterReadingDayPo record);

    int insertSelective(SteamMeterReadingDayPo record);

    SteamMeterReadingDayPo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SteamMeterReadingDayPo record);

    int updateByPrimaryKey(SteamMeterReadingDayPo record);

    List<SteamVo> steamDayData();

    void updateBatch(@Param("steamVoList") List<SteamVo> steamVoList);

    List<SteamVo> steamDayDataByCondition(CalculateParam param);
}