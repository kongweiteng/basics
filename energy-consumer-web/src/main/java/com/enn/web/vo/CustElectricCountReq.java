package com.enn.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 企业用电统计请求实体
 *
 * @Author: 张洪源
 * @Date: 2018-06-08 16:20
 */
@Data
@ApiModel("企业用电")
public class CustElectricCountReq {

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    @NotNull(message = "custId 不能为空！")
    private Long custId;

    /**
     * 时间维度（1=day，2=month，3=year)
     */
    @ApiModelProperty("时间维度（1=day，2=month，3=year)")
    @NotNull(message = "dateType 不能为空！")
    private Integer dateType;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @NotBlank(message = "startTime 不能为空！")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @NotBlank(message = "endTime 不能为空！")
    private String endTime;
}
