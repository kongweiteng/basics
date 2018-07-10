package com.enn.energy.business.rest;

import com.enn.constant.StatusCode;
import com.enn.energy.business.service.IAccountingUnitService;
import com.enn.energy.business.service.ISteamMeterReadingService;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.bo.*;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.CustMeter;
import com.enn.vo.energy.business.po.SteamMeterReadingDayPo;
import com.enn.vo.energy.business.po.SteamMeterReadingMonthPo;
import com.enn.vo.energy.business.req.AccountUnitReq;
import com.enn.vo.energy.business.req.ElectricMeterReadingReq;
import com.enn.vo.energy.business.req.SteamMeterReadingReq;
import com.enn.vo.energy.business.resp.*;
import com.enn.vo.energy.common.enums.EnergyTypeEnum;
import com.enn.vo.energy.web.EnterpriseSteam;
import com.enn.vo.energy.web.SteamMonthNum;
import com.enn.vo.energy.web.SteamTimeNum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

/**
 * 用汽统计接口
 *
 * @author zxj
 * @since 2018-06-08
 */
@RestController
@RequestMapping("/steamMeterReading")
@Api(value = "用汽统计接口", tags = {"用汽统计"})
public class SteamMeterReadingController {

    private static final Logger logger = LoggerFactory.getLogger(SteamMeterReadingController.class);

    @Autowired
    private ISteamMeterReadingService steamMeterReadingService;
    @Autowired
    private IAccountingUnitService accountingUnitService;

    /**
     * 企业下核算单元的用汽统计
     *
     * @param readingReq
     * @return
     */
    @PostMapping("/getSteamUnitDay")
    public EnergyResp<List<SteamMonthNum>> getSteamUnitDay(@RequestBody SteamMeterReadingReq readingReq) {
        EnergyResp<List<SteamMonthNum>> enterpriseSteams = new EnergyResp<>();
        logger.info("取出核算单元 start");
        List<AccountingUnit> unitResps = accountingUnitService.getUnitByCompanyisd(readingReq.getCustId(), readingReq.getAccountingType());
        logger.info("取出核算单元 end");

        Collections.sort(unitResps, new Comparator<AccountingUnit>() {
            @Override
            public int compare(AccountingUnit o1, AccountingUnit o2) {
                return o1.getAccountingType().compareTo(o2.getAccountingType());
            }
        });


        List<SteamMonthNum> steamMonthNums = new ArrayList<>();
        SteamMonthNum steamMonthNum = null;
        for (AccountingUnit unitResp : unitResps) {
            List<CustMeter> meterResps = accountingUnitService.getAccountEleMeter(unitResp.getId(),"02");
            if(meterResps.size() == 0){
                EnergyResp<List<AccountingUnit>> listEnergyResp = accountingUnitService.getChildUnit(unitResp.getId()); //子核算单元
                List<AccountingUnit> acc = listEnergyResp.getData();
                if(acc.size()>0) {
                    List<Long> ids = new ArrayList<>();
                    for (AccountingUnit ac : acc) {
                        ids.add(ac.getId());
                    }
                    meterResps = accountingUnitService.getAccountEleMeter(ids, "02");
                }
            }
            List<String> equipID = new ArrayList<>();
            for (CustMeter meterResp : meterResps) {
                equipID.add(meterResp.getLoopNumber());
            }
            BigDecimal totalSteamPower = null;
            BigDecimal totalSteamFees = null;
            if (equipID.size() > 0) {
                steamMonthNum = new SteamMonthNum();
                steamMonthNum.setAccountingType(unitResp.getAccountingType());
                steamMonthNum.setName(unitResp.getName());

                if (readingReq.getDateType() == 1) {
                    SteamMeterReadingHourBo meterReadingHourBo = new SteamMeterReadingHourBo();
                    meterReadingHourBo.setStart(readingReq.getStartTime());
                    meterReadingHourBo.setEnd(readingReq.getEndTime());
                    meterReadingHourBo.setEquipID(equipID);
                    SteamMeterReadingHourStatisticsBo meterReadingHourStatisticsBo = steamMeterReadingService.countSteamMeterReadingHourByAssignedConditon(meterReadingHourBo);

                    if (meterReadingHourStatisticsBo != null) {
                        totalSteamPower = meterReadingHourStatisticsBo.getTotalSteamPower();  // 蒸汽量
                        totalSteamFees = meterReadingHourStatisticsBo.getTotalSteamFees();  // 蒸汽费用
                    }
                } else if (readingReq.getDateType() == 2) {
                    SteamMeterReadingDayBo meterReadingDayBo = new SteamMeterReadingDayBo();
                    meterReadingDayBo.setStart(readingReq.getStartTime());
                    meterReadingDayBo.setEnd(readingReq.getEndTime());
                    meterReadingDayBo.setEquipID(equipID);
                    SteamMeterReadingDayStatisticsBo meterReadingDayStatisticsBo = steamMeterReadingService.countSteamMeterReadingDayByAssignedConditon(meterReadingDayBo);
                    if (meterReadingDayStatisticsBo != null) {
                        totalSteamPower = meterReadingDayStatisticsBo.getTotalSteamPower();  // 蒸汽量
                        totalSteamFees = meterReadingDayStatisticsBo.getTotalSteamFees();  // 蒸汽费用
                    }
                } else if (readingReq.getDateType() == 3) {
                    SteamMeterReadingMonthBo meterReadingMonthBo = new SteamMeterReadingMonthBo();
                    meterReadingMonthBo.setStart(readingReq.getStartTime());
                    meterReadingMonthBo.setEnd(readingReq.getEndTime());
                    meterReadingMonthBo.setEquipID(equipID);
                    SteamMeterReadingMonthStatisticsBo meterReadingMonthStatisticsBo = steamMeterReadingService.countSteamMeterReadingMonthByAssignedConditon(meterReadingMonthBo);
                    if (meterReadingMonthStatisticsBo != null) {
                        totalSteamPower = meterReadingMonthStatisticsBo.getTotalSteamPower();
                        totalSteamFees = meterReadingMonthStatisticsBo.getTotalSteamFees();
                    }

                    //如果包含当月，取日表数据
                    String month = DateUtil.getMonth();
                    if (readingReq.getStartTime().compareTo(month) >= 0 || readingReq.getEndTime().compareTo(month) >= 0) {
                        SteamMeterReadingDayBo meterReadingDayBo = new SteamMeterReadingDayBo();
                        meterReadingDayBo.setStart(DateUtil.getMouthOneDay());
                        meterReadingDayBo.setEnd(DateUtil.getDay());
                        meterReadingDayBo.setEquipID(equipID);
                        SteamMeterReadingDayStatisticsBo meterReadingDayStatisticsBo = steamMeterReadingService.countSteamMeterReadingDayByAssignedConditon(meterReadingDayBo);
                        if (meterReadingDayStatisticsBo != null) {
                            totalSteamPower = MathUtils.add(totalSteamPower, meterReadingDayStatisticsBo.getTotalSteamPower());
                            totalSteamFees = MathUtils.add(totalSteamFees, meterReadingDayStatisticsBo.getTotalSteamFees());
                        }
                    }
                }
                steamMonthNum.setSteamAmount(MathUtils.towDecimal(totalSteamPower));
                steamMonthNum.setSteamMonth(MathUtils.towDecimal(totalSteamFees));
                steamMonthNums.add(steamMonthNum);
            }
        }

        enterpriseSteams.ok(steamMonthNums);
        return enterpriseSteams;
    }


    /**
     * 企业下核算单元月、年用汽统计
     *
     * @param readingReq
     * @return
     */
    @PostMapping("/getSteamTimeMonthDay")
    public EnergyResp<List<SteamTimeNum>> getSteamTimeMonthDay(@RequestBody SteamMeterReadingReq readingReq) {
        EnergyResp<List<SteamTimeNum>> listEnergyResp = new EnergyResp<>();
        List<SteamTimeNum> steamTimeNums = new ArrayList<>();

        List<MeterResp> meteResps = accountingUnitService.queryEleMeterListByCompany(Long.valueOf(readingReq.getCustId()), "02");
        if (meteResps.size() > 0) {
            SteamTimeNum steamTimeNum = null;
            List<String> equipID = new ArrayList<>();
            for(MeterResp meter:meteResps){
                equipID.add(meter.getLoopNumber());
            }
            //equipID.add(meteResps.get(0).getLoopNumber());
            if (readingReq.getDateType() == 2) {
                SteamMeterReadingDayBo meterReadingDayBo = new SteamMeterReadingDayBo();
                meterReadingDayBo.setStart(readingReq.getStartTime());
                meterReadingDayBo.setEnd(readingReq.getEndTime());
                meterReadingDayBo.setEquipID(equipID);
                List<SteamMeterReadingDayPo> steamMeterReadingDayPos = steamMeterReadingService.querySteamDayGroup(meterReadingDayBo);

                if (steamMeterReadingDayPos != null) {
                    for (SteamMeterReadingDayPo po : steamMeterReadingDayPos) {
                        steamTimeNum = new SteamTimeNum();
                        steamTimeNum.setValue(MathUtils.towDecimal(po.getUseQuantity()));
                        steamTimeNum.setDateTime(DateUtil.getDay(po.getReadTime()));
                        steamTimeNums.add(steamTimeNum);
                    }
                }
            } else if (readingReq.getDateType() == 3) {
                SteamMeterReadingMonthBo meterReadingMonthBo = new SteamMeterReadingMonthBo();
                meterReadingMonthBo.setStart(readingReq.getStartTime());
                meterReadingMonthBo.setEnd(readingReq.getEndTime());
                meterReadingMonthBo.setEquipID(equipID);
                List<SteamMeterReadingMonthPo> steamMeterReadingMonthPos = steamMeterReadingService.querySteamMonthGroup(meterReadingMonthBo);
                if (steamMeterReadingMonthPos != null) {
                    for (SteamMeterReadingMonthPo po : steamMeterReadingMonthPos) {
                        steamTimeNum = new SteamTimeNum();
                        steamTimeNum.setValue(MathUtils.towDecimal(po.getUseQuantity()));
                        steamTimeNum.setDateTime(po.getReadTime());
                        steamTimeNums.add(steamTimeNum);
                    }
                }
                //如果包含当月，取日表数据
                String month = DateUtil.getMonth();
                if (readingReq.getStartTime().compareTo(month) >= 0 || readingReq.getEndTime().compareTo(month) >= 0) {
                    SteamMeterReadingDayBo meterReadingDayBo = new SteamMeterReadingDayBo();
                    meterReadingDayBo.setStart(DateUtil.getMouthOneDay());
                    meterReadingDayBo.setEnd(DateUtil.getDay());
                    meterReadingDayBo.setEquipID(equipID);
                    SteamMeterReadingDayStatisticsBo meterReadingDayStatisticsBo = steamMeterReadingService.countSteamMeterReadingDayByAssignedConditon(meterReadingDayBo);
                    if (meterReadingDayStatisticsBo != null) {
                        steamTimeNum = new SteamTimeNum();
                        steamTimeNum.setDateTime(month);
                        steamTimeNum.setValue(MathUtils.towDecimal(meterReadingDayStatisticsBo.getTotalSteamPower()));
                        steamTimeNums.add(steamTimeNum);
                    }
                }
            }
        }
        listEnergyResp.ok(steamTimeNums);
        return listEnergyResp;
    }


    /**
     * 企业下按天用汽统计
     */

    @ApiOperation(value = "查询企业每日用汽总计")
    @RequestMapping(value = "getSteamByDay", method = RequestMethod.POST)
    public EnergyResp<List<StatisticsDataResp>> getSteamByDay(@RequestBody @Valid SteamMeterReadingDayBo req, BindingResult result) {
        EnergyResp<List<StatisticsDataResp>> resp = new EnergyResp<List<StatisticsDataResp>>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (req.getStart().compareTo(req.getEnd()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        List<StatisticsDataResp> statisticsDataRespList = steamMeterReadingService.getSteamByDay(req);
        resp.ok(statisticsDataRespList);
        return resp;
    }

    @ApiOperation(value = "查询企业每月用汽总计")
    @RequestMapping(value = "getElecSumByMonth", method = RequestMethod.POST)
    public EnergyResp<List<StatisticsDataResp>> getSteamByMonth(@RequestBody @Valid SteamMeterReadingMonthBo req, BindingResult result) {
        EnergyResp<List<StatisticsDataResp>> resp = new EnergyResp<List<StatisticsDataResp>>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (req.getStart().compareTo(req.getEnd()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        resp.ok(steamMeterReadingService.getSteamByMonth(req));
        return resp;
    }


    /**
     * 根据车间id查询各个生产线的用气情况（昨日数值）
     */
    @ApiOperation(value = "（昨日数值）根据车间id查询各个生产线的用气情况")
    @RequestMapping(value = "getYesterdayBoardUnit", method = RequestMethod.POST)
    public EnergyResp<List<YesterdayBoardUnitResp>> getYesterdayBoardUnit(@RequestBody @Valid DefaultReq defaultReq, BindingResult result) {
        EnergyResp<List<YesterdayBoardUnitResp>> resp = new EnergyResp<List<YesterdayBoardUnitResp>>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        EnergyResp<List<YesterdayBoardUnitResp>> yesterdayBoardUnit = steamMeterReadingService.getYesterdayBoardUnit(defaultReq);
        return yesterdayBoardUnit;
    }


    @ApiOperation(value = "用汽日均量（7天的平均数）、日偏差量")
    @RequestMapping(value = "getSteamDayAve", method = RequestMethod.POST)
    public EnergyResp<SteamDayAve> getSteamDayAve(@RequestBody List<String> meterNos) {
        EnergyResp<SteamDayAve> resp = new EnergyResp<>();
        SteamDayAve steamDayAve = new SteamDayAve();

        String end = DateUtil.getLastOneday();
        String start = DateUtil.getDay(DateUtil.add(new Date(), Calendar.DATE, -7));
        SteamMeterReadingDayBo bo = new SteamMeterReadingDayBo();
        bo.setStart(start);
        bo.setEnd(end);
        bo.setEquipID(meterNos);
        SteamMeterReadingDayStatisticsBo statisticsBo = steamMeterReadingService.countSteamMeterReadingDayByAssignedConditon(bo);  //七天数据
        BigDecimal ave = null;
        if (statisticsBo != null) {
            List<SteamMeterReadingDayPo> po = steamMeterReadingService.querySteamDayGroup(bo);
            ave = MathUtils.divide(statisticsBo.getTotalSteamPower(), new BigDecimal(po.size())); //用汽日均量
            steamDayAve.setDayAve(MathUtils.towDecimal(ave));
        } else {
            resp.ok(steamDayAve);
            return resp;
        }

        bo.setStart(DateUtil.getLastTwoday());
        bo.setEnd(DateUtil.getLastTwoday());
        SteamMeterReadingDayStatisticsBo statisticsBo1 = steamMeterReadingService.countSteamMeterReadingDayByAssignedConditon(bo); //前日数据
        BigDecimal deviation = null;
        if (statisticsBo1 != null) {
            deviation = MathUtils.divide(MathUtils.sub(statisticsBo1.getTotalSteamPower(), ave), ave);  //日偏差量
        }
        steamDayAve.setDayDeviation(deviation);
        String meterNo = null;
        deviation = new BigDecimal(0);
        for (String meter : meterNos) {
            bo.setStart(DateUtil.getLastOneday());
            bo.setEnd(DateUtil.getLastOneday());
            List<String> mes = new ArrayList<>();
            mes.add(meter);
            bo.setEquipID(mes);
            SteamMeterReadingDayStatisticsBo statisticsBo2 = steamMeterReadingService.countSteamMeterReadingDayByAssignedConditon(bo);
            if (statisticsBo2 != null) {
                if (statisticsBo2.getTotalSteamPower().compareTo(deviation) == 1) {
                    meterNo = meter;
                    deviation = statisticsBo2.getTotalSteamPower();
                }
            }
        }
        steamDayAve.setMeterNo(meterNo);
        resp.ok(steamDayAve);
        return resp;
    }

    @ApiOperation(value = "根据表号查询昨日用汽量 、昨日环比（昨日-前日）/前日*100%")
    @RequestMapping(value = "getSteamYesterday", method = RequestMethod.POST)
    public EnergyResp<SteamDayAve> getSteamYesterday(@RequestBody List<String> meterNo) {
        EnergyResp<SteamDayAve> resp = new EnergyResp<>();
        SteamDayAve steamDayAve = new SteamDayAve();
        String end = DateUtil.getLastOneday();
        SteamMeterReadingDayBo bo = new SteamMeterReadingDayBo();
        bo.setStart(end);
        bo.setEnd(end);
        bo.setEquipID(meterNo);
        SteamMeterReadingDayStatisticsBo statisticsBo = steamMeterReadingService.countSteamMeterReadingDayByAssignedConditon(bo);  //昨日数据
        if (statisticsBo != null) {
            steamDayAve.setDayAve(MathUtils.towDecimal(statisticsBo.getTotalSteamPower()));
            steamDayAve.setYesterdayMonth(MathUtils.towDecimal(statisticsBo.getTotalSteamFees()));
        }
        String start = DateUtil.getDay(DateUtil.add(new Date(), Calendar.DATE, -2));
        bo.setStart(start);
        bo.setEnd(start);
        SteamMeterReadingDayStatisticsBo frontBo = steamMeterReadingService.countSteamMeterReadingDayByAssignedConditon(bo);  //前天数据
        BigDecimal deviation = null;
        BigDecimal monthRatio = null;
        if (frontBo != null) {
            deviation = MathUtils.divide(MathUtils.sub(steamDayAve.getDayAve(), frontBo.getTotalSteamPower()), frontBo.getTotalSteamPower());
            monthRatio = MathUtils.divide(MathUtils.sub(steamDayAve.getYesterdayMonth(), frontBo.getTotalSteamFees()), frontBo.getTotalSteamFees());
        }
        steamDayAve.setDayDeviation(deviation);
        steamDayAve.setMonthRatio(monthRatio);
        resp.ok(steamDayAve);
        return resp;
    }

    @ApiOperation(value = "根据车间id查询上月各生产线总用汽量、总金额和车间每日用汽量")
    @RequestMapping(value = "getLastMonthDetailed", method = RequestMethod.POST)
    public EnergyResp<DetailedResp> getLastMonthDetailed(@RequestBody String id) {
        EnergyResp<DetailedResp> energyResp = new EnergyResp<>();
        DetailedResp detailedResp = new DetailedResp();
        SteamMeterReadingDayBo bo = new SteamMeterReadingDayBo();
        bo.setStart(DateUtil.getLastMonthFirst());
        bo.setEnd(DateUtil.getLastMonth());

        List<CustMeter> custMeters = accountingUnitService.getAccountEleMeter(Long.valueOf(id), "02");  //车间下电表
        List<ProductionLineData> productionLineDataList = new ArrayList<>(); //车间每日用汽量
        List<ProductionLineResp> productionLineResps = new ArrayList<>(); //车间下生产线总用汽量、总金额

        List<String> es = new ArrayList<>();
        EnergyResp<List<AccountingUnit>> energyResp1 = accountingUnitService.getChildUnit(Long.valueOf(id));
        if (StatusCode.SUCCESS.getCode().equals(energyResp1.getCode()) && energyResp1.getData().size() > 0) {
            List<AccountingUnit> accountingUnits = energyResp1.getData();
            for (AccountingUnit accountingUnit : accountingUnits) {
                List<CustMeter> custMeters1 = accountingUnitService.getAccountEleMeter(accountingUnit.getId(), "02");
                if (custMeters1.size() > 0) {
                    ProductionLineResp productionLineResp = new ProductionLineResp();
                    productionLineResp.setLineName(accountingUnit.getName());
                    List<String> equid = new ArrayList<>();
                    equid.add(custMeters1.get(0).getLoopNumber());
                    es.add(custMeters1.get(0).getLoopNumber());
                    SteamMeterReadingMonthBo mbo = new SteamMeterReadingMonthBo();
                    mbo.setEnd(DateUtil.getLastMonthFirst().substring(0, 7));
                    mbo.setStart(DateUtil.getLastMonthFirst().substring(0, 7));
                    mbo.setEquipID(equid);
                    SteamMeterReadingMonthStatisticsBo bo1 = steamMeterReadingService.countSteamMeterReadingMonthByAssignedConditon(mbo);
                    if (bo1 != null) {
                        productionLineResp.setTotalSteamFees(MathUtils.towDecimal(bo1.getTotalSteamFees()));
                        productionLineResp.setTotalSteamPower(MathUtils.towDecimal(bo1.getTotalSteamPower()));
                    }
                    productionLineResps.add(productionLineResp);
                }

            }
        }

//        Map<String,BigDecimal> map = new TreeMap<>();
        if (custMeters.size() > 0) {
            ProductionLineData data = new ProductionLineData();
            List<String> equid = new ArrayList<>();
            equid.add(custMeters.get(0).getLoopNumber());
            bo.setEquipID(equid);
            data.setName(accountingUnitService.getUnitName(custMeters.get(0).getLoopNumber()).getUnitName());
            data.setDataResp(getDataResps(bo));
            productionLineDataList.add(data);
        } else {
            for (String s : es) {
                ProductionLineData data = new ProductionLineData();
                List<String> equid = new ArrayList<>();
                equid.add(s);
                bo.setEquipID(equid);
                data.setName(accountingUnitService.getUnitName(s).getUnitName());
                data.setDataResp(getDataResps(bo));
                productionLineDataList.add(data);
            }
        }
        detailedResp.setProductionLineDatas(productionLineDataList);
        detailedResp.setProductionLineResps(productionLineResps);
        energyResp.ok(detailedResp);
        return energyResp;
    }

    private List<DataResp> getDataResps(SteamMeterReadingDayBo bo){
        List<SteamMeterReadingDayPo> steamMeterReadingDayPos = steamMeterReadingService.querySteamMeterReadingDayByAssignedConditon(bo);
        Map<String, String> map = DateUtil.getMonthDay(bo.getEnd().substring(0,7));
        List<DataResp> dataResps = new ArrayList<>();
        for(Map.Entry<String,String> entry:map.entrySet()) {
            DataResp dataResp = new DataResp();
            dataResp.setTime(entry.getKey());
            for (SteamMeterReadingDayPo po : steamMeterReadingDayPos) {
                if(DateUtil.getDay(po.getReadTime()).equals(entry.getKey())) {
                    dataResp.setValue(MathUtils.towDecimal(po.getUseQuantity()));
                }
            }
            dataResps.add(dataResp);
        }
        return dataResps;
    }

    @ApiOperation(value = "根据表号，查询本月及上月同期电量，环比(本月-上月)/上月")
    @RequestMapping(value = "getLastMonthRatio", method = RequestMethod.POST)
    public EnergyResp<LastMonthRatio> getLastMonthRatio(@RequestBody List<String> meterNo) {
        EnergyResp<LastMonthRatio> resp = new EnergyResp<>();
        LastMonthRatio ratio = new LastMonthRatio();

        SteamMeterReadingDayBo bo = new SteamMeterReadingDayBo();
        bo.setEquipID(meterNo);
        bo.setStart(DateUtil.getMouthOneDay());
        Date end = DateUtil.add(new Date(), Calendar.DAY_OF_MONTH, -1);
        bo.setEnd(DateUtil.getDay(end));
        /* 本月用量 */
        SteamMeterReadingDayStatisticsBo bo1 = steamMeterReadingService.countSteamMeterReadingDayByAssignedConditon(bo);
        bo.setStart(DateUtil.getLastMonthFirst());
        bo.setEnd(DateUtil.getLastMonthForDay(DateUtil.getTime(end)));
        /* 上月同期用量 */
        SteamMeterReadingDayStatisticsBo bo2 = steamMeterReadingService.countSteamMeterReadingDayByAssignedConditon(bo);
        BigDecimal whenMonth = null;
        BigDecimal lastMonth = null;
        if (bo1 != null) {
            whenMonth = MathUtils.towDecimal(bo1.getTotalSteamPower());
        }
        if (bo2 != null) {
            lastMonth = MathUtils.towDecimal(bo2.getTotalSteamPower());
        }
        ratio.setLastMonth(lastMonth);
        ratio.setWhenMonth(whenMonth);
        ratio.setRatio(MathUtils.towDecimal(MathUtils.divide(MathUtils.sub(whenMonth, lastMonth), lastMonth)));
        resp.ok(ratio);
        return resp;
    }
}
