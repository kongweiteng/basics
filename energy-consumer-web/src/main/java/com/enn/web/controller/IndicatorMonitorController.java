package com.enn.web.controller;

import com.enn.constant.Constant;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.service.business.ICustMeterService;
import com.enn.service.business.IElectricityDistributionRoomService;
import com.enn.service.business.IOpentsdbService;
import com.enn.service.business.IPowerFactorService;
import com.enn.service.system.IDictService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.dto.PowerFactor;
import com.enn.vo.energy.business.po.CustMeter;
import com.enn.vo.energy.business.po.CustWifi;
import com.enn.vo.energy.business.po.DistributionMeter;
import com.enn.vo.energy.business.req.*;
import com.enn.vo.energy.business.resp.*;
import com.enn.vo.energy.system.Dict;
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
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/indicatorMonitor")
@Api(tags = {"用能质量检测"})
public class IndicatorMonitorController {

    protected static final Logger logger = LoggerFactory.getLogger(IndicatorMonitorController.class);
    @Autowired
    private IOpentsdbService rmiOpentsdbService;
    @Autowired
    private ICustMeterService custMeterService;
    @Autowired
    private IElectricityDistributionRoomService distributionRoomService;
    @Autowired
    private IPowerFactorService powerFactorService;
    @Autowired
    private IDictService dictService;

    @RequestMapping(value = "/findRooms", method = RequestMethod.POST)
    @ApiOperation(value = "通过企业ID获取配电室信息", notes = "通过企业ID获取配电室信息")
    public EnergyResp findDistributionRoom(@RequestBody @Valid CustReq custReq, BindingResult result) {
        EnergyResp<List<ElectricityDistributionRoom>> resp = new EnergyResp<>();
        if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
        logger.info("通过企业ID获取配电室信息");
        EnergyResp<List<ElectricityDistributionRoom>> energyResp = distributionRoomService.getDistributionRoomByCustID(custReq);
        if (StatusCode.SUCCESS.getCode().equals(energyResp.getCode())) {
            logger.info("查询的配电室信息为："+energyResp.getData().toString());
            resp.ok(energyResp.getData());
        } else {
            resp.setMsg(energyResp.getMsg());
            resp.setCode(energyResp.getCode());
        }
        return energyResp;
    }

    @RequestMapping(value = "/findMeters", method = RequestMethod.POST)
    @ApiOperation(value = "通过配电室ID获取回路编号（表号）和站点号", notes = "通过配电室ID获取回路编号（表号）和站点号")
    public EnergyResp<List<DistributionMeter>> findMeter(@RequestBody @Valid DistributeRoomReq distributeRoomReq, BindingResult result) {
        EnergyResp<List<DistributionMeter>> resp = new EnergyResp<>();
        if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
        DefaultReq defaultReq = new DefaultReq();
        defaultReq.setId(distributeRoomReq.getDistributeRoomId());
        logger.info("通过配电室获取计量点信息");
        EnergyResp<List<DistributionMeter>> energyResp = custMeterService.getCustMetersByDistributionId(defaultReq);
        if (StatusCode.SUCCESS.getCode().equals(energyResp.getCode())) {
            logger.info("查询的计量点信息为："+energyResp.getData().toString());
            resp.ok(energyResp.getData());
        } else {
            resp.setCode(energyResp.getCode());
            resp.setMsg(energyResp.getMsg());
            return resp;
        }
        return resp;
    }

    @RequestMapping(value = "/findPowerFactor", method = RequestMethod.POST)
    @ApiOperation(value = "通过计量表ID查询功率因数", notes = "通过计量表ID查询功率因数")
    public EnergyResp<PowerFactor> findPowerFactor(@RequestBody @Valid DefaultReq defaultReq, BindingResult result) {
        EnergyResp<PowerFactor> resp = new EnergyResp<>();
        PowerFactor powerFactor = new PowerFactor();
        if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }

        logger.info("查询功率因数------start");
        EnergyResp<List<MetricAnalysisResp>> energyResp = findIndex(defaultReq, Constant.AVG_POWER_FACTOR);
        if (!StatusCode.SUCCESS.getCode().equals(energyResp.getCode())) {
            resp.setCode(energyResp.getCode());
            resp.setMsg(energyResp.getMsg());
            return resp;
        }
        if (energyResp.getData() != null && energyResp.getData().size() > 0) {
            logger.info("查询功率因数" + energyResp.getData().get(0).getMetricData().toString());
            List<MetricAnalysisResp> metricAnalysisRespList = new ArrayList<>();
            MetricAnalysisResp metricAnalysisResp = new MetricAnalysisResp();
            metricAnalysisResp.setMetric(energyResp.getData().get(0).getMetric());
            metricAnalysisResp.setMetricName(energyResp.getData().get(0).getMetricName());
            List<DataResp> dataRespList = energyResp.getData().get(0).getMetricData();
            List<DataResp> list = dataRespList.stream().map(s -> {
                DataResp dataResp = new DataResp();
                dataResp.setTime(s.getTime());
                dataResp.setValue(MathUtils.towDecimal(s.getValue()));
                return dataResp;
            }).collect(Collectors.toList());
            metricAnalysisResp.setMetricData(list);
            metricAnalysisRespList.add(metricAnalysisResp);
            powerFactor.setDataResp(metricAnalysisRespList);
            DataResp data = dataRespList.stream().max(Comparator.comparing(DataResp::getTime)).get();
            powerFactor.setLastValue(MathUtils.towDecimal(data.getValue()));
            BigDecimal total = dataRespList.stream().filter(temp -> null != temp.getValue()).map(DataResp::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal avg = MathUtils.towDecimal(MathUtils.divide(total, new BigDecimal(dataRespList.size())));
            powerFactor.setAvgValue(avg);
            DataResp dataResp= dataRespList.stream().filter(a->null!=a.getValue()).min(Comparator.comparing(DataResp::getValue)).get();
//            DataResp dataResp = dataRespList.stream().min((a, b) -> compareBig(a.getValue(), b.getValue())).get();
            powerFactor.setTime(dataResp.getTime());
            powerFactor.setMinValue(MathUtils.towDecimal(dataResp.getValue()));
            resp.ok(powerFactor);

        } else {
            logger.info("功率因数为空！！！");
        }
        return resp;
    }

    @RequestMapping(value = "/findVolUnbalance", method = RequestMethod.POST)
    @ApiOperation(value = "通过计量表ID查询电压不平衡度", notes = "通过计量表ID查询电压不平衡度")
    public EnergyResp<List<MetricAnalysisResp>> findVolUnbalance(@RequestBody @Valid DefaultReq defaultReq, BindingResult result) {
        EnergyResp<List<MetricAnalysisResp>> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.setCode(StatusCode.C.getCode());
            energyResp.setMsg(result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        logger.info("查询电压不平衡度--------start");
        EnergyResp<List<MetricAnalysisResp>> resp = findIndex(defaultReq, Constant.VOLTAGE_IMBALANCE);
        if (!StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
            energyResp.setCode(resp.getCode());
            energyResp.setMsg(resp.getMsg());
        }
        if (resp.getData() != null && resp.getData().size() > 0) {
            List<MetricAnalysisResp> metricAnalysisRespList = new ArrayList<>();
            for (int i = 0; i < resp.getData().size(); i++) {
                MetricAnalysisResp metricAnalysisResp = new MetricAnalysisResp();
                metricAnalysisResp.setMetric(resp.getData().get(i).getMetric());
                metricAnalysisResp.setMetricName(resp.getData().get(i).getMetricName());
                List<DataResp> dataRespList = resp.getData().get(i).getMetricData();
                List<DataResp> list = dataRespList.stream().map(s -> {
                    DataResp dataResp = new DataResp();
                    dataResp.setTime(s.getTime());
                    dataResp.setValue(MathUtils.towDecimal(MathUtils.mul(s.getValue(), new BigDecimal(100))));
                    return dataResp;
                }).collect(Collectors.toList());
                metricAnalysisResp.setMetricData(list);
                metricAnalysisRespList.add(metricAnalysisResp);
            }
            energyResp.ok(metricAnalysisRespList);
            logger.info("查询电压不平衡度--------end");
        } else {
            logger.info("查询电压不平衡度为空");
        }
        return energyResp;
    }

    @RequestMapping(value = "/findCurUnbalance", method = RequestMethod.POST)
    @ApiOperation(value = "通过计量表ID查询电流不平衡度", notes = "通过计量表ID查询电流不平衡度")
    public EnergyResp<List<MetricAnalysisResp>> findCurUnbalance(@RequestBody @Valid DefaultReq defaultReq, BindingResult result) {
        EnergyResp<List<MetricAnalysisResp>> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.setCode(StatusCode.C.getCode());
            energyResp.setMsg(result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        logger.info("查询电流不平衡度--------start");
        EnergyResp<List<MetricAnalysisResp>> resp = findIndex(defaultReq, Constant.CURRENT_IMBALANCE);
        if (!StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
            energyResp.setCode(resp.getCode());
            energyResp.setMsg(resp.getMsg());
        }
        if (resp.getData() != null && resp.getData().size() > 0) {
            List<MetricAnalysisResp> metricAnalysisRespList = new ArrayList<>();
            for (int i = 0; i < resp.getData().size(); i++) {
                MetricAnalysisResp metricAnalysisResp = new MetricAnalysisResp();
                metricAnalysisResp.setMetric(resp.getData().get(i).getMetric());
                metricAnalysisResp.setMetricName(resp.getData().get(i).getMetricName());
                List<DataResp> dataRespList = resp.getData().get(i).getMetricData();
                List<DataResp> list = dataRespList.stream().map(s -> {
                    DataResp dataResp = new DataResp();
                    dataResp.setTime(s.getTime());
                    dataResp.setValue(MathUtils.towDecimal(MathUtils.mul(s.getValue(), new BigDecimal(100))));
                    return dataResp;
                }).collect(Collectors.toList());
                metricAnalysisResp.setMetricData(list);
                metricAnalysisRespList.add(metricAnalysisResp);
            }
            energyResp.ok(metricAnalysisRespList);
            logger.info("查询电流不平衡度--------end");
        } else {
            logger.info("查询电流不平衡度为空");
        }
        return energyResp;
    }

    @RequestMapping(value = "/findVolAberrRate", method = RequestMethod.POST)
    @ApiOperation(value = "通过计量表ID查询三相电压谐波畸变率", notes = "通过计量表ID查询三相电压谐波畸变率")
    public EnergyResp<List<MetricAnalysisResp>> findVolAberrRate(@RequestBody @Valid DefaultReq defaultReq, BindingResult result) {
        EnergyResp<List<MetricAnalysisResp>> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.setCode(StatusCode.C.getCode());
            energyResp.setMsg(result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        logger.info("查询三相电压谐波畸变率--------start");
        EnergyResp<List<MetricAnalysisResp>> resp = findIndex(defaultReq, Constant.VOLTAGE_ABERRATION_RATE);
        if (!StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
            energyResp.setCode(resp.getCode());
            energyResp.setMsg(resp.getMsg());
        }
        if (resp.getData() != null && resp.getData().size() > 0) {
            List<MetricAnalysisResp> metricAnalysisRespList = new ArrayList<>();
            for (int i = 0; i < resp.getData().size(); i++) {
                MetricAnalysisResp metricAnalysisResp = new MetricAnalysisResp();
                metricAnalysisResp.setMetric(resp.getData().get(i).getMetric());
                metricAnalysisResp.setMetricName(resp.getData().get(i).getMetricName());
                List<DataResp> dataRespList = resp.getData().get(i).getMetricData();
                List<DataResp> list = dataRespList.stream().map(s -> {
                    DataResp dataResp = new DataResp();
                    dataResp.setTime(s.getTime());
                    dataResp.setValue(MathUtils.towDecimal(s.getValue()));
                    return dataResp;
                }).collect(Collectors.toList());
                metricAnalysisResp.setMetricData(list);
                metricAnalysisRespList.add(metricAnalysisResp);
            }
            energyResp.ok(metricAnalysisRespList);
            logger.info("查询三相电压谐波畸变率--------end");
        } else {
            logger.info("查询三相电压谐波畸变率为空");
        }
        return energyResp;
    }

    @RequestMapping(value = "/findCurAberrRate", method = RequestMethod.POST)
    @ApiOperation(value = "通过计量表ID查询三相电流谐波畸变率", notes = "通过计量表ID查询三相电流谐波畸变率")
    public EnergyResp<List<MetricAnalysisResp>> findCurAberrRate(@RequestBody @Valid DefaultReq defaultReq, BindingResult result) {
        EnergyResp<List<MetricAnalysisResp>> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.setCode(StatusCode.C.getCode());
            energyResp.setMsg(result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        logger.info("查询三相电流谐波畸变率--------start");
        EnergyResp<List<MetricAnalysisResp>> resp = findIndex(defaultReq, Constant.CURRENT_ABERRATION_RATE);
        if (!StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
            energyResp.setCode(resp.getCode());
            energyResp.setMsg(resp.getMsg());
        }
        if (resp.getData() != null && resp.getData().size() > 0) {
            List<MetricAnalysisResp> metricAnalysisRespList = new ArrayList<>();
            for(int i=0;i<resp.getData().size();i++){
                MetricAnalysisResp metricAnalysisResp = new MetricAnalysisResp();
                metricAnalysisResp.setMetric(resp.getData().get(i).getMetric());
                metricAnalysisResp.setMetricName(resp.getData().get(i).getMetricName());
                List<DataResp> dataRespList = resp.getData().get(i).getMetricData();
                List<DataResp> list = dataRespList.stream().map(s -> {
                    DataResp dataResp = new DataResp();
                    dataResp.setTime(s.getTime());
                    dataResp.setValue(MathUtils.towDecimal(s.getValue()));
                    return dataResp;
                }).collect(Collectors.toList());
                metricAnalysisResp.setMetricData(list);
                metricAnalysisRespList.add(metricAnalysisResp);
            }
            energyResp.ok(metricAnalysisRespList);
            logger.info("查询三相电流谐波畸变率--------end");
        } else {
            logger.info("查询三相电流谐波畸变率--------end");
        }
        return energyResp;
    }

//    @RequestMapping(value = "/findMaxDemand", method = RequestMethod.POST)
//    @ApiOperation(value = "通过回路编号（表号）获取最大需量", notes = "通过回路编号（表号）获取最大需量")
//    public EnergyResp<MaxDemand> findMaxDemand(@RequestBody @Valid LoopNumberReq loopNumberReq, BindingResult result) {
//        EnergyResp<MaxDemand> resp = new EnergyResp<>();
//        MaxDemand maxDemand = new MaxDemand();
//        if (result.hasErrors()) {
//            resp.setCode(StatusCode.C.getCode());
//            resp.setMsg(result.getFieldError().getDefaultMessage());
//            return resp;
//        }
//        List<String> equipIDs = new ArrayList<>();
//        equipIDs.add(loopNumberReq.getLoopNumberId());
//        logger.info("查询三相电流谐波畸变率--------start");
//        EnergyResp<List<DataResp>> energyResp = find(equipIDs, loopNumberReq.getStatId(), "EMS." + CollectItemEnum.Enow);
//        if (StatusCode.SUCCESS.getCode().equals(energyResp.getCode())) {
//            maxDemand.setDataResp(energyResp.getData());
//        }
//        resp.setData(maxDemand);
//        return resp;
//    }

//    private EnergyResp<List<DataResp>> find(List<String> equipIDs, String statId, String metric) {
//        EnergyResp<List<DataResp>> energyResp = new EnergyResp<>();
//        logger.info("用能质量监测 --- start");
//        //获取几个指标的数据并进行封装
//        SamplDataReq samplDataReq = new SamplDataReq();
//        samplDataReq.setEquipID(equipIDs);
//        samplDataReq.setMetric(metric);
//        //计算开始时间和结束时间
//        Date currentDate = new Date();
//        String end = DateUtil.format(currentDate, DateUtil.BASIC_PATTEN);
//        String start = DateUtil.getBeforeHourTime(currentDate, -6, DateUtil.BASIC_PATTEN);
//        samplDataReq.setStart(start);
//        samplDataReq.setEnd(end);
//        samplDataReq.setDownsample(Constant.DOWNSMPLE_1M);
//        samplDataReq.setStaId(statId);
//        samplDataReq.setEquipMK(Constant.ELEC_EQUIPMK);
//        EnergyResp<ListResp<RmiSamplDataResp>> samplData = rmiOpentsdbService.getSamplData(samplDataReq);
//        if (samplData.getCode().equals(samplData.getCode())) {
//            List<DataResp> dataRespList = new ArrayList<>();
//            if (samplData.getData().getList().size() > 0) {
//                dataRespList = samplData.getData().getList().get(0).getDataResp();
//                energyResp.ok(dataRespList);
//            } else {
//                energyResp.null_obj(dataRespList);
//            }
//        } else {
//            energyResp.setCode(samplData.getCode());
//            energyResp.setMsg(samplData.getMsg());
//        }
//        logger.info("用能质量监测 --- end");
//        return energyResp;
//    }


    public EnergyResp<List<MetricAnalysisResp>> findIndex(DefaultReq defaultReq, String metricType) {
        EnergyResp<List<MetricAnalysisResp>> energyResp = new EnergyResp();
        List<MetricAnalysisResp> metricAnalysisResps = new ArrayList<>();
        ProductTrackResp productTrackResp = new ProductTrackResp();
        productTrackResp.setIndexType(metricType);
        logger.info("通过各采集项的值--------start");
        EnergyResp<CustMeter> meterEnergyResp = custMeterService.getMeterByMeterId(defaultReq);
        EnergyResp<CustWifi> wifiEnergyResp = custMeterService.getWifiByMeteId(defaultReq);
        if ((!StatusCode.SUCCESS.getCode().equals(meterEnergyResp.getCode())) || (!StatusCode.SUCCESS.getCode().equals(wifiEnergyResp.getCode()))) {
            energyResp.setCode(StatusCode.A.getCode());
            energyResp.setMsg(StatusCode.A.getMsg());
            return energyResp;
        }
        //计算开始时间和结束时间
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -1);// 1分钟之前的时间
        Date beforeD = beforeTime.getTime();
        String end = DateUtil.format(beforeD,DateUtil.BASIC_PATTEN);
        String start = DateUtil.getBeforeHourTime(beforeD, -6, DateUtil.BASIC_PATTEN);
        logger.info("请求的开始时间："+start);
        logger.info("请求的结束时间："+end);
        List<String> equipIDs = new ArrayList<>();
        equipIDs.add(meterEnergyResp.getData().getLoopNumber());
        //根据metricCode 查询出所有的指标
        EnergyResp<List<Dict>> dictListResp = dictService.getDictList(metricType);
        if (StatusCode.SUCCESS.getCode().equals(dictListResp.getCode())) {
            List<Dict> dictList = dictListResp.getData();
            for (Dict dict : dictList) {
                Boolean flag = false;
                logger.info("从大数据平台获取"+dict.getLabel()+"的数据------------start");
                String metric = dict.getValue();
                //获取几个指标的数据并进行封装
                SamplDataReq samplDataReq = new SamplDataReq();
                samplDataReq.setStart(start);
                samplDataReq.setEnd(end);
                samplDataReq.setStaId(wifiEnergyResp.getData().getStaId());
                samplDataReq.setEquipMK(Constant.ELEC_EQUIPMK);
                samplDataReq.setEquipID(equipIDs);
                samplDataReq.setMetric("EMS." + metric);
                samplDataReq.setDownsample(Constant.DOWNSMPLE_1M);
                EnergyResp<ListResp<RmiSamplDataResp>> samplData = rmiOpentsdbService.getSamplData(samplDataReq);
                MetricAnalysisResp metricAnalysisResp = new MetricAnalysisResp();
                metricAnalysisResp.setMetric(metric);
                metricAnalysisResp.setMetricName(dict.getLabel());
                if (!StatusCode.SUCCESS.getCode().equals(samplData.getCode()) && !flag) {
                    flag = true;
                    samplData = rmiOpentsdbService.getSamplData(samplDataReq);
                }
                if (!StatusCode.SUCCESS.getCode().equals(samplData.getCode()) && flag) {
                    metricAnalysisResp.setMetricData(DateUtil.getDataResp(start));
                    logger.info("请求大数据平台获取"+dict.getLabel()+"出现错误");
                    throw new EnergyException(StatusCode.E_C.getCode(),StatusCode.E_C.getMsg(),"请求大数据平台获取"+dict.getLabel()+"出现错误");
                }
                if (samplData.getData() != null && samplData.getData().getList().size() > 0) {
                    if (samplData.getData().getList().get(0) != null) {
                        logger.info("请求大数据平台" + dict.getLabel() + "的数据:" + samplData.getData().getList().get(0).getDataResp().toString());
                        metricAnalysisResp.setMetricData(samplData.getData().getList().get(0).getDataResp());
                        metricAnalysisResps.add(metricAnalysisResp);
                    }
                } else {
                    logger.info("请求大数据平台获取" + dict.getLabel() + "的数据为空");
                }

            }
        }
        energyResp.ok(metricAnalysisResps);
        logger.info(metricAnalysisResps.toString());
        return energyResp;
    }

    private static int compareBig(BigDecimal a, BigDecimal b){
        if (a==null || b==null){
            return 0;
        }
        int i = a.compareTo(b);
        return i;
    }


}
