package com.enn.vo.energy.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 告警信息
 */
@ApiModel(value = "首页事件信息")
public class MonitorNum implements Serializable {
    private static final long serialVersionUID = -3286564461647015366L;
    @ApiModelProperty(value="告警类型",name="alarmType",example="01")
    private String alarmType;   //告警类型

    @ApiModelProperty(value="告警等级（01红色、02黄色、03绿色）",name="alarmGrade",example="01")
    private String alarmGrade;  //告警类型

    @ApiModelProperty(value="告警次数",name="value",example="120")
    private int value;       //告警次数

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmGrade() {
        return alarmGrade;
    }

    public void setAlarmGrade(String alarmGrade) {
        this.alarmGrade = alarmGrade;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

}
