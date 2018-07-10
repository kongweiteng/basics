package com.enn.vo.energy.business.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月11日 下午3:55:22
* @Description 类描述
*/
@Data
public class ProductDataStatisticsItemDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7040198018614244780L;
	
	
    /**
     * 车间ID
     */
    private Long workshopId;

    /**
     * 车间名称
     */
    private String workshopName;
    
    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 数量
     */
    private BigDecimal number;
    
    /**
     * 规格
     */
    private String specification;
    
    /**
     * 重量（Kg）
     */
    private BigDecimal weight;

}
