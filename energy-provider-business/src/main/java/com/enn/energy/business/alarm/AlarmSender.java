package com.enn.energy.business.alarm;

import com.enn.vo.energy.business.CusAlarmValues;
import com.enn.vo.energy.business.MonitorNum;
import com.enn.vo.energy.business.resp.EventResp;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kexing on 2018/4/14.
 */
@Service
public class AlarmSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    /**
     *根据告警映射告警用户
    * */
    public void sendAlarm(CusAlarmValues alarmValues){
        System.out.print("start to send message.");
        List<MonitorNum> monitorList=new ArrayList<MonitorNum>();
        for(String key:alarmValues.getAlarmMap().keySet()){
            monitorList.add(alarmValues.getAlarmMap().get(key));
        }
        EventResp eventResp=new EventResp();
//        for (MonitorNum monitorNum : monitorList) {
//            if ("01".equals(monitorNum.getAlarmGrade())) {
//                eventResp.setUseTime("0");
//                break;
//            }
//        }
//        eventResp.setMonitor(monitorList);
        this.rabbitTemplate.convertAndSend("amq.topic", "etsp.alarm."+alarmValues.getCusId(), eventResp);
        System.out.print("send message end.");
    }
}
