package com.enn.energy.business.service;

import com.enn.vo.energy.business.req.MonthBoardParamRep;
import com.enn.vo.energy.business.resp.MonthEnergyBoardResp;
import com.enn.vo.energy.business.resp.SteamMeterReadingResp;

import java.util.List;

/**
* @author sl
* @version 创建时间：2018年6月7日 上午10:02:13
* @Description 月能源看板
*/
public interface IMonthEnergyBoardService {


    MonthEnergyBoardResp boderData(MonthBoardParamRep req);

    List<SteamMeterReadingResp> boderSteamCurve(MonthBoardParamRep req);
}
