package com.enn.service.business;

import java.util.List;

import javax.validation.Valid;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.dto.DepartUnitDto;
import com.enn.vo.energy.business.dto.ProductDataStatisticsDto;
import com.enn.vo.energy.business.vo.ProductDataStatisticsQueryParams;

/**
* @author kai.guo
* @version 创建时间：2018年6月11日 上午11:33:52
* @Description 产品数据统计服务
*/
@FeignClient(value = "energy-zuul-gateway")
public interface IProductDataStatisticsFacade {
	
	
	/**
	 * 查询指定企业id下的车间信息
	 * @param obj
	 * @return
	 */
	@RequestMapping(value="/energy-proxy/energy-provider-business/productDataStatistics/queryProductionDepartment",method=RequestMethod.POST)
	public EnergyResp<List<DepartUnitDto>> queryProductionDepartment(@RequestParam("companyId") Long companyId);
	
	/**
	 * 生产数据统计
	 * @param queryParams
	 * @return
	 */
	@RequestMapping(value="/energy-proxy/energy-provider-business/productDataStatistics/queryProductDataStatisticsInfo",method=RequestMethod.POST)
	EnergyResp<PagedList<ProductDataStatisticsDto>>	queryProductDataStatisticsInfoByAssignedCondition(@RequestBody @Valid ProductDataStatisticsQueryParams queryParams);
		

}
