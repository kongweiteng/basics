package com.enn.vo.energy.business.condition;


import com.enn.vo.energy.business.vo.Base;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* @author kai.guo
* @version 创建时间：2018年6月11日 下午6:47:15
* @Description 类描述
*/
@Data
@EqualsAndHashCode(callSuper=true)
public class ProductDataConditon  extends Base{/**
	 * 
	 */
	private static final long serialVersionUID = -3546012783257770943L;
	
	
    private Long id;

    private String startDate;

    private String endDate;

    private Long workshopId;
    
	/**
	 * 企业id
	 */
    private Long custId;


    private Integer delFlag;
    
    private String formatHour;
	
    private Integer pageStart;
    
    private Integer pageCustomSize;
}
