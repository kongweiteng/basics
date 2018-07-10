package com.enn.vo.energy.business.vo;


import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* @author kai.guo
* @version 创建时间：2018年6月11日 下午1:08:17
* @Description 类描述
*/

@ApiModel("生产数据统计请求实体")
@Data
@EqualsAndHashCode(callSuper=true)
public class ProductDataStatisticsQueryParams extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4838272304327481414L;
	
	/**
	 * 时间维度
	 */
	@ApiModelProperty(name="timeDimension", value="1代表年,2:表示月,3表示日")
	@NotBlank(message="时间维度不能为空")
	private String timeDimension;
	
	/**
	 * 起始时间
	 */
	@ApiModelProperty(name="startDate", value="起始时间(年格式：yyyy-MM   月格式:yyyy-MM-dd   日格式:yyyy-MM-dd HH:mm)", example="2018-06-13 11:00")
	@NotBlank(message="查询起始时间不能为空")
	private String startDate;
	
	/**
	 * 终止时间
	 */
	@ApiModelProperty(name="endDate", value="起始时间(年格式：yyyy-MM   月格式:yyyy-MM-dd   日格式:yyyy-MM-dd HH:mm)", example="2018-06-13 11:00")
	@NotBlank(message="查询终止时间不能为空")
	private String endDate;
	
	/**
	 * 车间id
	 */
	@ApiModelProperty("车间id")
//	@NotBlank(message="车间id不能为空")
	private Long workShopId;
	
	
	/**
	 * 企业id
	 */
	@ApiModelProperty("企业id")
	@NotNull(message="企业id不能为空")
    private Long custId;

}
