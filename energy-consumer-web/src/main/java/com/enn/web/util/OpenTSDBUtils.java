package com.enn.web.util;

import com.alibaba.fastjson.JSON;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.service.business.IOpentsdbService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.req.SamplDataStaReq;
import com.enn.vo.energy.business.resp.DataResp;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class OpenTSDBUtils {

    private static IOpentsdbService opentsdbService;


    @Autowired
    public void setDatastore(IOpentsdbService opentsdbService) {
        OpenTSDBUtils.opentsdbService = opentsdbService;
    }

    public static Map<String, String> call(String taskNum, Map<String, String> returnMap, List<SamplDataStaReq> samplDataStaReqs, StringBuilder str) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(">>>" + taskNum + "任务启动");
        Date dateTmp1 = new Date();
        Thread.sleep(1000);

        for (SamplDataStaReq samplDataStaReq : samplDataStaReqs) {
            samplDataStaReq.setEnd(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            EnergyResp<ListResp<RmiSamplDataResp>> samplDataStaReq1 = opentsdbService.getSamplDataStaReq(samplDataStaReq);
            if (!samplDataStaReq1.getCode().equals(StatusCode.SUCCESS.getCode())) {
                throw new EnergyException(samplDataStaReq1.getCode(), samplDataStaReq1.getMsg(), samplDataStaReq1.getError());
            }
            for (RmiSamplDataResp rmi : samplDataStaReq1.getData().getList()) {
                if (rmi.getDataResp() != null) {
                    for (DataResp dataResp : rmi.getDataResp()) {
                        returnMap.put("时间:" + dataResp.getTime() + ",表号:" + rmi.getEquipID() + ",测点：" + rmi.getMetric(), " 值：" + dataResp.getValue());
                        str.append("时间:" + dataResp.getTime() + ",表号:" + rmi.getEquipID() + ",测点：" + rmi.getMetric() + " 值：" + dataResp.getValue()).append("\r\n");
                        log.info("任务"+taskNum+"返回数据："+ str);
                    }
                }
            }
        }
        Date dateTmp2 = new Date();
        long time = dateTmp2.getTime() - dateTmp1.getTime();
        System.out.println(">>>" + taskNum + "任务终止");
        return returnMap;
    }
}
