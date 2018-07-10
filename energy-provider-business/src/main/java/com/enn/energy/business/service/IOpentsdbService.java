package com.enn.energy.business.service;

import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.req.*;
import com.enn.vo.energy.business.resp.LastResp;
import com.enn.vo.energy.business.resp.RmiFirstLastDataResp;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;

import java.util.List;
import java.util.Map;

/*
 * 获取opentsdb数据
 */
public interface IOpentsdbService {

    /**
     * 获取采样数据
     */
    public EnergyResp<ListResp<RmiSamplDataResp>> getSamplData(SamplDataReq samplDataReq);
    /**
     * 获取采样数据
     */
    public EnergyResp<ListResp<RmiSamplDataResp>> getSamplData(SamplDataStaReq samplDataStaReq);

    /**
     * 获取时间段内数据的第一条和最后一条
     */
    public EnergyResp<List<RmiFirstLastDataResp>> getFirstLastData(FirstLastDataReq firstLastDataReq);

    /**
     * 获取所有指标名称
     */
    public List<String> getAllMetric();

    /**
     * 获取所有的设备号
     */

    public List<String> getAllDevice();

    /**
     * 获取最后一条数据
     */
    public EnergyResp<ListResp<LastResp>> getLastDatas(LastReq last);
    /**
     * 获取最后一条数据--不同站点
     */
    public EnergyResp<ListResp<LastResp>> getLastDatas(LastStaReq last);

}
