package com.enn.energy.system.service;

import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.ElectricInfo;
import com.enn.vo.energy.system.req.ElectricInfoReq;

import java.util.List;

public interface ElectricInfoService {

    EnergyResp<Integer>  addElectric(ElectricInfo electricInfo);

    EnergyResp<Integer>  delElectric(String id);

    EnergyResp<ElectricInfo> getElectricOne(String id);

    EnergyResp<PagedList<ElectricInfo>> getElectricList(ElectricInfoReq infoReq);

    EnergyResp<ElectricInfo> getElectricInfo(ElectricInfoReq infoReq);
}
