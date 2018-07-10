package com.enn.energy.passage.dao;

import com.enn.energy.passage.vo.CalculateParam;
import com.enn.vo.energy.passage.vo.SteamMeterReadingMonth;
import com.enn.vo.energy.passage.vo.SteamPercentVo;
import com.enn.vo.energy.passage.vo.SteamVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SteamMeterReadingMonthMapper {


    List<SteamVo> steamMonthData();

    List<SteamPercentVo> percentVos();

    List<SteamPercentVo> lastPercentVos();

    void updateBatch(@Param("monthDatas") List<SteamMeterReadingMonth> monthDatas);

    void updateLastBatch(@Param("monthDatas") List<SteamMeterReadingMonth> monthDatas);

    void updateBatchFees(@Param("steamVoList") List<SteamVo> steamVoList);

    List<SteamVo> steamMonthDataByCondition(CalculateParam param);
}
