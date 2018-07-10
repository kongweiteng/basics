package com.enn.vo.energy.business;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kexing on 2018/4/16.
 */
public class CusAlarmValues implements Serializable {

    private static final long serialVersionUID = -3286564461647015367L;
    private String cusId;
    private Map<String,MonitorNum> alarmMap;

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public Map<String, MonitorNum> getAlarmMap() {
        return alarmMap;
    }

    public CusAlarmValues(){
        alarmMap=new HashMap<String,MonitorNum>();
        for(int i=1;i<=4;i++){
            String type="0"+i;
            MonitorNum monitorNum=new MonitorNum();
            monitorNum.setAlarmGrade("03");
            monitorNum.setAlarmType(type);
            monitorNum.setValue(0);
            alarmMap.put(type,monitorNum);
        }
    }

    public void resetValues(){
        for(String key:alarmMap.keySet()){
            alarmMap.get(key).setValue(0);
            alarmMap.get(key).setAlarmGrade("03");
        }
    }

    public void updateValue(String alarmType,MonitorNum monitorNum){
        if (alarmMap.containsKey(alarmType)) {
            alarmMap.put(alarmType,monitorNum);
        }
    }
}
