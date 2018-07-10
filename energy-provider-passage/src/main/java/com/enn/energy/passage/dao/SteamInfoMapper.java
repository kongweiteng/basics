package com.enn.energy.passage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.enn.vo.energy.passage.vo.SteamInfoVo;
import com.enn.vo.energy.system.SteamInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SteamInfoMapper extends BaseMapper<SteamInfo> {


    SteamInfo findSteamByCustId(@Param("id") Long id);

    SteamInfoVo steamInfosByLoopNumber(String meterNo);
}
