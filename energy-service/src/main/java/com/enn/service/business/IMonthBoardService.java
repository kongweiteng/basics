package com.enn.service.business;

import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.req.MonthBoardParamRep;
import com.enn.vo.energy.business.resp.MonthEnergyBoardResp;
import com.enn.vo.energy.business.resp.SteamMeterReadingResp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
* @author sl
* @version 创建时间：2018年6月11日 上午11:33:52
* @Description 月能源看板
*/
@FeignClient(value = "energy-zuul-gateway")
public interface IMonthBoardService {

	/**
	 * 月能源看板
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/energy-proxy/energy-provider-business/monthEnergy/boderData",method=RequestMethod.POST)
    public EnergyResp<MonthEnergyBoardResp> boderData(@RequestBody MonthBoardParamRep req);


    /**
     * 月能源看板-用汽曲线
     * @param req
     * @return
     */
    @RequestMapping(value="/energy-proxy/energy-provider-business/monthEnergy/boderSteamCurve",method=RequestMethod.POST)
    public EnergyResp<List<SteamMeterReadingResp>> boderSteamCurve(@RequestBody MonthBoardParamRep req);
}
