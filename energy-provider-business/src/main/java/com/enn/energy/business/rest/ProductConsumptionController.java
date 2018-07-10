package com.enn.energy.business.rest;


import com.enn.constant.StatusCode;
import com.enn.energy.business.service.*;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.bo.*;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.ProductDataPo;
import com.enn.vo.energy.business.req.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;


@Api(tags = {"产品单耗分析"})
@RestController
@RequestMapping("/productConsumption")
public class ProductConsumptionController {

    @Autowired
    private IProductConsumptionService productConsumptionService;

    /**
     * {
     * "produceCurveReq": [
     * {
     * "startDate": "2018-06-10 06:00:00",
     * "endDate": "2018-06-10 08:40:00",
     * "lineId": "13",
     * "name": "达利电表1号总进",
     * "equipID": "METE340340001975"
     * },
     * {
     * "startDate": "2018-06-10 06:00:00",
     * "endDate": "2018-06-10 08:40:00",
     * "lineId": "23",
     * "name": "电表",
     * "equipID": "METE340340003197"
     * }
     * ]
     * }
     */
    @ApiOperation(value = "根据时间区间查询产品的生产线")
    @RequestMapping(value = "/findLineByProductId", method = RequestMethod.POST)
    public EnergyResp<List<ProductDataPo>> findLineByProductId(@RequestBody ProductConsumptionReq productConsumptionReq) {
        EnergyResp<List<ProductDataPo>> resp = new EnergyResp<List<ProductDataPo>>();
        try {
            List<ProductDataPo> productDataPoList = productConsumptionService.findLineByProductId(productConsumptionReq.getStartDate(), productConsumptionReq.getEndDate(), productConsumptionReq.getProductId());
            resp.ok(productDataPoList);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.faile(StatusCode.B.getCode(), StatusCode.B.getMsg());
            return resp;
        }
    }

    @ApiOperation(value = "查询指定条件下的Minute用电统计")
    @RequestMapping(value = "/countElectricMeterReadingMinuteByAssignedConditon", method = RequestMethod.POST)
    public EnergyResp<ElectricMeterReadingMinuteStatisticsBo> countElectricMeterReadingMinuteByAssignedConditon(@RequestBody ElectricMeterReadingMinuteBo elecMeterReadingDayBo) {
        EnergyResp<ElectricMeterReadingMinuteStatisticsBo> resp = new EnergyResp<ElectricMeterReadingMinuteStatisticsBo>();
        try {
            ElectricMeterReadingMinuteStatisticsBo electricMeterReadingMinuteStatisticsBo = productConsumptionService.countElectricMeterReadingMinuteByAssignedConditon(elecMeterReadingDayBo);
            resp.ok(electricMeterReadingMinuteStatisticsBo);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.faile(StatusCode.B.getCode(), StatusCode.B.getMsg());
            return resp;
        }
    }


    @ApiOperation(value = "获取指定时间的总蒸汽量和费用")
    @RequestMapping(value = "/countSteamMeterReadingMinuteByAssignedConditon", method = RequestMethod.POST)
    public EnergyResp<SteamMeterReadingMinuteStatisticsBo> countSteamMeterReadingMinuteByAssignedConditon(@RequestBody SteamMeterReadingMinuteBo steamMeterReadingDayBo) {
        EnergyResp<SteamMeterReadingMinuteStatisticsBo> resp = new EnergyResp<SteamMeterReadingMinuteStatisticsBo>();
        try {
            SteamMeterReadingMinuteStatisticsBo steamMeterReadingMinuteStatisticsBo = productConsumptionService.countSteamMeterReadingMinuteByAssignedConditon(steamMeterReadingDayBo);
            resp.ok(steamMeterReadingMinuteStatisticsBo);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.faile(StatusCode.B.getCode(), StatusCode.B.getMsg());
            return resp;
        }
    }

    @ApiOperation(value = "获取指定时间的总蒸汽费用")
    @RequestMapping(value = "/querySteamMeterReadingMinuteByAssignedConditon", method = RequestMethod.POST)
    public EnergyResp<BigDecimal> querySteamMeterReadingMinuteByAssignedConditon(@RequestBody SteamMeterReadingMinuteBo steamMeterReadingDayBo) {
        EnergyResp<BigDecimal> resp = new EnergyResp<BigDecimal>();
        try {
            BigDecimal bigDecimal = productConsumptionService.querySteamMeterReadingMinuteByAssignedConditon(steamMeterReadingDayBo);
            resp.ok(bigDecimal);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.faile(StatusCode.B.getCode(), StatusCode.B.getMsg());
            return resp;
        }
    }

    @ApiOperation(value = "根据生产线ID集合查询名称")
    @RequestMapping(value = "/findLineNameByLineId", method = RequestMethod.POST)
    public EnergyResp<List<AccountingUnit>> findLineNameByLineId(@RequestBody CustMeterReq custMeterReq) {
        EnergyResp<List<AccountingUnit>> resp = new EnergyResp<List<AccountingUnit>>();
        try {
            List<Long> lineIdList = custMeterReq.getIds();
            List<AccountingUnit> accountingUnitList = productConsumptionService.findLineNameByLineId(lineIdList);
            resp.ok(accountingUnitList);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp.faile(StatusCode.B.getCode(), StatusCode.B.getMsg());
            return resp;
        }
    }
}
