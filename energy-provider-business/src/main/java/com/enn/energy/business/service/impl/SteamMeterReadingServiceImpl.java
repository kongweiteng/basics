package com.enn.energy.business.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.enn.constant.StatusCode;
import com.enn.energy.business.dao.*;
import com.enn.energy.business.service.IAccountingUnitService;
import com.enn.energy.business.service.ISteamMeterReadingService;
import com.enn.energy.system.common.util.*;
import com.enn.service.upload.IUploadService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.bo.*;
import com.enn.vo.energy.business.condition.SteamMeterReadingDayCondition;
import com.enn.vo.energy.business.condition.SteamMeterReadingHourCondition;
import com.enn.vo.energy.business.condition.SteamMeterReadingMinuteCondition;
import com.enn.vo.energy.business.condition.SteamMeterReadingMonthCondition;
import com.enn.vo.energy.business.po.CustMeter;
import com.enn.vo.energy.business.po.SteamMeterReadingDayPo;
import com.enn.vo.energy.business.po.SteamMeterReadingMinutePo;
import com.enn.vo.energy.business.po.SteamMeterReadingMonthPo;
import com.enn.vo.energy.business.req.AccountUnitReq;
import com.enn.vo.energy.business.req.SteamMeterDayReq;
import com.enn.vo.energy.business.resp.*;
import com.enn.vo.energy.business.upload.UploadResp;
import com.enn.vo.energy.common.enums.SteamMetricEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author kai.guo
 * @version 创建时间：2018年6月7日 上午10:03:27
 * @Description 蒸汽费用，用气量（日，小时，分钟）统计
 */
@Service
public class SteamMeterReadingServiceImpl implements ISteamMeterReadingService {

    protected static final Logger logger = LoggerFactory.getLogger(SteamMeterReadingServiceImpl.class);

    @Autowired
    private SteamMeterReadingDayPoMapper meterReadingDayPoMapper;

    @Autowired
    private SteamMeterReadingHourPoMapper meterReadingHourPoMapper;

    @Autowired
    private SteamMeterReadingMinutePoMapper meterReadingMinutePoMapper;

    @Autowired
    private SteamMeterReadingMonthPoMapper meterReadingMonthPoMapper;
    @Autowired
    private IAccountingUnitService accountingUnitService;

    @Autowired
    private CustMeterMapper custMeterMapper;

    @Autowired
    protected IUploadService uploadService;

    /**
     * 获取指定时间段内的hour 蒸汽费用，用量信息
     */
    @Override
    public SteamMeterReadingHourStatisticsBo countSteamMeterReadingHourByAssignedConditon(
            SteamMeterReadingHourBo meterReadingHourBo) {

        SteamMeterReadingHourCondition steamMeterReadingHourCondition = CommonConverter.map(meterReadingHourBo, SteamMeterReadingHourCondition.class);

        SteamMeterReadingHourStatisticsBo meterReadingHourStatisticsBo = meterReadingHourPoMapper.countSteamMeterReadingHourByAssignedConditon(steamMeterReadingHourCondition);

        return meterReadingHourStatisticsBo;
    }

    @Override
    public SteamMeterReadingMinuteStatisticsBo countSteamMeterReadingMinuteByAssignedConditon(
            SteamMeterReadingMinuteBo meterReadingMinuteBo) {

        SteamMeterReadingMinuteCondition steamMeterReadingMinuteCondition = CommonConverter.map(meterReadingMinuteBo, SteamMeterReadingMinuteCondition.class);
        steamMeterReadingMinuteCondition.setMetric(SteamMetricEnum.STEAM_TOTAL_FLOW.getMetric());

        SteamMeterReadingMinuteStatisticsBo meterReadingMinuteStatisticsBo = meterReadingMinutePoMapper.countSteamMeterReadingMinuteByAssignedConditon(steamMeterReadingMinuteCondition);

        return meterReadingMinuteStatisticsBo;
    }

    @Override
    public SteamMeterReadingDayStatisticsBo countSteamMeterReadingDayByAssignedConditon(SteamMeterReadingDayBo meterReadingDayBo) {

        SteamMeterReadingDayCondition steamMeterReadingDayCondition = CommonConverter.map(meterReadingDayBo, SteamMeterReadingDayCondition.class);

        SteamMeterReadingDayStatisticsBo steamMeterReadingDayStatisticsBo = meterReadingDayPoMapper.countSteamMeterReadingDayByAssignedConditon(steamMeterReadingDayCondition);

        return steamMeterReadingDayStatisticsBo;
    }

    @Override
    public SteamMeterReadingMonthStatisticsBo countSteamMeterReadingMonthByAssignedConditon(SteamMeterReadingMonthBo meterReadingMonthBo) {

        SteamMeterReadingMonthCondition steamMeterReadingMonthCondition = CommonConverter.map(meterReadingMonthBo, SteamMeterReadingMonthCondition.class);

        SteamMeterReadingMonthStatisticsBo steamMeterReadingMonthStatisticsBo = meterReadingMonthPoMapper.countSteamMeterReadingMonthByAssignedConditon(steamMeterReadingMonthCondition);

        return steamMeterReadingMonthStatisticsBo;
    }
    
    @Override
	public List<SteamMeterReadingMinuteResultBo> querySteamMeterReadingMinuteByAssignedConditon(
			SteamMeterReadingMinuteBo meterReadingDayBo) {
    	
    	SteamMeterReadingMinuteCondition  condition=CommonConverter.map(meterReadingDayBo, SteamMeterReadingMinuteCondition.class);
    	condition.setMetric(SteamMetricEnum.STEAM_TOTAL_FLOW.getMetric());
    	
    	List<SteamMeterReadingMinutePo>  meterReadingMinutePos=meterReadingMinutePoMapper.querySteamMeterReadingMinuteByAssignedConditon(condition);
    	
    	List<SteamMeterReadingMinuteResultBo> resultBos=CommonConverter.mapList(meterReadingMinutePos, SteamMeterReadingMinuteResultBo.class);
		
		return resultBos;
	}

    @Override
    public List<SteamMeterReadingDayPo> querySteamMeterReadingDayByAssignedConditon(SteamMeterReadingDayBo meterReadingDayBo) {
        SteamMeterReadingDayCondition steamMeterReadingDayCondition = CommonConverter.map(meterReadingDayBo, SteamMeterReadingDayCondition.class);

        List<SteamMeterReadingDayPo> steamMeterReadingDayPos = meterReadingDayPoMapper.querySteamMeterReadingDayByAssignedConditon(steamMeterReadingDayCondition);

        return steamMeterReadingDayPos;
    }

    @Override
    public List<SteamMeterReadingDayPo> querySteamDayGroup(SteamMeterReadingDayBo meterReadingDayBo) {
        SteamMeterReadingDayCondition steamMeterReadingDayCondition = CommonConverter.map(meterReadingDayBo, SteamMeterReadingDayCondition.class);
        return meterReadingDayPoMapper.querySteamDayGroup(steamMeterReadingDayCondition);
    }

    @Override
    public List<SteamMeterReadingMonthPo> querySteamMeterReadingMonthByAssignedConditon(SteamMeterReadingMonthBo meterReadingMonthBo) {
        SteamMeterReadingMonthCondition steamMeterReadingMonthCondition = CommonConverter.map(meterReadingMonthBo, SteamMeterReadingMonthCondition.class);
        List<SteamMeterReadingMonthPo> steamMeterReadingMonthPos = meterReadingMonthPoMapper.querySteamMeterReadingMonthByAssignedConditon(steamMeterReadingMonthCondition);
        return steamMeterReadingMonthPos;
    }

    @Override
    public List<SteamMeterReadingMonthPo> querySteamMonthGroup(SteamMeterReadingMonthBo meterReadingMonthBo) {
        SteamMeterReadingMonthCondition steamMeterReadingMonthCondition = CommonConverter.map(meterReadingMonthBo, SteamMeterReadingMonthCondition.class);
        return meterReadingMonthPoMapper.querySteamMonthGroup(steamMeterReadingMonthCondition);
    }

    @Override
    public List<StatisticsDataResp> getSteamByMonth(SteamMeterReadingMonthBo meterReadingMonthBo) {

        SteamMeterReadingMonthCondition steamMeterReadingMonthCondition = CommonConverter.map(meterReadingMonthBo, SteamMeterReadingMonthCondition.class);

        List<StatisticsDataResp> statisticsDataRespList = meterReadingMonthPoMapper.getSteamByMonth(steamMeterReadingMonthCondition);

        return statisticsDataRespList;
    }

    @Override
    public List<StatisticsDataResp> getSteamByDay(SteamMeterReadingDayBo meterReadingDayBo) {
        SteamMeterReadingDayCondition steamMeterReadingDayCondition = CommonConverter.map(meterReadingDayBo, SteamMeterReadingDayCondition.class);

        List<StatisticsDataResp> statisticsDataRespList = meterReadingDayPoMapper.getSteamByDay(steamMeterReadingDayCondition);

        return statisticsDataRespList;
    }

    /**
     * 根据车间id查询各个生产线的用气情况（昨日数值）
     */
    @Override
    public EnergyResp<List<YesterdayBoardUnitResp>> getYesterdayBoardUnit(DefaultReq defaultReq) {
        EnergyResp<List<YesterdayBoardUnitResp>> resp = new EnergyResp<>();
        List<YesterdayBoardUnitResp> yesterdayBoardUnits = new ArrayList<>();
        //根据车间id获取生产线信息
        AccountUnitReq accountUnitReq = new AccountUnitReq();
        accountUnitReq.setId(defaultReq.getId());
        accountUnitReq.setIsAccount(true);
        accountUnitReq.setAccountingType("03");
        EnergyResp<List<UnitResp>> listEnergyResp = accountingUnitService.queryAccountListT(accountUnitReq);
        //遍历生产线
        if (listEnergyResp.getData() != null) {

            for (UnitResp unitResp : listEnergyResp.getData()) {
                List<String> metersParam = new ArrayList<>();
                List<MeterResp> meters = unitResp.getMeters();
                //拿到表号
                for (MeterResp me : meters) {
                    if (me.getEnergyType().equals("02")) {//拿到蒸汽表号
                        metersParam.add(me.getLoopNumber());
                    }
                }
                //定义时间
                String yesteday = DateUtil.formatDate(DateUtil.parseTime(DateUtil.getLastDay(DateUtil.formatDateTime(new Date()))), "yyyy-MM-dd");
                //拿到蒸汽表号后，使用表号请求数据
                YesterdayBoardUnitResp yesterdayBoardUnit1 =null;
                if(metersParam!=null && metersParam.size()>0){
                    yesterdayBoardUnit1 = meterReadingDayPoMapper.getYesterdayBoardUnit(yesteday, metersParam);
                }
                if(yesterdayBoardUnit1 !=null){
                    if(yesterdayBoardUnit1.getUseQuantity()!=null){
                        yesterdayBoardUnit1.setUseQuantity(MathUtils.point(yesterdayBoardUnit1.getUseQuantity(),2));
                    }
                    if(yesterdayBoardUnit1.getFees()!=null){
                        yesterdayBoardUnit1.setFees(MathUtils.point(yesterdayBoardUnit1.getFees(),2));
                    }
                    yesterdayBoardUnit1.setName(unitResp.getName());
                    yesterdayBoardUnits.add(yesterdayBoardUnit1);
                }
            }

            resp.ok(yesterdayBoardUnits);
        }
        return resp;
    }

    /**
     *
     * <车间-天-用汽曲线><功能具体实现>
     *
     * @create：2018/6/11 上午10:42
     * @author：sl
     * @param req
     * @return com.enn.vo.energy.business.resp.SteamMeterReadingResp
     */
    @Override
    public List<SteamMeterReadingResp> getSteamMeterForDay(SteamMeterDayReq req) {
        // 判断车间是否存在汽表
        checkExistLoop(req);
        List<SteamMeterReadingResp> resp = meterReadingDayPoMapper.getSteamMeterForDay(req);
        return resp;
    }

    /**
     *
     * <判断车间是否存在汽表><功能具体实现>
     *
     * @create：2018/6/12 下午2:39
     * @author：sl
     * @param req
     * @return void
     */
    public void checkExistLoop(SteamMeterDayReq req) {
        // 判断车间是否存在汽表
        EntityWrapper<CustMeter> wrapper = new EntityWrapper<>();
        wrapper.eq("energy_type", "02").eq("accounting_id", req.getUnitId()).eq("is_accoun", "1").isNotNull("loop_number");
        int count = custMeterMapper.selectCount(wrapper);
        if (count > 0) {
            req.setExist(1);
        } else {
            req.setFlag(0);
        }
    }

    /**
     *
     * <车间-天-用汽详情></车间-天-用汽详情><功能具体实现>
     *
     * @create：2018/6/11 下午1:44
     * @author：sl
     * @param req
     * @return com.enn.energy.system.common.util.PagedList<com.enn.vo.energy.business.resp.SteamMeterReadingResp>
     */
    @Override
    public PagedList<SteamMeterReadingResp> getSteamMeterDetailForDay(SteamMeterDayReq req) {
        // 判断车间是否存在汽表
        checkExistLoop(req);

        // page
        PageHelper.startPage(req.getPageNum(), req.getPageSize());

        // page data
        List<SteamMeterReadingResp> list = meterReadingDayPoMapper.getSteamMeterForDay(req);
        PageInfo<SteamMeterReadingResp> pageInfo = new PageInfo<SteamMeterReadingResp>(list);

        // new page
        PagedList<SteamMeterReadingResp> poPagedList = PagedList.newMe(pageInfo.getPageNum(), pageInfo.getPageSize(),
                pageInfo.getTotal(), list);

        return poPagedList;
    }

    /**
     *
     * <车间-年-用汽曲线><功能具体实现>
     *
     * @create：2018/6/11 下午1:52
     * @author：sl
     * @param req
     * @return java.util.List<com.enn.vo.energy.business.resp.SteamMeterReadingResp>
     */
    @Override
    public List<SteamMeterReadingResp> getSteamMeterForYear(SteamMeterDayReq req) {
        // 判断车间是否存在汽表
        checkExistLoop(req);

        List<SteamMeterReadingResp> resp = meterReadingDayPoMapper.getSteamMeterForYear(req);
        return resp;
    }

    /**
     *
     * <车间-年-用汽详情><功能具体实现>
     *
     * @create：2018/6/11 下午1:57
     * @author：sl
     * @param req
     * @return com.enn.energy.system.common.util.PagedList<com.enn.vo.energy.business.resp.SteamMeterReadingDetailResp>
     */
    @Override
    public PagedList<SteamMeterReadingDetailResp> getSteamMeterDetailForYear(SteamMeterDayReq req) {
        // 判断车间是否存在汽表
        checkExistLoop(req);

        // page
        PageHelper.startPage(req.getPageNum(), req.getPageSize());

        // page data
        List<SteamMeterReadingDetailResp> list = meterReadingDayPoMapper.getSteamMeterDetailForYear(req);
        PageInfo<SteamMeterReadingDetailResp> pageInfo = new PageInfo<SteamMeterReadingDetailResp>(list);

        // new page
        PagedList<SteamMeterReadingDetailResp> poPagedList = PagedList.newMe(pageInfo.getPageNum(), pageInfo.getPageSize(),
                pageInfo.getTotal(), list);


        return poPagedList;
    }

    /**
     *
     * <用汽报表-车间用汽-天><功能具体实现>
     *
     * @create：2018/6/28 下午5:25
     * @author：sl
     * @param req
     * @return com.enn.vo.energy.EnergyResp<com.enn.vo.energy.business.upload.UploadResp>
     */
    @Override
    public EnergyResp<UploadResp> exportDetailDataForExcelDay(SteamMeterDayReq req) {
        EnergyResp<UploadResp> resp = new EnergyResp<UploadResp>();
        try {
            // 判断车间是否存在汽表
            checkExistLoop(req);

            // page data
            List<SteamMeterReadingResp> results = meterReadingDayPoMapper.getSteamMeterForDay(req);

            List<LinkedHashMap<String, String>> rows = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(results)) {
                rows = results.stream().map(item -> {
                    LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap();
                    linkedHashMap.put("1", item.getReadTime());
                    linkedHashMap.put("2", item.getUseQuantity().toString());
                    linkedHashMap.put("3", item.getFees().toString());
                    return linkedHashMap;

                }).collect(Collectors.toList());
            }
            String[] tableHeaders = {"统计时间", "总用汽量(t)", "总蒸汽费用(元)"};
            ResponseEntity<byte[]> responseEntity = ExcelUtils.exportExcel(tableHeaders, rows, System.currentTimeMillis() + "", ExcelUtils.ExcelSuffix.xls);
            Map map = new HashMap() {{
                put("obj", responseEntity.getBody());
            }};
            logger.info("将导出文件上传文件服务器");
            UploadResp uploadResp = uploadService.upObj(map, ExcelUtils.ExcelSuffix.xls.toString());
            if (uploadResp.getRetCode() == 0) {
                uploadResp.setPath(uploadResp.getPath());
                resp.ok(uploadResp);
            } else {
                logger.error("上传文件服务器异常");
                resp.faile(StatusCode.ERROR.getCode(), "上传文件服务器异常");
            }

        } catch (Exception e) {
            logger.error("用汽报表-车间用汽-天-导出excel失败 {}", e.getMessage());
            resp.faile(StatusCode.ERROR.getCode(), e.getMessage());
        }
        return resp;
    }

    /**
     *
     * <用汽报表-车间用汽(年)><功能具体实现>
     *
     * @create：2018/6/28 下午5:38
     * @author：sl
     * @param req
     * @return com.enn.vo.energy.EnergyResp<com.enn.vo.energy.business.upload.UploadResp>
     */
    @Override
    public EnergyResp<UploadResp> detailDataForExcelYear(SteamMeterDayReq req) {
        EnergyResp<UploadResp> resp = new EnergyResp<UploadResp>();
        try {
            // 判断车间是否存在汽表
            checkExistLoop(req);

            // page data
            List<SteamMeterReadingDetailResp> results = meterReadingDayPoMapper.getSteamMeterDetailForYear(req);

            List<LinkedHashMap<String, String>> rows = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(results)) {
                rows = results.stream().map(item -> {
                    LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap();
                    linkedHashMap.put("1", item.getReadTime());
                    linkedHashMap.put("2", item.getUseQuantity().toString());
                    linkedHashMap.put("3", item.getFees().toString());
                    linkedHashMap.put("4", item.getLastMonthPercent().toString());
                    linkedHashMap.put("5", item.getSamePeriodPercent().toString());
                    return linkedHashMap;

                }).collect(Collectors.toList());
            }
            String[] tableHeaders = {"统计时间", "总用汽量(t)", "总蒸汽费用(元)", "上月同比(%)", "同期环比(%)"};
            ResponseEntity<byte[]> responseEntity = ExcelUtils.exportExcel(tableHeaders, rows, System.currentTimeMillis() + "", ExcelUtils.ExcelSuffix.xls);
            Map map = new HashMap() {{
                put("obj", responseEntity.getBody());
            }};
            logger.info("将导出文件上传文件服务器");
            UploadResp uploadResp = uploadService.upObj(map, ExcelUtils.ExcelSuffix.xls.toString());
            if (uploadResp.getRetCode() == 0) {
                uploadResp.setPath(uploadResp.getPath());
                resp.ok(uploadResp);
            } else {
                logger.error("上传文件服务器异常");
                resp.faile(StatusCode.ERROR.getCode(), "上传文件服务器异常");
            }
        } catch (Exception e) {
            logger.error("用汽报表-车间用汽-年-导出excel失败 {}", e.getMessage());
            resp.faile(StatusCode.ERROR.getCode(), e.getMessage());
        }
        return resp;
    }

}
