package com.enn.vo.energy.business.req;

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
public class ProduceCurveReq {

    @ApiModelProperty(value = "开始时间", name = "startDate")
    @NotBlank(message = "开始时间不能为空！")
    @Length(min = 16, max = 16, message = "开始时间格式为：yyyy-MM-dd HH:mm！！！")
    private String startDate;

    @ApiModelProperty(value = "结束时间", name = "endDate")
    @NotBlank(message = "结束时间不能为空！")
    @Length(min = 16, max = 16, message = "结束时间格式为：yyyy-MM-dd HH:mm！！！")
    private String endDate;

    @ApiModelProperty(value = "生产线ID", name = "lineId")
    @NotNull(message = "生产线ID不能能为空！")
    private Long lineId;

    @ApiModelProperty(value = "生产线名称", name = "name")
    @NotNull(message = "生产线名称不能能为空！")
    private String name;

    @ApiModelProperty(value = "生产线表号", name = "equipID")
    @NotNull(message = "生产线表号不能能为空！")
    private String equipID;

}