package com.enn.vo.energy.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 产品单耗请求实体
 *
 */
@Data
@ApiModel("产品单耗请求实体")
public class ProductConsumptionReq {

    @ApiModelProperty(value = "开始时间", name = "startDate")
    @NotBlank(message = "开始时间不能为空！")
    @Length(min = 10, max = 10, message = "开始时间格式为：yyyy-MM-dd！！！")
    private String startDate;

    @ApiModelProperty(value = "结束时间", name = "endDate")
    @NotBlank(message = "结束时间不能为空！")
    @Length(min = 10, max = 10, message = "结束时间格式为：yyyy-MM-dd！！！")
    private String endDate;

    @ApiModelProperty(value = "产品ID", name = "productId")
    @NotNull(message = "产品ID不能为空！")
    private Long productId;


}