package com.enn.energy.business.rest;

import com.enn.energy.business.service.IMaxDemandService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.po.DataResp;
import com.enn.vo.energy.business.req.DemandReq;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/demand")
@Api( tags="需量管理")
public class MaxDemandController {

    private IMaxDemandService maxDemandService;
    public EnergyResp<List<DataResp>> findDemandValue(DemandReq demandReq){
        EnergyResp<List<DataResp>> energyResp = new EnergyResp<>();
        try{
            maxDemandService.findMaxDemand(demandReq);}catch (Exception e){

        }
        return energyResp;
    }
}
