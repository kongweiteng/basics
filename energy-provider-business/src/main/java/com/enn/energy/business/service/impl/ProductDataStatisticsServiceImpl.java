package com.enn.energy.business.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.enn.energy.business.service.IProduceOnlineMonitorService;
import com.enn.energy.business.service.IProductDataService;
import com.enn.energy.business.service.IProductDataStatisticsService;
import com.enn.energy.system.common.util.CommonConverter;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.business.bo.ProductDataStatisticsQueryBo;
import com.enn.vo.energy.business.bo.ProductDataStatisticsQueryResultBo;
import com.enn.vo.energy.business.dto.DepartUnitDto;
import com.enn.vo.energy.business.dto.ProductDataStatisticsDto;
import com.enn.vo.energy.business.dto.ProductDataStatisticsItemDto;
import com.enn.vo.energy.business.resp.ProductDataResp;
import com.google.common.collect.Lists;

/**
* @author kai.guo
* @version 创建时间：2018年6月11日 上午11:34:47
* @Description 产品数据统计服务
*/
@Service
public class ProductDataStatisticsServiceImpl implements IProductDataStatisticsService {
	
	@Autowired
	private IProduceOnlineMonitorService produceOnlineMonitorService;
	
	@Autowired
	private IProductDataService productDataService;
	
	private static final Logger logger=LoggerFactory.getLogger(ProductDataStatisticsServiceImpl.class);

	@Override
	public List<DepartUnitDto> queryProductionDepartment(Long companyId) {
		
		return produceOnlineMonitorService.queryProductionDepartment(companyId);
	}
	
	/**
	 * 判断产品规格类型
	 * @param specification
	 * @return
	 */
	private Boolean judgeSpecificationType(String specification){
		Boolean result=true;
		try {
			new BigDecimal(specification);
		} catch (Exception e) {
			logger.error("【产品数据统计】,method:[queryProductDataStatisticsInfoByAssignedCondition],产品specification:{}类型异常!",specification);
			result=false;
			return result;
		}
		
		if(StringUtils.isEmpty(specification)){
			result=false;
		}
		return result;
	}

	@Override
	public PagedList<ProductDataStatisticsDto> queryProductDataStatisticsInfoByAssignedCondition(ProductDataStatisticsQueryBo queryBo) {
		
		PagedList<ProductDataResp> pagedList=productDataService.getRespPageListForProductDataStatistics(queryBo);
		
		
		List<ProductDataStatisticsDto>  dataStatisticsDtos=Lists.newArrayList();
		List<ProductDataResp> productDataResps=pagedList.getData();
		
		Map<Long,List<ProductDataResp>> dataRespMaps=productDataResps.stream().collect(Collectors.groupingBy(ProductDataResp::getProductId));
		Set<Entry<Long,List<ProductDataResp>>> dataSets=dataRespMaps.entrySet();
		for(Entry<Long,List<ProductDataResp>> entry: dataSets){
			List<ProductDataResp> dataResps=entry.getValue();
			ProductDataStatisticsDto dataStatisticsDto=new ProductDataStatisticsDto();
			List<ProductDataStatisticsQueryResultBo> queryResultBos=Lists.newArrayList();
			
			BigDecimal totalNumber=dataResps.stream().filter(p->p.getNumber()!=null).collect(Collectors.reducing(BigDecimal.ZERO,ProductDataResp::getNumber, BigDecimal::add));
			
			BigDecimal totalWeight=dataResps.stream().filter(p->p.getNumber()!=null && judgeSpecificationType(p.getSpecification()))
							.map(m->MathUtils.mul(m.getNumber(), new BigDecimal(m.getSpecification()),4))
							.collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add));
			
			dataResps.stream().forEach(p->{
				try {
					ProductDataStatisticsQueryResultBo resultBo=CommonConverter.map(p, ProductDataStatisticsQueryResultBo.class);
					BigDecimal number=resultBo.getNumber()==null?BigDecimal.ZERO:resultBo.getNumber();
					BigDecimal spercification=judgeSpecificationType(p.getSpecification())?new BigDecimal(p.getSpecification()):BigDecimal.ZERO;
					BigDecimal weight=MathUtils.mul(number, spercification,2);
					resultBo.setWeight(weight);
					resultBo.setNumber(number.setScale(2,  BigDecimal.ROUND_HALF_UP));
					resultBo.setSpecification(spercification.toString());
					queryResultBos.add(resultBo);
				} catch (Exception e) {
					logger.error("【产品数据统计】,method:[queryProductDataStatisticsInfoByAssignedCondition],数据项统计异常，异常原因reason:{}",e.getMessage());
				}
			});
			
			List<ProductDataStatisticsItemDto> dataStatisticsItem=CommonConverter.mapList(queryResultBos, ProductDataStatisticsItemDto.class);
			dataStatisticsDto.setTotalNumber(totalNumber.setScale(2, BigDecimal.ROUND_HALF_UP));
			dataStatisticsDto.setTotalWeight(totalWeight.setScale(2, BigDecimal.ROUND_HALF_UP));
			dataStatisticsDto.setDataStatisticsItem(dataStatisticsItem);
			dataStatisticsDto.setProductId(entry.getKey());
			
			dataStatisticsDtos.add(dataStatisticsDto);
		}
		
		PagedList<ProductDataStatisticsDto> pagedListDto = PagedList.newMe(pagedList.getPageNum(), pagedList.getPageSize(), pagedList.getTotalCount(), dataStatisticsDtos);
		
		return pagedListDto;
	}

}
