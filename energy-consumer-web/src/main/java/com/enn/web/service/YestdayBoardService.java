package com.enn.web.service;

import com.alibaba.fastjson.JSON;
import com.enn.constant.Constant;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.service.business.IAccountUnitService;
import com.enn.service.business.ICustElectricCountService;
import com.enn.service.business.IOpentsdbService;
import com.enn.service.business.ISteamMeterReadingService;
import com.enn.service.system.ICompanyService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.req.*;
import com.enn.vo.energy.business.resp.*;
import com.enn.vo.energy.common.enums.SteamMetricEnum;
import com.enn.web.redis.RedisLogService;
import com.enn.web.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class YestdayBoardService {
    @Autowired
    private ICustElectricCountService custElectricCountService;
    @Autowired
    private ISteamMeterReadingService steamMeterReadingService;
    @Autowired
    private IAccountUnitService accountUnitService;

    @Autowired
    private ICompanyService companyService;
    @Autowired
    private IOpentsdbService opentsdbService;

    // -------------------------------------------汇总接口------------start------------------------------------------

    @RedisLogService(group = {Constant.ENERGY_GROUP}, key = "#defaultReq.id",expire = Constant.ENERGY_TIME)
    public EnergyResp<List<EnergyBoardResp>> getYesterdayCompanyTab(DefaultReq defaultReq) {
        EnergyResp<List<EnergyBoardResp>> resp = new EnergyResp();
        List<EnergyBoardResp> resps = new ArrayList<>();
        //根据企业id查询查询车间
        AccountUnitReq accountUnitReq = new AccountUnitReq();
        accountUnitReq.setId(defaultReq.getId());
        accountUnitReq.setIsAccount(false);
        accountUnitReq.setAccountingType("02");
        EnergyResp<List<UnitResp>> listEnergyResp = accountUnitService.queryAccountList(accountUnitReq);
        if (listEnergyResp.getData() != null && listEnergyResp.getData().size() > 0) {
            for (UnitResp unitResp : listEnergyResp.getData()) {
                //根据车间id查询 数据
                DefaultReq de = new DefaultReq();
                de.setId(unitResp.getId());
                EnergyResp<EnergyBoardResp> yesterdayUnitTab = getYesterdayUnitTab(de);
                if (yesterdayUnitTab.getData() != null) {
                    EnergyBoardResp data = yesterdayUnitTab.getData();
                    data.setName(unitResp.getName());
                    data.setId(unitResp.getId());
                    resps.add(data);
                }
            }
        }
        resp.ok(resps);
        return resp;

    }


    /**
     * 单个车间的汇总信息
     *
     * @param defaultReq
     * @return
     */

    public EnergyResp<EnergyBoardResp> getYesterdayUnitTab(DefaultReq defaultReq) {
        EnergyResp<EnergyBoardResp> resp = new EnergyResp();
        EnergyBoardResp energyBoardResp = new EnergyBoardResp();
        /**
         * 获取能源昨日用量
         */
        EnergyDayResp energyDay = getEnergyDay(defaultReq);
        EnergyQuantityAndFee elec = energyDay.getElec();
        EnergyQuantityAndFee steam = energyDay.getSteam();
        if (elec != null) {
            energyBoardResp.setDayEleQuantity(MathUtils.point(elec.getQuantity(), 2) == null ? null : JSON.toJSONString(MathUtils.point(elec.getQuantity(), 2)));//日用电量
            energyBoardResp.setDayEleQuantityRatio(MathUtils.mul(elec.getQuantityRatio(), new BigDecimal(100), 2) == null ? null : JSON.toJSONString(MathUtils.mul(elec.getQuantityRatio(), new BigDecimal(100), 2)));//用电量环比
        }

        if (steam != null) {
            energyBoardResp.setDaySeatQuantity(MathUtils.point(steam.getQuantity(), 2) == null ? null : JSON.toJSONString(MathUtils.point(steam.getQuantity(), 2)));
            energyBoardResp.setDaySeatQuantityRatio(MathUtils.mul(steam.getQuantityRatio(), new BigDecimal(100), 2) == null ? null : JSON.toJSONString(MathUtils.mul(steam.getQuantityRatio(), new BigDecimal(100), 2)));
        }
        //计算总费用
        energyBoardResp.setTotalCost(MathUtils.point(MathUtils.add(elec == null ? null : elec.getFee(), steam == null ? null : steam.getFee()), 2) == null ? null : JSON.toJSONString(MathUtils.point(MathUtils.add(elec == null ? null : elec.getFee(), steam == null ? null : steam.getFee()), 2)));


        /**
         * 获取 能源日均量 偏差
         */
        EnergyDayAveResp energyDayAve = getEnergyDayAve(defaultReq);

        SteamDayAve elec1 = energyDayAve.getElec();
        SteamDayAve steam1 = energyDayAve.getSteam();
        //电
        if (elec1 != null) {
            energyBoardResp.setDayEleAveQuantity(MathUtils.point(elec1.getDayAve(), 2) == null ? null : JSON.toJSONString(MathUtils.point(elec1.getDayAve(), 2)));
            energyBoardResp.setDayEleDeviation(MathUtils.mul(elec1.getDayDeviation(), new BigDecimal(100), 2) == null ? null : JSON.toJSONString(MathUtils.mul(elec1.getDayDeviation(), new BigDecimal(100), 2)));
            //警告灯-电
            boolean compare = true;
            if (elec1.getDayDeviation() != null) {
                compare = MathUtils.compare(Constant.ELECTRIC_ALARM, elec1.getDayDeviation().abs());
            }
            if (!compare) {
                energyBoardResp.setDayEleAlarmName(elec1.getMeterNo());
            }
            energyBoardResp.setDayEleAlarm(compare);
        }
        //汽
        if (steam1 != null) {
            energyBoardResp.setDaySeatAveQuantity(MathUtils.point(steam1.getDayAve(), 2) == null ? null : JSON.toJSONString(MathUtils.point(steam1.getDayAve(), 2)));
            energyBoardResp.setDaySeatDeviation(MathUtils.mul(steam1.getDayDeviation(), new BigDecimal(100), 2) == null ? null : JSON.toJSONString(MathUtils.mul(steam1.getDayDeviation(), new BigDecimal(100), 2)));
            //警告灯-汽
            boolean compare = true;
            if (steam1.getDayDeviation() != null) {
                compare = MathUtils.compare(Constant.ELECTRIC_ALARM, steam1.getDayDeviation().abs());
            }
            if (!compare) {
                energyBoardResp.setDaySeatAlarmName(steam1.getMeterNo());
            }
            energyBoardResp.setDaySeatAlarm(compare);
        }

        //是否展开
        energyBoardResp.setOpen(false);
        resp.ok(energyBoardResp);

        return resp;
    }


    /**
     * 企业的汇总信息
     *
     * @param defaultReq
     * @return
     */
    @RedisLogService(group = {Constant.ENERGY_GROUP}, key = "#defaultReq.id",expire = Constant.ENERGY_TIME)
    public EnergyResp<EnergyBoardResp> getCompanyYesterdayUnitTab(DefaultReq defaultReq) {
        EnergyResp<EnergyBoardResp> resp = new EnergyResp();
        EnergyBoardResp energyBoardResp = new EnergyBoardResp();
        /**
         * 获取能源昨日用量
         */
        EnergyDayResp energyDay = getConpEnergyDay(defaultReq);
        EnergyQuantityAndFee elec = energyDay.getElec();
        EnergyQuantityAndFee steam = energyDay.getSteam();
        if (elec != null) {
            energyBoardResp.setDayEleQuantity(MathUtils.point(elec.getQuantity(), 2) == null ? null : JSON.toJSONString(MathUtils.point(elec.getQuantity(), 2)));//日用电量
            energyBoardResp.setDayEleQuantityRatio(MathUtils.mul(elec.getQuantityRatio(), new BigDecimal(100), 2) == null ? null : JSON.toJSONString(MathUtils.mul(elec.getQuantityRatio(), new BigDecimal(100), 2)));//用电量环比
        }

        if (steam != null) {
            energyBoardResp.setDaySeatQuantity(MathUtils.point(steam.getQuantity(), 2) == null ? null : JSON.toJSONString(MathUtils.point(steam.getQuantity(), 2)));
            energyBoardResp.setDaySeatQuantityRatio(MathUtils.mul(steam.getQuantityRatio(), new BigDecimal(100), 2) == null ? null : JSON.toJSONString(MathUtils.mul(steam.getQuantityRatio(), new BigDecimal(100), 2)));
        }
        //计算总费用
        energyBoardResp.setTotalCost(MathUtils.point(MathUtils.add(elec == null ? null : elec.getFee(), steam == null ? null : steam.getFee()), 2) == null ? null : JSON.toJSONString(MathUtils.point(MathUtils.add(elec == null ? null : elec.getFee(), steam == null ? null : steam.getFee()), 2)));


        /**
         * 获取 能源日均量 偏差
         */
        EnergyDayAveResp energyDayAve = getCompanEnergyDayAve(defaultReq);

        SteamDayAve elec1 = energyDayAve.getElec();
        SteamDayAve steam1 = energyDayAve.getSteam();
        //电
        if (elec1 != null) {
            energyBoardResp.setDayEleAveQuantity(MathUtils.point(elec1.getDayAve(), 2) == null ? null : JSON.toJSONString(MathUtils.point(elec1.getDayAve(), 2)));
            energyBoardResp.setDayEleDeviation(MathUtils.mul(elec1.getDayDeviation(), new BigDecimal(100), 2) == null ? null : JSON.toJSONString(MathUtils.mul(elec1.getDayDeviation(), new BigDecimal(100), 2)));
            //警告灯-电
            boolean compare = true;
            if (elec1.getDayDeviation() != null) {
                compare = MathUtils.compare(Constant.ELECTRIC_ALARM, elec1.getDayDeviation().abs());
            }
            if (!compare) {
                energyBoardResp.setDayEleAlarmName(elec1.getMeterNo());
            }
            energyBoardResp.setDayEleAlarm(compare);
        }
        //汽
        if (steam1 != null) {
            energyBoardResp.setDaySeatAveQuantity(MathUtils.point(steam1.getDayAve(), 2) == null ? null : JSON.toJSONString(MathUtils.point(steam1.getDayAve(), 2)));
            energyBoardResp.setDaySeatDeviation(MathUtils.mul(steam1.getDayDeviation(), new BigDecimal(100), 2) == null ? null : JSON.toJSONString(MathUtils.mul(steam1.getDayDeviation(), new BigDecimal(100), 2)));
            //警告灯-汽
            boolean compare = true;
            if (steam1.getDayDeviation() != null) {
                compare = MathUtils.compare(Constant.ELECTRIC_ALARM, steam1.getDayDeviation().abs());
            }
            if (!compare) {
                energyBoardResp.setDaySeatAlarmName(steam1.getMeterNo());
            }
            energyBoardResp.setDaySeatAlarm(compare);
        }

        //是否展开
        energyBoardResp.setOpen(false);
        resp.ok(energyBoardResp);

        return resp;
    }


    /**
     * 企业下  功率曲线
     * 1.获取企业下的一级计量表
     * 2.使用一级表获取大数据平台的数据
     */
    public EnergyResp<List<EnergyBoardLineResp>> getEnergyBoardLineResp(DefaultReq defaultReq) {
        EnergyResp<List<EnergyBoardLineResp>> res = new EnergyResp<>();
        //1.获取企业下的一级计量表
        CompanyMeteReq companyMeteReq = new CompanyMeteReq();
        companyMeteReq.setId(defaultReq.getId());
        companyMeteReq.setEnergyType("01");//电表
        EnergyResp<List<MeterResp>> resp = accountUnitService.queryMeterListByCompanyAndType(companyMeteReq);
        List<EnergyBoardLineResp> energResp = new ArrayList<>();
        if (resp.getData() != null) {
            List<Equip> equips = new ArrayList<>();
            Equip equip = null;
            //创建一个保存设备号和名称的map
            Map<String, String> map = new HashMap<>();
            for (MeterResp meterResp : resp.getData()) {
                map.put(meterResp.getLoopNumber(), meterResp.getMeterName());
                if ("1".equals(meterResp.getIsAccoun())) {
                    equip = new Equip();
                    equip.setEquipID(meterResp.getLoopNumber());
                    equip.setEquipMK("METE");
                    equip.setStaId(meterResp.getStaId());
                    equips.add(equip);
                }
            }
            SamplDataStaReq samplDataStaReq = new SamplDataStaReq();
            samplDataStaReq.setEquips(equips);
            samplDataStaReq.setDownsample("1m-first-nan");
            samplDataStaReq.setMetric(SteamMetricEnum.EMS_P.getMetric());
            samplDataStaReq.setStart(DateUtil.getLastDay(DateUtil.getNowDay()));
            samplDataStaReq.setEnd(DateUtil.getNowDay());
            samplDataStaReq.setPoint(2);
            EnergyResp<ListResp<RmiSamplDataResp>> sam = null;
            try {
                sam = opentsdbService.getSamplDataStaReq(samplDataStaReq);
            } catch (Exception e) {
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), sam.getError());
            }

            EnergyBoardLineResp energy = null;
            if (sam.getData() != null && sam.getData().getList() != null) {
                List<RmiSamplDataResp> list = sam.getData().getList();
                for (RmiSamplDataResp rmi : list) {
                    energy = new EnergyBoardLineResp();
                    energy.setEquipId(rmi.getEquipID());
                    energy.setName(SteamMetricEnum.EMS_P.getDesc());
                    energy.setData(rmi.getDataResp());
                    energResp.add(energy);
                }
            }
        }

        //获取蒸汽表
        companyMeteReq = new CompanyMeteReq();
        companyMeteReq.setId(defaultReq.getId());
        companyMeteReq.setEnergyType("02");//蒸汽表
        EnergyResp<List<MeterResp>> respSeat = accountUnitService.queryMeterListByCompanyAndType(companyMeteReq);
        if (respSeat.getData() != null) {
            List<Equip> equips = new ArrayList<>();
            Equip equip = null;
            //创建一个保存设备号和名称的map
            Map<String, String> map = new HashMap<>();
            for (MeterResp meterResp : resp.getData()) {
                if ("1".equals(meterResp.getIsAccoun())) {
                    map.put(meterResp.getLoopNumber(), meterResp.getMeterName());
                    equip = new Equip();
                    equip.setEquipID(meterResp.getLoopNumber());
                    equip.setEquipMK("CEU");
                    equip.setStaId(meterResp.getStaId());
                    equips.add(equip);
                }

            }
            SamplDataStaReq samplDataStaReq = new SamplDataStaReq();
            samplDataStaReq.setEquips(equips);
            samplDataStaReq.setDownsample("1m-first-nan");
            samplDataStaReq.setMetric(SteamMetricEnum.STEAM_FLOW.getMetric());
            samplDataStaReq.setStart(DateUtil.getLastDay(DateUtil.getNowDay()));
            samplDataStaReq.setEnd(DateUtil.getNowDay());
            samplDataStaReq.setPoint(2);
            EnergyResp<ListResp<RmiSamplDataResp>> sam = null;
            try {
                log.info("请求参数:" + JSON.toJSONString(samplDataStaReq));
                sam = opentsdbService.getSamplDataStaReq(samplDataStaReq);
            } catch (Exception e) {
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), sam.getError());
            }
            EnergyBoardLineResp seat = null;
            if (sam.getData() != null && sam.getData().getList() != null) {
                List<RmiSamplDataResp> list = sam.getData().getList();
                for (RmiSamplDataResp rmi : list) {
                    seat = new EnergyBoardLineResp();
                    seat.setEquipId(rmi.getEquipID());
                    seat.setName(SteamMetricEnum.STEAM_FLOW.getDesc());
                    seat.setData(rmi.getDataResp());
                    energResp.add(seat);
                }
            }
        }
        res.ok(energResp);
        return res;

    }


    /**
     * 车间下---用汽统计图
     * 1.获取企业下的一级计量表
     * * 2.使用一级表获取大数据平台的数据
     */
    public EnergyResp<List<EnergyBoardLineResp>> getmmp(DefaultReq defaultReq) {
        EnergyResp<List<EnergyBoardLineResp>> energyResp = new EnergyResp<>();
        List<EnergyBoardLineResp> ens = new ArrayList<>();
        EnergyBoardLineResp en = null;
        //获取车间下的一块表
        CompanyMeteReq de = new CompanyMeteReq();
        de.setEnergyType("02");
        de.setId(defaultReq.getId());
        EnergyResp<List<MeterResp>> resp = accountUnitService.queryMeterListByAccountAndType(de);
        for (MeterResp meterResp : resp.getData()) {
            SamplDataReq samplDataReq = new SamplDataReq();
            if (resp.getData() != null) {
                //=使用给计量表查询数据
                samplDataReq.setDownsample("1m-first-nan");
                samplDataReq.setMetric(SteamMetricEnum.STEAM_FLOW.getMetric());
                samplDataReq.setEquipMK("STE");
                samplDataReq.setStaId(meterResp.getStaId());
                samplDataReq.setStart(DateUtil.getLastDay(DateUtil.getNowDay()));
                samplDataReq.setEnd(DateUtil.getNowDay());
                samplDataReq.setPoint(2);
                List<String> meter = new ArrayList<>();
                meter.add(meterResp.getLoopNumber());
                samplDataReq.setEquipID(meter);
                samplDataReq.setPoint(2);
                EnergyResp<ListResp<RmiSamplDataResp>> samplData = opentsdbService.getSamplData(samplDataReq);
                en = new EnergyBoardLineResp();
                if (samplData.getData() != null && samplData.getData().getList() != null) {
                    if (samplData.getData().getList().size() > 0) {
                        RmiSamplDataResp rmiSamplDataResp = samplData.getData().getList().get(0);
                        en.setEquipId(rmiSamplDataResp.getEquipID());
                        en.setData(rmiSamplDataResp.getDataResp());
                        en.setName(meterResp.getMeterName());
                        ens.add(en);
                    }
                }
            }
            //获取压力值
            samplDataReq.setMetric(SteamMetricEnum.STEAM_PRESSURE.getMetric());
            EnergyResp<ListResp<RmiSamplDataResp>> samplData = opentsdbService.getSamplData(samplDataReq);
            en = new EnergyBoardLineResp();
            if (samplData.getData() != null && samplData.getData().getList() != null) {
                if (samplData.getData().getList().size() > 0) {
                    RmiSamplDataResp rmiSamplDataResp = samplData.getData().getList().get(0);
                    en.setEquipId(rmiSamplDataResp.getEquipID());
                    en.setData(rmiSamplDataResp.getDataResp());
                    en.setName(meterResp.getMeterName());
                    ens.add(en);
                }
            }
        }
        energyResp.ok(ens);
        return energyResp;
    }


    // -------------------------------------------汇总接口------------end------------------------------------------

    /**
     * 车间昨日用电、用汽接口----用能明细表格
     */
    public UnitEnergyInfoResp getYesterdayBoardUnit(@RequestBody DefaultReq defaultReq) {
        UnitEnergyInfoResp unitEnergyInfoResp = new UnitEnergyInfoResp();
        //查询用车间用电信息
        EnergyResp<List<YesterdayBoardUnitResp>> yesterdayBoardUnit = custElectricCountService.getYesterdayBoardUnit(defaultReq);
        if (!yesterdayBoardUnit.getCode().equals(StatusCode.SUCCESS.getCode())) {
            throw new EnergyException(yesterdayBoardUnit.getCode(), yesterdayBoardUnit.getMsg(), yesterdayBoardUnit.getError());
        }
        if (yesterdayBoardUnit.getCode().equals(StatusCode.SUCCESS.getCode())) {
            unitEnergyInfoResp.setElectric(yesterdayBoardUnit.getData());
        } else {
            log.error(yesterdayBoardUnit.getError());
        }
        //查询用车间用汽信息
        EnergyResp<List<YesterdayBoardUnitResp>> yesterdayBoardUnit1 = steamMeterReadingService.getYesterdayBoardUnit(defaultReq);
        if (!yesterdayBoardUnit1.getCode().equals(StatusCode.SUCCESS.getCode())) {
            throw new EnergyException(yesterdayBoardUnit1.getCode(), yesterdayBoardUnit1.getMsg(), yesterdayBoardUnit1.getError());
        }
        if (yesterdayBoardUnit1.getCode().equals(StatusCode.SUCCESS.getCode())) {
            unitEnergyInfoResp.setSteam(yesterdayBoardUnit1.getData());
        } else {
            log.error(yesterdayBoardUnit1.getError());
        }
        return unitEnergyInfoResp;
    }


    /**
     * 昨日看板 车间 电 尖峰平谷---柱状统计图
     */
    public List<ElectricTypeQuantityResp> getElectricTypeQuantityResp(@RequestBody DefaultReq de) {
        List<ElectricTypeQuantityResp> electricTypeQuantityResps = new ArrayList<>();
        ElectricTypeQuantityResp el = null;
        //定义开始时间和结束时间
        String start = DateUtil.getLastDay(DateUtil.getNowDay());
        String end = DateUtil.getNowDay();
        //根据车间id查询生产线ids
        AccountUnitReq accountUnitReq = new AccountUnitReq();
        accountUnitReq.setAccountingType("03");
        accountUnitReq.setIsAccount(true);
        accountUnitReq.setId(de.getId());
        EnergyResp<List<UnitResp>> listEnergyResp = accountUnitService.queryAccountList(accountUnitReq);
        if (!listEnergyResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), listEnergyResp.getError());
        }

        if (listEnergyResp.getData() != null) {
            for (UnitResp unitResp : listEnergyResp.getData()) {
                //根据生产线id查询该生产线下的电表
                DefaultReq dedd = new DefaultReq();
                dedd.setId(unitResp.getId());
                EnergyResp<List<MeterResp>> resp1 = accountUnitService.queryMeterListByAccount(dedd);
                if (!resp1.getCode().equals(StatusCode.SUCCESS.getCode())) {
                    throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), resp1.getError());
                }
                //将表号取出来
                List<String> meters = new ArrayList<>();
                if (resp1 != null) {
                    for (MeterResp mete : resp1.getData()) {
                        if (mete.getEnergyType().equals("01")) {
                            meters.add(mete.getLoopNumber());
                        }
                    }
                    if (meters.size() > 0) {
                        //查询这些表的用电情况
                        ElectricMeterReadingReq req = new ElectricMeterReadingReq();
                        req.setStartTime(DateUtil.format(start, "yyyy-MM-dd"));
                        req.setEndTime(DateUtil.format(end, "yyyy-MM-dd"));
                        req.setMeterNoList(meters);
                        EnergyResp<ElectricMeterReadingResp> sumByDay = custElectricCountService.getSumByDay(req);
                        if (!sumByDay.getCode().equals(StatusCode.SUCCESS.getCode())) {
                            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), sumByDay.getError());
                        }
                        el = new ElectricTypeQuantityResp();
                        if (sumByDay.getData() != null) {
                            el.setUnitName(unitResp.getName());
                            el.setUnitId(unitResp.getId());
                            BigDecimal peakQuantity = sumByDay.getData().getPeakQuantity();
                            BigDecimal tipQuantity = sumByDay.getData().getTipQuantity();
                            BigDecimal valleyQuantity = sumByDay.getData().getValleyQuantity();
                            BigDecimal flatQuantity = sumByDay.getData().getFlatQuantity();
                            if (peakQuantity != null) {
                                peakQuantity = MathUtils.point(peakQuantity, 2);
                            }
                            if (tipQuantity != null) {
                                tipQuantity = MathUtils.point(tipQuantity, 2);
                            }
                            if (valleyQuantity != null) {
                                valleyQuantity = MathUtils.point(valleyQuantity, 2);
                            }
                            if (flatQuantity != null) {
                                flatQuantity = MathUtils.point(flatQuantity, 2);
                            }
                            el.setPeakQuantity(peakQuantity);
                            el.setTipQuantity(tipQuantity);
                            el.setValleyQuantity(valleyQuantity);
                            el.setFlatQuantity(flatQuantity);
                        }
                        electricTypeQuantityResps.add(el);
                    }
                }
            }
        }
        return electricTypeQuantityResps;
    }


    /**
     * 能源日均用量和偏差---电和汽---车间
     */
    public EnergyDayAveResp getEnergyDayAve(@RequestBody DefaultReq de) {
        EnergyDayAveResp en = new EnergyDayAveResp();
        //根据车间查询蒸汽表
        CompanyMeteReq param = new CompanyMeteReq();
        param.setEnergyType("02");
        param.setId(de.getId());
        log.info(JSON.toJSONString(param));
        EnergyResp<List<MeterResp>> meterList = accountUnitService.queryMeterListByAccountAndType(param);
        if (!meterList.getCode().equals(StatusCode.SUCCESS.getCode())) {
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), meterList.getError());
        }

        //使用蒸汽表获取用汽量
        if (meterList.getData() != null && meterList.getData().size() > 0) {
            List<String> meters = new ArrayList<>();
            for (MeterResp meterResp : meterList.getData()) {
                meters.add(meterResp.getLoopNumber());
            }
            log.info(JSON.toJSONString(meters));
            EnergyResp<SteamDayAve> steamDayAve = steamMeterReadingService.getSteamDayAve(meters);
            if (!steamDayAve.getCode().equals(StatusCode.SUCCESS.getCode())) {
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), steamDayAve.getError());
            }
            SteamDayAve data = steamDayAve.getData();


            //根据用蒸汽量最大的表号--查询到该车间的信息
            EnergyResp<List<AccountingUnit>> unitByMete = null;
            if (steamDayAve.getData().getMeterNo() != null) {
                unitByMete = accountUnitService.getUnitByMete(steamDayAve.getData().getMeterNo());
            }
            if (unitByMete != null && unitByMete.getData().size() > 0) {
                AccountingUnit accountingUnit = unitByMete.getData().get(0);
                data.setMeterNo(accountingUnit.getName());
            }
            en.setSteam(data);

        }

        //根据车间查询电表
        param = new CompanyMeteReq();
        param.setEnergyType("01");
        param.setId(de.getId());
        EnergyResp<List<MeterResp>> eleMeterList = accountUnitService.queryMeterListByAccountAndType(param);
        if (!eleMeterList.getCode().equals(StatusCode.SUCCESS.getCode())) {
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), eleMeterList.getError());
        }
        //使用电表查询用电信息
        if (eleMeterList.getData() != null && eleMeterList.getData().size() > 0) {
            List<String> meters = new ArrayList<>();
            for (MeterResp meterResp : eleMeterList.getData()) {
                meters.add(meterResp.getLoopNumber());
            }
            EnergyResp<ElecDayAve> elecDayAve = custElectricCountService.getElecDayAve(meters);
            if (!elecDayAve.getCode().equals(StatusCode.SUCCESS.getCode())) {
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), elecDayAve.getError());
            }
            if (elecDayAve.getData() != null) {
                SteamDayAve elec = new SteamDayAve();
                elec.setDayAve(elecDayAve.getData().getDayAve());
                elec.setDayDeviation(elecDayAve.getData().getDayDeviation());

                //根据用电量最大的表号--查询到该车间的信息
                EnergyResp<List<AccountingUnit>> unitByMete = null;
                if (elecDayAve.getData().getMeterNo() != null) {
                    unitByMete = accountUnitService.getUnitByMete(elecDayAve.getData().getMeterNo());
                }
                if (unitByMete != null && unitByMete.getData().size() > 0) {
                    AccountingUnit accountingUnit = unitByMete.getData().get(0);
                    elec.setMeterNo(accountingUnit.getName());
                }
                en.setElec(elec);
            }
        }
        return en;
    }


    /**
     * 能源日均用量和偏差---电和汽---企业
     */
    public EnergyDayAveResp getCompanEnergyDayAve(@RequestBody DefaultReq de) {
        EnergyDayAveResp en = new EnergyDayAveResp();
        //根据车间查询蒸汽表
        CompanyMeteReq param = new CompanyMeteReq();
        param.setEnergyType("02");
        param.setId(de.getId());
        EnergyResp<List<MeterResp>> meterList = accountUnitService.queryMeterListByCompanyAndType(param);
        if (!meterList.getCode().equals(StatusCode.SUCCESS.getCode())) {
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), meterList.getError());
        }
        //使用蒸汽表获取用汽量
        if (meterList.getData() != null && meterList.getData().size() > 0) {
            List<String> meters = new ArrayList<>();
            for (MeterResp meterResp : meterList.getData()) {
                meters.add(meterResp.getLoopNumber());
            }
            EnergyResp<SteamDayAve> steamDayAve = steamMeterReadingService.getSteamDayAve(meters);
            if (!steamDayAve.getCode().equals(StatusCode.SUCCESS.getCode())) {
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), steamDayAve.getError());
            }

            //根据用蒸汽量最大的表号--查询到该车间的信息
            EnergyResp<List<AccountingUnit>> unitByMete = null;
            if (steamDayAve.getData().getMeterNo() != null) {
                unitByMete = accountUnitService.getUnitByMete(steamDayAve.getData().getMeterNo());
            }

            SteamDayAve data = steamDayAve.getData();
            if (unitByMete != null && unitByMete.getData().size() > 0) {
                AccountingUnit accountingUnit = unitByMete.getData().get(0);
                data.setMeterNo(accountingUnit.getName());
            }
            en.setSteam(data);

        }
        //根据企业查询电表
        param = new CompanyMeteReq();
        param.setEnergyType("01");
        param.setId(de.getId());
        EnergyResp<List<MeterResp>> eleMeterList = accountUnitService.queryMeterListByCompanyAndType(param);
        if (!eleMeterList.getCode().equals(StatusCode.SUCCESS.getCode())) {
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), eleMeterList.getError());
        }
        //使用电表查询用电信息
        if (eleMeterList.getData() != null && eleMeterList.getData().size() > 0) {
            List<String> meters = new ArrayList<>();
            for (MeterResp meterResp : eleMeterList.getData()) {
                meters.add(meterResp.getLoopNumber());
            }
            EnergyResp<ElecDayAve> elecDayAve = custElectricCountService.getElecDayAve(meters);
            if (!elecDayAve.getCode().equals(StatusCode.SUCCESS.getCode())) {
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), elecDayAve.getError());
            }
            if (elecDayAve.getData() != null) {
                SteamDayAve elec = new SteamDayAve();
                elec.setDayAve(elecDayAve.getData().getDayAve());
                elec.setDayDeviation(elecDayAve.getData().getDayDeviation());

                //根据用电量最大的表号--查询到该车间的信息
                EnergyResp<List<AccountingUnit>> unitByMete = null;
                if (elecDayAve.getData().getMeterNo() != null) {
                    unitByMete = accountUnitService.getUnitByMete(elecDayAve.getData().getMeterNo());
                }
                if (unitByMete != null && unitByMete.getData().size() > 0) {
                    AccountingUnit accountingUnit = unitByMete.getData().get(0);
                    elec.setMeterNo(accountingUnit.getName());
                }
                en.setElec(elec);
            }
        }
        return en;
    }


    /**
     * 能源昨日用量---电和汽---车间级别
     */
    public EnergyDayResp getEnergyDay(@RequestBody DefaultReq de) {
        EnergyDayResp en = new EnergyDayResp();
        //根据车间查询蒸汽表
        CompanyMeteReq param = new CompanyMeteReq();
        param.setEnergyType("02");
        param.setId(de.getId());
        EnergyResp<List<MeterResp>> meterList = accountUnitService.queryMeterListByAccountAndType(param);

        if (!meterList.getCode().equals(StatusCode.SUCCESS.getCode())) {
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), meterList.getError());
        }

        //使用蒸汽表获取用汽量
        if (meterList.getData() != null && meterList.getData().size() > 0) {
            List<String> meters = new ArrayList<>();
            for (MeterResp meterResp : meterList.getData()) {
                meters.add(meterResp.getLoopNumber());
            }
            EnergyResp<SteamDayAve> steamDayAve = steamMeterReadingService.getSteamYesterday(meters);
            if (!steamDayAve.getCode().equals(StatusCode.SUCCESS.getCode())) {
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), steamDayAve.getError());
            }


            EnergyQuantityAndFee energyQuantityAndFee = new EnergyQuantityAndFee();
            energyQuantityAndFee.setQuantity(steamDayAve.getData().getDayAve());
            energyQuantityAndFee.setQuantityRatio(steamDayAve.getData().getDayDeviation());
            energyQuantityAndFee.setFee(steamDayAve.getData().getYesterdayMonth());
            en.setSteam(energyQuantityAndFee);
        }

        //根据车间查询电表
        param = new CompanyMeteReq();
        param.setEnergyType("01");
        param.setId(de.getId());
        EnergyResp<List<MeterResp>> eleMeterList = accountUnitService.queryMeterListByAccountAndType(param);
        if (!eleMeterList.getCode().equals(StatusCode.SUCCESS.getCode())) {
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), eleMeterList.getError());
        }


        //使用电表获取用电量
        if (eleMeterList.getData() != null && eleMeterList.getData().size() > 0) {
            List<String> meters = new ArrayList<>();
            for (MeterResp meterResp : eleMeterList.getData()) {
                meters.add(meterResp.getLoopNumber());
            }
            //查询电量
            ElectricMeterReadingReq req = new ElectricMeterReadingReq();
            String start = DateUtil.getLastDay(DateUtil.getNowDay());
            String end = DateUtil.getNowDay();
            req.setStartTime(DateUtil.format(start, "yyyy-MM-dd"));
            req.setEndTime(DateUtil.format(end, "yyyy-MM-dd"));
            req.setMeterNoList(meters);
            EnergyResp<List<StatisticsDataResp>> elecSumByDay = custElectricCountService.getElecSumByDay(req);
            if (!elecSumByDay.getCode().equals(StatusCode.SUCCESS.getCode())) {
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), elecSumByDay.getError());
            }


            if (elecSumByDay.getData() != null && elecSumByDay.getData().size() > 0) {
                StatisticsDataResp statisticsDataResp = elecSumByDay.getData().get(0);
                //封装数据
                EnergyQuantityAndFee ele = new EnergyQuantityAndFee();
                BigDecimal sumQuantity1 = statisticsDataResp.getSumQuantity();
                ele.setQuantity(sumQuantity1);
                ele.setFee(statisticsDataResp.getSumFees());
                //计算环比
                //1.获取昨日的昨日的数据
                req.setStartTime(DateUtil.format(DateUtil.getLastDay(start), "yyyy-MM-dd"));
                req.setEndTime(DateUtil.format(DateUtil.getLastDay(end), "yyyy-MM-dd"));
                req.setMeterNoList(meters);
                EnergyResp<List<StatisticsDataResp>> elecyestyest = custElectricCountService.getElecSumByDay(req);
                if (!elecyestyest.getCode().equals(StatusCode.SUCCESS.getCode())) {
                    throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), elecyestyest.getError());
                }


                if (elecyestyest.getData() != null && elecyestyest.getData().size() > 0) {
                    StatisticsDataResp elecyestyestResp = elecyestyest.getData().get(0);
                    //2.计算环比--昨日环比（昨日-前日）/前日*100%
                    BigDecimal sumQuantity = elecyestyestResp.getSumQuantity();
                    if (sumQuantity != null && sumQuantity1 != null) {
                        BigDecimal sub = MathUtils.sub(sumQuantity1, sumQuantity);
                        if (sub != null) {
                            BigDecimal divide = MathUtils.divide(sub, sumQuantity);
                            ele.setQuantityRatio(divide);
                            en.setElec(ele);
                        }
                    }
                }
            }

        }
        return en;
    }

    /**
     * 能源昨日用量---电和汽---企业级别
     */
    public EnergyDayResp getConpEnergyDay(@RequestBody DefaultReq de) {
        EnergyDayResp en = new EnergyDayResp();
        //根据企业查询蒸汽表
        CompanyMeteReq param = new CompanyMeteReq();
        param.setEnergyType("02");
        param.setId(de.getId());
        EnergyResp<List<MeterResp>> meterList = accountUnitService.queryMeterListByCompanyAndType(param);
        if (!meterList.getCode().equals(StatusCode.SUCCESS.getCode())) {
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), meterList.getError());
        }


        //使用蒸汽表获取用汽量
        if (meterList.getData() != null && meterList.getData().size() > 0) {
            List<String> meters = new ArrayList<>();
            for (MeterResp meterResp : meterList.getData()) {
                meters.add(meterResp.getLoopNumber());
            }
            EnergyResp<SteamDayAve> steamDayAve = steamMeterReadingService.getSteamYesterday(meters);
            if (!steamDayAve.getCode().equals(StatusCode.SUCCESS.getCode())) {
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), steamDayAve.getError());
            }

            EnergyQuantityAndFee energyQuantityAndFee = new EnergyQuantityAndFee();
            energyQuantityAndFee.setQuantity(steamDayAve.getData().getDayAve());
            energyQuantityAndFee.setQuantityRatio(steamDayAve.getData().getDayDeviation());
            energyQuantityAndFee.setFee(steamDayAve.getData().getYesterdayMonth());
            en.setSteam(energyQuantityAndFee);
        }

        // ----------------------电-----------------------------------------------

        //根据企业查询电表
        param = new CompanyMeteReq();
        param.setEnergyType("01");
        param.setId(de.getId());
        EnergyResp<List<MeterResp>> eleMeterList = accountUnitService.queryMeterListByCompanyAndType(param);
        if (!eleMeterList.getCode().equals(StatusCode.SUCCESS.getCode())) {
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), eleMeterList.getError());
        }

        //使用电表获取用电量
        if (eleMeterList.getData() != null && eleMeterList.getData().size() > 0) {
            List<String> meters = new ArrayList<>();
            for (MeterResp meterResp : eleMeterList.getData()) {
                meters.add(meterResp.getLoopNumber());
            }
            //查询电量
            ElectricMeterReadingReq req = new ElectricMeterReadingReq();
            String start = DateUtil.getLastDay(DateUtil.getNowDay());
            String end = DateUtil.getNowDay();
            req.setStartTime(DateUtil.format(start, "yyyy-MM-dd"));
            req.setEndTime(DateUtil.format(end, "yyyy-MM-dd"));
            req.setMeterNoList(meters);
            EnergyResp<List<StatisticsDataResp>> elecSumByDay = custElectricCountService.getElecSumByDay(req);
            if (!elecSumByDay.getCode().equals(StatusCode.SUCCESS.getCode())) {
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), elecSumByDay.getError());
            }


            if (elecSumByDay.getData() != null && elecSumByDay.getData().size() > 0) {
                StatisticsDataResp statisticsDataResp = elecSumByDay.getData().get(0);
                //封装数据
                EnergyQuantityAndFee ele = new EnergyQuantityAndFee();
                BigDecimal sumQuantity1 = statisticsDataResp.getSumQuantity();
                ele.setQuantity(sumQuantity1);
                ele.setFee(statisticsDataResp.getSumFees());
                //计算环比
                //1.获取昨日的昨日的数据
                req.setStartTime(DateUtil.format(DateUtil.getLastDay(start), "yyyy-MM-dd"));
                req.setEndTime(DateUtil.format(DateUtil.getLastDay(end), "yyyy-MM-dd"));
                req.setMeterNoList(meters);
                EnergyResp<List<StatisticsDataResp>> elecyestyest = custElectricCountService.getElecSumByDay(req);
                if (!elecyestyest.getCode().equals(StatusCode.SUCCESS.getCode())) {
                    throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), elecyestyest.getError());
                }


                if (elecyestyest.getData() != null && elecyestyest.getData().size() > 0) {
                    StatisticsDataResp elecyestyestResp = elecyestyest.getData().get(0);
                    //2.计算环比--昨日环比（昨日-前日）/前日*100%
                    BigDecimal sumQuantity = elecyestyestResp.getSumQuantity();
                    if (sumQuantity != null && sumQuantity1 != null) {
                        BigDecimal sub = MathUtils.sub(sumQuantity1, sumQuantity);
                        if (sub != null) {
                            BigDecimal divide = MathUtils.divide(sub, sumQuantity);
                            ele.setQuantityRatio(divide);
                        }
                    }
                }
                en.setElec(ele);
            }
        }
        return en;
    }


}
