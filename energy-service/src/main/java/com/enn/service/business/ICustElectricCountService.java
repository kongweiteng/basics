package com.enn.service.business;

import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.po.ElectricMeterReadingDayPo;
import com.enn.vo.energy.business.req.ElectricMeterReadingReq;
import com.enn.vo.energy.business.req.StatisticsDataReq;
import com.enn.vo.energy.business.resp.*;
import org.apache.taglibs.standard.lang.jstl.ELEvaluator;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * 企业用电统计Service
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 18:25
 */
@FeignClient(value = "energy-zuul-gateway")
public interface ICustElectricCountService {

    /**
     * 查询企业分时电费占比(分钟)
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/custElectricCount/getSumByMinute", method = RequestMethod.POST)
    EnergyResp<ElectricMeterReadingResp> getSumByMinute(@RequestBody ElectricMeterReadingReq req);

    /**
     * 查询企业分时电费占比(日)
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/custElectricCount/getSumByDay", method = RequestMethod.POST)
    EnergyResp<ElectricMeterReadingResp> getSumByDay(@RequestBody ElectricMeterReadingReq req);

    /**
     * 查询企业分时电费占比(月)
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/custElectricCount/getSumByMonth", method = RequestMethod.POST)
    EnergyResp<ElectricMeterReadingResp> getSumByMonth(@RequestBody ElectricMeterReadingReq req);

    /**
     * 某时间段尖峰平谷用电曲线(月)
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/custElectricCount/getMeterByDay")
    EnergyResp<MeterReadingResp> getMeterByDay(@RequestBody ElectricMeterReadingReq req);

    /**
     * 某时间段尖峰平谷用电曲线(年)
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/custElectricCount/getMeterByMonth")
    EnergyResp<MeterReadingResp> getMeterByMonth(@RequestBody ElectricMeterReadingReq req);


    /**
     * 根据车间id查询各个生产线的用电情况（昨日数值）
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/custElectricCount/getYesterdayBoardUnit")
    public EnergyResp<List<YesterdayBoardUnitResp>> getYesterdayBoardUnit(@RequestBody DefaultReq defaultReq);


    /**
     * 查询企业每日电量、电费总计
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/custElectricCount/getElecSumByDay")
    EnergyResp<List<StatisticsDataResp>> getElecSumByDay(@RequestBody ElectricMeterReadingReq req);

    @RequestMapping(value = "/energy-proxy/energy-provider-business/custElectricCount/getElecSumByMonth")
    EnergyResp<List<StatisticsDataResp>> getElecSumByMonth(@RequestBody ElectricMeterReadingReq req);


    /**
     * 统计当月电量
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/custElectricCount/month")
    public EnergyResp<StatisticsDataResp> getMonthElectricity(StatisticsDataReq statisticsDataReq);

    /**
     * 统计上月同期
     * @return
     */
//    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/custElectricCount/lastMonth")
//    public EnergyResp<StatisticsDataResp> getLastMonthElectricity(StatisticsDataReq statisticsDataReq);

    /**
     * 统计当日电量
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/custElectricCount/day")
    public EnergyResp<StatisticsDataResp> getDayElectricity(StatisticsDataReq statisticsDataReq);

    /**
     * 统计昨日同期
     * @return
     */
//    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/custElectricCount/yesterday")
//    public EnergyResp<StatisticsDataResp> getYesterdayElectricity(StatisticsDataReq statisticsDataReq);


    /**
     * 统计当年电量
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/custElectricCount/year")
    public EnergyResp<StatisticsDataResp> getYearElectricity(StatisticsDataReq statisticsDataReq);

    /**
     * 统计去年同期
     * @return
     */
//    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/custElectricCount/lastYear")
//    public EnergyResp<StatisticsDataResp> getLastYearElectricity(StatisticsDataReq statisticsDataReq);


    /**
     * 用电日均量（7天的平均数）、日偏差量
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/custElectricCount/getElecDayAve")
    public EnergyResp<ElecDayAve> getElecDayAve(@RequestBody List<String> meters);

    /**
     * 查询日总用电量和总费用
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/custElectricCount/getElecSumByD", method = RequestMethod.POST)
    EnergyResp<ElectricMeterReadingDayPo> getElecSumByD(@RequestBody ElectricMeterReadingReq req);

    /**
     * 查询月总用电量和总费用
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/custElectricCount/getElecSumByM", method = RequestMethod.POST)
    EnergyResp<ElectricMeterReadingDayPo> getElecSumByM(@RequestBody ElectricMeterReadingReq req);
}
