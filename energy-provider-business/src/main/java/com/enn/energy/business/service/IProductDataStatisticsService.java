package com.enn.energy.business.service;

import java.util.List;

import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.business.bo.ProductDataStatisticsQueryBo;
import com.enn.vo.energy.business.dto.DepartUnitDto;
import com.enn.vo.energy.business.dto.ProductDataStatisticsDto;

/**
* @author kai.guo
* @version 创建时间：2018年6月11日 上午11:33:52
* @Description 产品数据统计服务
*/
public interface IProductDataStatisticsService {
	
	
	/**
	 * 查询指定企业id下的车间信息
	 * @param obj
	 * @return
	 */
	List<DepartUnitDto> queryProductionDepartment(Long companyId);
	
	/**
	 * 生产数据统计
	 * @param queryBo
	 * @return
	 */
	PagedList<ProductDataStatisticsDto>	queryProductDataStatisticsInfoByAssignedCondition(ProductDataStatisticsQueryBo queryBo);

}
