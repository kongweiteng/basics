package com.enn.energy.business.alarm;

import com.enn.constant.Constant;
import com.enn.energy.business.redis.RedisService;
import com.enn.energy.system.common.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by kexing on 2018/4/14.
 */
@Component
public class AlarmUpdateScheduler implements CommandLineRunner {

    @Autowired
    private RedisService redisService;

    @Autowired
    private AlarmSender alarmSender;

//    @Resource
//    private IUserDataService userDataService;

//    @Resource
//    private IAlarmsDataService alarmsDataService;

    //@Scheduled(fixedRate = 10000)
    @Scheduled(cron = "58 59 23 * * ?")  //每天23:59:58执行任务
    private void updateAlarmData(){
        //此处获取用户数据
        updateAlarmStatus();
    }


    @Scheduled(cron = "0 0 1 * * ?")  //每天凌晨1:00执行任务
    private void updateMeterData(){
        //此处获取用户数据
        updateCusMeterData();
    }

    /*
    *更新用户表计关系信息
     */
    private void updateCusMeterData(){
//        List<CusMeterRelation> list=userDataService.findCusMeterRelation();
//        HashMap<String,ArrayList<String>> cusMeters=new HashMap<String,ArrayList<String>>();
//        if(list!=null) {
//            for(CusMeterRelation meterRelation:list){
//                //fanneng-etsp-cusmeter- 作为配置项
//                redisService.set(Constants.prefixCusMeter+meterRelation.getMeterNo(),meterRelation.getCusId());
//                if(cusMeters.containsKey(meterRelation.getCusId())){
//                    ArrayList<String> meters=cusMeters.get(meterRelation.getCusId());
//                    meters.add(meterRelation.getMeterNo());
//                }
//                else{
//                    ArrayList<String> meters=new ArrayList<String>();
//                    meters.add(meterRelation.getMeterNo());
//                    cusMeters.put(meterRelation.getCusId(),meters);
//                }
//            }
//        }
        //redisService.removePattern(Constants.prefixAlarmKey+"*");

//        for(String key:cusMeters.keySet()){
//            //更新用户告警缓存，如果没有该用户缓存，则从数据库更新
//            if(!redisService.exists(Constants.prefixAlarmKey+key)) {
//                List<String> meters=cusMeters.get(key);
//                //获取告警事件
//                AlarmsNumReq alarmsNumReq = new AlarmsNumReq();
//                alarmsNumReq.setMeterNos(meters);
//                List<String> types = new ArrayList<>();
//                types.add("01");
//                types.add("02");
//                types.add("03");
//                types.add("04");
//                alarmsNumReq.setAlarmTypes(types);
//                String start = DateUtil.getNowDay();
//                String end = DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
//                alarmsNumReq.setStartDate(start);
//                alarmsNumReq.setEndDate(end);
//                List<MonitorNum> findAlarmNum = alarmsDataService.getFindAlarmNum(alarmsNumReq);
//                CusAlarmValues alarmValues=new CusAlarmValues();
//                alarmValues.setCusId(key);
//                for(MonitorNum monitorNum:findAlarmNum){
//                    alarmValues.updateValue(monitorNum.getAlarmType(),monitorNum);
//                }
//                redisService.set(Constant.prefixAlarmKey+key,alarmValues);
//
//            }
//        }
    }

    /*
    *更新用户当日告警状态，在上一天23:59:59秒更新所有用户状态，并发送消息通知
     */
    private void updateAlarmStatus(){
        //获取以fanneng-etsp-alarm-开头的key值
        Set<String> keys=redisService.findPattern(Constant.prefixAlarmKey+"*");
        if(keys.size()>0){
            for(String key:keys){
                Object obj=redisService.get(key);
                ObjectMapper mapper = new ObjectMapper();
//                CusAlarmValues alarmValues = mapper.convertValue(obj,CusAlarmValues.class);
//                alarmValues.resetValues();
//                redisService.set(key, alarmValues);
                //发送消息通知
//                alarmSender.sendAlarm(alarmValues);
            }
        }
    }

    /*
    *启动时加载用户信息
     */
    @Override
    public void run(String... strings) throws Exception {
        updateCusMeterData();
    }
}
