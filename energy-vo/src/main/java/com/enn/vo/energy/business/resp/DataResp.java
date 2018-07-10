package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采集值返回 -时间和数值
 */
@ApiModel("数据返回实体")
public class DataResp implements Serializable {
    @ApiModelProperty(value="时间",name="time",example="2018-04-24 23:31:22")
    private String time;

    @ApiModelProperty(value="值",name="value",example="0.059")
    private BigDecimal value;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DataResp{" +
                "time='" + time + '\'' +
                ", value=" + value +
                '}';
    }
}
