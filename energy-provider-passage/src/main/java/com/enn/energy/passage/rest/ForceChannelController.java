package com.enn.energy.passage.rest;

import com.enn.constant.StatusCode;
import com.enn.energy.passage.service.IForceChannelService;
import com.enn.energy.passage.vo.EnergyPriceReq;
import com.enn.energy.passage.vo.ForceChannelResp;
import com.enn.energy.passage.vo.SteamPriceReq;
import com.enn.vo.energy.CollectItemEnum;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.CustMeter;
import com.enn.vo.energy.system.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 生产数据Controller
 *
 * @Author: 张洪源
 * @Date: 2018-06-05 09:49
 */
@Api(tags = {"算力通道"})
@RestController
@RequestMapping("/forceChannel")
public class ForceChannelController {

    @Autowired
    private IForceChannelService forceChannelService;

    @ApiOperation(value = "查询电能源单价/分钟")
    @RequestMapping(value = "/queryEnergyPrice", method = RequestMethod.POST)
    public EnergyResp queryEnergyPrice(@RequestBody @Valid EnergyPriceReq energyPriceReq, BindingResult result) {
        EnergyResp resp = new EnergyResp();
        ForceChannelResp forceChannelResp = new ForceChannelResp();
        try {
            if (result.hasErrors()) {
                resp.setCode(StatusCode.C.getCode());
                resp.setMsg(result.getFieldError().getDefaultMessage());
                return resp;
            }
            resp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg());
            //获取计量仪表
            CustMeter custMeter = forceChannelService.findByCustMeter(energyPriceReq.getMeterNo());
            if (custMeter == null)
                return resp;
            //获取该计量仪表的核算单元
            AccountingUnit accountingUnit = forceChannelService.findByAccountingId(custMeter.getAccountingId());
            if (accountingUnit == null)
                return resp;
            //获取企业
            CompanyCust companyCust = forceChannelService.findByCustId(accountingUnit.getCustId());
            if (companyCust == null)
                return resp;
            //获取该企业下的能源基础信息
            ElectricInfo electricInfo = forceChannelService.findElectricByCustId(companyCust.getId());
            if (electricInfo == null)
                return resp;
            Date dateReq = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(energyPriceReq.getDate());
            String reqDate = new SimpleDateFormat("HH:mm").format(dateReq);
            ElectricPriceTime electricPriceTime = forceChannelService.findElectricPriceTimeByElectricInfoId(electricInfo.getId(), reqDate);
            if (electricPriceTime == null)
                return resp;
            if ("01".equals(electricPriceTime.getType())) {  //尖
                forceChannelResp.setDate(energyPriceReq.getDate());
                forceChannelResp.setMeterNo(energyPriceReq.getMeterNo());
                forceChannelResp.setPrice(electricInfo.getTipElePrice());
                forceChannelResp.setType("01");
                forceChannelResp.setMetric("EMS." + CollectItemEnum.Epsp);
                forceChannelResp.setFormula("(" + "EMS." + CollectItemEnum.Epsp + "-" + "EMS." + CollectItemEnum.Epsp + ")" + "*" + electricInfo.getTipElePrice());
                resp.ok(forceChannelResp);
                return resp;
            } else if ("02".equals(electricPriceTime.getType())) { //峰
                forceChannelResp.setDate(energyPriceReq.getDate());
                forceChannelResp.setMeterNo(energyPriceReq.getMeterNo());
                forceChannelResp.setPrice(electricInfo.getPeakElePrice());
                forceChannelResp.setType("02");
                forceChannelResp.setMetric("EMS." + CollectItemEnum.Eppp);
                forceChannelResp.setFormula("(" + "EMS." + CollectItemEnum.Eppp + "-" + "EMS." + CollectItemEnum.Eppp + ")" + "*" + electricInfo.getPeakElePrice());
                resp.ok(forceChannelResp);
                return resp;
            } else if ("03".equals(electricPriceTime.getType())) {  //平
                forceChannelResp.setDate(energyPriceReq.getDate());
                forceChannelResp.setMeterNo(energyPriceReq.getMeterNo());
                forceChannelResp.setPrice(electricInfo.getFlatElePrice());
                forceChannelResp.setType("03");
                forceChannelResp.setMetric("EMS." + CollectItemEnum.Epfp);
                forceChannelResp.setFormula("(" + "EMS." + CollectItemEnum.Epfp + "-" + "EMS." + CollectItemEnum.Epfp + ")" + "*" + electricInfo.getFlatElePrice());
                resp.ok(forceChannelResp);
                return resp;
            } else if ("04".equals(electricPriceTime.getType())) {  //谷
                forceChannelResp.setDate(energyPriceReq.getDate());
                forceChannelResp.setMeterNo(energyPriceReq.getMeterNo());
                forceChannelResp.setPrice(electricInfo.getValleyElePrice());
                forceChannelResp.setType("04");
                forceChannelResp.setMetric("EMS." + CollectItemEnum.Epbp);
                forceChannelResp.setFormula("(" + "EMS." + CollectItemEnum.Epbp + "-" + "EMS." + CollectItemEnum.Epbp + ")" + "*" + electricInfo.getValleyElePrice());
                resp.ok(forceChannelResp);
                return resp;
            }
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.faile(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMsg());
            return resp;
        }
    }


    @ApiOperation(value = "查询蒸汽能源单价")
    @RequestMapping(value = "/querySteamPrice", method = RequestMethod.POST)
    public EnergyResp querySteamPrice(@RequestBody @Valid SteamPriceReq steamPriceReq, BindingResult result) {
        EnergyResp resp = new EnergyResp();
        ForceChannelResp forceChannelResp = new ForceChannelResp();
        try {
            if (result.hasErrors()) {
                resp.setCode(StatusCode.C.getCode());
                resp.setMsg(result.getFieldError().getDefaultMessage());
                return resp;
            }
            resp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg());
            //获取计量仪表
            CustMeter custMeter = forceChannelService.findByCustMeter(steamPriceReq.getMeterNo());
            if (custMeter == null)
                return resp;
            //获取该计量仪表的核算单元
            AccountingUnit accountingUnit = forceChannelService.findByAccountingId(custMeter.getAccountingId());
            if (accountingUnit == null)
                return resp;
            //获取企业
            CompanyCust companyCust = forceChannelService.findByCustId(accountingUnit.getCustId());
            if (companyCust == null)
                return resp;
            //获取该企业下的能源基础信息
            SteamInfo steamInfo = forceChannelService.findSteamByCustId(companyCust.getId());
            if (steamInfo == null)
                return resp;
            //拿着蒸汽信息ID + 量值 取得单价
            SteamLadderPrice steamLadderPrice = forceChannelService.findSteamLadderPriceBySteamInfoId(steamInfo.getId(), steamPriceReq.getDosage());
            if (steamLadderPrice == null)
                return resp;
            forceChannelResp.setDate(steamPriceReq.getDate());
            forceChannelResp.setMeterNo(steamPriceReq.getMeterNo());
            forceChannelResp.setPrice(steamLadderPrice.getSteamPrice());
            forceChannelResp.setType("01");
            forceChannelResp.setMetric("EMS." + CollectItemEnum.Epsp);
            forceChannelResp.setFormula("(" + "EMS." + CollectItemEnum.Epsp + "-" + "EMS." + CollectItemEnum.Epsp + ")" + "*" + steamLadderPrice.getSteamPrice());
            resp.ok(forceChannelResp);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.faile(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMsg());
            return resp;
        }
    }
}
