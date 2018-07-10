package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 车间用汽量及费用实体
 *
 * @Author: sl
 * @Date: 2018-06-07 15:20
 */
@ApiModel("车间用汽量及费用实体")
@Getter
@Setter
public class SteamMeterReadingDetailResp extends SteamMeterReadingResp implements Serializable {

    @ApiModelProperty(value="上月同比",name="lastMonthPercent",example="3.5")
    private BigDecimal lastMonthPercent;

    @ApiModelProperty(value="同期环比",name="samePeriodPercent",example="3.5")
    private BigDecimal samePeriodPercent;
}
