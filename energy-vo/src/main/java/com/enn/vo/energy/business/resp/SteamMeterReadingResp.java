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
public class SteamMeterReadingResp implements Serializable, Comparable<SteamMeterReadingResp>{

    @ApiModelProperty(value="日期",name="readTime",example="2018-06")
    private String readTime;

    @ApiModelProperty(value="用汽量",name="useQuantity",example="1000.00")
    private BigDecimal useQuantity;

    @ApiModelProperty(value="费用",name="fees",example="1000.00")
    private BigDecimal fees;

    public SteamMeterReadingResp() {
    }

    public SteamMeterReadingResp(String readTime, BigDecimal useQuantity, BigDecimal fees) {
        this.readTime = readTime;
        this.useQuantity = useQuantity;
        this.fees = fees;
    }

    //覆盖Comparable接口里的compareTo方法
    @Override
    public int compareTo(SteamMeterReadingResp o) {
        // 以useQuantity进行比较
        return readTime.compareTo(o.getReadTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SteamMeterReadingResp resp = (SteamMeterReadingResp) o;

        return readTime != null ? readTime.equals(resp.readTime) : resp.readTime == null;
    }

    @Override
    public int hashCode() {
        return readTime != null ? readTime.hashCode() : 0;
    }
}
