package com.enn.vo.energy.business.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月11日 下午3:52:51
* @Description 产品数据统计
*/
@Data
public class ProductDataStatisticsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5244318100750120104L;
	
	/**
	 * 产品id
	 */
	private Long productId;
	
	/**
	 * 总数量
	 */
	private BigDecimal totalNumber;
	
	/**
	 * 总重量（Kg）
	 */
	private BigDecimal totalWeight;
	
	/**
	 * 产品数据统计Item项
	 */
	private List<ProductDataStatisticsItemDto> dataStatisticsItem;

}
