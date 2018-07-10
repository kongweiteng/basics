package com.enn.service.business;

import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.req.SteamMeterDayReq;
import com.enn.vo.energy.business.req.SteamMeterReadingReq;
import com.enn.vo.energy.business.resp.*;
import com.enn.vo.energy.business.upload.UploadResp;
import com.enn.vo.energy.web.SteamMonthNum;
import com.enn.vo.energy.web.SteamTimeNum;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 企业用汽接口
 * @author zxj
 * @since 2018-06-11
 */
@FeignClient(value = "energy-zuul-gateway")
public interface ISteamMeterReadingService {


    /**
     * 企业下核算单元的用汽统计
     * @param readingReq
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/steamMeterReading/getSteamUnitDay")
    public EnergyResp<List<SteamMonthNum>> getSteamUnitDay(@RequestBody SteamMeterReadingReq readingReq);


    /**
     * 企业下核算单元月、年用汽统计
     * @param readingReq
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/steamMeterReading/getSteamTimeMonthDay")
    public EnergyResp<List<SteamTimeNum>> getSteamTimeMonthDay(@RequestBody SteamMeterReadingReq readingReq);


    /**
     *  根据车间id查询各个生产线的用气情况（昨日数值）
     */

    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/steamMeterReading/getYesterdayBoardUnit")
    public EnergyResp<List<YesterdayBoardUnitResp>> getYesterdayBoardUnit(@RequestBody DefaultReq defaultReq);

    /**
     * 车间用汽统计-用汽曲线day
     * @param req
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/steamMeter/workshop/curveDataForDay")
    public EnergyResp<List<SteamMeterReadingResp>> curveDataForDay(@RequestBody SteamMeterDayReq req);

    /**
     * 车间用汽统计-车间用汽详情day
     * @param req
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/steamMeter/workshop/detailDataForDay")
    public EnergyResp<PagedList<SteamMeterReadingResp>> detailDataForDay(@RequestBody SteamMeterDayReq req);


    /**
     * 车间用汽统计-用汽曲线year
     * @param req
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/steamMeter/workshop/curveDataForYear")
    public EnergyResp<List<SteamMeterReadingResp>> curveDataForYear(@RequestBody SteamMeterDayReq req);

    /**
     * 车间用汽统计-车间用汽详情year
     * @param req
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/steamMeter/workshop/detailDataForYear")
    public EnergyResp<PagedList<SteamMeterReadingDetailResp>> detailDataForYear(@RequestBody SteamMeterDayReq req);


    /**
     * 用汽日均量（7天的平均数）、日偏差量
     * @param meterNos
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/steamMeterReading/getSteamDayAve")
    public EnergyResp<SteamDayAve> getSteamDayAve(@RequestBody List<String> meterNos);


    /**
     * 根据车间id查询昨日用汽量 、昨日环比（昨日-前日）/前日*100%
     * @param meterNos
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/steamMeterReading/getSteamYesterday")
    public EnergyResp<SteamDayAve> getSteamYesterday(@RequestBody List<String> meterNos);

    /**
     * 根据车间id查询上月各生产线总用汽量、总金额和车间每日用汽量
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/steamMeterReading/getLastMonthDetailed")
    public EnergyResp<DetailedResp> getLastMonthDetailed(@RequestBody String id);

    /**
     * 根据表号，查询本月及上月同期电量，环比(本月-上月)/上月
     * @param meterNo
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/steamMeterReading/getLastMonthRatio")
    public EnergyResp<LastMonthRatio> getLastMonthRatio(@RequestBody List<String> meterNo);

    /**
     * 车间用汽统计-车间用汽详情excel-day
     * @param req
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/steamMeter/workshop/detailDataForExcelDay")
    public EnergyResp<UploadResp> detailDataForExcelDay(@RequestBody SteamMeterDayReq req);

    /**
     * 车间用汽统计-车间用汽详情excel-year
     * @param req
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/steamMeter/workshop/detailDataForExcelYear")
    public EnergyResp<UploadResp> detailDataForExcelYear(@RequestBody SteamMeterDayReq req);

}
