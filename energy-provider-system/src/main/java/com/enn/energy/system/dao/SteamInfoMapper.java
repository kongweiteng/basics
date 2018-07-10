package com.enn.energy.system.dao;

import com.enn.vo.energy.system.SteamInfo;
import com.enn.vo.energy.system.SteamLadderPrice;
import com.enn.vo.energy.system.req.ElectricInfoReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SteamInfoMapper {

    /**
     * 新增蒸汽基础信息
     * @param steamInfo
     * @return
     */
    int addSteamInfo(SteamInfo steamInfo);

    /**
     * 新增蒸汽价格阶梯
     * @param steamLadderPrice
     * @return
     */
    int addSteamLadderPrice(SteamLadderPrice steamLadderPrice);

    /**
     * 修改蒸汽基础信息
     * @param steamInfo
     * @return
     */
    int updateSteamInfo(SteamInfo steamInfo);

    /**
     * 修改蒸汽价格阶梯
     * @param steamLadderPrice
     * @return
     */
    int updateSteamLadderPrice(SteamLadderPrice steamLadderPrice);

    /**
     * 删除蒸汽基础信息
     * @param id
     * @return
     */
    int delSteamInfo(@Param("id") String id);

    /**
     * 删除蒸汽价格阶梯
     * @param steamId
     * @return
     */
    int delSteamLadderPrice(@Param("steamId") String steamId);

    /**
     * 查询一条蒸汽基础信息
     * @param id
     * @return
     */
    SteamInfo getSteamInfoOne(@Param("id") String id);

    /**
     * 查询多条蒸汽基础信息
     * @param infoReq
     * @return
     */
    List<SteamInfo> getSteamInfoList(ElectricInfoReq infoReq);

    /**
     * 查询多条蒸汽价格阶梯
     * @param steamId
     * @return
     */
    List<SteamLadderPrice> getSteamLadderPrices(@Param("steamId") String steamId);

}
