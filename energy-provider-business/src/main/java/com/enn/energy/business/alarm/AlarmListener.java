package com.enn.energy.business.alarm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enn.constant.Constant;
import com.enn.energy.business.redis.RedisService;
import com.enn.vo.energy.business.Alarm;
import com.enn.vo.energy.business.CusAlarmValues;
import com.enn.vo.energy.business.MonitorNum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Created by kexing on 2018/4/14.
 */
@Component
//@RabbitListener(queues = "${spring.rabbitmq.alarmQueue}")
public class AlarmListener  {

    @Autowired
    private AlarmSender alarmSender;

    @Autowired
    private RedisService redisService;

    @RabbitHandler
    public void process(@Payload byte[] bytes ){
        System.out.println("Receive message ");
        try {
            //byte[] body=message.getBody();
            String s = new String(bytes);
            JSONArray jsonArray = JSON.parseArray(s);
            System.out.println(s);
            ObjectMapper mapper=new ObjectMapper();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Alarm alarm=mapper.convertValue(jsonObject,Alarm.class);
                handleAlarm(alarm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * 告警处理，并发送告警到前端队列
     */
//    private void handleAlarm(Alarm alarm) {
//        if(redisService.exists("fanneng-etsp-cusmeter-"+alarm.getEquipID())){
//            //获取设备对应的cusId
//            String cusId = redisService.get("fanneng-etsp-cusmeter-" + alarm.getEquipID()).toString();
//            if(redisService.exists("fanneng-etsp-alarm-"+cusId)){
//                Object obj=redisService.get("fanneng-etsp-alarm-"+cusId);
//                ObjectMapper mapper = new ObjectMapper();
//                CusAlarmValues alarmValues = mapper.convertValue(obj,CusAlarmValues.class);
//                MonitorNum monitorNum=alarmValues.getAlarmMap().get(alarm.getAlarm_type());
//                if((monitorNum.getAlarmGrade()=="02"&&alarm.getAlarm_level()=="03")
//                    ||(monitorNum.getAlarmGrade()=="01"&&(alarm.getAlarm_level()=="03"||alarm.getAlarm_level()=="02"))) {
//                    monitorNum.setAlarmGrade(alarm.getAlarm_level());
//                }
//                alarmValues.updateValue(alarm.getAlarm_type(),monitorNum);
//                redisService.set("fanneng-etsp-alarm-"+cusId, alarmValues);
//                alarmSender.sendAlarm(alarmValues);
//            }
//        }
//    }

    /*
     * 告警处理，并发送告警到前端队列
     */
    private void handleAlarm(Alarm alarm) {
        if (redisService.exists(Constant.prefixCus + alarm.getEquipID())) {
            //获取设备对应的cusId
            String cusId = redisService.get(Constant.prefixCus + alarm.getEquipID()).toString();
            if (redisService.exists(Constant.prefixAlarmKey + cusId)) {
                Object obj = redisService.get(Constant.prefixAlarmKey + cusId);
                ObjectMapper mapper = new ObjectMapper();
                CusAlarmValues alarmValues = mapper.convertValue(obj, CusAlarmValues.class);
                MonitorNum monitorNum = alarmValues.getAlarmMap().get(alarm.getAlarm_type());
                monitorNum.setValue(monitorNum.getValue()+1);
                if (("03".equals(monitorNum.getAlarmGrade())&& "02".equals(alarm.getAlarm_level()))
                        || ("01".equals(alarm.getAlarm_level()) && ("03".equals(monitorNum.getAlarmGrade()) || "02".equals(monitorNum.getAlarmGrade())))) {
                    monitorNum.setAlarmGrade(alarm.getAlarm_level());
                }
                alarmValues.updateValue(alarm.getAlarm_type(), monitorNum);
                redisService.set(Constant.prefixAlarmKey + cusId, alarmValues);
//                alarmSender.sendAlarm(alarmValues);
            }
        }
    }




}
