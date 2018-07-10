package com.enn.web.controller;

import com.enn.constant.Constant;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.service.business.IAccountUnitService;
import com.enn.service.business.IOpentsdbService;
import com.enn.service.system.IEnergyBasicsService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.app.EnergyTrend;
import com.enn.vo.energy.business.req.AccountUnitReq;
import com.enn.vo.energy.business.req.ElecCostReq;
import com.enn.vo.energy.business.req.Equip;
import com.enn.vo.energy.business.req.SamplDataStaReq;
import com.enn.vo.energy.business.resp.DataResp;
import com.enn.vo.energy.business.resp.MeterResp;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.system.ElectricInfo;
import com.enn.vo.energy.system.req.ElectricInfoReq;
import com.enn.vo.energy.web.ElectricityCostOptimization;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"用电成本优化"})
@RestController
@RequestMapping(value = "/web/elecCost")
public class ElecCostOptimizationController {
    @Autowired
    private IOpentsdbService opentsdbService;
    @Autowired
    private IAccountUnitService accountUnitService;
    @Autowired
    private IEnergyBasicsService energyBasicsService;

    @RequestMapping(value = "/addElectric", method = RequestMethod.POST)
    @ApiOperation(value = "用电成本分析-需量分布图", notes = "用电成本分析-需量分布图")
    public EnergyResp<ElectricityCostOptimization> findDemand(@RequestBody @Valid ElecCostReq elecCostReq, BindingResult bindingResult) {
        EnergyResp<ElectricityCostOptimization> energyResp = new EnergyResp<>();
        ElectricityCostOptimization optimization = new ElectricityCostOptimization();
        List<Equip> equips = new ArrayList<>();
        String start = elecCostReq.getTime() + " 00:00:00";
        String end = elecCostReq.getTime() + " 23:59:00";

        //查询企业下一级电表
        AccountUnitReq req = new AccountUnitReq();
        req.setId(elecCostReq.getCustId());
        req.setAccountingType(Constant.ACCOUNTING_TYPE_00);
        req.setIsAccount(false);
        EnergyResp<List<UnitResp>> unitListResp = accountUnitService.queryAccountList(req);
        if (StatusCode.SUCCESS.getCode().equals(unitListResp.getCode())) {
            Equip equip = new Equip();
            List<UnitResp> unitList = unitListResp.getData();
            for (UnitResp unitResp : unitList) {
                List<MeterResp> meterRespList = unitResp.getMeters();
                for (MeterResp meterResp : meterRespList) {
                    if ("01".equals(meterResp.getEnergyType())) {
                        equip.setEquipID(meterResp.getLoopNumber());
                        equip.setStaId(meterResp.getStaId());
                        equip.setEquipMK(Constant.ELEC_EQUIPMK);
                        equips.add(equip);
                    }
                }
            }
            //从大数据平台获取最大需量值
            SamplDataStaReq sampleReq = new SamplDataStaReq();
            sampleReq.setEquips(equips);
            sampleReq.setDownsample(Constant.DOWNSMPLE_1M);
            sampleReq.setMetric("EMS.Enow");
            sampleReq.setStart(start);
            sampleReq.setEnd(end);
            EnergyResp<ListResp<RmiSamplDataResp>> resp = opentsdbService.getSamplDataStaReq(sampleReq);
            if (StatusCode.SUCCESS.getCode().equals(resp.getCode()) && resp.getData().getList().size() > 0
                    ) {
                List<DataResp> dataRespList = resp.getData().getList().get(0).getDataResp();

                List<DataResp> list = dataRespList.stream().map(s -> {
                    DataResp dataResp = new DataResp();
                    dataResp.setTime(s.getTime());
                    dataResp.setValue(MathUtils.towDecimal(s.getValue()));
                    return dataResp;
                }).collect(Collectors.toList());
                optimization.setDataResp(list);
                //获取优化分析报告
                ElectricInfoReq infoReq = new ElectricInfoReq();
                infoReq.setCustId(elecCostReq.getCustId());
                infoReq.setStartExecuteDate(elecCostReq.getTime());
                EnergyResp<ElectricInfo> infoEnergyResp = energyBasicsService.getElectricInfo(infoReq);
                if (StatusCode.SUCCESS.getCode().equals(infoEnergyResp.getCode())) {
                    ElectricInfo electricInfo = infoEnergyResp.getData();
                    double basicCapacity = electricInfo.getBasicCapacity();//基本容量
                    double maxDemand = electricInfo.getRatifiedMaxDemand();//最大需量
                    double capacityUnitPrice = electricInfo.getBasicTransCapacity();//基本电价变压器容量
                    double demandUnitPrice = electricInfo.getBasicElePriceMaxCapacity();//基本电价最大需量
                    BigDecimal capacityPrice = MathUtils.mul(new BigDecimal(basicCapacity), new BigDecimal(capacityUnitPrice));//容量基本电价
                    BigDecimal loadDmand = MathUtils.divide(capacityPrice, new BigDecimal(demandUnitPrice));//容需平衡负荷需量（kW)
                    List<DataResp> order = dataRespList.stream().filter(p -> 1 == compareBig(p.getValue(),loadDmand)).collect(Collectors.toList());
                    BigDecimal devide = MathUtils.divide(new BigDecimal(order.size()), new BigDecimal(dataRespList.size()));
                    BigDecimal loadRate = MathUtils.mul(devide, new BigDecimal(100));
                    optimization.setCapacityPrice(capacityPrice);
                    optimization.setLoadDemand(loadDmand);
                    optimization.setLoadRate(MathUtils.towDecimal(loadRate));
                    if (loadRate.compareTo(new BigDecimal(80)) == 1) {
                        optimization.setAdvice("基本容量单价");
                    } else {
                        optimization.setAdvice("基本需量单价");
                    }
                }
                energyResp.ok(optimization);
            } else {
                energyResp.setCode(resp.getCode());
                energyResp.setMsg(resp.getMsg());
                return energyResp;
            }
        } else {
            energyResp.setCode(unitListResp.getCode());
            energyResp.setMsg(unitListResp.getMsg());
            return energyResp;
        }
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
