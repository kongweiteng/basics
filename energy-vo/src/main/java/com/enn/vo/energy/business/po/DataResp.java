package com.enn.vo.energy.business.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采集值返回 -时间和数值
 * @Author: xiaomingyu
 */
@Data
@ApiModel("曲线图实体类")
public class DataResp implements Serializable, Comparable<DataResp> {
	@ApiModelProperty(value="时间",name="time",example="2018-04-24 23:31:22")
	private String dateTime;

	@ApiModelProperty(value="值",name="value",example="0.059")
	private BigDecimal dateValue;

    public DataResp() {
    }

    public DataResp(String dateTime, BigDecimal dateValue) {
        this.dateTime = dateTime;
        this.dateValue = dateValue;
    }

    @Override
    public int compareTo(DataResp o) {
        return dateTime.compareTo(o.getDateTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataResp resp = (DataResp) o;

        return dateTime != null ? dateTime.equals(resp.dateTime) : resp.dateTime == null;
    }

    @Override
    public int hashCode() {
        return dateTime != null ? dateTime.hashCode() : 0;
    }
}
