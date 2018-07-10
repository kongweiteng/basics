package com.enn.web.controller;

import com.enn.constant.Constant;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.energy.system.common.util.PagedList;
import com.enn.energy.system.common.util.StringUtil;
import com.enn.service.business.IAccountUnitService;
import com.enn.service.business.IOpentsdbService;
import com.enn.service.system.IDictService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.TeamInfoPo;
import com.enn.vo.energy.business.req.*;
import com.enn.vo.energy.business.resp.*;
import com.enn.vo.energy.business.vo.Base;
import com.enn.vo.energy.system.Dict;
import com.enn.vo.energy.system.ProductInfo;
import com.enn.web.vo.ProductDataAddReq;
import com.enn.web.vo.ProductDataUpdateReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 生产数据接口
 */
@Api(tags = {"生产用能追溯"})
@RestController
@RequestMapping(value = "/web/productTrack")
public class ProductionTracedBackController extends BaseController {
    protected static final Logger logger = LoggerFactory.getLogger(ProductInfoController.class);
    @Autowired
    private IDictService dictService;
    @Autowired
    private IOpentsdbService opentsdbService;
    @Autowired
    private IAccountUnitService accountUnitService;

    @ApiOperation(value = "通过企业ID查询车间信息")
    @RequestMapping(value = "/getWorkshopByCustId", method = RequestMethod.POST)
    public EnergyResp<List<UnitResp>> getWorkshopByCustId(@RequestBody @Valid CustReq custReq, BindingResult result) {
        EnergyResp<List<UnitResp>> resp = new EnergyResp<List<UnitResp>>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        AccountUnitReq req = new AccountUnitReq();
        req.setId(Long.parseLong(custReq.getCustID()));
        req.setAccountingType(Constant.ACCOUNTING_TYPE_02);
        req.setIsAccount(false);
        return accountUnitService.queryAccountList(req);
    }

    @ApiOperation(value = "通过车间ID查询生产线信息")
    @RequestMapping(value = "/getLineByWorkshopId", method = RequestMethod.POST)
    public EnergyResp<List<UnitResp>> getLineByWorkshopId(@RequestBody @Valid DefaultReq defaultReq, BindingResult result) {
        EnergyResp<List<UnitResp>> resp = new EnergyResp<List<UnitResp>>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        AccountUnitReq req = new AccountUnitReq();
        req.setId(defaultReq.getId());
        req.setAccountingType(Constant.ACCOUNTING_TYPE_03);
        req.setIsAccount(true);
        return accountUnitService.queryAccountList(req);
    }

    @RequestMapping(value = "/findLineVoltage", method = RequestMethod.POST)
    @ApiOperation(value = "三相线电压查询", notes = "三相线电压查询")
    public EnergyResp<List<MetricAnalysisResp>> findLineVoltage(@RequestBody @Valid ProductTrackReq productTrackReq, BindingResult result) {
        EnergyResp<List<MetricAnalysisResp>> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.setCode(StatusCode.C.getCode());
            energyResp.setMsg(result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        EnergyResp<List<MetricAnalysisResp>> resp = findIndex(productTrackReq, Constant.INDEX_TYPE_LINE_VOLTAGE);
        if (StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
            energyResp.ok(resp.getData());
        } else {
            energyResp.setCode(resp.getCode());
            energyResp.setMsg(resp.getMsg());
        }
        return energyResp;
    }

    @RequestMapping(value = "/findCurrent", method = RequestMethod.POST)
    @ApiOperation(value = "三相电流查询", notes = "三相电流查询")
    public EnergyResp<List<MetricAnalysisResp>> findCurrent(@RequestBody @Valid ProductTrackReq productTrackReq, BindingResult result) {
        EnergyResp<List<MetricAnalysisResp>> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.setCode(StatusCode.C.getCode());
            energyResp.setMsg(result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        EnergyResp<List<MetricAnalysisResp>> resp = findIndex(productTrackReq, Constant.INDEX_TYPE_CURRENT);
        if (StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
            energyResp.ok(resp.getData());
        } else {
            energyResp.setCode(resp.getCode());
            energyResp.setMsg(resp.getMsg());
        }
        return energyResp;
    }

    @RequestMapping(value = "/findActivePower", method = RequestMethod.POST)
    @ApiOperation(value = "有功功率查询", notes = "有功功率查询")
    public EnergyResp<List<MetricAnalysisResp>> findActivePower(@RequestBody @Valid ProductTrackReq productTrackReq, BindingResult result) {
        EnergyResp<List<MetricAnalysisResp>> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.setCode(StatusCode.C.getCode());
            energyResp.setMsg(result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        EnergyResp<List<MetricAnalysisResp>> resp = findIndex(productTrackReq, Constant.ACTIVE_POWER);
        if (!StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
            energyResp.setCode(resp.getCode());
            energyResp.setMsg(resp.getMsg());
            return energyResp;
        }
        DefaultReq defaultReq = new DefaultReq();
        defaultReq.setId(productTrackReq.getProLineId());
        EnergyResp<AccountingUnit> accountResp = accountUnitService.getOne(defaultReq);
        if (!StatusCode.SUCCESS.getCode().equals(accountResp.getCode())) {
            energyResp.setCode(accountResp.getCode());
            energyResp.setMsg("查询企业Id错误");
            return energyResp;
        }

        if (resp.getData() != null && resp.getData().size() > 0) {
            Long custId = accountResp.getData().getCustId();
            List<MetricAnalysisResp> metricAnalysisRespList = resp.getData();
            List<MetricAnalysisResp> metricAnalysisResps = new ArrayList<>();
            for (int i = 0; i < metricAnalysisRespList.size(); i++) {
                MetricAnalysisResp metricAnalysisResp = new MetricAnalysisResp();
                metricAnalysisResp.setMetric(metricAnalysisRespList.get(i).getMetric());
                metricAnalysisResp.setMetricName(metricAnalysisRespList.get(i).getMetricName());
                List<DataResp> dataRespList = metricAnalysisRespList.get(i).getMetricData();
                if ((custId != null) && (custId.equals(2L))) {
                    List<DataResp> list = dataRespList.stream().map(s -> {
                        DataResp dataResp = new DataResp();
                        dataResp.setTime(s.getTime());
                        dataResp.setValue(MathUtils.towDecimal(MathUtils.divide(s.getValue(), new BigDecimal(1000))));
                        return dataResp;
                    }).collect(Collectors.toList());
                    metricAnalysisResp.setMetricData(list);
                }else{
                    metricAnalysisResp.setMetricData(dataRespList);
                }
                metricAnalysisResps.add(metricAnalysisResp);
            }

            energyResp.ok(metricAnalysisResps);
        } else {
            energyResp.setCode(StatusCode.SUCCESS.getCode());
            energyResp.setMsg("查询到的信息为空");
        }
        return energyResp;
    }

    @RequestMapping(value = "/findReactivePower", method = RequestMethod.POST)
    @ApiOperation(value = "无功功率查询", notes = "无功功率查询")
    public EnergyResp<List<MetricAnalysisResp>> findReactivePower(@RequestBody @Valid ProductTrackReq productTrackReq, BindingResult result) {
        EnergyResp<List<MetricAnalysisResp>> energyResp = new EnergyResp();
        if (result.hasErrors()) {
            energyResp.setCode(StatusCode.C.getCode());
            energyResp.setMsg(result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        EnergyResp<List<MetricAnalysisResp>> resp = findIndex(productTrackReq, Constant.REACTIVE_POWER);
        if (StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
            energyResp.ok(resp.getData());
        } else {
            energyResp.setCode(resp.getCode());
            energyResp.setMsg(resp.getMsg());
        }
        return energyResp;
    }


    public EnergyResp<List<MetricAnalysisResp>> findIndex(ProductTrackReq productTrackReq, String metricType) {
        EnergyResp<List<MetricAnalysisResp>> energyResp = new EnergyResp();
        List<MetricAnalysisResp> metricAnalysisResps = new ArrayList<>();
        //计算开始时间和结束时间
        String start = productTrackReq.getStartTime() + ":00";
        String end = productTrackReq.getEndTime() + ":00";
        EnergyResp checkDateResp = checkDateByDay(start, end);
        if (!StatusCode.SUCCESS.getCode().equals(checkDateResp.getCode())) {
            energyResp.setCode(checkDateResp.getCode());
            energyResp.setMsg(checkDateResp.getMsg());
            return energyResp;
        }
        logger.info("产品回溯查询 --- start");

        DefaultReq defaultReq = new DefaultReq();
        defaultReq.setId(productTrackReq.getProLineId());
        EnergyResp<List<MeterResp>> resp = accountUnitService.queryMeterListByAccount(defaultReq);
        if (StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
            List<MeterResp> list = resp.getData();
            List<Equip> equips = new ArrayList<>();
            for (MeterResp meter : list) {
                if (StringUtils.isNoneBlank(meter.getLoopNumber())&&"01".equals(meter.getEnergyType())) {
                    Equip equip = new Equip();
                    equip.setEquipID(meter.getLoopNumber());
                    equip.setStaId(meter.getStaId());
                    equip.setEquipMK(Constant.ELEC_EQUIPMK);
                    equips.add(equip);
                }
            }
            if (equips.size() > 0) {
                ProductTrackResp productTrackResp = new ProductTrackResp();
                productTrackResp.setIndexType(metricType);
                //根据metricCode 查询出所有的指标
                EnergyResp<List<Dict>> dictListResp = dictService.getDictList(metricType);
                if (StatusCode.SUCCESS.getCode().equals(dictListResp.getCode())) {
                    List<Dict> dictList = dictListResp.getData();
                    for (Dict dict : dictList) {
                        Boolean flag = false;
                        String metric = dict.getValue();
                        //获取几个指标的数据并进行封装
                        SamplDataStaReq samplDataReq = new SamplDataStaReq();
                        samplDataReq.setStart(start);
                        samplDataReq.setEnd(end);
                        samplDataReq.setEquips(equips);
                        samplDataReq.setMetric("EMS." + metric);
                        samplDataReq.setDownsample(Constant.DOWNSMPLE_1M);
                        EnergyResp<ListResp<RmiSamplDataResp>> samplData = opentsdbService.getSamplDataStaReq(samplDataReq);
                        MetricAnalysisResp metricAnalysisResp = new MetricAnalysisResp();
                        metricAnalysisResp.setMetric(metric);
                        metricAnalysisResp.setMetricName(dict.getLabel());
                        if (!StatusCode.SUCCESS.getCode().equals(samplData.getCode()) && !flag) {
                            flag = true;
                            samplData = opentsdbService.getSamplDataStaReq(samplDataReq);
                        }
                        if (!StatusCode.SUCCESS.getCode().equals(samplData.getCode()) && flag) {
                            metricAnalysisResp.setMetricData(DateUtil.getDataResp(start));
                            logger.info("请求大数据平台获取"+dict.getLabel()+"出现错误");
                            throw new EnergyException(StatusCode.E_C.getCode(),StatusCode.E_C.getMsg(),"请求大数据平台获取"+dict.getLabel()+"出现错误");
                        }
                        if (samplData.getData() != null && samplData.getData().getList().size() > 0) {
                            if (samplData.getData().getList().get(0) != null) {
                                List<DataResp> dataRespList = samplData.getData().getList().get(0).getDataResp();
                                List<DataResp> dataResps = dataRespList.stream().map(s -> {
                                    DataResp dataResp = new DataResp();
                                    dataResp.setTime(s.getTime());
                                    dataResp.setValue(MathUtils.towDecimal(s.getValue()));
                                    return dataResp;
                                }).collect(Collectors.toList());
                                metricAnalysisResp.setMetricData(dataResps);
                            }
                        } else {
                            logger.info("请求大数据平台获取" + dict.getLabel() + "的数据为空");
                        }
                        metricAnalysisResps.add(metricAnalysisResp);
                    }
                }
                energyResp.ok(metricAnalysisResps);
            } else {
                energyResp.setCode(resp.getCode());
                energyResp.setMsg("该生产线下无电表！！");
                return energyResp;
            }
        } else {
            energyResp.setCode(resp.getCode());
            energyResp.setMsg(resp.getMsg());
            return energyResp;
        }
        return energyResp;
    }


    /**
     * 验证日期
     *
     * @return
     */
    private EnergyResp checkDateByDay(String startTime, String endTime) {
        EnergyResp result = new EnergyResp();
        if (!DateUtil.isValidTime(startTime) || !DateUtil.isValidTime(endTime)) {
            result.faile(StatusCode.C.getCode(), "时间格式不正确");
            return result;
        }
        if(startTime.compareTo(endTime)>0){
            result.faile(StatusCode.C.getCode(), "开始日期小于结束日期");
            return result;
        }

        //时间跨度不能超过72小时
        if (DateUtil.addHour(startTime, Calendar.HOUR_OF_DAY, 72).compareTo(endTime) < 0) {
            result.faile(StatusCode.C.getCode(), "查询日统计，时间跨度不能大于72小时");
            return result;
        }
        result.ok(null);
        return result;
    }


}
