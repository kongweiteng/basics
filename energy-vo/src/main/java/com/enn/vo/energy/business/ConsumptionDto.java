package com.enn.vo.energy.business;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 生产成本分析 单耗排序pojo
 */
@Data
public class ConsumptionDto {
    //生产线ID
    private Long lineId;

    //生产线表号
    private List<String> loopNumber;

    //生产产品数
    private BigDecimal number;

    //开始时间
    private String startDate;

    //结束时间
    private String endDate;

    //总电量
    private BigDecimal eleCount;

    //单耗
    private BigDecimal consumption;

    //核算单元名称
    private String meterName;


}
