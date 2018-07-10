package com.enn.service.business;

import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.bo.SteamMeterReadingDayBo;
import com.enn.vo.energy.business.req.StatisticsDataReq;
import com.enn.vo.energy.business.req.SteamMeterReadingReq;
import com.enn.vo.energy.business.resp.StatisticsDataResp;
import com.enn.vo.energy.web.SteamMonthNum;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@FeignClient(value = "energy-zuul-gateway")
public interface ICustSteamCountService {
    @RequestMapping(value = "/energy-proxy/energy-provider-business/steamMeterReading/getSteamUnitDay", method = RequestMethod.POST)
    EnergyResp<List<SteamMonthNum>> getSteamUnitDay(@RequestBody SteamMeterReadingReq readingReq);

    @RequestMapping(value = "/energy-proxy/energy-provider-business/steamMeterReading/getSteamByDay", method = RequestMethod.POST)
    EnergyResp<List<StatisticsDataResp>> getSteamByDay(@RequestBody SteamMeterReadingDayBo req);

    @RequestMapping(value = "/energy-proxy/energy-provider-business/steamMeterReading/getSteamSumByMonth", method = RequestMethod.POST)
    EnergyResp<List<StatisticsDataResp>> getSteamSumByMonth(@RequestBody SteamMeterReadingDayBo req);

    /**
     * 统计当月电量
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/steamMeterReading/month")
    public EnergyResp<BigDecimal> getMonthSteam(StatisticsDataReq statisticsDataReq);

    /**
     * 统计上月同期
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/steamMeterReading/lastMonth")
    public EnergyResp<BigDecimal> getLastMonthSteam(StatisticsDataReq statisticsDataReq);
}
