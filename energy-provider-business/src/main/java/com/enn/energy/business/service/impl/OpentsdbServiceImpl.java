package com.enn.energy.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.enn.constant.StatusCode;
import com.enn.energy.business.dao.CustMeterMapper;
import com.enn.energy.business.service.IOpentsdbService;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.HttpClientUtils;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.energy.system.common.util.StringUtil;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.req.*;
import com.enn.vo.energy.business.resp.DataResp;
import com.enn.vo.energy.business.resp.LastResp;
import com.enn.vo.energy.business.resp.RmiFirstLastDataResp;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service
@Slf4j
public class OpentsdbServiceImpl implements IOpentsdbService {

    @Value("${opentsdb.url}")
    private String url;
    @Value("${opentsdb.userKey}")
    private String userKey;


    private static Logger logger = LoggerFactory.getLogger(OpentsdbServiceImpl.class);

    @Autowired
    private CustMeterMapper custMeterMapper;

    @Override
    public EnergyResp<ListResp<RmiSamplDataResp>> getSamplData(SamplDataReq samplDataReq) {
        EnergyResp<ListResp<RmiSamplDataResp>> returnResp = new EnergyResp<>();
        //接受参数 -封装成opentsdb需要的参数
        OpentsdbReq opentsdbReq = new OpentsdbReq();
        opentsdbReq.setTimezone("Asia/Shanghai");
        opentsdbReq.setUseCalendar(true);
        opentsdbReq.setUserKey(userKey);
        opentsdbReq.setDataSource(samplDataReq.getMetric().split("[.]")[0]);
        opentsdbReq.setStart((DateUtil.parseTime(samplDataReq.getStart()).getTime()) / 1000);
        opentsdbReq.setEnd((DateUtil.parseTime(samplDataReq.getEnd()).getTime()) / 1000);

        List<QueriesReq> queriesReqs = new ArrayList<>();

        //遍历设备号
        for (String equip : samplDataReq.getEquipID()) {
            QueriesReq queriesReq = new QueriesReq();
            queriesReq.setDownsample(samplDataReq.getDownsample());
            queriesReq.setMetric(samplDataReq.getMetric());
            queriesReq.setAggregator("none");
            //创建tags
            TagsReq tags = new TagsReq();
            tags.setEquipID(equip);
            tags.setEquipMK(samplDataReq.getEquipMK());
            tags.setStaId(samplDataReq.getStaId());
            queriesReq.setTags(tags);
            queriesReqs.add(queriesReq);
        }
        opentsdbReq.setQueries(queriesReqs);
        //请求opentsdb 获取到数据
        String s = null;
        try {
            Date dateTmp1 = new Date();
            log.info("请求大数据平台开始！ 接口：" + url + "/api/query");
            logger.info("采样获取数据-请求参数：" + JSON.toJSONString(opentsdbReq));
            s = HttpClientUtils.httpPostStr(url + "/api/query", JSON.toJSONString(opentsdbReq));
            Date dateTmp2 = new Date();
            long time = dateTmp2.getTime() - dateTmp1.getTime();
            log.info("请求大数据平台结束！ 用时：" + time);
        } catch (Exception e) {
            logger.info("大数据平台服务请求错误！！！-服务错误！");
            //returnResp.faile(StatusCode.E_G.getCode(),StatusCode.E_G.getMsg(),e.getMessage());
            //return returnResp;
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), "大数据平台服务请求错误！！！-服务错误！" + "错误信息：" + e.getMessage());
        }
        //log.info(s);
        //请求回数据后，判断是否请求到数据，如果有异常，则将opentsdb的异常信息获取到后返回
        boolean va = StringUtil.va(s);
        if (va) {//是jsonarray
            JSONArray array = JSON.parseArray(s);
            //将数据封装成实体，返回
            RmiSamplDataResp resp = null;
            List<RmiSamplDataResp> rmiSamplDataResps = new ArrayList<>();
            ListResp<RmiSamplDataResp> respListResp = new ListResp<>();
            for (Object obj : array) {
                JSONObject jsonObject = JSON.parseObject(obj.toString());
                String metric = jsonObject.getString("metric");
                JSONObject tags = jsonObject.getJSONObject("tags");
                String equipID = tags.getString("equipID");
                String equipMK = tags.getString("equipMK");
                String staId = tags.getString("staId");
                String dps = jsonObject.getString("dps");
                Map<String, Object> dpsMap = JSON.parseObject(dps, TreeMap.class);
                dpsMap = MathUtils.sortMapByKey(dpsMap);
                resp = new RmiSamplDataResp();
                resp.setMetric(metric);
                resp.setEquipID(equipID);
                resp.setStaId(staId);
                resp.setEquipMK(equipMK);
                BigDecimal capacity = null;  //电表所在配电室最大铭牌容量
                BigDecimal frontNum = null;
                if("EMS.Eptp".equals(metric) && "METE".equals(equipMK)){
                    capacity = custMeterMapper.getCapacity(equipID);
                }
                DataResp dataResp = null;
                List<DataResp> dataResps = new ArrayList<>();
                //遍历jsonobj
                for (String key : dpsMap.keySet()) {
                    //判断value是字符串还是 BigDecimal
                    String value = null;
                    value = dpsMap.get(key).toString();

//                    dataResp = new DataResp();
//                    dataResp.setTime(DateUtil.stampToDate(key));
//                    if (!"NaN".equals(value)) {
//                        if (samplDataReq.getPoint() != null) {
//                            dataResp.setValue(MathUtils.point(new BigDecimal(value), samplDataReq.getPoint()));
//                        } else {
//                            dataResp.setValue(new BigDecimal(value));
//                        }
//                    }
//                    dataResps.add(dataResp);

                    if(capacity != null) {
                        dataResp = new DataResp();
                        boolean kk = false;
                        if (frontNum != null && !"NaN".equals(value)) {
                            long ab = MathUtils.divide(MathUtils.sub(new BigDecimal(value), frontNum),new BigDecimal(60)).longValue();
                            if (!(ab >= 0 && ab <= capacity.longValue())) {
                                kk = true;
                            }
                        }

                        dataResp.setTime(DateUtil.stampToDate(key));
                        if (!"NaN".equals(value)) {
                            if (kk) {
                                if(samplDataReq.getPoint()!=null){
                                    dataResp.setValue(MathUtils.point(frontNum, samplDataReq.getPoint()));
                               }else {
                                    dataResp.setValue(MathUtils.point(frontNum, 4));
                                }
                            } else {
                                if (!"NaN".equals(value)) {
                                    if (samplDataReq.getPoint() != null) {
                                        dataResp.setValue(MathUtils.point(new BigDecimal(value), samplDataReq.getPoint()));
                                    } else {
                                        dataResp.setValue(new BigDecimal(value));
                                    }
                                }
                            }
                        } else {
                            if(samplDataReq.getPoint()!=null){
                                dataResp.setValue(MathUtils.point(frontNum, samplDataReq.getPoint()));
                            }else {
                                dataResp.setValue(MathUtils.point(frontNum, 4));
                            }
                        }
                        dataResps.add(dataResp);
                        if (!"NaN".equals(value) && !kk) {
                            frontNum = new BigDecimal(value);
                        }
                    }else{
                        dataResp = new DataResp();
                        dataResp.setTime(DateUtil.stampToDate(key));
                        if (!"NaN".equals(value)) {
                            if (samplDataReq.getPoint() != null) {
                                dataResp.setValue(MathUtils.point(new BigDecimal(value), samplDataReq.getPoint()));
                            } else {
                                dataResp.setValue(new BigDecimal(value));
                            }
                        }
                        dataResps.add(dataResp);
                    }
                }
                resp.setDataResp(dataResps);
                rmiSamplDataResps.add(resp);
            }
            respListResp.setList(rmiSamplDataResps);
            returnResp.ok(respListResp);
            return returnResp;
        } else {//是jsonobj
            JSONObject jsonObject = JSON.parseObject(s);
            //returnResp.faile(StatusCode.E_A.getCode(),StatusCode.E_A.getMsg(),jsonObject.toString());
            //return returnResp;
            throw new EnergyException(StatusCode.E_A.getCode(), StatusCode.E_A.getMsg(), jsonObject.toString());
        }
    }

    /**
     * 不同站点下的设备同时查询  数据的方法
     *
     * @param samplDataStaReq
     * @return
     */
    @Override
    public EnergyResp<ListResp<RmiSamplDataResp>> getSamplData(SamplDataStaReq samplDataStaReq) {
        EnergyResp<ListResp<RmiSamplDataResp>> returnResp = new EnergyResp<>();
        //接受参数 -封装成opentsdb需要的参数
        OpentsdbReq opentsdbReq = new OpentsdbReq();
        opentsdbReq.setTimezone("Asia/Shanghai");
        opentsdbReq.setUseCalendar(true);
        opentsdbReq.setUserKey(userKey);
        opentsdbReq.setDataSource(samplDataStaReq.getMetric().split("[.]")[0]);
        opentsdbReq.setStart((DateUtil.parseTime(samplDataStaReq.getStart()).getTime()) / 1000);
        opentsdbReq.setEnd((DateUtil.parseTime(samplDataStaReq.getEnd()).getTime()) / 1000);

        List<QueriesReq> queriesReqs = new ArrayList<>();

        //遍历设备信息
        for (Equip equip : samplDataStaReq.getEquips()) {
            QueriesReq queriesReq = new QueriesReq();
            queriesReq.setDownsample(samplDataStaReq.getDownsample());
            queriesReq.setMetric(samplDataStaReq.getMetric());
            queriesReq.setAggregator("none");
            //创建tags
            TagsReq tags = new TagsReq();
            tags.setEquipID(equip.getEquipID());
            tags.setEquipMK(equip.getEquipMK());
            tags.setStaId(equip.getStaId());
            queriesReq.setTags(tags);
            queriesReqs.add(queriesReq);
        }
        opentsdbReq.setQueries(queriesReqs);
        //请求opentsdb 获取到数据
        String s = null;
        try {
            Date dateTmp1 = new Date();
            log.info("请求大数据平台开始！ 接口：" + url + "/api/query");
            logger.info("采样获取数据-请求参数：" + JSON.toJSONString(opentsdbReq));
            s = HttpClientUtils.httpPostStr(url + "/api/query", JSON.toJSONString(opentsdbReq));
            Date dateTmp2 = new Date();
            long time = dateTmp2.getTime() - dateTmp1.getTime();
            log.info("请求大数据平台结束！ 用时：" + time);
        } catch (Exception e) {
            logger.info("大数据平台服务请求错误！！！-服务错误！");
            //returnResp.faile(StatusCode.E_G.getCode(),StatusCode.E_G.getMsg(),e.getMessage());
            //return returnResp;
            throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(), "大数据平台服务请求错误！！！-服务错误！" + "错误信息：" + e.getMessage());
        }
        //log.info(s);
        //请求回数据后，判断是否请求到数据，如果有异常，则将opentsdb的异常信息获取到后返回
        boolean va = StringUtil.va(s);
        if (va) {//是jsonarray
            JSONArray array = JSON.parseArray(s);
            //将数据封装成实体，返回
            RmiSamplDataResp resp = null;
            List<RmiSamplDataResp> rmiSamplDataResps = new ArrayList<>();
            ListResp<RmiSamplDataResp> respListResp = new ListResp<>();
            for (Object obj : array) {
                JSONObject jsonObject = JSON.parseObject(obj.toString());
                String metric = jsonObject.getString("metric");
                JSONObject tags = jsonObject.getJSONObject("tags");
                String equipID = tags.getString("equipID");
                String equipMK = tags.getString("equipMK");
                String staId = tags.getString("staId");
                String dps = jsonObject.getString("dps");
                Map<String, Object> dpsMap =  JSON.parseObject(dps, TreeMap.class);
                dpsMap = MathUtils.sortMapByKey(dpsMap);
                resp = new RmiSamplDataResp();
                resp.setMetric(metric);
                resp.setEquipID(equipID);
                resp.setStaId(staId);
                resp.setEquipMK(equipMK);
                BigDecimal capacity = null;  //电表所在配电室最大铭牌容量
                BigDecimal frontNum = null;
                if("EMS.Eptp".equals(metric) && "METE".equals(equipMK)){
                    capacity = custMeterMapper.getCapacity(equipID);
                }
                DataResp dataResp = null;
                List<DataResp> dataResps = new ArrayList<>();
                //遍历jsonobj
                for (String key : dpsMap.keySet()) {
                    //判断value是字符串还是 BigDecimal
                    String value = null;
                    value = dpsMap.get(key).toString();

//                    dataResp = new DataResp();
//                    dataResp.setTime(DateUtil.stampToDate(key));
//                    if (!"NaN".equals(value)) {
//                        if (samplDataStaReq.getPoint() != null) {
//                            dataResp.setValue(MathUtils.point(new BigDecimal(value), samplDataStaReq.getPoint()));
//                        } else {
//                            dataResp.setValue(new BigDecimal(value));
//                        }
//                    }
//                    dataResps.add(dataResp);
                    if(capacity != null) {
                        dataResp = new DataResp();
                        boolean kk = false;
                        if (frontNum != null && !"NaN".equals(value)) {
                            long ab = MathUtils.divide(MathUtils.sub(new BigDecimal(value), frontNum),new BigDecimal(60)).longValue();
                            if (!(ab >= 0 && ab <= capacity.longValue())) {
                                kk = true;
                            }
                        }

                        dataResp.setTime(DateUtil.stampToDate(key));
                        if (!"NaN".equals(value)) {
                            if (kk) {
                                if(samplDataStaReq.getPoint()!=null){
                                    dataResp.setValue(MathUtils.point(frontNum, samplDataStaReq.getPoint()));
                                }else {
                                    dataResp.setValue(MathUtils.point(frontNum, 4));
                                }
                            } else {
                                if (!"NaN".equals(value)) {
                                    if (samplDataStaReq.getPoint() != null) {
                                        dataResp.setValue(MathUtils.point(new BigDecimal(value), samplDataStaReq.getPoint()));
                                    } else {
                                        dataResp.setValue(new BigDecimal(value));
                                    }
                                }
                            }
                        } else {
                            if(samplDataStaReq.getPoint()!=null){
                                dataResp.setValue(MathUtils.point(frontNum, samplDataStaReq.getPoint()));
                            }else {
                                dataResp.setValue(MathUtils.point(frontNum, 4));
                            }
                        }
                        dataResps.add(dataResp);
                        if (!"NaN".equals(value) && !kk) {
                            frontNum = new BigDecimal(value);
                        }
                    }else{
                        dataResp = new DataResp();
                        dataResp.setTime(DateUtil.stampToDate(key));
                        if (!"NaN".equals(value)) {
                            if (samplDataStaReq.getPoint() != null) {
                                dataResp.setValue(MathUtils.point(new BigDecimal(value), samplDataStaReq.getPoint()));
                            } else {
                                dataResp.setValue(new BigDecimal(value));
                            }
                        }
                        dataResps.add(dataResp);
                    }
                }
                resp.setDataResp(dataResps);
                rmiSamplDataResps.add(resp);
            }
            respListResp.setList(rmiSamplDataResps);
            returnResp.ok(respListResp);
            return returnResp;
        } else {//是jsonobj
            JSONObject jsonObject = JSON.parseObject(s);
            //returnResp.faile(StatusCode.E_A.getCode(),StatusCode.E_A.getMsg(),jsonObject.toString());
            //return returnResp;
            throw new EnergyException(StatusCode.E_A.getCode(), StatusCode.E_A.getMsg(), jsonObject.toString());
        }
    }

    @Override
    public EnergyResp<List<RmiFirstLastDataResp>> getFirstLastData(FirstLastDataReq firstLastDataReq) {
        SamplDataReq samplDataReq = new SamplDataReq();
        samplDataReq.setDownsample("1d-first-nan");
        EnergyResp<ListResp<RmiSamplDataResp>> samplData = getSamplData(samplDataReq);
        return null;
    }

    @Override
    public List<String> getAllMetric() {
        return null;
    }

    @Override
    public List<String> getAllDevice() {
        return null;
    }

    @Override
    public EnergyResp<ListResp<LastResp>> getLastDatas(LastReq last) {
        EnergyResp<ListResp<LastResp>> returnResp = new EnergyResp<>();
        LastDataReq lastDataReq = new LastDataReq();
        lastDataReq.setUserKey(userKey);
        lastDataReq.setDataSource(last.getMetric().split("[.]")[0]);
        lastDataReq.setResolveNames(true);
        lastDataReq.setBackScan(24);
        List<QueriesLastReq> queriesLastReqs = new ArrayList<>();
        QueriesLastReq queriesLastReq = null;
        TagsReq tagsReq = null;
        for (String eq : last.getEquipID()) {
            queriesLastReq = new QueriesLastReq();
            queriesLastReq.setMetric(last.getMetric());
            tagsReq = new TagsReq();
            tagsReq.setEquipID(eq);
            tagsReq.setEquipMK(last.getEquipMK());
            tagsReq.setStaId(last.getStaId());
            queriesLastReq.setTags(tagsReq);
            queriesLastReqs.add(queriesLastReq);
        }
        lastDataReq.setQueries(queriesLastReqs);
        //请求opentsdb数据
        String s = null;
        try {
            Date dateTmp1 = new Date();
            log.info("请求大数据平台开始！ 接口：" + url + "/api/query/last");
            logger.info("采样获取数据-请求参数：" + JSON.toJSONString(lastDataReq));
            s = HttpClientUtils.httpPostStr(url + "/api/query/last", JSON.toJSONString(lastDataReq));
            Date dateTmp2 = new Date();
            long time = dateTmp2.getTime() - dateTmp1.getTime();
            log.info("请求大数据平台结束！ 用时：" + time);
        } catch (Exception e) {
            logger.info("大数据平台服务请求错误！！！-服务错误！");
            //returnResp.faile(StatusCode.E_G.getCode(),StatusCode.E_G.getMsg(),e.getMessage());
            //return returnResp;
            throw new EnergyException(StatusCode.E_G.getCode(), StatusCode.E_G.getMsg(), "大数据平台服务请求错误！！！-服务错误！" + "错误信息：" + e.getMessage());

        }
        //请求回数据后，判断是否请求到数据，如果有异常，则将opentsdb的异常信息获取到后返回
        boolean va = StringUtil.va(s);
        LastResp dataresp = null;
        ListResp<LastResp> listResps = new ListResp();
        List<LastResp> lastResps = new ArrayList<>();
        if (va) {
            JSONArray array = JSON.parseArray(s);
            for (Object obj : array) {
                JSONObject jsonObject = JSON.parseObject(obj.toString());
                String value = jsonObject.getString("value");
                //String substring = jsonObject.getJSONObject("tags").getString("equipID").substring(4);
                Date timestamp = jsonObject.getDate("timestamp");
                JSONObject tags = jsonObject.getJSONObject("tags");
                String equipID = tags.getString("equipID");
                String staId = tags.getString("staId");
                dataresp = new LastResp();
                dataresp.setValue(value);
                dataresp.setEquipID(equipID);
                dataresp.setStaId(staId);
                dataresp.setTime(DateUtil.formatDate(timestamp, "yyyy-MM-dd HH:mm:ss"));
                lastResps.add(dataresp);
            }
            listResps.setList(lastResps);
            returnResp.ok(listResps);
            return returnResp;
        } else {
            JSONObject jsonObject = JSON.parseObject(s);
            //returnResp.faile(StatusCode.E_A.getCode(),StatusCode.E_A.getMsg(),jsonObject.toString());
            //return returnResp;
            throw new EnergyException(StatusCode.E_A.getCode(), StatusCode.E_A.getMsg(), jsonObject.toString());

        }
    }

    @Override
    public EnergyResp<ListResp<LastResp>> getLastDatas(LastStaReq last) {
        EnergyResp<ListResp<LastResp>> returnResp = new EnergyResp<>();
        LastDataReq lastDataReq = new LastDataReq();
        lastDataReq.setUserKey(userKey);
        lastDataReq.setDataSource(last.getMetric().split("[.]")[0]);
        lastDataReq.setResolveNames(true);
        lastDataReq.setBackScan(24);
        List<QueriesLastReq> queriesLastReqs = new ArrayList<>();
        QueriesLastReq queriesLastReq = null;
        TagsReq tagsReq = null;
        for (Equip eq : last.getEquips()) {
            queriesLastReq = new QueriesLastReq();
            queriesLastReq.setMetric(last.getMetric());
            tagsReq = new TagsReq();
            tagsReq.setEquipID(eq.getEquipID());
            tagsReq.setEquipMK(eq.getEquipMK());
            tagsReq.setStaId(eq.getStaId());
            queriesLastReq.setTags(tagsReq);
            queriesLastReqs.add(queriesLastReq);
        }
        lastDataReq.setQueries(queriesLastReqs);
        //请求opentsdb数据
        String s = null;
        try {
            Date dateTmp1 = new Date();
            log.info("请求大数据平台开始！ 接口：" + url + "/api/query/last");
            logger.info("采样获取数据-请求参数：" + JSON.toJSONString(lastDataReq));
            s = HttpClientUtils.httpPostStr(url + "/api/query/last", JSON.toJSONString(lastDataReq));
            Date dateTmp2 = new Date();
            long time = dateTmp2.getTime() - dateTmp1.getTime();
            log.info("请求大数据平台结束！ 用时：" + time);
        } catch (Exception e) {
            logger.info("大数据平台服务请求错误！！！-服务错误！");
            //returnResp.faile(StatusCode.E_G.getCode(),StatusCode.E_G.getMsg(),e.getMessage());
            //return returnResp;
            throw new EnergyException(StatusCode.E_G.getCode(), StatusCode.E_G.getMsg(), "大数据平台服务请求错误！！！-服务错误！" + "错误信息是：" + e.getMessage());

        }
        //请求回数据后，判断是否请求到数据，如果有异常，则将opentsdb的异常信息获取到后返回
        boolean va = StringUtil.va(s);
        LastResp dataresp = null;
        ListResp<LastResp> listResps = new ListResp();
        List<LastResp> lastResps = new ArrayList<>();
        if (va) {
            JSONArray array = JSON.parseArray(s);
            for (Object obj : array) {
                JSONObject jsonObject = JSON.parseObject(obj.toString());
                String value = jsonObject.getString("value");
                //String substring = jsonObject.getJSONObject("tags").getString("equipID").substring(4);
                Date timestamp = jsonObject.getDate("timestamp");
                JSONObject tags = jsonObject.getJSONObject("tags");
                String equipID = tags.getString("equipID");
                String staId = tags.getString("staId");
                dataresp = new LastResp();
                dataresp.setValue(value);
                dataresp.setEquipID(equipID);
                dataresp.setStaId(staId);
                dataresp.setTime(DateUtil.formatDate(timestamp, "yyyy-MM-dd HH:mm:ss"));
                lastResps.add(dataresp);
            }
            listResps.setList(lastResps);
            returnResp.ok(listResps);
            return returnResp;
        } else {
            JSONObject jsonObject = JSON.parseObject(s);
            //returnResp.faile(StatusCode.E_A.getCode(),StatusCode.E_A.getMsg(),jsonObject.toString());
            //return returnResp;
            throw new EnergyException(StatusCode.E_A.getCode(), StatusCode.E_A.getMsg(), jsonObject.toString());

        }
    }
}
