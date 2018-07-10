package com.enn.energy.business.rest;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.enn.energy.business.service.IProductDataStatisticsService;
import com.enn.energy.system.common.util.CommonConverter;
import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.bo.ProductDataStatisticsQueryBo;
import com.enn.vo.energy.business.dto.DepartUnitDto;
import com.enn.vo.energy.business.dto.ProductDataStatisticsDto;
import com.enn.vo.energy.business.vo.ProductDataStatisticsQueryParams;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
* @author kai.guo
* @version 创建时间：2018年6月12日 下午12:38:54
* @Description 生产数据统计
*/
@RestController
@RequestMapping("/productDataStatistics")
@Api( tags="产品数据统计-日/月/年")
public class ProductDataStatisticsController {
	
	private static final Logger logger=LoggerFactory.getLogger(ProductDataStatisticsController.class);
	
	@Autowired
	private IProductDataStatisticsService productDataStatisticsService;
	
	
	/**
	 * 查询指定企业id的生产车间列表
	 * @param companyId
	 */
	@ApiOperation(value="查询指定企业的生产车间列表", notes="根据企业id查询生产车间列表")
	@RequestMapping(value="/queryProductionDepartment",method=RequestMethod.POST)
	public EnergyResp<List<DepartUnitDto>> queryProductionDepartment(@RequestParam("companyId") Long companyId){
		
		logger.info("【产品数据统计-日/月/年】,查询企业生产车间列表, method:[queryProductionDepartment],companyId:{}",companyId);
		
		List<DepartUnitDto> departUnitBos=productDataStatisticsService.queryProductionDepartment(companyId);
		EnergyResp<List<DepartUnitDto>> resp = new EnergyResp<>();
		resp.ok(departUnitBos);
		return resp;
	}
	
	/**
	 * 生产数据统计
	 * @param queryBo
	 * @return
	 */
	@ApiOperation(value="查询指定车间的生产数据信息", notes="查询指定车间的生产数据信息")
	@RequestMapping(value="/queryProductDataStatisticsInfo",method=RequestMethod.POST)
	EnergyResp<PagedList<ProductDataStatisticsDto>>	queryProductDataStatisticsInfoByAssignedCondition(@RequestBody @Valid ProductDataStatisticsQueryParams queryParams){
		
		logger.info("【产品数据统计-日/月/年】,查询指定车间生产数据信息, method:[queryProductDataStatisticsInfoByAssignedCondition],queryParams:{}",JSON.toJSON(queryParams));
		
		ProductDataStatisticsQueryBo queryBo=CommonConverter.map(queryParams, ProductDataStatisticsQueryBo.class);
		
		PagedList<ProductDataStatisticsDto> pagedList=productDataStatisticsService.queryProductDataStatisticsInfoByAssignedCondition(queryBo);
		EnergyResp<PagedList<ProductDataStatisticsDto>> resp = new EnergyResp<>();
		resp.ok(pagedList);
		return resp;
	}
	

}
