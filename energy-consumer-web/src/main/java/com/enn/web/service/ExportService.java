package com.enn.web.service;

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
import com.enn.web.util.MyCallable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
public class ExportService {

    @Autowired
    private IOpentsdbService opentsdbService;

    public EnergyResp<Map<String, String>> getsb(List<SamplDataStaReq> params) throws Exception {
        EnergyResp<Map<String, String>> resp = new EnergyResp<>();
        Map<String, String> map = new LinkedHashMap<>();
        StringBuilder str = new StringBuilder();
        for (SamplDataStaReq samplDataStaReq : params) {
            samplDataStaReq.setEnd(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

            EnergyResp<ListResp<RmiSamplDataResp>> samplDataStaReq1 = opentsdbService.getSamplDataStaReq(samplDataStaReq);
            if (!samplDataStaReq1.getCode().equals(StatusCode.SUCCESS.getCode())) {
                throw new EnergyException(samplDataStaReq1.getCode(), samplDataStaReq1.getMsg(), samplDataStaReq1.getError());
            }
            for (RmiSamplDataResp rmi : samplDataStaReq1.getData().getList()) {
                if (rmi.getDataResp() != null) {
                    for (DataResp dataResp : rmi.getDataResp()) {

                        if (dataResp.getValue() == null) {
                            map.put("时间:" + dataResp.getTime() + ",表号:" + rmi.getEquipID()+ ",测点：" + rmi.getMetric() , "数据出现断点");
                            str.append("时间:" + dataResp.getTime() + ",表号:" + rmi.getEquipID() + ",测点：" + rmi.getMetric() + " 数据出现断点").append("\r\n");
                        }
                    }
                }
            }
        }
        writeStringToFile("E:/nan.txt", str.toString());
        resp.ok(map);
        return resp;
    }


    public EnergyResp<Map<String, String>> getsb2(List<SamplDataStaReq> params) throws Exception {
        EnergyResp<Map<String, String>> resp = new EnergyResp<>();
        Map<String, String> map = new LinkedHashMap<>();
        StringBuilder str = new StringBuilder();
        for (SamplDataStaReq samplDataStaReq : params) {
            samplDataStaReq.setEnd(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

            EnergyResp<ListResp<RmiSamplDataResp>> samplDataStaReq1 = opentsdbService.getSamplDataStaReq(samplDataStaReq);
            if (!samplDataStaReq1.getCode().equals(StatusCode.SUCCESS.getCode())) {
                throw new EnergyException(samplDataStaReq1.getCode(), samplDataStaReq1.getMsg(), samplDataStaReq1.getError());
            }
            for (RmiSamplDataResp rmi : samplDataStaReq1.getData().getList()) {
                if (rmi.getDataResp() != null) {
                    for (DataResp dataResp : rmi.getDataResp()) {
                        map.put("时间:" + dataResp.getTime() + ",表号:" + rmi.getEquipID() +",测点：" + rmi.getMetric(),  " 值：" + dataResp.getValue());
                        str.append("时间:" + dataResp.getTime() + ",表号:" + rmi.getEquipID() + ",测点：" + rmi.getMetric() + " 值：" + dataResp.getValue()).append("\r\n");
                    }
                }
            }
        }
        //Map<String, String> map = get(params, 15);

        writeStringToFile("E:/data.txt", str.toString());
        resp.ok(map);
        return resp;
    }


    /**
     * 将数据写进文件流中
     */
    public void writeStringToFile(String filePath, String str) throws Exception {
        File file = new File(filePath);
        PrintStream ps = new PrintStream(new FileOutputStream(file));
        ps.println(str);// 往文件里写入字符串
        //ps.append("http://www.jb51.net");// 在已有的基础上添加字符串
    }


    /**
     * 生成一个测点的List
     */
    public List<String> getList() {
        List<String> a = new ArrayList<>();
        a.add("EMS.P");
        a.add("EMS.Ua");
        a.add("EMS.Ub");
        a.add("EMS.Uc");
        a.add("EMS.Uab");
        a.add("EMS.Ubc");
        a.add("EMS.Uac");
        a.add("EMS.Ia");
        a.add("EMS.Ib");
        a.add("EMS.Ic");
        a.add("EMS.Q");
        a.add("EMS.Eptp");
        return a;
    }


    /**
     * 创建多线程分发任务，并返回数据
     */
    public Map<String,String> get(List<SamplDataStaReq> params, int taskSize) {
        Map<String,String> returMap= new HashMap<>();

        taskSize = taskSize < 2 ? 2 : taskSize;
        int size = params.size();
        int count = size / taskSize + 1;
        Date date1 = new Date();
        // 创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(taskSize);
        // 创建多个有返回值的任务
        List<Future> list = new ArrayList<Future>();
        for (int i = 0; i < taskSize; i++) {
            int startIndex = i * count;
            int endIndex = (i + 1) * count < size ? (i + 1) * count : size;
            Callable c = new MyCallable(i + " ", params.subList(startIndex, endIndex));
            // 执行任务并获取Future对象
            Future f = pool.submit(c);
            // System.out.println(">>>" + f.get().toString());
            list.add(f);
        }
        // 关闭线程池
        pool.shutdown();
        // 获取所有并发任务的运行结果
        for (Future f : list) {
            // 从Future对象上获取任务的返回值，并输出到控制台
            try {
                Map<String,String> map = (ConcurrentHashMap)f.get();
                for(String  k :map.keySet()){
                    returMap.put(k,map.get(k));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returMap;

    }

}
