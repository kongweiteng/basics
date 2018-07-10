package com.enn.app.controller;

import com.enn.constant.StatusCode;
import com.enn.service.business.IAccountUnitService;
import com.enn.service.business.ICustMeterService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.req.AccountUnitReq;
import com.enn.vo.energy.business.req.CompanyMeteReq;
import com.enn.vo.energy.business.req.MeterListReq;
import com.enn.vo.energy.business.resp.MeterResp;
import com.enn.vo.energy.business.resp.UnitResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BaseController {
    private static Logger logger = LoggerFactory.getLogger(BaseController.class);
    @Autowired
    public IAccountUnitService accountUnitService;
    /**
     * 根据企业ID获取主电表号
     */
    public EnergyResp<List<String>> findElecNosByCust(String custId) {
        EnergyResp<List<String>> listResp = new EnergyResp<>();

        CompanyMeteReq meteReq = new CompanyMeteReq();
        meteReq.setId(Long.parseLong(custId));
        meteReq.setEnergyType("01");
        EnergyResp<List<MeterResp>> listEnergyResp = accountUnitService.queryMeterListByCompanyAndType(meteReq);
        logger.info("获取企业下主电表号");
        List<String> equipIds = new ArrayList<>();
        if (StatusCode.SUCCESS.getCode().equals(listEnergyResp.getCode())) {
            List<MeterResp> meterRespList = listEnergyResp.getData();
            for (MeterResp meterResp : meterRespList) {
                equipIds.add(meterResp.getLoopNumber());
            }
            logger.info("获取的主电表号为："+equipIds.toString());
            listResp.ok(equipIds);
        } else {
            listResp.setMsg(listEnergyResp.getMsg());
            listResp.setCode(listEnergyResp.getCode());
        }
        return listResp;
    }

    /**
     * 根据企业ID获取主蒸汽表号
     */
    public EnergyResp<List<String>> findSteamNosByCust(String custId) {
        EnergyResp<List<String>> listResp = new EnergyResp<>();
        CompanyMeteReq meteReq = new CompanyMeteReq();
        DefaultReq defaultReq = new DefaultReq();
        defaultReq.setId(Long.parseLong(custId));
        EnergyResp<ListResp<AccountingUnit>> accountResp = accountUnitService.getParentEqu(defaultReq);
        if(!StatusCode.SUCCESS.getCode().equals(accountResp.getCode())){
            listResp.setMsg(accountResp.getMsg());
            listResp.setCode(accountResp.getCode());
            return listResp;
        }
        if(accountResp.getData()!=null && accountResp.getData().getList().size()>0){
            Long accountId = accountResp.getData().getList().get(0).getId();
            AccountUnitReq accountUnitReq = new AccountUnitReq();
            accountUnitReq.setAccountingType("00");
            accountUnitReq.setIsAccount(true);
            accountUnitReq.setId(accountId);
            EnergyResp<List<UnitResp>> listEnergyResp = accountUnitService.queryAccountList(accountUnitReq);
            if(!StatusCode.SUCCESS.getCode().equals(listEnergyResp.getCode())){
                listResp.setMsg(listEnergyResp.getMsg());
                listResp.setCode(listEnergyResp.getCode());
                return listResp;
            }
            if(listEnergyResp.getData()!=null && listEnergyResp.getData().size()>0){
                List<MeterResp> meterRespList = listEnergyResp.getData().get(0).getMeters();
                List<String> equipIds = new ArrayList<>();
                for(MeterResp meterResp:meterRespList){
                    if("02".equals(meterResp.getEnergyType())){
                        equipIds.add(meterResp.getLoopNumber());
                    }
                }
                listResp.ok(equipIds);

            }
        }
//        AccountUnitReq accountUnitReq = new AccountUnitReq();
//        accountUnitReq.setAccountingType("");
//        meteReq.setId(Long.parseLong(custId));
//        meteReq.setEnergyType("02");
//        logger.info("获取企业下主蒸汽表号");
//        EnergyResp<List<MeterResp>> listEnergyResp = accountUnitService.unitListByCondition(meteReq);
//        List<String> equipIds = new ArrayList<>();
//        if (StatusCode.SUCCESS.getCode().equals(listEnergyResp.getCode())) {
//            List<MeterResp> meterRespList = listEnergyResp.getData();
//            for (MeterResp meterResp : meterRespList) {
//                equipIds.add(meterResp.getLoopNumber());
//            }
//            logger.info("获取的主蒸汽表号为："+equipIds.toString());
//            listResp.ok(equipIds);
//        } else {
//            listResp.setMsg(listEnergyResp.getMsg());
//            listResp.setCode(listEnergyResp.getCode());
//        }
        return listResp;
    }
}
