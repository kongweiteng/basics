package com.enn.web.controller;

import com.enn.constant.Constant;
import com.enn.constant.FeesTypeEnum;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.ExcelUtils;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.service.business.ICustElectricCountService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.ElectricMeterReadingDayPo;
import com.enn.vo.energy.business.req.*;
import com.enn.vo.energy.business.resp.*;
import com.enn.vo.energy.business.upload.UploadResp;
import com.enn.vo.energy.common.enums.EnergyTypeEnum;
import com.enn.web.vo.CustElectricCountDetail;
import com.enn.web.vo.CustElectricCountLineResp;
import com.enn.web.vo.CustElectricCountReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

/**
 * 企业用电统计Controller
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 18:29
 */
@Api(tags = {"用电报表--企业用电"})
@RestController
@RequestMapping(value = "/web/custElectricCount")
public class CustElectricCountController extends BaseController {

    @Value("${company.electric.count.day}")
    private Integer day;
    @Value("${company.electric.count.month}")
    private Integer month;
    @Value("${company.electric.count.year}")
    private Integer year;

    @ApiOperation(value = "企业分时电费占比")
    @RequestMapping(value = "/getPercent", method = RequestMethod.POST)
    public EnergyResp<ElectricMeterReadingResp> getPercent(@RequestBody @Valid CustElectricCountReq req, BindingResult result) {
        EnergyResp<ElectricMeterReadingResp> resp = new EnergyResp<ElectricMeterReadingResp>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        ElectricMeterReadingReq electricMeterReadingReq = new ElectricMeterReadingReq();
        electricMeterReadingReq.setStartTime(req.getStartTime());
        electricMeterReadingReq.setEndTime(req.getEndTime());
        List<String> meterNoList = getMeterListByCustId(req.getCustId());
        if (null == meterNoList || meterNoList.size() <= 0) {
            resp.faile(StatusCode.C.getCode(), "该公司下未查到电表信息");
            return resp;
        }
        electricMeterReadingReq.setMeterNoList(meterNoList);
        resp = getSum(electricMeterReadingReq, req.getDateType());
        if (resp.getCode().equals(StatusCode.SUCCESS.getCode())) {
            ElectricMeterReadingResp electricMeterReadingResp = resp.getData();
            electricMeterReadingResp.setSumQuantity(MathUtils.twoDecimal(electricMeterReadingResp.getSumQuantity()));
            electricMeterReadingResp.setTipQuantity(MathUtils.twoDecimal(electricMeterReadingResp.getTipQuantity()));
            electricMeterReadingResp.setPeakQuantity(MathUtils.twoDecimal(electricMeterReadingResp.getPeakQuantity()));
            electricMeterReadingResp.setFlatQuantity(MathUtils.twoDecimal(electricMeterReadingResp.getFlatQuantity()));
            electricMeterReadingResp.setValleyQuantity(MathUtils.twoDecimal(electricMeterReadingResp.getValleyQuantity()));
            electricMeterReadingResp.setSumFees(MathUtils.twoDecimal(electricMeterReadingResp.getSumFees()));
            electricMeterReadingResp.setTipFees(MathUtils.twoDecimal(electricMeterReadingResp.getTipFees()));
            electricMeterReadingResp.setPeakFees(MathUtils.twoDecimal(electricMeterReadingResp.getPeakFees()));
            electricMeterReadingResp.setFlatFees(MathUtils.twoDecimal(electricMeterReadingResp.getFlatFees()));
            electricMeterReadingResp.setValleyFees(MathUtils.twoDecimal(electricMeterReadingResp.getValleyFees()));
        }
        return resp;
    }

    @ApiOperation(value = "各车间分时用电量统计")
    @RequestMapping(value = "/getSumQuantityByWorkshop", method = RequestMethod.POST)
    public EnergyResp getSumQuantityByWorkshop(@RequestBody @Valid CustElectricCountReq req, BindingResult result) {
        EnergyResp resp = new EnergyResp();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        ElectricMeterReadingReq electricMeterReadingReq = new ElectricMeterReadingReq();
        electricMeterReadingReq.setStartTime(req.getStartTime());
        electricMeterReadingReq.setEndTime(req.getEndTime());
        List<UnitResp> unitRespList = getWorkshopListByCustId(req.getCustId());
        if (null == unitRespList || unitRespList.size() <= 0) {
            resp.faile(StatusCode.C.getCode(), "该公司下未查到车间信息");
            return resp;
        }
        resp = getResultMap(unitRespList, electricMeterReadingReq, req.getDateType());
        if (resp.getCode().equals(StatusCode.SUCCESS.getCode())) {
            List<Map<String, Object>> mapList = (List<Map<String, Object>>) resp.getData();
            if (null != mapList) {
                for (Map<String, Object> map : mapList) {
                    if (null != map) {
                        ElectricMeterReadingResp electricMeterReadingResp = (ElectricMeterReadingResp) map.get("electricMeterReadingResp");
                        if (null != electricMeterReadingResp) {
                            electricMeterReadingResp.setSumQuantity(MathUtils.twoDecimal(electricMeterReadingResp.getSumQuantity()));
                            electricMeterReadingResp.setTipQuantity(MathUtils.twoDecimal(electricMeterReadingResp.getTipQuantity()));
                            electricMeterReadingResp.setPeakQuantity(MathUtils.twoDecimal(electricMeterReadingResp.getPeakQuantity()));
                            electricMeterReadingResp.setFlatQuantity(MathUtils.twoDecimal(electricMeterReadingResp.getFlatQuantity()));
                            electricMeterReadingResp.setValleyQuantity(MathUtils.twoDecimal(electricMeterReadingResp.getValleyQuantity()));
                            electricMeterReadingResp.setSumFees(MathUtils.twoDecimal(electricMeterReadingResp.getSumFees()));
                            electricMeterReadingResp.setTipFees(MathUtils.twoDecimal(electricMeterReadingResp.getTipFees()));
                            electricMeterReadingResp.setPeakFees(MathUtils.twoDecimal(electricMeterReadingResp.getPeakFees()));
                            electricMeterReadingResp.setFlatFees(MathUtils.twoDecimal(electricMeterReadingResp.getFlatFees()));
                            electricMeterReadingResp.setValleyFees(MathUtils.twoDecimal(electricMeterReadingResp.getValleyFees()));
                        }
                    }
                }
            }
        }
        return resp;
    }

    @ApiOperation(value = "用电量最大生产线与最小生产线负荷变化曲线")
    @RequestMapping(value = "/getSumQuantityByLine", method = RequestMethod.POST)
    public EnergyResp getSumQuantityByLine(@RequestBody @Valid CustElectricCountReq req, BindingResult result) {
        EnergyResp resp = new EnergyResp();
        List<CustElectricCountLineResp> resultList = new ArrayList<CustElectricCountLineResp>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        ElectricMeterReadingReq electricMeterReadingReq = new ElectricMeterReadingReq();
        electricMeterReadingReq.setStartTime(req.getStartTime());
        electricMeterReadingReq.setEndTime(req.getEndTime());
        switch (req.getDateType()) {
            //日统计
            case 1:
                EnergyResp<ElectricMeterReadingResp> electricMeterReadingRespEnergyResp = checkDateByDay(electricMeterReadingReq);
                if (!electricMeterReadingRespEnergyResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
                    return electricMeterReadingRespEnergyResp;
                }
                List<UnitResp> unitRespList = getLineListByCustId(req.getCustId());
                if (null == unitRespList || unitRespList.size() <= 0) {
                    resp.faile(StatusCode.C.getCode(), "该公司下未查到生产线信息");
                    return resp;
                }
                EnergyResp<Map<Long, ElectricMeterReadingResp>> lineMapEnergyResp = getResultMapKeyId(unitRespList, electricMeterReadingReq, req);
                if (null != lineMapEnergyResp && null != lineMapEnergyResp.getData()) {
                    Map<Long, ElectricMeterReadingResp> map = getMapMaxAndMin(lineMapEnergyResp.getData());
                    Iterator keys = map.keySet().iterator();
                    while (keys.hasNext()) {
                        Long key = (Long) keys.next();
                        List<Equip> equipList = getEquipListByAccountingUnitId(key);
                        if (null != equipList && equipList.size() > 0) {
                            SamplDataStaReq samplDataStaReq = new SamplDataStaReq();
                            samplDataStaReq.setStart(req.getStartTime());
                            samplDataStaReq.setEnd(req.getEndTime());
                            samplDataStaReq.setDownsample("1m-first-nan");
                            samplDataStaReq.setEquips(equipList);
                            samplDataStaReq.setMetric(Constant.ELECTRIC_METRIC_EMS);
                            logger.info("请求大数据平台-天-(有功功率)：参数：{}", getJsonString(samplDataStaReq));
                            EnergyResp<ListResp<RmiSamplDataResp>> rmiSamplDataRespListRespEnergyResp = opentsdbService.getSamplDataStaReq(samplDataStaReq);
                            logger.info("请求大数据平台-天-(有功功率)：结果：{}", getJsonString(rmiSamplDataRespListRespEnergyResp));
                            unitRespList = getLineListByCustId(req.getCustId());
                            for (UnitResp unitResp : unitRespList) {
                                if (key.equals(unitResp.getId())) {
                                    CustElectricCountLineResp custElectricCountLineResp = new CustElectricCountLineResp();
                                    custElectricCountLineResp.setName(unitResp.getName());
                                    custElectricCountLineResp.setList(getRmiSamplDataRespListResp(rmiSamplDataRespListRespEnergyResp.getData(), req.getCustId()));
                                    resultList.add(custElectricCountLineResp);
                                }
                            }
                        }
                    }
                }
                resp.ok(resultList);
                break;
            //月统计
            case 2:
                electricMeterReadingRespEnergyResp = checkDateByMonth(electricMeterReadingReq);
                if (!electricMeterReadingRespEnergyResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
                    return electricMeterReadingRespEnergyResp;
                }
                unitRespList = getWorkshopListByCustId(req.getCustId());
                if (null == unitRespList || unitRespList.size() <= 0) {
                    resp.faile(StatusCode.C.getCode(), "该公司下未查到车间信息");
                    return resp;
                }
                EnergyResp<Map<Long, ElectricMeterReadingDayPo>> workshopMapEnergyResp = getElectricResultMapKeyId(unitRespList, electricMeterReadingReq, req);
                if (null != workshopMapEnergyResp && null != workshopMapEnergyResp.getData()) {
                    Map<Long, ElectricMeterReadingDayPo> map = getMapMaxAndMinByMap(workshopMapEnergyResp.getData());
                    Iterator keys = map.keySet().iterator();
                    while (keys.hasNext()) {
                        Long key = (Long) keys.next();
                        List<String> equipNameList = getMeterListByAccountingUnitId(key);
                        if (null != equipNameList && equipNameList.size() > 0) {
                            electricMeterReadingReq.setMeterNoList(equipNameList);
                            EnergyResp<List<StatisticsDataResp>> statisticsDataRespListEnergyResp = countService.getElecSumByDay(electricMeterReadingReq);
                            if (null != statisticsDataRespListEnergyResp.getData() && statisticsDataRespListEnergyResp.getData().size() >= 0) {
                                for (UnitResp unitResp : unitRespList) {
                                    if (key.equals(unitResp.getId())) {
                                        CustElectricCountLineResp custElectricCountLineResp = new CustElectricCountLineResp();
                                        custElectricCountLineResp.setName(unitResp.getName());
                                        custElectricCountLineResp.setList(getStatisticsDataRespListResp(statisticsDataRespListEnergyResp.getData(), req.getCustId()));
                                        resultList.add(custElectricCountLineResp);
                                    }
                                }
                            }
                        }
                    }
                }
                resp.ok(resultList);
                break;
            //年统计
            case 3:
                electricMeterReadingRespEnergyResp = checkDateByYear(electricMeterReadingReq);
                if (!electricMeterReadingRespEnergyResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
                    return electricMeterReadingRespEnergyResp;
                }
                unitRespList = getWorkshopListByCustId(req.getCustId());
                if (null == unitRespList || unitRespList.size() <= 0) {
                    resp.faile(StatusCode.C.getCode(), "该公司下未查到车间信息");
                    return resp;
                }

                workshopMapEnergyResp = getElectricResultMapKeyId(unitRespList, electricMeterReadingReq, req);
                if (null != workshopMapEnergyResp && null != workshopMapEnergyResp.getData()) {
                    Map<Long, ElectricMeterReadingDayPo> map = getMapMaxAndMinByMap(workshopMapEnergyResp.getData());
                    Iterator keys = map.keySet().iterator();
                    while (keys.hasNext()) {
                        Long key = (Long) keys.next();
                        List<String> equipNameList = getMeterListByAccountingUnitId(key);
                        if (null != equipNameList && equipNameList.size() > 0) {
                            electricMeterReadingReq.setMeterNoList(equipNameList);
                            EnergyResp<List<StatisticsDataResp>> statisticsDataRespListEnergyResp = countService.getElecSumByMonth(electricMeterReadingReq);
                            if (null != statisticsDataRespListEnergyResp.getData() && statisticsDataRespListEnergyResp.getData().size() >= 0) {
                                for (UnitResp unitResp : unitRespList) {
                                    if (key.equals(unitResp.getId())) {
                                        CustElectricCountLineResp custElectricCountLineResp = new CustElectricCountLineResp();
                                        custElectricCountLineResp.setName(unitResp.getName());
                                        custElectricCountLineResp.setList(getStatisticsDataRespListResp(statisticsDataRespListEnergyResp.getData(), req.getCustId()));
                                        resultList.add(custElectricCountLineResp);
                                    }
                                }
                            }
                        }
                    }
                }
                resp.ok(resultList);
                break;
            default:
                resp.faile(StatusCode.C.getCode(), "时间维度(dateType)参数错误");
                break;
        }
        return resp;
    }

    /**
     * 处理两位小数
     *
     * @param statisticsDataRespList
     * @return
     */
    private ListResp<RmiSamplDataResp> getStatisticsDataRespListResp(List<StatisticsDataResp> statisticsDataRespList, Long custId) {
        ListResp<RmiSamplDataResp> statisticsDataRespListResp = new ListResp<RmiSamplDataResp>();
        List<RmiSamplDataResp> rmiSamplDataRespList = new ArrayList<RmiSamplDataResp>();
        if (null != statisticsDataRespList && statisticsDataRespList.size() >= 0) {
            RmiSamplDataResp rmiSamplDataResp = new RmiSamplDataResp();
            List<DataResp> dataRespList = new ArrayList<DataResp>();
            for (StatisticsDataResp statisticsDataResp : statisticsDataRespList) {
                if (null != statisticsDataResp) {
                    DataResp dataResp = new DataResp();
                    dataResp.setTime(statisticsDataResp.getReadTime());
                    //龙辉电镀分公司低压侧表数据需要除以1000
                    if (custId.equals(2L)) {
                        dataResp.setValue(MathUtils.twoDecimal(MathUtils.divide(statisticsDataResp.getSumQuantity(), new BigDecimal(1000))));
                    } else {
                        dataResp.setValue(MathUtils.twoDecimal(statisticsDataResp.getSumQuantity()));
                    }
                    dataRespList.add(dataResp);
                }
            }
            rmiSamplDataResp.setDataResp(dataRespList);
            rmiSamplDataRespList.add(rmiSamplDataResp);
        }
        statisticsDataRespListResp.setList(rmiSamplDataRespList);
        return statisticsDataRespListResp;
    }

    /**
     * 处理两位小数
     *
     * @param rmiSamplDataRespListResp
     * @return
     */
    private ListResp<RmiSamplDataResp> getRmiSamplDataRespListResp(ListResp<RmiSamplDataResp> rmiSamplDataRespListResp, Long custId) {
        if (null != rmiSamplDataRespListResp && rmiSamplDataRespListResp.getList().size() >= 0) {
            for (RmiSamplDataResp rmiSamplDataResp : rmiSamplDataRespListResp.getList()) {
                if (null != rmiSamplDataResp) {
                    List<DataResp> dataRespList = rmiSamplDataResp.getDataResp();
                    if (null != dataRespList && dataRespList.size() >= 0) {
                        for (DataResp dataResp : dataRespList) {
                            if (null != dataResp) {
                                //龙辉电镀分公司低压侧表数据需要除以1000
                                if (custId.equals(2L)) {
                                    dataResp.setValue(MathUtils.divide(dataResp.getValue(), new BigDecimal(1000)));
                                }
                                dataResp.setValue(MathUtils.twoDecimal(dataResp.getValue()));
                            }
                        }
                    }
                }
            }
        }
        return rmiSamplDataRespListResp;
    }

    /**
     * 计算企业用电
     *
     * @param req
     * @param excelFlag
     * @return
     */
    private EnergyResp getCustElectricCountDetailListResp(CustElectricCountReq req, boolean excelFlag) {
        EnergyResp<List<CustElectricCountDetail>> resp = new EnergyResp<List<CustElectricCountDetail>>();
        List<CustElectricCountDetail> custElectricCountDetailList = new ArrayList<CustElectricCountDetail>();
        //计算企业总用电
        List<String> meterNoList = getMeterListByCustId(req.getCustId());
        if (null == meterNoList || meterNoList.size() <= 0) {
            resp.faile(StatusCode.C.getCode(), "该公司下未查到电表信息");
            return resp;
        }
        ElectricMeterReadingReq electricMeterReadingReq = new ElectricMeterReadingReq();
        electricMeterReadingReq.setStartTime(req.getStartTime());
        electricMeterReadingReq.setEndTime(req.getEndTime());
        electricMeterReadingReq.setMeterNoList(meterNoList);
        EnergyResp<ElectricMeterReadingResp> electricMeterReadingRespEnergyRespNew = getSum(electricMeterReadingReq, req.getDateType());
        if (!electricMeterReadingRespEnergyRespNew.getCode().equals(StatusCode.SUCCESS.getCode())) {
            return electricMeterReadingRespEnergyRespNew;
        }
        CustElectricCountDetail detail = CustElectricCountDetail.trans(electricMeterReadingRespEnergyRespNew.getData());
        EnergyResp<ElectricMeterReadingReq> getDealDateResp = getDealDate(electricMeterReadingReq, req);
        if (!getDealDateResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
            return getDealDateResp;
        }
        EnergyResp<ElectricMeterReadingResp> electricMeterReadingRespEnergyRespOld = getSum(getDealDateResp.getData(), req.getDateType());
        if (!electricMeterReadingRespEnergyRespOld.getCode().equals(StatusCode.SUCCESS.getCode())) {
            return electricMeterReadingRespEnergyRespOld;
        }
        ElectricMeterReadingResp old = electricMeterReadingRespEnergyRespOld.getData();
        detail.setAccountingUnitName("企业总用电");
        if (!excelFlag) {
            if (detail.getSumFees().compareTo(old.getSumFees()) >= 0) {
                detail.setFeesType(FeesTypeEnum.UP.getCode());
            } else {
                detail.setFeesType(FeesTypeEnum.DOWN.getCode());
            }
        }
        custElectricCountDetailList.add(detail);

        //计算车间用电
        List<UnitResp> unitRespList = getWorkshopListByCustId(req.getCustId());
        EnergyResp<List<CustElectricCountDetail>> custElectricCountDetailListEnergyResp = getSumForWorkshopAndLine(unitRespList, electricMeterReadingReq, req, excelFlag);
        List<CustElectricCountDetail> getDealDate = custElectricCountDetailListEnergyResp.getData();
        custElectricCountDetailList.addAll(getDealDate);
        //计算生产线用电
        unitRespList = getLineListByCustId(req.getCustId());
        custElectricCountDetailListEnergyResp = getSumForWorkshopAndLine(unitRespList, electricMeterReadingReq, req, excelFlag);
        getDealDate = custElectricCountDetailListEnergyResp.getData();
        custElectricCountDetailList.addAll(getDealDate);
        if (null != custElectricCountDetailList && custElectricCountDetailList.size() >= 0) {
            for (CustElectricCountDetail countDetail : custElectricCountDetailList) {
                countDetail.setSumQuantity(MathUtils.twoDecimal(countDetail.getSumQuantity()));
                countDetail.setSumFees(MathUtils.twoDecimal(countDetail.getSumFees()));
                countDetail.setSumTipFees(MathUtils.twoDecimal(countDetail.getSumTipFees()));
                countDetail.setSumPeakFees(MathUtils.twoDecimal(countDetail.getSumPeakFees()));
                countDetail.setSumFlatFees(MathUtils.twoDecimal(countDetail.getSumFlatFees()));
                countDetail.setSumValleyFees(MathUtils.twoDecimal(countDetail.getSumValleyFees()));
            }
        }
        resp.ok(custElectricCountDetailList);
        return resp;
    }

    @ApiOperation(value = "企业用电详情")
    @RequestMapping(value = "/getDetail", method = RequestMethod.POST)
    public EnergyResp getDetail(@RequestBody @Valid CustElectricCountReq req, BindingResult result) {
        EnergyResp<List<CustElectricCountDetail>> resp = new EnergyResp<List<CustElectricCountDetail>>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        return getCustElectricCountDetailListResp(req, false);
    }

    /**
     * 处理日期
     *
     * @param req
     * @return
     */
    private EnergyResp<ElectricMeterReadingReq> getDealDate(ElectricMeterReadingReq electricMeterReadingReq, CustElectricCountReq req) {
        EnergyResp<ElectricMeterReadingReq> resp = new EnergyResp<ElectricMeterReadingReq>();
        ElectricMeterReadingReq meterReadingReq = new ElectricMeterReadingReq();
        switch (req.getDateType()) {
            case 1:
                meterReadingReq.setStartTime(DateUtil.getLastDay(req.getStartTime()));
                meterReadingReq.setEndTime(DateUtil.getLastDay(req.getEndTime()));
                break;
            case 2:
                meterReadingReq.setStartTime(DateUtil.getLastMonthForDay(req.getStartTime() + " 00:00:00"));
                meterReadingReq.setEndTime(DateUtil.getLastMonthForDay(req.getEndTime() + " 00:00:00"));
                break;
            case 3:
                meterReadingReq.setStartTime(DateUtil.getLastYearForYear(req.getStartTime() + "-01 00:00:00"));
                meterReadingReq.setEndTime(DateUtil.getLastYearForYear(req.getEndTime() + "-01 00:00:00"));
                break;
            default:
                resp.faile(StatusCode.C.getCode(), "时间维度(dateType)参数错误");
                return resp;
        }
        meterReadingReq.setMeterNoList(electricMeterReadingReq.getMeterNoList());
        resp.ok(meterReadingReq);
        return resp;
    }

    /**
     * 计算车间用电和生产线用电
     *
     * @param unitRespList
     * @param electricMeterReadingReq
     * @param req
     * @param excelFlag
     * @return
     */
    private EnergyResp getSumForWorkshopAndLine(List<UnitResp> unitRespList, ElectricMeterReadingReq electricMeterReadingReq, CustElectricCountReq req, boolean excelFlag) {
        EnergyResp<List<CustElectricCountDetail>> resp = new EnergyResp<List<CustElectricCountDetail>>();
        List<CustElectricCountDetail> custElectricCountDetailList = new ArrayList<CustElectricCountDetail>();
        if (null != unitRespList && unitRespList.size() > 0) {
            EnergyResp<Map<Long, ElectricMeterReadingResp>> electricMeterReadingRespMapEnergyRespNew = getResultMapKeyId(unitRespList, electricMeterReadingReq, req);
            Map<Long, ElectricMeterReadingResp> electricMeterReadingRespMapNew = electricMeterReadingRespMapEnergyRespNew.getData();
            if (null != electricMeterReadingRespMapNew) {
                EnergyResp<ElectricMeterReadingReq> getDealDateResp = getDealDate(electricMeterReadingReq, req);
                if (!getDealDateResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
                    return getDealDateResp;
                }
                EnergyResp<Map<Long, ElectricMeterReadingResp>> electricMeterReadingRespMapEnergyRespOld = new EnergyResp<Map<Long, ElectricMeterReadingResp>>();
                if (!excelFlag) {
                    electricMeterReadingRespMapEnergyRespOld = getResultMapKeyId(unitRespList, getDealDateResp.getData(), req);
                }
                Map<Long, ElectricMeterReadingResp> electricMeterReadingRespMapOld = electricMeterReadingRespMapEnergyRespOld.getData();
                Iterator keysNew = electricMeterReadingRespMapNew.keySet().iterator();
                while (keysNew.hasNext()) {
                    Long keyNew = (Long) keysNew.next();
                    BigDecimal valueNew = new BigDecimal(0);
                    if (null != electricMeterReadingRespMapNew && null != electricMeterReadingRespMapNew.get(keyNew)) {
                        valueNew = electricMeterReadingRespMapNew.get(keyNew).getSumFees();
                    }
                    BigDecimal valueOld = new BigDecimal(0);
                    if (null != electricMeterReadingRespMapOld && null != electricMeterReadingRespMapOld.get(keyNew)) {
                        valueOld = electricMeterReadingRespMapOld.get(keyNew).getSumFees();
                    }
                    CustElectricCountDetail detail = CustElectricCountDetail.trans(electricMeterReadingRespMapNew.get(keyNew));
                    String accountingUnitName = getAccountingUnitNameByAccountingUnitId(keyNew, unitRespList);
                    detail.setAccountingUnitName(accountingUnitName);
                    if (valueNew.compareTo(valueOld) >= 0) {
                        detail.setFeesType(FeesTypeEnum.UP.getCode());
                    } else {
                        detail.setFeesType(FeesTypeEnum.DOWN.getCode());
                    }
                    custElectricCountDetailList.add(detail);
                }
            }
        }
        resp.ok(custElectricCountDetailList);
        return resp;
    }

    @ApiOperation(value = "企业用电详情导出")
    @RequestMapping(value = "/getDetailExcel", method = RequestMethod.POST)
    public EnergyResp getDetailExcel(@RequestBody @Valid CustElectricCountReq req, BindingResult result) {
        EnergyResp resp = new EnergyResp();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        logger.info("导出文件：参数：{}", getJsonString(req));
        resp = getCustElectricCountDetailListResp(req, true);
        logger.info("导出文件：结果：{}", getJsonString(resp));
        if (resp.getCode().equals(StatusCode.SUCCESS.getCode())) {
            List<CustElectricCountDetail> custElectricCountDetailList = (List<CustElectricCountDetail>) resp.getData();
            if (null == custElectricCountDetailList || custElectricCountDetailList.size() <= 0) {
                resp.faile(StatusCode.ERROR.getCode(), "未查到企业用电详情");
                return resp;
            }
            //导出Excel
            String[] headers = {"核算单元", "总用电量(kWh)", "总电费(元)", "尖电费(元)", "峰电费(元)", "平电费(元)", "谷电费(元)"};
            String exportExcelName = "用电报表--企业用电";
            List<LinkedHashMap<String, String>> rows = new ArrayList<>();
            for (CustElectricCountDetail custElectricCountDetail : custElectricCountDetailList) {
                if (null != custElectricCountDetail) {
                    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                    map.put("1", custElectricCountDetail.getAccountingUnitName());
                    map.put("2", custElectricCountDetail.getSumQuantity().toString());
                    map.put("3", custElectricCountDetail.getSumFees().toString());
                    map.put("4", custElectricCountDetail.getSumTipFees().toString());
                    map.put("5", custElectricCountDetail.getSumPeakFees().toString());
                    map.put("6", custElectricCountDetail.getSumFlatFees().toString());
                    map.put("7", custElectricCountDetail.getSumValleyFees().toString());
                    rows.add(map);
                }
            }
            ResponseEntity<byte[]> responseEntity = ExcelUtils.exportExcel(headers, rows, exportExcelName, ExcelUtils.ExcelSuffix.xls);
            Map map = new HashMap() {{
                put("obj", responseEntity.getBody());
            }};
            logger.info("将导出文件上传文件服务器");
            UploadResp uploadResp = uploadService.upObj(map, ExcelUtils.ExcelSuffix.xls.toString());
            if (uploadResp.getRetCode() == 0) {
                resp.ok(baseFileUrl + uploadResp.getPath() + "?filename=" + exportExcelName + ".xls");
            } else {
                logger.error("上传文件服务器异常");
            }
        }
        return resp;
    }

    /**
     * 根据核算单元ID查询核算单元名称
     *
     * @param accountingUnitId
     * @param unitRespList
     * @return
     */
    private String getAccountingUnitNameByAccountingUnitId(Long accountingUnitId, List<UnitResp> unitRespList) {
        for (UnitResp unitResp : unitRespList) {
            if (accountingUnitId.equals(unitResp.getId())) {
                return unitResp.getName();
            }
        }
        return accountingUnitId.toString();
    }

    /**
     * 取出集合中value最大值和最小值
     *
     * @param map
     * @return
     */
    private Map<Long, ElectricMeterReadingResp> getMapMaxAndMin(Map<Long, ElectricMeterReadingResp> map) {
        Map<Long, ElectricMeterReadingResp> resultMap = new HashMap<Long, ElectricMeterReadingResp>();
        List<Map.Entry<Long, ElectricMeterReadingResp>> list = new ArrayList<Map.Entry<Long, ElectricMeterReadingResp>>(map.entrySet());

        //保留大于10的数据
        List<Map.Entry<Long, ElectricMeterReadingResp>> resultList = new ArrayList<Map.Entry<Long, ElectricMeterReadingResp>>();
        logger.info("处理集合中最大值和最小值数据：{}", getJsonString(resultList));
        for (Map.Entry<Long, ElectricMeterReadingResp> electricMeterReadingRespMap : list) {
            if (electricMeterReadingRespMap.getValue().sumQuantity().compareTo(new BigDecimal(10)) > 0) {
                resultList.add(electricMeterReadingRespMap);
            }
        }
        if (null != resultList && resultList.size() > 0) {
            Collections.sort(resultList, new Comparator<Map.Entry<Long, ElectricMeterReadingResp>>() {
                @Override
                public int compare(Map.Entry<Long, ElectricMeterReadingResp> o1, Map.Entry<Long, ElectricMeterReadingResp> o2) {
                    return (o1.getValue().getSumQuantity().compareTo(o2.getValue().getSumQuantity()));
                }
            });
            resultMap.put(resultList.get(0).getKey(), resultList.get(0).getValue());
            if (resultList.size() > 1) {
                resultMap.put(resultList.get(resultList.size() - 1).getKey(), resultList.get(resultList.size() - 1).getValue());
            }
        }
        return resultMap;
    }

    /**
     * 取出集合中value最大值和最小值
     *
     * @param map
     * @return
     */
    private Map<Long, ElectricMeterReadingDayPo> getMapMaxAndMinByMap(Map<Long, ElectricMeterReadingDayPo> map) {
        Map<Long, ElectricMeterReadingDayPo> resultMap = new HashMap<Long, ElectricMeterReadingDayPo>();
        List<Map.Entry<Long, ElectricMeterReadingDayPo>> list = new ArrayList<Map.Entry<Long, ElectricMeterReadingDayPo>>(map.entrySet());

        //保留大于10的数据
        List<Map.Entry<Long, ElectricMeterReadingDayPo>> resultList = new ArrayList<Map.Entry<Long, ElectricMeterReadingDayPo>>();
        for (Map.Entry<Long, ElectricMeterReadingDayPo> electricMeterReadingRespMap : list) {
            if (electricMeterReadingRespMap.getValue().getUseQuantity().compareTo(new BigDecimal(10)) > 0) {
                resultList.add(electricMeterReadingRespMap);
            }
        }
        if (null != resultList && resultList.size() > 0) {
            Collections.sort(resultList, new Comparator<Map.Entry<Long, ElectricMeterReadingDayPo>>() {
                @Override
                public int compare(Map.Entry<Long, ElectricMeterReadingDayPo> o1, Map.Entry<Long, ElectricMeterReadingDayPo> o2) {
                    return (o1.getValue().getUseQuantity().compareTo(o2.getValue().getUseQuantity()));
                }
            });
            resultMap.put(resultList.get(0).getKey(), resultList.get(0).getValue());
            if (resultList.size() > 1) {
                resultMap.put(resultList.get(resultList.size() - 1).getKey(), resultList.get(resultList.size() - 1).getValue());
            }
        }
        logger.info("处理集合中最大值和最小值数据：{}", getJsonString(resultList));
        return resultMap;
    }

    /**
     * 处理核算单元汇总
     *
     * @param unitRespList
     * @param electricMeterReadingReq
     * @param dateType
     * @return
     */
    private EnergyResp getResultMap(List<UnitResp> unitRespList, ElectricMeterReadingReq electricMeterReadingReq, Integer dateType) {
        EnergyResp<List<Map<String, Object>>> resp = new EnergyResp<List<Map<String, Object>>>();
        List<Map<String, Object>> respMapList = new ArrayList<Map<String, Object>>();
        for (UnitResp unitResp : unitRespList) {
            List<String> meterNoList = getMeterListByAccountingUnitId(unitResp.getId());
            if (null != meterNoList && meterNoList.size() > 0) {
                electricMeterReadingReq.setMeterNoList(meterNoList);
                EnergyResp<ElectricMeterReadingResp> electricMeterReadingRespEnergyResp = getSum(electricMeterReadingReq, dateType);
                if (!electricMeterReadingRespEnergyResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
                    return electricMeterReadingRespEnergyResp;
                }
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("name", unitResp.getName());
                resultMap.put("electricMeterReadingResp", electricMeterReadingRespEnergyResp.getData());
                respMapList.add(resultMap);
            }
        }
        resp.ok(respMapList);
        return resp;
    }

    /**
     * 处理核算单元用电量汇总
     *
     * @param unitRespList
     * @param electricMeterReadingReq
     * @param req
     * @return
     */
    private EnergyResp getElectricResultMapKeyId(List<UnitResp> unitRespList, ElectricMeterReadingReq electricMeterReadingReq, CustElectricCountReq req) {
        EnergyResp<Map<Long, ElectricMeterReadingDayPo>> resp = new EnergyResp<Map<Long, ElectricMeterReadingDayPo>>();
        Map<Long, ElectricMeterReadingDayPo> resultMap = new HashMap<Long, ElectricMeterReadingDayPo>();
        for (UnitResp unitResp : unitRespList) {
            List<String> meterNoList = getMeterListByAccountingUnitId(unitResp.getId());
            if (null != meterNoList && meterNoList.size() > 0) {
                electricMeterReadingReq.setMeterNoList(meterNoList);
                EnergyResp<ElectricMeterReadingDayPo> electricMeterReadingDayPoEnergyResp = new EnergyResp<ElectricMeterReadingDayPo>();
                switch (req.getDateType()) {
                    case 1:
                        break;
                    case 2:
                        electricMeterReadingDayPoEnergyResp = countService.getElecSumByD(electricMeterReadingReq);
                        break;
                    case 3:
                        electricMeterReadingDayPoEnergyResp = countService.getElecSumByM(electricMeterReadingReq);
                }
                if (!electricMeterReadingDayPoEnergyResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
                    return electricMeterReadingDayPoEnergyResp;
                }
                if (null != electricMeterReadingDayPoEnergyResp.getData()) {
                    resultMap.put(unitResp.getId(), electricMeterReadingDayPoEnergyResp.getData());
                }
            }
        }
        resp.ok(resultMap);
        return resp;
    }

    /**
     * 处理核算单元汇总
     *
     * @param unitRespList
     * @param electricMeterReadingReq
     * @param req
     * @return
     */
    private EnergyResp getResultMapKeyId(List<UnitResp> unitRespList, ElectricMeterReadingReq electricMeterReadingReq, CustElectricCountReq req) {
        EnergyResp<Map<Long, ElectricMeterReadingResp>> resp = new EnergyResp<Map<Long, ElectricMeterReadingResp>>();
        Map<Long, ElectricMeterReadingResp> resultMap = new HashMap<Long, ElectricMeterReadingResp>();
        for (UnitResp unitResp : unitRespList) {
            List<String> meterNoList = getMeterListByAccountingUnitId(unitResp.getId());
            if (null != meterNoList && meterNoList.size() > 0) {
                electricMeterReadingReq.setMeterNoList(meterNoList);
                EnergyResp<ElectricMeterReadingResp> electricMeterReadingRespEnergyResp = getSum(electricMeterReadingReq, req.getDateType());
                if (!electricMeterReadingRespEnergyResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
                    return electricMeterReadingRespEnergyResp;
                }
                resultMap.put(unitResp.getId(), electricMeterReadingRespEnergyResp.getData());
            }
        }
        resp.ok(resultMap);
        return resp;
    }

    /**
     * 计算日/月/年统计数据
     *
     * @param req
     * @return
     */
    private EnergyResp<ElectricMeterReadingResp> getSum(ElectricMeterReadingReq req, Integer dateType) {
        logger.info("请求数据汇总，参数：{}", getJsonString(req));
        EnergyResp<ElectricMeterReadingResp> result = new EnergyResp<ElectricMeterReadingResp>();
        if (req.getStartTime().compareTo(req.getEndTime()) > 0) {
            result.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return result;
        }
        switch (dateType) {
            //日统计
            case 1:
                result = checkDateTypeByDay(req);
                break;
            //月统计
            case 2:
                result = checkDateTypeByMonth(req);
                break;
            //年统计
            case 3:
                result = checkDateTypeByYear(req);
                break;
            default:
                result.faile(StatusCode.C.getCode(), "时间维度(dateType)参数错误");
                break;
        }
        logger.info("请求数据汇总，结果：{}", getJsonString(result));
        return result;
    }

    /**
     * 日统计数据
     *
     * @param electricMeterReadingReq
     * @return
     */
    private EnergyResp<ElectricMeterReadingResp> checkDateTypeByDay(ElectricMeterReadingReq electricMeterReadingReq) {
        if (!checkDateByDay(electricMeterReadingReq).getCode().equals(StatusCode.SUCCESS.getCode())) {
            return checkDateByDay(electricMeterReadingReq);
        }
        return countService.getSumByMinute(electricMeterReadingReq);
    }

    /**
     * 验证日期
     *
     * @param req
     * @return
     */
    private EnergyResp<ElectricMeterReadingResp> checkDateByDay(ElectricMeterReadingReq req) {
        EnergyResp<ElectricMeterReadingResp> result = new EnergyResp<ElectricMeterReadingResp>();
        if (!DateUtil.isValidTime(req.getStartTime()) || !DateUtil.isValidTime(req.getEndTime())) {
            result.faile(StatusCode.C.getCode(), "日统计时间格式不正确");
            return result;
        }
        //时间跨度不能超过24小时
        if (DateUtil.addHour(req.getStartTime(), Calendar.HOUR_OF_DAY, day).compareTo(req.getEndTime()) < 0) {
            result.faile(StatusCode.C.getCode(), "查询日统计，时间跨度不能大于24小时");
            return result;
        }
        result.ok(null);
        return result;
    }

    /**
     * 月统计数据
     *
     * @param electricMeterReadingReq
     * @return
     */
    private EnergyResp<ElectricMeterReadingResp> checkDateTypeByMonth(ElectricMeterReadingReq electricMeterReadingReq) {
        if (!checkDateByMonth(electricMeterReadingReq).getCode().equals(StatusCode.SUCCESS.getCode())) {
            return checkDateByMonth(electricMeterReadingReq);
        }
        return countService.getSumByDay(electricMeterReadingReq);
    }

    /**
     * 验证日期
     *
     * @param req
     * @return
     */
    private EnergyResp<ElectricMeterReadingResp> checkDateByMonth(ElectricMeterReadingReq req) {
        EnergyResp<ElectricMeterReadingResp> result = new EnergyResp<ElectricMeterReadingResp>();
        if (!DateUtil.isValidDate(req.getStartTime()) || !DateUtil.isValidDate(req.getEndTime())) {
            result.faile(StatusCode.C.getCode(), "月统计时间格式不正确");
            return result;
        }
        //时间跨度不能超过3个月
        if (DateUtil.getNextMonth(req.getStartTime() + " 00:00:00", month).compareTo(req.getEndTime()) < 0) {
            result.faile(StatusCode.C.getCode(), "查询月统计，时间跨度不能大于3个月");
            return result;
        }
        result.ok(null);
        return result;
    }

    /**
     * 年统计数据
     *
     * @param electricMeterReadingReq
     * @return
     */
    private EnergyResp<ElectricMeterReadingResp> checkDateTypeByYear(ElectricMeterReadingReq electricMeterReadingReq) {
        if (!checkDateByYear(electricMeterReadingReq).getCode().equals(StatusCode.SUCCESS.getCode())) {
            return checkDateByYear(electricMeterReadingReq);
        }
        return countService.getSumByMonth(electricMeterReadingReq);
    }

    /**
     * 验证日期
     *
     * @param req
     * @return
     */
    private EnergyResp<ElectricMeterReadingResp> checkDateByYear(ElectricMeterReadingReq req) {
        EnergyResp<ElectricMeterReadingResp> result = new EnergyResp<ElectricMeterReadingResp>();
        if (!DateUtil.yearMonth(req.getStartTime()) || !DateUtil.yearMonth(req.getEndTime())) {
            result.faile(StatusCode.C.getCode(), "年统计时间格式不正确");
            return result;
        }
        //时间跨度不能超过24个月
        if (DateUtil.getNextMonth(req.getStartTime() + "-01 00:00:00", year).compareTo(req.getEndTime()) < 0) {
            result.faile(StatusCode.C.getCode(), "查询年统计，时间跨度不能大于24个月");
            return result;
        }
        result.ok(null);
        return result;
    }

    /**
     * 根据企业ID查询电表数据
     *
     * @param custId
     * @return
     */
    private List<String> getMeterListByCustId(Long custId) {
        DefaultReq de = new DefaultReq();
        de.setId(custId);
        EnergyResp<ListResp<AccountingUnit>> accountingUnitListRespEnergyResp = accountUnitService.getParentEqu(de);
        if (null == accountingUnitListRespEnergyResp
                || null == accountingUnitListRespEnergyResp.getData()
                || null == accountingUnitListRespEnergyResp.getData().getList()
                || accountingUnitListRespEnergyResp.getData().getList().size() <= 0) {
            return null;
        }
        ListResp<AccountingUnit> accountingUnitListResp = accountingUnitListRespEnergyResp.getData();
        List<AccountingUnit> accountingUnitList = accountingUnitListResp.getList();
        logger.info("请求核算单元数据，参数：{}，结果：{}", custId, getJsonString(accountingUnitList));
        List<String> meterNoList = new ArrayList<String>();
        for (AccountingUnit accountingUnit : accountingUnitList) {
            DefaultReq defaultReq = new DefaultReq();
            defaultReq.setId(accountingUnit.getId());
            EnergyResp<List<MeterResp>> meterRespListEnergyResp = accountUnitService.queryMeterListByAccount(defaultReq);
            logger.info("请求计量表设备，参数：{}，结果：{}", accountingUnit.getId(), getJsonString(meterRespListEnergyResp));
            if (null != meterRespListEnergyResp && null != meterRespListEnergyResp.getData() && meterRespListEnergyResp.getData().size() > 0) {
                for (MeterResp meterResp : meterRespListEnergyResp.getData()) {
                    if (meterResp.getEnergyType().equals(EnergyTypeEnum.ENERGY_ELECTRICITY.getValue())) {
                        meterNoList.add(meterResp.getLoopNumber());
                    }
                }
            }
        }
        return meterNoList;
    }

    /**
     * 根据企业ID查询车间数据
     *
     * @param custId
     * @return
     */
    private List<UnitResp> getWorkshopListByCustId(Long custId) {
        AccountUnitReq accountUnitReq = new AccountUnitReq();
        accountUnitReq.setId(custId);
        accountUnitReq.setAccountingType(Constant.ACCOUNTING_TYPE_02);
        accountUnitReq.setIsAccount(false);
        EnergyResp<List<UnitResp>> unitRespListEnergyResp = accountUnitService.queryAccountList(accountUnitReq);
        if (null == unitRespListEnergyResp
                || null == unitRespListEnergyResp.getData()
                || unitRespListEnergyResp.getData().size() <= 0) {
            return null;
        }
        logger.info("请求车间数据，参数：{}，结果：{}", custId, getJsonString(unitRespListEnergyResp));
        return unitRespListEnergyResp.getData();
    }

    /**
     * 根据企业ID查询生产线数据
     *
     * @param custId
     * @return
     */
    private List<UnitResp> getLineListByCustId(Long custId) {
        AccountUnitReq accountUnitReq = new AccountUnitReq();
        accountUnitReq.setId(custId);
        accountUnitReq.setAccountingType(Constant.ACCOUNTING_TYPE_03);
        accountUnitReq.setIsAccount(false);
        EnergyResp<List<UnitResp>> unitRespListEnergyResp = accountUnitService.queryAccountList(accountUnitReq);
        if (null == unitRespListEnergyResp
                || null == unitRespListEnergyResp.getData()
                || unitRespListEnergyResp.getData().size() <= 0) {
            return null;
        }
        logger.info("请求生产线数据，参数：{}，结果：{}", custId, getJsonString(unitRespListEnergyResp));
        return unitRespListEnergyResp.getData();
    }

    /**
     * 根据核算单元ID查询电表数据
     *
     * @param accountingUnitId
     * @return
     */
    private List<String> getMeterListByAccountingUnitId(Long accountingUnitId) {
        DefaultReq de = new DefaultReq();
        de.setId(accountingUnitId);
        EnergyResp<List<MeterResp>> meterRespListEnergyResp = accountUnitService.queryMeterListByAccount(de);
        if (null == meterRespListEnergyResp
                || null == meterRespListEnergyResp.getData()
                || meterRespListEnergyResp.getData().size() <= 0) {
            return null;
        }
        List<String> meterNoList = new ArrayList<String>();
        logger.info("请求计量表设备，参数：{}，结果：{}", accountingUnitId, getJsonString(meterRespListEnergyResp));
        for (MeterResp meterResp : meterRespListEnergyResp.getData()) {
            if (meterResp.getEnergyType().equals(EnergyTypeEnum.ENERGY_ELECTRICITY.getValue())) {
                meterNoList.add(meterResp.getLoopNumber());
            }
        }
        return meterNoList;
    }

    /**
     * 根据核算单元ID查询电表数据
     *
     * @param accountingUnitId
     * @return
     */
    private List<Equip> getEquipListByAccountingUnitId(Long accountingUnitId) {
        DefaultReq de = new DefaultReq();
        de.setId(accountingUnitId);
        EnergyResp<List<MeterResp>> meterRespListEnergyResp = accountUnitService.queryMeterListByAccount(de);
        if (null == meterRespListEnergyResp
                || null == meterRespListEnergyResp.getData()
                || meterRespListEnergyResp.getData().size() <= 0) {
            return null;
        }
        List<Equip> meterNoList = new ArrayList<Equip>();
        logger.info("请求计量表数据，参数：{}，结果：{}", accountingUnitId, getJsonString(meterRespListEnergyResp));
        for (MeterResp meterResp : meterRespListEnergyResp.getData()) {
            if (meterResp.getEnergyType().equals(EnergyTypeEnum.ENERGY_ELECTRICITY.getValue())) {
                Equip equip = new Equip();
                equip.setEquipMK(Constant.ELEC_EQUIPMK);
                equip.setEquipID(meterResp.getLoopNumber());
                equip.setStaId(meterResp.getStaId());
                meterNoList.add(equip);
            }
        }
        return meterNoList;
    }
}
