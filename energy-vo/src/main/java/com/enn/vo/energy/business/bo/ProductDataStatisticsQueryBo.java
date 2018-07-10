package com.enn.vo.energy.business.bo;



import com.enn.vo.energy.business.vo.Base;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* @author kai.guo
* @version 创建时间：2018年6月11日 下午1:08:17
* @Description 类描述
*/

@Data
@EqualsAndHashCode(callSuper=true)
public class ProductDataStatisticsQueryBo extends Base {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 241288501269528676L;

	/**
	 * 时间维度
	 */
	private String timeDimension;
	
	/**
	 * 起始时间
	 */
	private String startDate;
	
	/**
	 * 终止时间
	 */
	private String endDate;
	
	/**
	 * 车间id
	 */
	private Long workShopId;
	
	/**
	 * 企业id
	 */
    private Long custId;

}
