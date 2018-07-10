package com.enn.energy.business.service;

import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.po.ElectricMeterReadingDayPo;
import com.enn.vo.energy.business.req.ElectricMeterReadingReq;
import com.enn.vo.energy.business.resp.ElectricMeterReadingResp;
import com.enn.vo.energy.business.resp.MeterReadingResp;
import com.enn.vo.energy.business.resp.StatisticsDataResp;

import java.math.BigDecimal;
import java.util.List;


/**
 * 企业用电统计Service
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 16:08
 */
public interface ICustElectricCountService {

    /**
     * 查询企业分时电费占比(分钟)
     *
     * @param req
     * @return
     */
    ElectricMeterReadingResp getSumByMinute(ElectricMeterReadingReq req);

    /**
     * 查询企业分时电费占比(日)
     *
     * @param req
     * @return
     */
    ElectricMeterReadingResp getSumByDay(ElectricMeterReadingReq req);

    /**
     * 查询企业分时电费占比(月)
     *
     * @param req
     * @return
     */
    ElectricMeterReadingResp getSumByMonth(ElectricMeterReadingReq req);

    /**
     *查询时间段尖峰平谷时段每天的数据
     */
    MeterReadingResp getMeterByDay(ElectricMeterReadingReq req);
    /**
     * 按月查询时间段尖峰平谷每天的数据
     */
    MeterReadingResp getMeterByMonth(ElectricMeterReadingReq req);

    /**
     * 查询企业每日总用电、总费用
     */
    List<StatisticsDataResp> getElecSumByDay(ElectricMeterReadingReq req);

    /**
     * 查询企业每月总用电、总费用
     */
    List<StatisticsDataResp> getElecSumByMonth(ElectricMeterReadingReq req);

    EnergyResp<StatisticsDataResp> getMonthElectricity(String start, String end, List<String> device);

    EnergyResp<StatisticsDataResp> getDayElectricity(String start, String end, List<String> device );

    EnergyResp<StatisticsDataResp> getHourElectricity(String start, String end, List<String> device );

    EnergyResp<StatisticsDataResp> getYearElectricity(String start, String end, List<String> device );




}
