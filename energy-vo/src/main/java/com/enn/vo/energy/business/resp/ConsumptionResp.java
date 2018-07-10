package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("产品单耗分析表返回信息")
public class ConsumptionResp {

    @ApiModelProperty("区间最大综合单耗生产线")
    private String maxLineName;

    @ApiModelProperty("最大综合单耗")
    private BigDecimal maxConsumption;

    @ApiModelProperty("最大单耗生产日期")
    private String maxProduceTime;

    @ApiModelProperty("最大单耗开始时间")
    private String maxStartTime;

    @ApiModelProperty("最大单耗结束时间")
    private String maxEndTime;

    @ApiModelProperty("平均综合单耗")
    private BigDecimal avgConsumption;

    @ApiModelProperty("综合单耗差异度")
    private BigDecimal differenceDegree;

    @ApiModelProperty("区间最小综合单耗生产线")
    private String minLineName;

    @ApiModelProperty("最小综合单耗")
    private BigDecimal minConsumption;

    @ApiModelProperty("最小单耗生产日期")
    private String minProduceTime;

    @ApiModelProperty("最小单耗开始时间")
    private String minStartTime;

    @ApiModelProperty("最小单耗结束时间")
    private String minEndTime;
}
