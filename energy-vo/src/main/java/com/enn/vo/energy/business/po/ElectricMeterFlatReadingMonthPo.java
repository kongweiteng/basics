package com.enn.vo.energy.business.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 月用电平时刻用电量、用电费用
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 14:36
 */
@Data
@TableName("electric_meter_flat_reading_month")
public class ElectricMeterFlatReadingMonthPo {

    /**
     * 主键ID
     */
    @TableField("id")
    private Long id;

    /**
     * 表号
     */
    @TableField("meter_no")
    private String meterNo;

    /**
     * 日期
     */
    @TableField("read_time")
    private String readTime;

    /**
     * 用电量
     */
    @TableField("use_quantity")
    private BigDecimal useQuantity;

    /**
     * 用电费用
     */
    @TableField("fees")
    private BigDecimal fees;
}
