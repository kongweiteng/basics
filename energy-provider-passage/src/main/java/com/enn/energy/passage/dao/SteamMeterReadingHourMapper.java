package com.enn.energy.passage.dao;

import com.enn.energy.passage.vo.CalculateParam;
import com.enn.vo.energy.business.po.SteamMeterReadingHourPo;
import com.enn.vo.energy.passage.vo.SteamVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SteamMeterReadingHourMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SteamMeterReadingHourPo record);

    int insertSelective(SteamMeterReadingHourPo record);

    SteamMeterReadingHourPo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SteamMeterReadingHourPo record);

    int updateByPrimaryKey(SteamMeterReadingHourPo record);


    List<SteamVo> steamHourData();

    void updateBatch(@Param("steamVoList") List<SteamVo> steamVoList);

    List<SteamVo> steamHourDatas(CalculateParam param);
}