package com.enn.web.controller;


import com.enn.constant.Constant;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.ExcelUtils;
import com.enn.energy.system.common.util.FormatUtil;
import com.enn.service.business.IProductConsumptionService;
import com.enn.service.system.IProductService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.ConsumptionDto;
import com.enn.vo.energy.business.bo.*;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.ProductDataPo;
import com.enn.vo.energy.business.req.*;
import com.enn.vo.energy.business.resp.*;
import com.enn.vo.energy.business.upload.UploadResp;
import com.enn.vo.energy.common.enums.EnergyTypeEnum;
import com.enn.vo.energy.system.ProductInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.math.BigDecimal.ROUND_CEILING;
import static java.math.BigDecimal.ROUND_HALF_DOWN;


@Api(tags = {"产品单耗分析"})
@RestController
@RequestMapping("/productConsumption")
public class ProductConsumptionController extends BaseController {

    @Autowired
    private IProductService productInfoService;
    @Autowired
    private IProductConsumptionService productConsumptionService;
    @Value("${company.electric.count.month}")
    private Integer month;

    @ApiOperation(value = "查询企业下的产品")
    @RequestMapping(value = "/findProductInfoByCustId", method = RequestMethod.POST)
    public EnergyResp findProductInfoByCustId(Long custId) {
        EnergyResp<List<ProductInfo>> resp = new EnergyResp<List<ProductInfo>>();
        if (null == custId || "".equals(custId)) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg());
            return resp;
        }
        return productInfoService.findListNoPage(custId);
    }


    @ApiOperation(value = "根据时间区间查看单耗分析表")
    @RequestMapping(value = "/queryConsumptionAnalysisTable", method = RequestMethod.POST)
    public EnergyResp<ConsumptionResp> queryConsumptionAnalysisTable(@RequestBody @Valid ProductConsumptionReq productConsumptionReq, BindingResult result) throws ParseException {
        EnergyResp<ConsumptionResp> resp = new EnergyResp<ConsumptionResp>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (productConsumptionReq.getStartDate().compareTo(productConsumptionReq.getEndDate()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        //时间跨度不能超过3个月
        if (DateUtil.getMonthAddDate(productConsumptionReq.getStartDate(), month).compareTo(productConsumptionReq.getEndDate()) < 0) {
            resp.faile(StatusCode.C.getCode(), "生产成本分析时间跨度不能大于3个月");
            return resp;
        }
        productConsumptionReq.setStartDate(DateFormatUtils.format(new SimpleDateFormat("yyyy-MM-dd").parse(productConsumptionReq.getStartDate()), "yyyy-MM-dd"));
        productConsumptionReq.setEndDate(DateFormatUtils.format(new SimpleDateFormat("yyyy-MM-dd").parse(productConsumptionReq.getEndDate()), "yyyy-MM-dd"));
        productConsumptionReq.setEndDate(productConsumptionReq.getEndDate().concat(" ").concat("23:59:59"));
        //查询该时间段内产品的所有生产线
        EnergyResp<List<ProductDataPo>> productDataPoResp = productConsumptionService.findLineByProductId(productConsumptionReq);
        if (!StatusCode.SUCCESS.getMsg().equals(productDataPoResp.getMsg())) {
            resp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg());
        }
        List<ProductDataPo> productDataPoList = productDataPoResp.getData();
        //返回生产成本单耗分析表
        ConsumptionResp consumptionResp = new ConsumptionResp();

        List<ConsumptionDto> mapList = new ArrayList<ConsumptionDto>();
        //生产线 == 核算单元  获取到所有生产线的电表
        //判断查询出来的产品
        if (!productDataPoList.isEmpty()) {
            //                所有生产线ID
            List<Long> lineIdList = new ArrayList<Long>();
            for (ProductDataPo productDataPo : productDataPoList) {
                lineIdList.add(productDataPo.getLineId());
            }
            CustMeterReq custMeterReq = new CustMeterReq();
            custMeterReq.setIds(lineIdList);
            EnergyResp<List<AccountingUnit>> accountingUnitListResp = productConsumptionService.findLineNameByLineId(custMeterReq);
            if (!StatusCode.SUCCESS.getMsg().equals(accountingUnitListResp.getMsg())) {
                resp.faile(accountingUnitListResp.getCode(), accountingUnitListResp.getMsg());
                return resp;
            }
            List<AccountingUnit> accountingUnitList = accountingUnitListResp.getData();
            for (ProductDataPo productDataPo : productDataPoList) {
                CompanyMeteReq de = new CompanyMeteReq();
                de.setId(productDataPo.getLineId());
                de.setEnergyType(EnergyTypeEnum.ENERGY_ELECTRICITY.getValue());
                EnergyResp<List<MeterResp>> meterListResp = accountUnitService.queryMeterListByAccountAndType(de);
                if (!StatusCode.SUCCESS.getMsg().equals(meterListResp.getMsg())) {
                    continue;
                }
                List<MeterResp> accountMeterT = meterListResp.getData();
                List<String> list = new ArrayList<String>();
                if (!accountMeterT.isEmpty()) {
                    ConsumptionDto testMap = new ConsumptionDto();
                    for (MeterResp meterResp : accountMeterT) {
                        list.add(meterResp.getLoopNumber());
                        testMap.setMeterName(queryLineName(productDataPo.getLineId(), accountingUnitList));
                    }
                    if (!list.isEmpty()) {
                        testMap.setLineId(productDataPo.getLineId());
                        testMap.setStartDate(productDataPo.getStartDate());
                        testMap.setEndDate(productDataPo.getEndDate());
                        testMap.setLoopNumber(list);
                        testMap.setNumber(productDataPo.getNumber());
                        mapList.add(testMap);
                    }
                }
            }
        } else {
            resp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg());
            return resp;
        }

        //获取指定时间的总电量和费用
        if (!mapList.isEmpty()) {
            for (ConsumptionDto testMap : mapList) {
                List<String> list = testMap.getLoopNumber();
                if (!list.isEmpty()) {
                    ElectricMeterReadingMinuteBo elecMeterReadingDayBo = new ElectricMeterReadingMinuteBo();
                    elecMeterReadingDayBo.setStart(testMap.getStartDate());
                    elecMeterReadingDayBo.setEnd(testMap.getEndDate());
                    elecMeterReadingDayBo.setEquipID(list);
                    EnergyResp<ElectricMeterReadingMinuteStatisticsBo> electricMeterReadingMinuteStatisticsBoResp = productConsumptionService.countElectricMeterReadingMinuteByAssignedConditon(elecMeterReadingDayBo);
                    if (!StatusCode.SUCCESS.getMsg().equals(electricMeterReadingMinuteStatisticsBoResp.getMsg())) {
                        continue;
                    }
                    ElectricMeterReadingMinuteStatisticsBo electricMeterReadingMinuteStatisticsBo = electricMeterReadingMinuteStatisticsBoResp.getData();
                    if (electricMeterReadingMinuteStatisticsBo != null) {
                        if (electricMeterReadingMinuteStatisticsBo.getTotalElectricPower().compareTo(BigDecimal.ZERO) == 0) {

                        } else {
                            testMap.setEleCount(electricMeterReadingMinuteStatisticsBo.getTotalElectricPower());
                        }
                    }


                }
            }
        } else {
            resp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg());
            return resp;
        }

        for (ConsumptionDto testMap : mapList) {
            testMap.setConsumption(testMap.getNumber().compareTo(BigDecimal.ZERO) == 0 ? testMap.getEleCount() == null ? new BigDecimal(0) : testMap.getEleCount() : testMap.getEleCount() == null ? new BigDecimal(0) : testMap.getEleCount().divide(testMap.getNumber(), 2, ROUND_CEILING));
        }

        //平均单耗
        //总产量
        BigDecimal countProductNum = BigDecimal.ZERO;
        //总电量
        BigDecimal countEleNum = BigDecimal.ZERO;
        for (ConsumptionDto testMap : mapList) {
            countProductNum = countProductNum.add(testMap.getNumber());
            countEleNum = countEleNum.add(testMap.getEleCount() == null ? BigDecimal.ZERO : testMap.getEleCount());
        }

        Collections.sort(mapList, (a, b) -> a.getConsumption().compareTo(b.getConsumption()));

        consumptionResp.setMaxConsumption(mapList.get(mapList.size() - 1).getConsumption());


        consumptionResp.setMaxEndTime(mapList.get(mapList.size() - 1).getEndDate());
        consumptionResp.setMaxStartTime(mapList.get(mapList.size() - 1).getStartDate());
        consumptionResp.setMaxLineName(mapList.get(mapList.size() - 1).getMeterName());
        consumptionResp.setMaxProduceTime(mapList.get(mapList.size() - 1).getStartDate());

        consumptionResp.setMinConsumption(mapList.get(0).getConsumption());
        consumptionResp.setMinEndTime(mapList.get(0).getEndDate());
        consumptionResp.setMinStartTime(mapList.get(0).getStartDate());
        consumptionResp.setMinLineName(mapList.get(0).getMeterName());
        consumptionResp.setMinProduceTime(mapList.get(0).getStartDate());

        //0的情况需要修改
        consumptionResp.setAvgConsumption(countEleNum.divide(countProductNum, 2, ROUND_CEILING));
        BigDecimal maxEle = mapList.get(mapList.size() - 1).getConsumption().subtract(mapList.get(0).getConsumption());

        //综合单耗差异度 （最大-最小）/最小*100%
        BigDecimal differenceDegree = maxEle.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : maxEle.divide(mapList.get(0).getConsumption().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : mapList.get(0).getConsumption(), 5, ROUND_HALF_DOWN).multiply(new BigDecimal(100));
        consumptionResp.setDifferenceDegree(differenceDegree.setScale(2, ROUND_CEILING));
        resp.ok(consumptionResp);
        return resp;
    }


    @ApiOperation(value = "根据时间区间查看生产线列表和负荷曲线")
    @RequestMapping(value = "/queryProduceLineList", method = RequestMethod.POST)
    public EnergyResp<ProductConsumptionListResp> queryProduceLineList(@RequestBody @Valid ProductConsumptionReq productConsumptionReq, BindingResult result) throws ParseException {
        EnergyResp<ProductConsumptionListResp> resp = new EnergyResp<ProductConsumptionListResp>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (productConsumptionReq.getStartDate().compareTo(productConsumptionReq.getEndDate()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        //时间跨度不能超过3个月
        if (DateUtil.getMonthAddDate(productConsumptionReq.getStartDate(), month).compareTo(productConsumptionReq.getEndDate()) < 0) {
            resp.faile(StatusCode.C.getCode(), "生产成本分析时间跨度不能大于3个月");
            return resp;
        }
        productConsumptionReq.setStartDate(DateFormatUtils.format(new SimpleDateFormat("yyyy-MM-dd").parse(productConsumptionReq.getStartDate()), "yyyy-MM-dd"));
        productConsumptionReq.setEndDate(DateFormatUtils.format(new SimpleDateFormat("yyyy-MM-dd").parse(productConsumptionReq.getEndDate()), "yyyy-MM-dd"));
        productConsumptionReq.setEndDate(productConsumptionReq.getEndDate().concat(" ").concat("23:59:59"));

        //查询该时间段内产品的所有生产线
        EnergyResp<List<ProductDataPo>> productDataPoResp = productConsumptionService.findLineByProductId(productConsumptionReq);
        if (!StatusCode.SUCCESS.getMsg().equals(productDataPoResp.getMsg())) {
            resp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg());
        }
        List<ProductDataPo> productDataPoList = productDataPoResp.getData();
        //生产线ID和电表号Map
        Map<Long, List<String>> electricityMap = new HashMap<Long, List<String>>();
        List<ProduceLineResp> produceLineRespList = new ArrayList<ProduceLineResp>();
        List<AccountingUnit> accountingUnitList = new ArrayList<AccountingUnit>();
        //生产线 == 核算单元  获取到所有生产线到电表
        if (!productDataPoList.isEmpty()) {
//                所有生产线ID
            List<Long> lineIdList = new ArrayList<Long>();
            for (ProductDataPo productDataPo : productDataPoList) {
                lineIdList.add(productDataPo.getLineId());
            }
            CustMeterReq custMeterReq = new CustMeterReq();
            custMeterReq.setIds(lineIdList);
            EnergyResp<List<AccountingUnit>> accountingUnitListResp = productConsumptionService.findLineNameByLineId(custMeterReq);
            if (!StatusCode.SUCCESS.getMsg().equals(accountingUnitListResp.getMsg())) {
                resp.faile(accountingUnitListResp.getCode(), accountingUnitListResp.getMsg());
                return resp;
            }
            accountingUnitList = accountingUnitListResp.getData();
            for (ProductDataPo productDataPo : productDataPoList) {
                CompanyMeteReq de = new CompanyMeteReq();
                de.setId(productDataPo.getLineId());
                de.setEnergyType(EnergyTypeEnum.ENERGY_ELECTRICITY.getValue());
                EnergyResp<List<MeterResp>> meterListResp = accountUnitService.queryMeterListByAccountAndType(de);
                if (!StatusCode.SUCCESS.getMsg().equals(meterListResp.getMsg())) {
                    continue;
                }
                List<MeterResp> accountMeterT = meterListResp.getData();
                List<String> list = new ArrayList<String>();
                ProduceLineResp produceLineResp = new ProduceLineResp();
                if (!accountMeterT.isEmpty()) {
                    for (MeterResp meterResp : accountMeterT) {
                        list.add(meterResp.getLoopNumber());
                        produceLineResp.setLineMetric(meterResp.getLoopNumber());
                        produceLineResp.setLineName(queryLineName(productDataPo.getLineId(), accountingUnitList));
                        produceLineResp.setStaId(meterResp.getStaId());
                    }
                }
                if (!list.isEmpty()) {
                    produceLineResp.setProductDataId(productDataPo.getId());
                    produceLineResp.setProduceNumber(productDataPo.getNumber());
                    produceLineResp.setLineId(productDataPo.getLineId());
                    produceLineResp.setCountDimensions(productDataPo.getStartDate().concat("-").concat(productDataPo.getEndDate()));
                    produceLineRespList.add(produceLineResp);
                    electricityMap.put(productDataPo.getId(), list);
                }
            }
        } else {
            resp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg());
            return resp;
        }

        //用电费用
        for (Map.Entry<Long, List<String>> entry : electricityMap.entrySet()) {
            List<String> list = entry.getValue();
            if (!list.isEmpty()) {
                ElectricMeterReadingMinuteBo elecMeterReadingDayBo = new ElectricMeterReadingMinuteBo();
                elecMeterReadingDayBo.setStart(queryStartTime(entry.getKey(), productDataPoList));
                elecMeterReadingDayBo.setEnd(queryEndTime(entry.getKey(), productDataPoList));
                elecMeterReadingDayBo.setEquipID(entry.getValue());
                EnergyResp<ElectricMeterReadingMinuteStatisticsBo> electricMeterReadingMinuteStatisticsBoResp = productConsumptionService.countElectricMeterReadingMinuteByAssignedConditon(elecMeterReadingDayBo);
                if (!StatusCode.SUCCESS.getMsg().equals(electricMeterReadingMinuteStatisticsBoResp.getMsg())) {
                    continue;
                }
                ElectricMeterReadingMinuteStatisticsBo elecMeterReadingDayStatisticsBo = electricMeterReadingMinuteStatisticsBoResp.getData();
                if (elecMeterReadingDayStatisticsBo != null) {
                    if (elecMeterReadingDayStatisticsBo.getTotalElectricPower().compareTo(BigDecimal.ZERO) == 0) {
                        for (ProduceLineResp produceLineResp : produceLineRespList) {
                            if (entry.getKey().equals(produceLineResp.getProductDataId())) {
                                produceLineResp.setCountElectricity(BigDecimal.ZERO);
                                produceLineResp.setCountElectricityFees(BigDecimal.ZERO);
                            }
                        }
                    } else {
                        for (ProduceLineResp produceLineResp : produceLineRespList) {
                            if (entry.getKey().equals(produceLineResp.getProductDataId())) {
                                //保留两位小数
                                produceLineResp.setCountElectricity(new BigDecimal(FormatUtil.formatNum(elecMeterReadingDayStatisticsBo.getTotalElectricPower())));
                                produceLineResp.setCountElectricityFees(new BigDecimal(FormatUtil.formatNum(elecMeterReadingDayStatisticsBo.getTotalElectricFees())));
                            }
                        }
                    }
                } else {
                    for (ProduceLineResp produceLineResp : produceLineRespList) {
                        if (entry.getKey().equals(produceLineResp.getProductDataId())) {
                            produceLineResp.setCountElectricity(BigDecimal.ZERO);
                            produceLineResp.setCountElectricityFees(BigDecimal.ZERO);
                        }
                    }
                }

            }
        }

        //蒸汽表
        Map<Long, List<String>> steamMap = new HashMap<Long, List<String>>();
        for (ProductDataPo productDataPo : productDataPoList) {
            CompanyMeteReq de = new CompanyMeteReq();
            de.setId(productDataPo.getLineId());
            de.setEnergyType(EnergyTypeEnum.ENERGY_STEAM.getValue());
            EnergyResp<List<MeterResp>> meterListResp = accountUnitService.queryMeterListByAccountAndType(de);
            if (!StatusCode.SUCCESS.getMsg().equals(meterListResp.getMsg())) {
                continue;
            }
            List<MeterResp> accountMeterT = meterListResp.getData();
            List<String> list = new ArrayList<String>();
//            if (!accountMeterT.isEmpty()) {
//                for (MeterResp meterResp : accountMeterT) {
//                    list.add("CEU" + meterResp.getLoopNumber());
//                }
//            }
//            if (!list.isEmpty()) {
//                steamMap.put(productDataPo.getId(), list);
//            }
            boolean isCreate = queryProduceLineResp(productDataPo.getId(), produceLineRespList);
            ProduceLineResp produceLineResp = new ProduceLineResp();
            if (!accountMeterT.isEmpty()) {
                for (MeterResp meterResp : accountMeterT) {
                    list.add(meterResp.getLoopNumber());
                    if (isCreate == true) {
                        produceLineResp.setLineName(queryLineName(productDataPo.getLineId(), accountingUnitList));
                    }
                }
            }
            if (!list.isEmpty()) {
                if (isCreate == true) {
                    produceLineResp.setProductDataId(productDataPo.getId());
                    produceLineResp.setProduceNumber(productDataPo.getNumber());
                    produceLineResp.setLineId(productDataPo.getLineId());
                    produceLineResp.setCountDimensions(productDataPo.getStartDate().concat("-").concat(productDataPo.getEndDate()));
                    produceLineRespList.add(produceLineResp);
                }
                steamMap.put(productDataPo.getId(), list);
            }
        }


        //蒸汽费用
        for (Map.Entry<Long, List<String>> entry : steamMap.entrySet()) {
            List<String> list = entry.getValue();
            if (!list.isEmpty()) {
                SteamMeterReadingMinuteBo steamMeterReadingDayBo = new SteamMeterReadingMinuteBo();
                steamMeterReadingDayBo.setStart(queryStartTime(entry.getKey(), productDataPoList));
                steamMeterReadingDayBo.setEnd(queryEndTime(entry.getKey(), productDataPoList));
                steamMeterReadingDayBo.setEquipID(entry.getValue());
                EnergyResp<SteamMeterReadingMinuteStatisticsBo> steamMeterReadingMinuteStatisticsBoResp = productConsumptionService.countSteamMeterReadingMinuteByAssignedConditon(steamMeterReadingDayBo);
                EnergyResp<BigDecimal> energyResp = productConsumptionService.querySteamMeterReadingMinuteByAssignedConditon(steamMeterReadingDayBo);
                if (!StatusCode.SUCCESS.getMsg().equals(steamMeterReadingMinuteStatisticsBoResp.getMsg())) {
                    continue;
                }
                SteamMeterReadingMinuteStatisticsBo steamMeterReadingMinuteStatisticsBo = steamMeterReadingMinuteStatisticsBoResp.getData();
                if (steamMeterReadingMinuteStatisticsBo != null) {
                    if (steamMeterReadingMinuteStatisticsBo.getTotalSteamPower().compareTo(BigDecimal.ZERO) == 0) {
                        for (ProduceLineResp produceLineResp : produceLineRespList) {
                            if (entry.getKey().equals(produceLineResp.getProductDataId())) {
                                produceLineResp.setCountSteam(BigDecimal.ZERO);
                                produceLineResp.setCountSteamFees(BigDecimal.ZERO);
                            }
                        }
                    } else {
                        for (ProduceLineResp produceLineResp : produceLineRespList) {
                            if (entry.getKey().equals(produceLineResp.getProductDataId())) {
                                produceLineResp.setCountSteam(steamMeterReadingMinuteStatisticsBo.getTotalSteamPower().setScale(2, ROUND_CEILING));
                                if (!StatusCode.SUCCESS.getMsg().equals(energyResp.getMsg())) {
                                    continue;
                                } else {
                                    produceLineResp.setCountSteamFees(energyResp.getData().setScale(2, ROUND_CEILING));
                                }
                            }
                        }
                    }
                }
            }
        }

        for (ProduceLineResp produceLineResp : produceLineRespList) {
            if (produceLineResp.getCountElectricity() != null) {
                produceLineResp.setProduceConsumptionElectricity(produceLineResp.getCountElectricity().divide(produceLineResp.getProduceNumber(), 2, ROUND_CEILING));
            }
            if (produceLineResp.getCountSteam() != null) {
                produceLineResp.setProduceConsumptionSteam(produceLineResp.getCountSteam().divide(produceLineResp.getProduceNumber(), 2, ROUND_CEILING));
            }
            if (produceLineResp.getCountElectricityFees() != null) {
                produceLineResp.setSumEnergyFees(produceLineResp.getCountElectricityFees().add(produceLineResp.getCountSteamFees() == null ? BigDecimal.ZERO : produceLineResp.getCountSteamFees()));
            } else if (produceLineResp.getCountSteamFees() != null) {
                produceLineResp.setSumEnergyFees(produceLineResp.getCountSteamFees().add(produceLineResp.getCountElectricityFees() == null ? BigDecimal.ZERO : produceLineResp.getCountElectricityFees()));
            }
        }
        //生产负荷曲线排序
        List<ProduceLineResp> produceLoadSortList = new ArrayList<ProduceLineResp>();
        for (ProduceLineResp produceLineResp : produceLineRespList) {
            if (produceLineResp.getProduceConsumptionElectricity() != null) {
                produceLoadSortList.add(produceLineResp);
            }
        }
        Collections.sort(produceLineRespList, (a, b) -> a.getProduceConsumptionElectricity() == null ? BigDecimal.ZERO.compareTo(b.getProduceConsumptionElectricity() == null ? BigDecimal.ZERO : b.getProduceConsumptionElectricity()) : a.getProduceConsumptionElectricity().compareTo(b.getProduceConsumptionElectricity() == null ? BigDecimal.ZERO : b.getProduceConsumptionElectricity()));
        //排序
        Collections.sort(produceLoadSortList, (a, b) -> a.getProduceConsumptionElectricity().compareTo(b.getProduceConsumptionElectricity()));
        ProduceLineResp maxProduceLine = null;
        ProduceLineResp minProduceLine = null;
        if (produceLoadSortList.size() == 1) {
            maxProduceLine = produceLoadSortList.get(0);
        } else if (produceLoadSortList.size() > 1) {
            minProduceLine = produceLoadSortList.get(0);
            maxProduceLine = produceLoadSortList.get(produceLoadSortList.size() - 1);
        }

        //负荷曲线
        List<ProduceLoadResp> produceLoadRespList = new ArrayList<ProduceLoadResp>();

        //采样获取数据参数封装
        if (maxProduceLine != null) {
            SamplDataStaReq samplDataStaReq = new SamplDataStaReq();
            samplDataStaReq.setStart(maxProduceLine.getCountDimensions().substring(0, 19));
            samplDataStaReq.setEnd(maxProduceLine.getCountDimensions().substring(20, maxProduceLine.getCountDimensions().length()));
            samplDataStaReq.setDownsample(Constant.DOWNSMPLE_1M);
            samplDataStaReq.setMetric(Constant.ELECTRIC_METRIC);
            List<Equip> maxEquips = new ArrayList<Equip>();
            Equip equip = new Equip();
            equip.setEquipID(maxProduceLine.getLineMetric());
            equip.setStaId(maxProduceLine.getStaId());
            equip.setEquipMK(Constant.ELEC_EQUIPMK);
            maxEquips.add(equip);
            samplDataStaReq.setEquips(maxEquips);
            EnergyResp<ListResp<RmiSamplDataResp>> maxRespEnergyResp = opentsdbService.getSamplDataStaReq(samplDataStaReq);
            if (StatusCode.SUCCESS.getMsg().equals(maxRespEnergyResp.getMsg())) {
                ProduceLoadResp produceLoadResp = new ProduceLoadResp();
                produceLoadResp.setName(maxProduceLine.getLineName());
                produceLoadResp.setEquipID(maxProduceLine.getLineMetric());
                produceLoadResp.setLineId(maxProduceLine.getLineId());
                for (RmiSamplDataResp rmiSamplDataResp : maxRespEnergyResp.getData().getList()) {
                    if (produceLoadResp.getEquipID().equals(rmiSamplDataResp.getEquipID())) {
                        List<DataResp> dataRespList = rmiSamplDataResp.getDataResp();
                        for (DataResp dataResp : dataRespList) {
//                            dataResp.setValue(new BigDecimal(FormatUtil.formatNum(dataResp.getValue())));
                            dataResp.setValue(dataResp.getValue() == null ? null : dataResp.getValue().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        }
                        produceLoadResp.setRmiSamplDataRespList(rmiSamplDataResp);
                    }
                }
                produceLoadRespList.add(produceLoadResp);
                produceLineRespList.get(produceLineRespList.size() - 1).setSelect(true);
            }else {
                resp.faile(maxRespEnergyResp.getCode(), maxRespEnergyResp.getMsg());
                return resp;
            }
        }

        if (minProduceLine != null) {
            SamplDataStaReq samplDataStaReq = new SamplDataStaReq();
            samplDataStaReq.setStart(minProduceLine.getCountDimensions().substring(0, 19));
            samplDataStaReq.setEnd(minProduceLine.getCountDimensions().substring(20, minProduceLine.getCountDimensions().length()));
            samplDataStaReq.setDownsample(Constant.DOWNSMPLE_1M);
            samplDataStaReq.setMetric(Constant.ELECTRIC_METRIC_EMS);
            List<Equip> minEquips = new ArrayList<Equip>();
            Equip equip = new Equip();
            equip.setEquipID(minProduceLine.getLineMetric());
            equip.setStaId(minProduceLine.getStaId());
            equip.setEquipMK(Constant.ELEC_EQUIPMK);
            minEquips.add(equip);
            samplDataStaReq.setEquips(minEquips);
            EnergyResp<ListResp<RmiSamplDataResp>> minRespEnergyResp = opentsdbService.getSamplDataStaReq(samplDataStaReq);
            if (StatusCode.SUCCESS.getMsg().equals(minRespEnergyResp.getMsg())) {
                ProduceLoadResp produceLoadResp = new ProduceLoadResp();
                produceLoadResp.setName(minProduceLine.getLineName());
                produceLoadResp.setEquipID(minProduceLine.getLineMetric());
                produceLoadResp.setLineId(minProduceLine.getLineId());
                for (RmiSamplDataResp rmiSamplDataResp : minRespEnergyResp.getData().getList()) {
                    if (produceLoadResp.getEquipID().equals(rmiSamplDataResp.getEquipID())) {
                        List<DataResp> dataRespList = rmiSamplDataResp.getDataResp();
                        for (DataResp dataResp : dataRespList) {
//                            dataResp.setValue(new BigDecimal(FormatUtil.formatNum(dataResp.getValue())));
                            dataResp.setValue(dataResp.getValue() == null ? null : dataResp.getValue().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        }
                        produceLoadResp.setRmiSamplDataRespList(rmiSamplDataResp);
                    }
                }
                produceLoadRespList.add(produceLoadResp);
                produceLineRespList.get(0).setSelect(true);
            } else {
                resp.faile(minRespEnergyResp.getCode(), minRespEnergyResp.getMsg());
                return resp;
            }
        }
        ProductConsumptionListResp productConsumptionListResp = new ProductConsumptionListResp();
        productConsumptionListResp.setProduceLineRespList(produceLineRespList);
        productConsumptionListResp.setProduceLoadRespList(produceLoadRespList);
        resp.ok(productConsumptionListResp);

        return resp;

    }

    /**
     * 根据productDataID查询 是否存在这条数据 存在false 不存在true
     *
     * @param id
     * @param produceLineRespList
     * @return
     */
    private boolean queryProduceLineResp(Long id, List<ProduceLineResp> produceLineRespList) {
        for (ProduceLineResp produceLineResp : produceLineRespList) {
            if (id.equals(produceLineResp.getProductDataId())) {
                return false;
            }
        }
        return true;
    }

    @ApiOperation(value = "根据生产线信息获取负荷曲线")
    @RequestMapping(value = "/queryCurveByLineInfo", method = RequestMethod.POST)
    public EnergyResp<List<ProduceLoadResp>> queryCurveByLineInfo(@RequestBody @Valid ProduceCurveListReq produceCurveListReq, BindingResult result) {
        EnergyResp<List<ProduceLoadResp>> resp = new EnergyResp<List<ProduceLoadResp>>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), result.getFieldError().getDefaultMessage());
            return resp;
        }

        List<ProduceLoadResp> produceLoadRespList = new ArrayList<ProduceLoadResp>();

        List<ProduceCurveReq> produceCurveReqList = produceCurveListReq.getProduceCurveReq();

        //站ID
        Map<String, String> staIdMap = new HashMap<String, String>();
        for (ProduceCurveReq produceCurveReq : produceCurveReqList) {
            //采样获取数据参数封装
            CompanyMeteReq de = new CompanyMeteReq();
            de.setId(produceCurveReq.getLineId());
            de.setEnergyType(EnergyTypeEnum.ENERGY_ELECTRICITY.getValue());
            EnergyResp<List<MeterResp>> meterListResp = accountUnitService.queryMeterListByAccountAndType(de);
            if (!StatusCode.SUCCESS.getMsg().equals(meterListResp.getMsg())) {
                continue;
            }
            List<MeterResp> accountMeterT = meterListResp.getData();
            if (!accountMeterT.isEmpty()) {
                for (MeterResp meterResp : accountMeterT) {
                    if (meterResp.getLoopNumber().equals(produceCurveReq.getEquipID())) {
                        staIdMap.put(produceCurveReq.getEquipID(), meterResp.getStaId());
                    }
                }
            }

            SamplDataStaReq samplDataStaReq = new SamplDataStaReq();
            samplDataStaReq.setStart(produceCurveReq.getStartDate());
            samplDataStaReq.setEnd(produceCurveReq.getEndDate());
            samplDataStaReq.setDownsample(Constant.DOWNSMPLE_1M);
            samplDataStaReq.setMetric(Constant.ELECTRIC_METRIC);
            List<Equip> equips = new ArrayList<Equip>();
            Equip equip = new Equip();
            equip.setEquipID(produceCurveReq.getEquipID());
            equip.setStaId(queryStaId(staIdMap, produceCurveReq.getEquipID()));
            equip.setEquipMK(Constant.ELEC_EQUIPMK);
            equips.add(equip);
            samplDataStaReq.setEquips(equips);
            EnergyResp<ListResp<RmiSamplDataResp>> respEnergyResp = opentsdbService.getSamplDataStaReq(samplDataStaReq);

            if (StatusCode.SUCCESS.getMsg().equals(respEnergyResp.getMsg())) {

                ProduceLoadResp produceLoadResp = new ProduceLoadResp();

                produceLoadResp.setName(produceCurveReq.getName());
                produceLoadResp.setLineId(produceCurveReq.getLineId());
                produceLoadResp.setEquipID(produceCurveReq.getEquipID());

                for (RmiSamplDataResp rmiSamplDataResp : respEnergyResp.getData().getList()) {
                    if (produceCurveReq.getEquipID().equals(rmiSamplDataResp.getEquipID())) {
                        List<DataResp> dataRespList = rmiSamplDataResp.getDataResp();
                        for (DataResp dataResp : dataRespList) {
//                            dataResp.setValue(new BigDecimal(FormatUtil.formatNum(dataResp.getValue())));
                            dataResp.setValue(dataResp.getValue() == null ? null : dataResp.getValue().setScale(2, BigDecimal.ROUND_HALF_EVEN));
                        }
                        produceLoadResp.setRmiSamplDataRespList(rmiSamplDataResp);
                    }
                }
                produceLoadRespList.add(produceLoadResp);
            }
        }

        resp.ok(produceLoadRespList);
        return resp;
    }

    @ApiOperation(value = "生产成本分析Excel导出")
    @RequestMapping(value = "/productConsumptionExport", method = RequestMethod.POST)
    public EnergyResp productConsumptionExport(@RequestBody @Valid ProductConsumptionReq productConsumptionReq, BindingResult result) {
        EnergyResp resp = new EnergyResp();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (productConsumptionReq.getStartDate().compareTo(productConsumptionReq.getEndDate()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        //时间跨度不能超过3个月
        if (DateUtil.getMonthAddDate(productConsumptionReq.getStartDate(), month).compareTo(productConsumptionReq.getEndDate()) < 0) {
            resp.faile(StatusCode.C.getCode(), "生产成本分析时间跨度不能大于3个月");
            return resp;
        }
        productConsumptionReq.setEndDate(productConsumptionReq.getEndDate().concat(" ").concat("23:59:59"));
        //查询该时间段内产品的所有生产线
        EnergyResp<List<ProductDataPo>> productDataPoResp = productConsumptionService.findLineByProductId(productConsumptionReq);
        if (!StatusCode.SUCCESS.getMsg().equals(productDataPoResp.getMsg())) {
            resp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg());
        }
        List<ProductDataPo> productDataPoList = productDataPoResp.getData();
        //返回生产成本单耗分析表
        ConsumptionResp consumptionResp = new ConsumptionResp();

        List<ConsumptionDto> mapList = new ArrayList<ConsumptionDto>();
        //生产线 == 核算单元  获取到所有生产线的电表
        //判断查询出来的产品
        List<AccountingUnit> accountingUnitList;
        if (!productDataPoList.isEmpty()) {
            //                所有生产线ID
            List<Long> lineIdList = new ArrayList<Long>();
            for (ProductDataPo productDataPo : productDataPoList) {
                lineIdList.add(productDataPo.getLineId());
            }
            CustMeterReq custMeterReq = new CustMeterReq();
            custMeterReq.setIds(lineIdList);
            EnergyResp<List<AccountingUnit>> accountingUnitListResp = productConsumptionService.findLineNameByLineId(custMeterReq);
            if (!StatusCode.SUCCESS.getMsg().equals(accountingUnitListResp.getMsg())) {
                resp.faile(accountingUnitListResp.getCode(), accountingUnitListResp.getMsg());
                return resp;
            }
            accountingUnitList = accountingUnitListResp.getData();
            for (ProductDataPo productDataPo : productDataPoList) {
                CompanyMeteReq de = new CompanyMeteReq();
                de.setId(productDataPo.getLineId());
                de.setEnergyType(EnergyTypeEnum.ENERGY_ELECTRICITY.getValue());
                EnergyResp<List<MeterResp>> meterListResp = accountUnitService.queryMeterListByAccountAndType(de);
                if (!StatusCode.SUCCESS.getMsg().equals(meterListResp.getMsg())) {
                    continue;
                }
                List<MeterResp> accountMeterT = meterListResp.getData();
                List<String> list = new ArrayList<String>();
                if (!accountMeterT.isEmpty()) {
                    ConsumptionDto testMap = new ConsumptionDto();
                    for (MeterResp meterResp : accountMeterT) {
                        list.add(meterResp.getLoopNumber());
                        testMap.setMeterName(queryLineName(productDataPo.getLineId(), accountingUnitList));
                    }
                    if (!list.isEmpty()) {
                        testMap.setLineId(productDataPo.getLineId());
                        testMap.setStartDate(productDataPo.getStartDate());
                        testMap.setEndDate(productDataPo.getEndDate());
                        testMap.setLoopNumber(list);
                        testMap.setNumber(productDataPo.getNumber());
                        mapList.add(testMap);
                    }
                }
            }
        } else {
            resp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg());
            return resp;
        }

        //获取指定时间的总电量和费用
        for (ConsumptionDto testMap : mapList) {
            List<String> list = testMap.getLoopNumber();
            if (!list.isEmpty()) {
                ElectricMeterReadingMinuteBo elecMeterReadingDayBo = new ElectricMeterReadingMinuteBo();
                elecMeterReadingDayBo.setStart(testMap.getStartDate());
                elecMeterReadingDayBo.setEnd(testMap.getEndDate());
                elecMeterReadingDayBo.setEquipID(list);
                EnergyResp<ElectricMeterReadingMinuteStatisticsBo> electricMeterReadingMinuteStatisticsBoResp = productConsumptionService.countElectricMeterReadingMinuteByAssignedConditon(elecMeterReadingDayBo);
                if (!StatusCode.SUCCESS.getMsg().equals(electricMeterReadingMinuteStatisticsBoResp.getMsg())) {
                    continue;
                }
                ElectricMeterReadingMinuteStatisticsBo electricMeterReadingMinuteStatisticsBo = electricMeterReadingMinuteStatisticsBoResp.getData();
                if (electricMeterReadingMinuteStatisticsBo != null) {
                    if (electricMeterReadingMinuteStatisticsBo.getTotalElectricPower().compareTo(BigDecimal.ZERO) == 0) {

                    } else {
                        testMap.setEleCount(electricMeterReadingMinuteStatisticsBo.getTotalElectricPower());
                    }
                }


            }
        }

        for (ConsumptionDto testMap : mapList) {
            testMap.setConsumption(testMap.getNumber().compareTo(BigDecimal.ZERO) == 0 ? testMap.getEleCount() == null ? new BigDecimal(0) : testMap.getEleCount() : testMap.getEleCount() == null ? new BigDecimal(0) : testMap.getEleCount().divide(testMap.getNumber(), 2, ROUND_CEILING));
        }

        //平均单耗
        //总产量
        BigDecimal countProductNum = BigDecimal.ZERO;
        //总电量
        BigDecimal countEleNum = BigDecimal.ZERO;
        for (ConsumptionDto testMap : mapList) {
            countProductNum = countProductNum.add(testMap.getNumber());
            countEleNum = countEleNum.add(testMap.getEleCount() == null ? BigDecimal.ZERO : testMap.getEleCount());
        }

        Collections.sort(mapList, (a, b) -> a.getConsumption().compareTo(b.getConsumption()));

        consumptionResp.setMaxConsumption(mapList.get(mapList.size() - 1).getConsumption());


        consumptionResp.setMaxEndTime(mapList.get(mapList.size() - 1).getEndDate().substring(mapList.get(mapList.size() - 1).getEndDate().indexOf(" ") + 1, mapList.get(mapList.size() - 1).getEndDate().lastIndexOf(":")));
        consumptionResp.setMaxStartTime(mapList.get(mapList.size() - 1).getStartDate().substring(mapList.get(mapList.size() - 1).getStartDate().indexOf(" ") + 1, mapList.get(mapList.size() - 1).getStartDate().lastIndexOf(":")));
        consumptionResp.setMaxLineName(mapList.get(mapList.size() - 1).getMeterName());
        consumptionResp.setMaxProduceTime(mapList.get(mapList.size() - 1).getStartDate().substring(0, mapList.get(mapList.size() - 1).getStartDate().indexOf(" ")));

        consumptionResp.setMinConsumption(mapList.get(0).getConsumption());
        consumptionResp.setMinEndTime(mapList.get(0).getEndDate().substring(mapList.get(0).getEndDate().indexOf(" ") + 1, mapList.get(0).getEndDate().lastIndexOf(":")));
        consumptionResp.setMinStartTime(mapList.get(0).getStartDate().substring(mapList.get(0).getStartDate().indexOf(" ") + 1, mapList.get(0).getStartDate().lastIndexOf(":")));
        consumptionResp.setMinLineName(mapList.get(0).getMeterName());
        consumptionResp.setMinProduceTime(mapList.get(0).getStartDate().substring(0, mapList.get(0).getStartDate().indexOf(" ")));

        //0的情况需要修改
        consumptionResp.setAvgConsumption(countEleNum.divide(countProductNum, 2, ROUND_CEILING));
        BigDecimal maxEle = mapList.get(mapList.size() - 1).getConsumption().subtract(mapList.get(0).getConsumption());

        BigDecimal differenceDegree = maxEle.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : maxEle.divide(mapList.get(0).getConsumption().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : mapList.get(0).getConsumption(), 5, ROUND_HALF_DOWN).multiply(new BigDecimal(100));
        consumptionResp.setDifferenceDegree(differenceDegree.setScale(2, ROUND_CEILING));

        //生产线ID和电表号Map
        Map<Long, List<String>> electricityMap = new HashMap<Long, List<String>>();
        List<ProduceLineResp> produceLineRespList = new ArrayList<ProduceLineResp>();
        //生产线 == 核算单元  获取到所有生产线到电表
        if (!productDataPoList.isEmpty()) {
            for (ProductDataPo productDataPo : productDataPoList) {
                CompanyMeteReq de = new CompanyMeteReq();
                de.setId(productDataPo.getLineId());
                de.setEnergyType(EnergyTypeEnum.ENERGY_ELECTRICITY.getValue());
                EnergyResp<List<MeterResp>> meterListResp = accountUnitService.queryMeterListByAccountAndType(de);
                if (!StatusCode.SUCCESS.getMsg().equals(meterListResp.getMsg())) {
                    continue;
                }
                List<MeterResp> accountMeterT = meterListResp.getData();
                List<String> list = new ArrayList<String>();
                ProduceLineResp produceLineResp = new ProduceLineResp();
                if (!accountMeterT.isEmpty()) {
                    for (MeterResp meterResp : accountMeterT) {
                        list.add(meterResp.getLoopNumber());
                        produceLineResp.setLineMetric(meterResp.getLoopNumber());
                        produceLineResp.setLineName(queryLineName(productDataPo.getLineId(), accountingUnitList));
                        produceLineResp.setStaId(meterResp.getStaId());
                    }
                }
                if (!list.isEmpty()) {
                    produceLineResp.setProductDataId(productDataPo.getId());
                    produceLineResp.setProduceNumber(productDataPo.getNumber());
                    produceLineResp.setLineId(productDataPo.getLineId());
                    produceLineResp.setCountDimensions(productDataPo.getStartDate().concat("-").concat(productDataPo.getEndDate()));
                    produceLineRespList.add(produceLineResp);
                    electricityMap.put(productDataPo.getId(), list);
                }
            }
        } else {
            resp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg());
            return resp;
        }

        //用电费用
        for (Map.Entry<Long, List<String>> entry : electricityMap.entrySet()) {
            List<String> list = entry.getValue();
            if (!list.isEmpty()) {
                ElectricMeterReadingMinuteBo elecMeterReadingDayBo = new ElectricMeterReadingMinuteBo();
                elecMeterReadingDayBo.setStart(queryStartTime(entry.getKey(), productDataPoList));
                elecMeterReadingDayBo.setEnd(queryEndTime(entry.getKey(), productDataPoList));
                elecMeterReadingDayBo.setEquipID(entry.getValue());
                EnergyResp<ElectricMeterReadingMinuteStatisticsBo> electricMeterReadingMinuteStatisticsBoResp = productConsumptionService.countElectricMeterReadingMinuteByAssignedConditon(elecMeterReadingDayBo);
                if (!StatusCode.SUCCESS.getMsg().equals(electricMeterReadingMinuteStatisticsBoResp.getMsg())) {
                    continue;
                }
                ElectricMeterReadingMinuteStatisticsBo elecMeterReadingDayStatisticsBo = electricMeterReadingMinuteStatisticsBoResp.getData();
                if (elecMeterReadingDayStatisticsBo != null) {
                    if (elecMeterReadingDayStatisticsBo.getTotalElectricPower().compareTo(BigDecimal.ZERO) == 0) {
                        for (ProduceLineResp produceLineResp : produceLineRespList) {

                            if (entry.getKey().equals(produceLineResp.getProductDataId())) {
                                produceLineResp.setCountElectricity(BigDecimal.ZERO);
                                produceLineResp.setCountElectricityFees(BigDecimal.ZERO);
                            }
                        }
                    } else {
                        for (ProduceLineResp produceLineResp : produceLineRespList) {

                            if (entry.getKey().equals(produceLineResp.getProductDataId())) {
                                //保留两位小数
                                produceLineResp.setCountElectricity(new BigDecimal(FormatUtil.formatNum(elecMeterReadingDayStatisticsBo.getTotalElectricPower())));
                                produceLineResp.setCountElectricityFees(new BigDecimal(FormatUtil.formatNum(elecMeterReadingDayStatisticsBo.getTotalElectricFees())));
                            }
                        }
                    }
                } else {
                    for (ProduceLineResp produceLineResp : produceLineRespList) {
                        if (entry.getKey().equals(produceLineResp.getProductDataId())) {
                            produceLineResp.setCountElectricity(BigDecimal.ZERO);
                            produceLineResp.setCountElectricityFees(BigDecimal.ZERO);
                        }
                    }
                }

            }
        }

        //蒸汽表
        Map<Long, List<String>> steamMap = new HashMap<Long, List<String>>();
        for (ProductDataPo productDataPo : productDataPoList) {
            CompanyMeteReq de = new CompanyMeteReq();
            de.setId(productDataPo.getLineId());
            de.setEnergyType(EnergyTypeEnum.ENERGY_STEAM.getValue());
            EnergyResp<List<MeterResp>> meterListResp = accountUnitService.queryMeterListByAccountAndType(de);
            if (!StatusCode.SUCCESS.getMsg().equals(meterListResp.getMsg())) {
                continue;
            }
            List<MeterResp> accountMeterT = meterListResp.getData();
            List<String> list = new ArrayList<String>();
//            if (!accountMeterT.isEmpty()) {
//                for (MeterResp meterResp : accountMeterT) {
//                    list.add("CEU" + meterResp.getLoopNumber());
//                }
//            }
//            if (!list.isEmpty()) {
//                steamMap.put(productDataPo.getId(), list);
//            }
            boolean isCreate = queryProduceLineResp(productDataPo.getId(), produceLineRespList);
            ProduceLineResp produceLineResp = new ProduceLineResp();
            if (!accountMeterT.isEmpty()) {
                for (MeterResp meterResp : accountMeterT) {
                    list.add(meterResp.getLoopNumber());
                    if (isCreate == true) {
                        produceLineResp.setLineName(queryLineName(productDataPo.getLineId(), accountingUnitList));
                    }
                }
            }
            if (!list.isEmpty()) {
                if (isCreate == true) {
                    produceLineResp.setProductDataId(productDataPo.getId());
                    produceLineResp.setProduceNumber(productDataPo.getNumber());
                    produceLineResp.setLineId(productDataPo.getLineId());
                    produceLineResp.setCountDimensions(productDataPo.getStartDate().concat("-").concat(productDataPo.getEndDate()));
                    produceLineRespList.add(produceLineResp);
                }
//                    electricityMap.put(productDataPo.getLineId(), list);
                steamMap.put(productDataPo.getId(), list);
            }
        }

        //蒸汽费用
        for (Map.Entry<Long, List<String>> entry : steamMap.entrySet()) {
            List<String> list = entry.getValue();
            if (!list.isEmpty()) {
                SteamMeterReadingMinuteBo steamMeterReadingDayBo = new SteamMeterReadingMinuteBo();
                steamMeterReadingDayBo.setStart(queryStartTime(entry.getKey(), productDataPoList));
                steamMeterReadingDayBo.setEnd(queryEndTime(entry.getKey(), productDataPoList));
                steamMeterReadingDayBo.setEquipID(entry.getValue());
                EnergyResp<SteamMeterReadingMinuteStatisticsBo> steamMeterReadingMinuteStatisticsBoResp = productConsumptionService.countSteamMeterReadingMinuteByAssignedConditon(steamMeterReadingDayBo);
                EnergyResp<BigDecimal> energyResp = productConsumptionService.querySteamMeterReadingMinuteByAssignedConditon(steamMeterReadingDayBo);
                if (!StatusCode.SUCCESS.getMsg().equals(steamMeterReadingMinuteStatisticsBoResp.getMsg())) {
                    continue;
                }
                SteamMeterReadingMinuteStatisticsBo steamMeterReadingMinuteStatisticsBo = steamMeterReadingMinuteStatisticsBoResp.getData();
                if (steamMeterReadingMinuteStatisticsBo != null) {
                    if (steamMeterReadingMinuteStatisticsBo.getTotalSteamPower().compareTo(BigDecimal.ZERO) == 0) {
                        for (ProduceLineResp produceLineResp : produceLineRespList) {
                            if (entry.getKey().equals(produceLineResp.getProductDataId())) {
                                produceLineResp.setCountSteam(BigDecimal.ZERO);
                                produceLineResp.setCountSteamFees(BigDecimal.ZERO);
                            }
                        }
                    } else {
                        for (ProduceLineResp produceLineResp : produceLineRespList) {
                            if (entry.getKey().equals(produceLineResp.getProductDataId())) {
                                produceLineResp.setCountSteam(steamMeterReadingMinuteStatisticsBo.getTotalSteamPower().setScale(2, ROUND_CEILING));
                                if (!StatusCode.SUCCESS.getMsg().equals(energyResp.getMsg())) {
                                    continue;
                                }
                                produceLineResp.setCountSteamFees(energyResp.getData().setScale(2, ROUND_CEILING));
                            }
                        }
                    }
                }
            }
        }

        for (ProduceLineResp produceLineResp : produceLineRespList) {
            if (produceLineResp.getCountElectricity() != null) {
                produceLineResp.setProduceConsumptionElectricity(produceLineResp.getCountElectricity().divide(produceLineResp.getProduceNumber(), 2, ROUND_CEILING));
            }
            if (produceLineResp.getCountSteam() != null) {
                produceLineResp.setProduceConsumptionSteam(produceLineResp.getCountSteam().divide(produceLineResp.getProduceNumber(), 2, ROUND_CEILING));
            }
            if (produceLineResp.getCountElectricityFees() != null) {
                produceLineResp.setSumEnergyFees(produceLineResp.getCountElectricityFees().add(produceLineResp.getCountSteamFees() == null ? BigDecimal.ZERO : produceLineResp.getCountSteamFees()));
            } else if (produceLineResp.getCountSteamFees() != null) {
                produceLineResp.setSumEnergyFees(produceLineResp.getCountSteamFees().add(produceLineResp.getCountElectricityFees() == null ? BigDecimal.ZERO : produceLineResp.getCountElectricityFees()));
            }
        }

        //生产线列表  排序跟页面一致
        Collections.sort(produceLineRespList, (a, b) -> a.getProduceConsumptionElectricity() == null ? BigDecimal.ZERO.compareTo(b.getProduceConsumptionElectricity() == null ? BigDecimal.ZERO : b.getProduceConsumptionElectricity()) : a.getProduceConsumptionElectricity().compareTo(b.getProduceConsumptionElectricity() == null ? BigDecimal.ZERO : b.getProduceConsumptionElectricity()));
        //导出Excel
        String[] headers1 = {"区间最大综合单耗生产线", "最大综合单耗(Kwh/t)", "最大综合单耗生产日期", "最大综合单耗开始时间", "最大综合单耗结束时间", "区间最小综合单耗生产线", "最小综合单耗(Kwh/t)", "最小综合单耗生产日期", "最小综合单耗开始时间", "最小综合单耗结束时间", "平均综合单耗kwh/t", "综合单耗差异度(%)"};
        List<LinkedHashMap<String, String>> rows1 = new ArrayList<>();
        LinkedHashMap<String, String> map1 = new LinkedHashMap<String, String>();
        map1.put("1", consumptionResp.getMaxLineName());
        map1.put("2", consumptionResp.getMaxConsumption().toString());
        map1.put("3", consumptionResp.getMaxProduceTime());
        map1.put("4", consumptionResp.getMaxStartTime());
        map1.put("5", consumptionResp.getMaxEndTime());
        map1.put("6", consumptionResp.getMinLineName());
        map1.put("7", consumptionResp.getMinConsumption().toString());
        map1.put("8", consumptionResp.getMinProduceTime());
        map1.put("9", consumptionResp.getMinStartTime());
        map1.put("10", consumptionResp.getMinEndTime());
        map1.put("11", consumptionResp.getAvgConsumption().toString());
        map1.put("12", consumptionResp.getDifferenceDegree().toString());
        rows1.add(map1);

        String[] headers2 = {"生产线", "统计维度", "综合能源费用", "总用电量(Kwh)", "总电费(元)", "总蒸汽量(t)", "总蒸汽费用(元)", "生产产量(t)", "生产单耗(电)(Kwh/t)", "生产单耗(蒸汽)(t/t)"};
        String exportExcelName = "生产成本分析";
        List<LinkedHashMap<String, String>> rows2 = new ArrayList<>();
        for (ProduceLineResp produceLineResp : produceLineRespList) {
            if (null != produceLineResp) {
                LinkedHashMap<String, String> map2 = new LinkedHashMap<String, String>();
                map2.put("1", produceLineResp.getLineName());
                map2.put("2", produceLineResp.getCountDimensions());
                map2.put("3", produceLineResp.getSumEnergyFees().toString());
                map2.put("4", produceLineResp.getCountElectricity().toString());
                map2.put("5", produceLineResp.getCountElectricityFees().toString());
                map2.put("6", produceLineResp.getCountSteam() == null ? "" : produceLineResp.getCountSteam().toString());
                map2.put("7", produceLineResp.getCountSteamFees() == null ? "" : produceLineResp.getCountSteamFees().toString());
                map2.put("8", produceLineResp.getProduceNumber() == null ? "" : produceLineResp.getProduceNumber().toString());
                map2.put("9", produceLineResp.getProduceConsumptionElectricity() == null ? "" : produceLineResp.getProduceConsumptionElectricity().toString());
                map2.put("10", produceLineResp.getProduceConsumptionSteam() == null ? "" : produceLineResp.getProduceConsumptionSteam().toString());
                rows2.add(map2);
            }
        }

        Workbook workbook = ExcelUtils.productConsumptionExportExcel(headers1, rows1, exportExcelName);
        ResponseEntity<byte[]> responseEntity = ExcelUtils.productConsumptionExportExcelTwo(headers2, rows2, exportExcelName, ExcelUtils.ExcelSuffix.xls, workbook, 5);
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
        return resp;
    }


    /**
     * 根据生产线ID获取生产线名称
     *
     * @param lineId
     * @param accountingUnitList
     * @return
     */
    private String queryLineName(Long lineId, List<AccountingUnit> accountingUnitList) {
        if (accountingUnitList != null) {
            for (AccountingUnit accountingUnit : accountingUnitList) {
                if (lineId.equals(accountingUnit.getId())) {
                    return accountingUnit.getName();
                }
            }
        }
        return "";
    }

    /**
     * 获取结束时间
     *
     * @param key
     * @param productDataPoList
     * @return
     */
    private String queryEndTime(Long key, List<ProductDataPo> productDataPoList) {
        for (ProductDataPo productDataPo : productDataPoList) {
            if (key.equals(productDataPo.getId())) {
                return productDataPo.getEndDate();
            }
        }
        return null;
    }

    /**
     * 获取开始时间
     *
     * @param key
     * @param productDataPoList
     * @return
     */
    private String queryStartTime(Long key, List<ProductDataPo> productDataPoList) {
        for (ProductDataPo productDataPo : productDataPoList) {
            if (key.equals(productDataPo.getId())) {
                return productDataPo.getStartDate();
            }
        }
        return null;
    }

    /**
     * 获取站ID
     *
     * @param staIdMap
     * @param equipID
     * @return
     */
    private String queryStaId(Map<String, String> staIdMap, String equipID) {
        for (Map.Entry<String, String> entry : staIdMap.entrySet()) {
            if (equipID.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "";
    }
}
