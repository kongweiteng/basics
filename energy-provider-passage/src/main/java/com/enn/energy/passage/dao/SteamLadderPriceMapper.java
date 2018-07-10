package com.enn.energy.passage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.enn.vo.energy.passage.vo.SteamLadderPriceVo;
import com.enn.vo.energy.system.SteamLadderPrice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SteamLadderPriceMapper extends BaseMapper<SteamLadderPrice>{

//    @Select("SELECT s.id,s.steam_id,s.start_value,s.end_value,s.steam_price FROM steam_ladder_price s WHERE steam_id=#{id} and start_value <= #{dosage} and end_value > #{dosage}")
    SteamLadderPrice findSteamLadderPriceBySteamInfoId(@Param("id") long id, @Param("dosage") String dosage);

    List<SteamLadderPriceVo> steamLadderPrices(Integer steamId);
}
