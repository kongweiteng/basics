package com.enn.energy.passage.service;

import com.enn.energy.passage.vo.CalculateParam;
import com.enn.vo.energy.passage.req.SteamFeesCalculateReq;
import com.enn.vo.energy.passage.resp.SteamFeesCalculateResp;

import java.math.BigDecimal;
import java.util.List;

public interface ISteamFeesTaskService {

    void steamFeesTaskForHour();

    BigDecimal steamFeesApi(SteamFeesCalculateReq req);
    
    /**
     * 多线程异步任务集
     * @param req
     */
    List<SteamFeesCalculateResp> steamFeesApi(List<SteamFeesCalculateReq> req);

    void steamFeesJobForDay();

    void steamFeesJobForMonth();

    void lastMonthPercent();

    void samePeriodPercent();

    void steamFeesJobForHour(CalculateParam param);

    void steamFeesJobForDayByCondition(CalculateParam param);

    void steamFeesJobForMonthByCondition(CalculateParam param);
}
