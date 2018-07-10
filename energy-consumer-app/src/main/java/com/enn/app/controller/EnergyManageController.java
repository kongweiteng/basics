package com.enn.app.controller;

import com.enn.constant.Constant;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.service.business.*;
import com.enn.vo.energy.CollectItemEnum;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.app.*;
import com.enn.vo.energy.business.bo.SteamMeterReadingDayBo;
import com.enn.vo.energy.business.req.*;
import com.enn.vo.energy.business.resp.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/energyManage")
@Api(tags = {"用能管理"})
public class EnergyManageController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(EnergyManageController.class);
    @Autowired
    private ICustElectricCountService custElectricCountService;
    @Autowired
    private ISteamMeterReadingService steamMeterReadingService;

    @Autowired
    private IOpentsdbService opentsdbService;

    @RequestMapping(value = "/findLastFees",method = RequestMethod.POST)
    @ApiOperation(value="获取昨日费用", notes="获取昨日费用")
    public EnergyResp<FeeCount> findLastFees(@RequestBody @Valid CustReq custReq, BindingResult result) {
        logger.info("获取昨日能源费用");
        EnergyResp<FeeCount> resp = new EnergyResp();
        FeeCount feeCount = new FeeCount();
        if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
        BigDecimal steamSum = null;
        BigDecimal elecSum = null;
        //获取昨日用电费用
        logger.info("获取昨日用电费用");
        EnergyResp<List<String>> meterNoResp = findElecNosByCust(custReq.getCustID());
        if (!StatusCode.SUCCESS.getCode().equals(meterNoResp.getCode())) {
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), meterNoResp.getError());
        }
        if (meterNoResp.getData() != null && meterNoResp.getData().size() > 0) {
            List<String> equipIDs = meterNoResp.getData();
            ElectricMeterReadingReq electricMeterReadingReq = new ElectricMeterReadingReq();
            String start = DateUtil.getLastDay(DateUtil.getNowDay());
            String end = DateUtil.getNowDay();
            electricMeterReadingReq.setStartTime(DateUtil.format(start, "yyyy-MM-dd"));
            electricMeterReadingReq.setEndTime(DateUtil.format(end, "yyyy-MM-dd"));
            electricMeterReadingReq.setMeterNoList(equipIDs);
            EnergyResp<List<StatisticsDataResp>> statistics = custElectricCountService.getElecSumByDay(electricMeterReadingReq);
            if (!StatusCode.SUCCESS.getCode().equals(statistics.getCode())) {
                logger.info("查询昨日用电费用错误");
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), statistics.getError());
            }
            if (statistics.getData() != null && statistics.getData().size() > 0) {
                List<StatisticsDataResp> statisticsDataRespList = statistics.getData();
                BigDecimal fees = statisticsDataRespList.get(0).getSumFees();
                elecSum = MathUtils.twoDecimal(MathUtils.divide(fees, new BigDecimal(10000)));
                feeCount.setElecFee(elecSum.toString());
                logger.info("昨日电费:" + elecSum);
            } else {
                logger.info("获取昨日用电费用为空");
            }
        } else {
            logger.info("企业下无主电表");
        }
        //获取昨日用气费用
        logger.info("获取昨日用汽费用");
        EnergyResp<List<String>> steamNoResp = findSteamNosByCust(custReq.getCustID());
        if (!StatusCode.SUCCESS.getCode().equals(steamNoResp.getCode())) {
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), steamNoResp.getError());
        }
        if (steamNoResp.getData() != null && steamNoResp.getData().size() > 0) {
            List<String> equipIDs = steamNoResp.getData();
            SteamMeterReadingDayBo steamMeterReadingDayBo = new SteamMeterReadingDayBo();
            String start = DateUtil.getLastDay(DateUtil.getNowDay());
            String end = DateUtil.getNowDay();
            steamMeterReadingDayBo.setStart(DateUtil.format(start, "yyyy-MM-dd"));
            steamMeterReadingDayBo.setEnd(DateUtil.format(end, "yyyy-MM-dd"));
            steamMeterReadingDayBo.setEquipID(equipIDs);

            EnergyResp<SteamDayAve> steamYesterday = steamMeterReadingService.getSteamYesterday(equipIDs);

            if (!StatusCode.SUCCESS.getCode().equals(steamYesterday.getCode())) {
                logger.info("查询昨日用电费用错误");
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), steamYesterday.getError());
            }
            if (steamYesterday.getData() != null) {
                SteamDayAve steamDayAve = steamYesterday.getData();
                BigDecimal fees = steamDayAve.getYesterdayMonth();
                steamSum = MathUtils.twoDecimal(MathUtils.divide(fees, new BigDecimal(10000)));
                feeCount.setSteamFee(steamSum.toString());
                logger.info("昨日汽费:" + steamSum);
            } else {
                logger.info("获取昨日用电费用为空");
            }
        } else {
            logger.info("企业下无主蒸汽表");
        }
        BigDecimal totalFee = MathUtils.add(steamSum, elecSum);
        logger.info("昨日能源总费用：" + totalFee);
        if (totalFee != null) {
            feeCount.setTotalFee(totalFee.toString());
        }
        resp.ok(feeCount);
        return resp;
    }

    @RequestMapping(value = "/elecOverview", method = RequestMethod.POST)
    @ApiOperation(value = "用电总览", notes = "用电总览")
    public EnergyResp<HistoryDataResp> elecOverView(@RequestBody @Valid CustReq custReq, BindingResult result) {
        EnergyResp<HistoryDataResp> energyResp = new EnergyResp<>();
        if (result.hasErrors()) {
            energyResp.setCode(StatusCode.C.getCode());
            energyResp.setMsg(result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        HistoryDataResp historyDataResp = new HistoryDataResp();
        EnergyResp<List<String>> meterNoResp = findElecNosByCust(custReq.getCustID());

        if (!meterNoResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
            logger.error("查询电表信息出现错误");
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), meterNoResp.getError());
        }
        //使用电表获取用电量
        if (meterNoResp.getData() != null && meterNoResp.getData().size() > 0) {
            List<String> meters = meterNoResp.getData();
            StatisticsDataReq month = new StatisticsDataReq();
            String startM = DateUtil.formatDate(DateUtil.getOneDay(), "yyyy-MM-dd HH:mm:ss");//获取本月1号0点0分0秒
            String end = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
            month.setStart(startM);
            month.setEnd(end);
            month.setMeters(meters);
            logger.info("获取本月用电量");
            EnergyResp<StatisticsDataResp> monthElectricity = custElectricCountService.getMonthElectricity(month);
            String lastMonthStart = DateUtil.getLastMonth(startM);//获取上月1号0点0分0秒
            String lastMonthEnd = DateUtil.getLastMonth(end);
            month.setStart(lastMonthStart);
            month.setEnd(lastMonthEnd);
            logger.info("获取上月同期用电量");
            EnergyResp<StatisticsDataResp> lastMonthElectricity = custElectricCountService.getMonthElectricity(month);
            BigDecimal quantity;
            BigDecimal lastquantity;
            if(!StatusCode.SUCCESS.getCode().equals(monthElectricity.getCode())){
                logger.error("查询本月用电量出错");
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), "查询本月用电量错误");
            }
            if ((StatusCode.SUCCESS.getCode().equals(monthElectricity.getCode())) && (monthElectricity.getData() != null)) {
                quantity = MathUtils.divide(monthElectricity.getData().getSumQuantity(), new BigDecimal(10000));
                logger.info("本月用电量为：" + monthElectricity.getData().getSumQuantity());
                if (quantity != null) {
                    historyDataResp.setQuantity(MathUtils.towDecimal(quantity).toString());
                }
            } else {
                logger.error("查询本月用电量为空");
            }
            if (!StatusCode.SUCCESS.getCode().equals(lastMonthElectricity.getCode())) {
                logger.error("查询上月同期用电量出错");
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), "查询上月同期用电量错误");
            }
            if ((StatusCode.SUCCESS.getCode().equals(lastMonthElectricity.getCode())) && (lastMonthElectricity.getData() != null)) {
                lastquantity = MathUtils.divide(lastMonthElectricity.getData().getSumQuantity(), new BigDecimal(10000));
                logger.info("上月同期用电量为：" + lastMonthElectricity.getData().getSumQuantity());
                if (lastquantity != null) {
                    historyDataResp.setLastQuantity(MathUtils.towDecimal(lastquantity).toString());
                }
            } else {
                logger.error("查询上月同期用电量为空");
            }
            BigDecimal sub = MathUtils.sub(monthElectricity.getData().getSumQuantity(), lastMonthElectricity.getData().getSumQuantity());
            BigDecimal divide = MathUtils.divide(sub, lastMonthElectricity.getData().getSumQuantity());
            historyDataResp.setQuantityRatio(divide == null ? "" : MathUtils.point(divide, 2).toString());
            energyResp.ok(historyDataResp);
        } else {

            energyResp.setMsg("该企业下无主电表");
            energyResp.setCode(StatusCode.SUCCESS.getCode());
            energyResp.setData(historyDataResp);
        }
        return energyResp;
    }

    @RequestMapping(value = "/steamOverview", method = RequestMethod.POST)
    @ApiOperation(value = "用汽总览", notes = "用汽总览")
    public EnergyResp<HistoryDataResp> steamOverview(@RequestBody @Valid CustReq custReq, BindingResult result) {
        EnergyResp<HistoryDataResp> energyResp = new EnergyResp<>();
        if (result.hasErrors()) {
            energyResp.setCode(StatusCode.C.getCode());
            energyResp.setMsg(result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        HistoryDataResp historyDataResp = new HistoryDataResp();
        EnergyResp<List<String>> meterNoResp = findSteamNosByCust(custReq.getCustID());

        if (!meterNoResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
            logger.info("查询企业用汽表出现错误");
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), meterNoResp.getError());
        }
        //使用蒸汽表获取蒸汽量
        if (meterNoResp.getData() != null && meterNoResp.getData().size() > 0) {
            List<String> meters = meterNoResp.getData();
            EnergyResp<LastMonthRatio> lastMonthRatioEnergyResp = steamMeterReadingService.getLastMonthRatio(meters);
            if (StatusCode.SUCCESS.getCode().equals(lastMonthRatioEnergyResp.getCode())) {

                BigDecimal quantity = MathUtils.divide(lastMonthRatioEnergyResp.getData().getWhenMonth(), new BigDecimal(10000));
                if (quantity != null) {
                    historyDataResp.setQuantity(MathUtils.towDecimal(quantity).toString());
                }
                BigDecimal lastquantity = MathUtils.divide(lastMonthRatioEnergyResp.getData().getLastMonth(), new BigDecimal(10000));
                if (lastquantity != null) {
                    historyDataResp.setLastQuantity(MathUtils.towDecimal(lastquantity).toString());
                }
                BigDecimal ratio = lastMonthRatioEnergyResp.getData().getRatio();
                if (ratio != null) {
                    historyDataResp.setQuantityRatio(MathUtils.towDecimal(ratio).toString());
                }
                energyResp.ok(historyDataResp);
            } else {
                energyResp.setCode(lastMonthRatioEnergyResp.getCode());
                energyResp.setMsg(lastMonthRatioEnergyResp.getMsg());
                return energyResp;
            }
        } else {
            energyResp.setCode(StatusCode.SUCCESS.getCode());
            energyResp.setData(historyDataResp);
            energyResp.setMsg("该企业无主蒸汽表");
        }
        return energyResp;
    }


    @ApiOperation(value = "首页-运行实时数据", notes = "实时数据")
    @RequestMapping(value = "/realData", method = RequestMethod.POST)
    public EnergyResp<RealTimeParameterResp> findCurrentValue(@RequestBody @Valid CustReq custReq, BindingResult result) {
        EnergyResp<RealTimeParameterResp> resp = new EnergyResp();
        RealTimeParameterResp realDataResp = new RealTimeParameterResp();
        if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
        CompanyMeteReq meteReq = new CompanyMeteReq();
        meteReq.setId(Long.parseLong(custReq.getCustID()));
        meteReq.setEnergyType("01");
        EnergyResp<List<MeterResp>> listEnergyResp = accountUnitService.queryMeterListByCompanyAndType(meteReq);
        logger.info("查询主电表号");
        List<String> equipIds = new ArrayList<>();
        if (StatusCode.SUCCESS.getCode().equals(listEnergyResp.getCode())) {
            LastReq lastReq = new LastReq();
            MeterResp meterResp = listEnergyResp.getData().get(0);
            equipIds.add(meterResp.getLoopNumber());
            lastReq.setMetric("EMS." + CollectItemEnum.COSavg);
            lastReq.setEquipID(equipIds);
            lastReq.setEquipMK(Constant.ELEC_EQUIPMK);
            lastReq.setStaId(meterResp.getStaId());

            EnergyResp<ListResp<LastResp>> listResp = opentsdbService.getLast(lastReq);
            if (StatusCode.SUCCESS.getCode().equals(listResp.getCode())) {
                List<LastResp> list = listResp.getData().getList();
                String sum = MathUtils.twoDecimal(new BigDecimal(list.get(0).getValue())).toString();
                logger.info("实时功率因数为：" + sum);
                realDataResp.setPowerFactor(sum);
            } else {
                logger.info("查询实时功率因数错误");
                throw new EnergyException(listEnergyResp.getCode(), listEnergyResp.getMsg(), listEnergyResp.getError());
            }
            lastReq.setMetric("EMS." + CollectItemEnum.Enow);
            EnergyResp<ListResp<LastResp>> lastListResp = opentsdbService.getLast(lastReq);
            if (StatusCode.SUCCESS.getCode().equals(lastListResp.getCode())) {
                List<LastResp> list = lastListResp.getData().getList();
                String sum = MathUtils.twoDecimal(new BigDecimal(list.get(0).getValue())).toString();
                realDataResp.setMaxDemand(sum);
            } else {
                logger.info("查询实时最大需量错误");
                throw new EnergyException(listEnergyResp.getCode(), listEnergyResp.getMsg(), listEnergyResp.getError());
            }
            if ((!StatusCode.SUCCESS.getCode().equals(lastListResp.getCode())) && (!StatusCode.SUCCESS.getCode().equals(listResp.getCode()))) {
                resp.setData(realDataResp);
                resp.setCode(listEnergyResp.getCode());
                resp.setMsg("从大数据平台获取数据失败");
                return resp;
            }
            resp.ok(realDataResp);
        } else {
            resp.setMsg(listEnergyResp.getMsg());
            resp.setCode(listEnergyResp.getCode());
        }
        return resp;
    }

}
