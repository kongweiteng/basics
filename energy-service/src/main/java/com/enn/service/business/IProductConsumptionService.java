package com.enn.service.business;

import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.bo.ElectricMeterReadingMinuteBo;
import com.enn.vo.energy.business.bo.ElectricMeterReadingMinuteStatisticsBo;
import com.enn.vo.energy.business.bo.SteamMeterReadingMinuteBo;
import com.enn.vo.energy.business.bo.SteamMeterReadingMinuteStatisticsBo;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.ProductDataPo;
import com.enn.vo.energy.business.req.CustMeterReq;
import com.enn.vo.energy.business.req.ProductConsumptionReq;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品单耗
 */
@FeignClient(value = "energy-zuul-gateway")
public interface IProductConsumptionService {

    /**
     * 查询该时间段内产品的所有生产线
     *
     * @param productConsumptionReq
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/productConsumption/findLineByProductId", method = RequestMethod.POST)
    EnergyResp<List<ProductDataPo>> findLineByProductId(@RequestBody ProductConsumptionReq productConsumptionReq);

    /**
     * 获取指定时间的总电量和费用
     *
     * @param elecMeterReadingDayBo
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/productConsumption/countElectricMeterReadingMinuteByAssignedConditon", method = RequestMethod.POST)
    EnergyResp<ElectricMeterReadingMinuteStatisticsBo> countElectricMeterReadingMinuteByAssignedConditon(@RequestBody ElectricMeterReadingMinuteBo elecMeterReadingDayBo);

     /**
     *根据生产线ID集合查询名称
     * @param custMeterReq
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/productConsumption/findLineNameByLineId", method = RequestMethod.POST)
    EnergyResp<List<AccountingUnit>> findLineNameByLineId(@RequestBody CustMeterReq custMeterReq);

    /**
     * 获取指定时间的总蒸汽量和费用
     *
     * @param steamMeterReadingDayBo
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/productConsumption/countSteamMeterReadingMinuteByAssignedConditon", method = RequestMethod.POST)
    EnergyResp<SteamMeterReadingMinuteStatisticsBo> countSteamMeterReadingMinuteByAssignedConditon(@RequestBody SteamMeterReadingMinuteBo steamMeterReadingDayBo);

    /**
     * 获取指定时间的总蒸汽费用
     *
     * @param steamMeterReadingDayBo
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/productConsumption/querySteamMeterReadingMinuteByAssignedConditon", method = RequestMethod.POST)
    EnergyResp<BigDecimal> querySteamMeterReadingMinuteByAssignedConditon(SteamMeterReadingMinuteBo steamMeterReadingDayBo);
}
