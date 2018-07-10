package com.enn.web.util;

import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.service.business.IOpentsdbService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.req.SamplDataStaReq;
import com.enn.vo.energy.business.resp.DataResp;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Wiley.Kou
 * @createTime 2018/6/3
 */
//@Service
public class MyCallable implements Callable<Object> {
    private String taskNum;
    private List<SamplDataStaReq> samplDataStaReqs;
    private Map<String,String> returnMap = new ConcurrentHashMap<>();
    private StringBuilder str = new StringBuilder();
    public MyCallable(String taskNum, List stationCodeList) {
        this.taskNum = taskNum;
        this.samplDataStaReqs = stationCodeList;
    }

    @Override
    public Object call() throws Exception {
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(">>>" + taskNum + "任务启动");
        Date dateTmp1 = new Date();
        Thread.sleep(1000);

        for(SamplDataStaReq samplDataStaReq : samplDataStaReqs){
            samplDataStaReq.setEnd(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            EnergyResp<ListResp<RmiSamplDataResp>> samplDataStaReq1 = opentsdbService.getSamplDataStaReq(samplDataStaReq);
            if (!samplDataStaReq1.getCode().equals(StatusCode.SUCCESS.getCode())) {
                throw new EnergyException(samplDataStaReq1.getCode(), samplDataStaReq1.getMsg(), samplDataStaReq1.getError());
            }
            for (RmiSamplDataResp rmi : samplDataStaReq1.getData().getList()) {
                if (rmi.getDataResp() != null) {
                    for (DataResp dataResp : rmi.getDataResp()) {
                        returnMap.put("时间:" + dataResp.getTime() + ",表号:" + rmi.getEquipID()+ ",测点：" + rmi.getMetric()," 值：" + dataResp.getValue());
                        str.append("时间:" + dataResp.getTime() + ",表号:" + rmi.getEquipID() + ",测点：" + rmi.getMetric() + " 值：" + dataResp.getValue()).append("\r\n");
                    }
                }
            }
        }
        Date dateTmp2 = new Date();
        long time = dateTmp2.getTime() - dateTmp1.getTime();
        System.out.println(">>>" + taskNum + "任务终止");*/
        return OpenTSDBUtils.call(taskNum,returnMap,samplDataStaReqs,str);
    }
}
