package com.enn.vo.energy.business.vo;

import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 能源计划管理前端Vo实体，查询参数实体
 * @author kai.guo
 *
 */
@ApiModel("能源计划管理请求实体")
@Data
@EqualsAndHashCode(callSuper=true)
public class EnergyPlanQueryParams extends Base {




	/**
	 * 
	 */
	private static final long serialVersionUID = -3911914503811755118L;

	@ApiModelProperty("能源类型")
	private List<String> energyTypes;

	@ApiModelProperty("查询年度")
	@NotBlank( message = "查询年度不能为空")
    private String yearPeriod;
	
	
    @ApiModelProperty("企业id")
    @NotNull(message ="企业id不能为空")
    @DecimalMin(value = "0", message="企业id必须为数值类型")
    private Long custId;
    
}
