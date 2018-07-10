package com.enn.energy.system.service;

import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.SteamInfo;
import com.enn.vo.energy.system.req.ElectricInfoReq;

import java.util.List;

public interface SteamInfoService {

    EnergyResp<Integer>  addSteam(SteamInfo steamInfo);

    EnergyResp<Integer>  delSteam(String id);

    EnergyResp<SteamInfo> getSteamOne(String id);

    EnergyResp<PagedList<SteamInfo>> getSteamInfoList(ElectricInfoReq infoReq);
}
