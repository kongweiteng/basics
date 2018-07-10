package com.enn.energy.business.rest;


import com.enn.constant.StatusCode;
import com.enn.energy.business.service.ICustMeterService;
import com.enn.energy.business.service.ICustWifiService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.po.*;
import com.enn.vo.energy.business.req.CustMeterReq;
import com.enn.vo.energy.business.req.MeterListReq;
import com.enn.vo.energy.business.resp.MeterResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequestMapping("/custMeter")
@RestController
@Api(value = "计量仪表信息", description = "计量仪表信息接口", tags = "计量仪表信息")
public class CustMeterController {

    private static final Logger logger = LoggerFactory.getLogger(ElectricityDistributionRoomController.class);

    @Autowired
    private ICustMeterService custMeterService;

    @Autowired
    private ICustWifiService custWifiService;

    @RequestMapping(value = "/getCustMeterByDistributionId", method = RequestMethod.POST)
    @ApiOperation(value = "查询计量仪表信息", notes = "根据配电室ID查询计量仪表信息")
    public EnergyResp<List<DistributionMeter>> getCustMeterByDistributionId(@RequestBody CustMeterReq custMeterReq) {
        EnergyResp<List<DistributionMeter>> energyResp= new EnergyResp<>();
        Long[] ids = new Long[custMeterReq.getIds().size()];
        custMeterReq.getIds().toArray(ids);
        List<DistributionMeter> custMeterList = custMeterService.getCustMeterByDistributionId(ids);

        energyResp.ok(custMeterList);
        return energyResp;
    }

    /**
     * 根据企业id查询所有的计量表信息
     */
    @ApiOperation(value = "根据企业id查询所有的计量表信息", notes = "根据企业id查询所有的计量表信息")
    @RequestMapping(value = "/getAllMeter", method = RequestMethod.POST)

    public EnergyResp<List<MeterResp>> getAllMeter(@RequestBody MeterListReq meterListReq){
        EnergyResp<List<MeterResp>> allMeter = custMeterService.getAllMeter(meterListReq);
        return allMeter;
    }



    /**
     * 保存 计量表信息
     */
    @ApiOperation(value = "保存计量表信息", notes = "保存计量表信息(id为空时 insert，id不为空时 update)")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public EnergyResp<Integer> save(@RequestBody @Valid CustMeter meter, BindingResult result){
        EnergyResp<Integer> resp = new EnergyResp<>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        resp = custMeterService.save(meter);
        return resp;
    }


    /**
     *  根据 计量表id  查询计量表所在的 wifi
     */
    @ApiOperation(value = "根据 计量表id  查询计量表所在的 wifi", notes = "根据 计量表id  查询计量表所在的 wifi")
    @RequestMapping(value = "/getWifiByMeteId", method = RequestMethod.POST)
    public EnergyResp<CustWifi> getWifiByMeteId(@RequestBody @Valid DefaultReq id, BindingResult result){
        EnergyResp<CustWifi> resp = new EnergyResp<>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        CustMeter custMeter = custMeterService.selectById(id.getId());
        CustWifi custWifi =new CustWifi();
        if(custMeter!=null && custMeter.getWifiId()!=null){
            //查询所在的wifi 信息
             custWifi = custWifiService.selectById(custMeter.getWifiId());
        }
        resp.ok(custWifi);
        return resp;
    }


    /**
     * 根据企业id查询所有的计量表信息
     */
    @ApiOperation(value = "根据计量表id查询计量表信息", notes = "根据计量表id查询计量表信息")
    @RequestMapping(value = "/getMeterByMeterId", method = RequestMethod.POST)

    public EnergyResp<CustMeter> getMeterByMeterId(@RequestBody @Valid DefaultReq defaultReq, BindingResult result){
        EnergyResp<CustMeter> resp = new EnergyResp<>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        try {
            CustMeter custMeter = custMeterService.selectById(defaultReq.getId());
            if("01".equals(custMeter.getEnergyType())){
                custMeter.setLoopNumber(custMeter.getLoopNumber());
            }
            resp.ok(custMeter);
        } catch (Exception e) {
            resp.setCode(StatusCode.A.getCode());
            resp.setMsg(StatusCode.A.getMsg());
            e.printStackTrace();
        }
        return resp;
    }


    @RequestMapping(value = "/getCustMetersByDistributionId", method = RequestMethod.POST)
    @ApiOperation(value = "查询计量仪表信息", notes = "根据配电室ID查询计量仪表信息")
    public EnergyResp<List<DistributionMeter>> getCustMetersByDistributionId(@RequestBody DefaultReq defaultReq) {
        EnergyResp<List<DistributionMeter>> energyResp= new EnergyResp<>();
        List<DistributionMeter> custMeterList = custMeterService.getCustMetersByDistributionId(defaultReq.getId());
        energyResp.ok(custMeterList);
        return energyResp;
    }


}
