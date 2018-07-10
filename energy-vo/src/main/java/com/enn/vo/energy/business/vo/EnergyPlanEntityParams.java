package com.enn.vo.energy.business.vo;


import javax.validation.constraints.DecimalMin;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 能源计划管理Controller层 Vo实体存储参数类
 * @author kai.guo
 *
 */
@Data
@ApiModel("能源计划管理新增类")
public class EnergyPlanEntityParams {
	
	/**
     * 能源计划主键id
     */
    private Long id;

    @ApiModelProperty("能源类型")
	@NotBlank( message = "能源类型不能为空")
    private String energyType;

    @ApiModelProperty("能源提供商")
//	@NotBlank( message = "能源提供商不能为空")
    private String energyProvider;
    
    @ApiModelProperty("企业id")
    private Long custId;

    @ApiModelProperty(name="transportationDates", value="投运时间(格式：yyyy-MM-dd)", example="2018-06-13")
//    @NotBlank( message = "投运时间不能为空")
    private String transportationDates;
    
    @ApiModelProperty(name="yearPeriod", value="年度(格式:yyyy)", example="2018")
	@NotBlank( message = "年度不能为空")
    private String yearPeriod;

    @ApiModelProperty("容量")
//	@NotBlank( message = "容量不能为空")
    private String capacity;

    @ApiModelProperty("合同价格")
//	@NotBlank( message = "合同价格不能为空")
    private String contractPrice;
    
    @ApiModelProperty("本年度采购量")
	@NotBlank( message = "本年度采购量不能为空")
    @DecimalMin(value = "0", message="采购量必须为数值类型")
    private String yearPurchaseVolume;

    @ApiModelProperty("商务联系人")
//	@NotBlank( message = "商务联系人不能为空")
    private String businessPerson;

    @ApiModelProperty("商务联系人联系方式")
//	@NotBlank( message = "商务联系人联系方式不能为空")
    private String businessMobile;

    @ApiModelProperty("技术联系人")
//	@NotBlank( message = "技术联系人不能为空")
    private String techPerson;

    @ApiModelProperty("技术联系人联系方式")
//	@NotBlank( message = "技术联系人联系方式不能为空")
    private String techMobile;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("更新人")
    private String updateBy;

}