package com.enn.vo.energy.business.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 日用电谷时刻用电量、用电费用
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 14:40
 */
@Data
@TableName("electric_meter_valley_reading_day")
public class ElectricMeterValleyReadingDayPo {

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
