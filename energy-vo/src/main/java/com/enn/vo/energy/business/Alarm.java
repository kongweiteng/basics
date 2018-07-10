package com.enn.vo.energy.business;

/**
 * Created by kexing on 2018/4/14.
 */
public class Alarm {

    private String equipID;
    private String alarm_type;
    private String alarm_level;
    private String alarm_time;
    private String desp;
    private String metric;
    private Double value;

    public String getEquipID() {
        return equipID;
    }

    public void setEquipID(String equipID) {
        this.equipID = equipID;
    }

    public String getAlarm_type() {
        return alarm_type;
    }

    public void setAlarm_type(String alarm_type) {
        this.alarm_type = alarm_type;
    }

    public String getAlarm_level() {
        return alarm_level;
    }

    public void setAlarm_level(String alarm_level) {
        this.alarm_level = alarm_level;
    }

    public String getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "equipID='" + equipID + '\'' +
                ", alarm_type='" + alarm_type + '\'' +
                ", alarm_level='" + alarm_level + '\'' +
                ", alarm_time='" + alarm_time + '\'' +
                ", desp='" + desp + '\'' +
                ", metric='" + metric + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}
