package com.enn.web.controller;

import com.alibaba.fastjson.JSON;
import com.enn.constant.Constant;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.service.business.IMonthBoardService;
import com.enn.service.business.ISteamMeterReadingService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.po.DataResp;
import com.enn.vo.energy.business.req.*;
import com.enn.vo.energy.business.resp.*;
import com.enn.vo.energy.common.enums.EnergyTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequestMapping("/monthEnergy")
@RestController
@Api(value = "月用能看板", description = "月用能看板", tags = "月用能看板")
public class MonthEnergyBoardController extends BaseController {

    @Autowired
    private IMonthBoardService monthBoardService;

    @Autowired
    private ISteamMeterReadingService steamMeterReadingService;

    @RequestMapping(value = "/boderData", method = RequestMethod.POST)
    @ApiOperation(value = "月能源看板", notes = "月能源看板")
    public EnergyResp<MonthEnergyBoardResp> boderData(@RequestBody MonthBoardParamRep req, BindingResult result) {

        EnergyResp<MonthEnergyBoardResp> resp = new EnergyResp<MonthEnergyBoardResp>();
        // param 校验，error back
        if (req.getCustID() == null) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "企业id不能为空");
            return resp;
        }
        try {
            resp = monthBoardService.boderData(req);
        } catch (Exception e) {
            logger.error("月能源看板 error: {}", e.getMessage());
            resp.faile(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMsg(), e.getMessage());
            return resp;
        }
        return resp;
    }

    @RequestMapping(value = "/boderSteamCurve", method = RequestMethod.POST)
    @ApiOperation(value = "月能源看板-用汽曲线", notes = "月能源看板-用汽曲线")
    public EnergyResp<List<SteamMeterReadingResp>> boderSteamCurve(@RequestBody MonthBoardParamRep req) {

        EnergyResp<List<SteamMeterReadingResp>> resp = new EnergyResp<List<SteamMeterReadingResp>>();
        // param 校验，error back
        if (req.getCustID() == null) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "企业id不能为空");
            return resp;
        }
        try {
            resp = monthBoardService.boderSteamCurve(req);
        } catch (Exception e) {
            logger.error("月能源看板-用汽曲线 error: {}", e.getMessage());
            resp.faile(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMsg(), e.getMessage());
            return resp;
        }
        return resp;
    }

    @RequestMapping(value = "/steamWorkShopDetail", method = RequestMethod.POST)
    @ApiOperation(value = "月能源看板-车间用汽明细+曲线", notes = "月能源看板-车间用汽明细+曲线")
    public EnergyResp<DetailedResp> getLastMonthDetailed(@RequestBody @Valid WorkshopReq req, BindingResult result) {
        EnergyResp<DetailedResp> resp = new EnergyResp<DetailedResp>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }

        resp = steamMeterReadingService.getLastMonthDetailed(req.getId());
        return resp;
    }

    @RequestMapping(value = "/boderElectricCurve", method = RequestMethod.POST)
    @ApiOperation(value = "获取月能源看板-用电曲线", notes = "获取月能源看板-用电曲线")
    public EnergyResp<List<DataResp>> boderCurve(@RequestBody @Valid CustReq custReq, BindingResult result) {
        EnergyResp<List<DataResp>> energyResp = new EnergyResp<>();
        if (result.hasErrors()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        String reg = "^[0-9]*[1-9][0-9]*$";
        if (!Pattern.compile(reg).matcher(custReq.getCustID()).find()) {
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "企业ID格式错误，提示:企业ID只能是数字");
            return energyResp;
        }
        CompanyMeteReq companyMeteReq = new CompanyMeteReq();
        companyMeteReq.setEnergyType(Constant.ACCOUNTING_TYPE_01);
        companyMeteReq.setId(Long.parseLong(custReq.getCustID()));
        logger.info("查询企业下的下级计量表 + accountingUnit/queryMeterListByCompanyAndType");
        EnergyResp<List<MeterResp>> meterListByCompanyAndType = accountUnitService.queryMeterListByCompanyAndType(companyMeteReq);
        logger.info("查询企业下的下级计量表 list: {}", JSON.toJSONString(meterListByCompanyAndType));
        if (!meterListByCompanyAndType.getMsg().equals(StatusCode.SUCCESS.getMsg())) {
            energyResp.faile(StatusCode.A.getCode(), StatusCode.A.getMsg(), "查询企业下级计量表异常");
            return energyResp;
        }
        if (CollectionUtils.isEmpty(meterListByCompanyAndType.getData())) {
            energyResp.faile(StatusCode.E_B.getCode(),StatusCode.E_B.getMsg(),"未发现企业下级计量表");
            return energyResp;
        }
        //得到企业下级表号
        //待确定表号前接口是否返回表号是否拼接METE
        List<String> meterNoList = meterListByCompanyAndType.getData().stream().map(s->s.getLoopNumber()).collect(Collectors.toList());
        //List<String> meterNoList = meterListByCompanyAndType.getData().stream().map(MeterResp::getLoopNumber).collect(Collectors.toList());
        ElectricMeterReadingReq electricMeterReadingReq = new ElectricMeterReadingReq();
        electricMeterReadingReq.setStartTime(DateUtil.getLastMonthFirst());
        electricMeterReadingReq.setEndTime(DateUtil.getLastMonth());
        electricMeterReadingReq.setMeterNoList(meterNoList);
        logger.info("调取查询用电曲线(月)+custElectricCount/getMeterByDay");
        EnergyResp<MeterReadingResp> meterReadingRespEnergyResp = null;
        try {
            meterReadingRespEnergyResp = countService.getMeterByDay(electricMeterReadingReq);
        } catch (Exception e) {
            logger.error("调取查询用电曲线(月)+custElectricCount/getMeterByDay接口异常" + e);
        }
        List<DataResp> sumList;
        if (!CollectionUtils.isEmpty(meterReadingRespEnergyResp.getData().getSumList())&&null != meterReadingRespEnergyResp) {
            sumList = meterReadingRespEnergyResp.getData().getSumList();
        } else {
            energyResp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), "请求查询用电曲线(月)数据为空");
            return energyResp;
        }

        if (sumList != null && sumList.size() >0) {
            List<DataResp> list = lastMonthDay();

            sumList.forEach(e -> {
                list.remove(e);
            });
            sumList.addAll(list);

            Collections.sort(sumList);
        }

        energyResp.ok(sumList);
        return energyResp;
    }

    public List<DataResp> lastMonthDay() {
        List<DataResp> list = new ArrayList();

        //获取前一个月第一天
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, 0);
        calendar1.set(Calendar.DAY_OF_MONTH, 1);

        int year = calendar1.get(Calendar.YEAR);//年份
        int month = calendar1.get(Calendar.MONTH);//月份

        //获取前一个月最后一天
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_MONTH, 0);
        int day = calendar2.get(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= day; i++) {
            DataResp o = new DataResp();
            String aDate = "";
            if (i < 10) {
                if (month < 10) {
                    aDate = new StringBuffer(String.valueOf(year)).append("-0").append(month).append("-0").append(i).toString();
                } else {
                    aDate = new StringBuffer(String.valueOf(year)).append("-").append(month).append("-0").append(i).toString();
                }
            } else {
                if (month < 10) {
                    aDate = new StringBuffer(String.valueOf(year)).append("-0").append(month).append("-").append(i).toString();
                } else {
                    aDate = new StringBuffer(String.valueOf(year)).append("-").append(month).append("-").append(i).toString();
                }
            }

            o.setDateTime(aDate);
            list.add(o);
        }
        return list;
    }

    @RequestMapping(value = "/boderElectricDetail", method = RequestMethod.POST)
    @ApiOperation(value = "获取月能源看板-车间用电明细", notes = "获取月能源看板-车间用电明细")
    public EnergyResp boderElectricDetail(@RequestBody @Valid WorkshopReq workshopReq, BindingResult result) {
        EnergyResp resp = new EnergyResp();
        List resultList = new ArrayList();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        ElectricMeterReadingReq electricMeterReadingReq = new ElectricMeterReadingReq();
        String startTime = DateUtil.getLastMonthOfYear(DateUtil.getMonth());
        String endTime = DateUtil.getLastMonthOfYear(DateUtil.getMonth());
        electricMeterReadingReq.setStartTime(startTime);
        electricMeterReadingReq.setEndTime(endTime);
        List<UnitResp> unitRespList = getLineListByCustId(Long.parseLong(workshopReq.getId()));
        if (null == unitRespList || unitRespList.size() <= 0) {
            resp.faile(StatusCode.C.getCode(), "该公司下未查到生产线信息");
            return resp;
        }
        for (UnitResp unitResp : unitRespList) {
            List<String> meterNoList = getMeterNoList(unitResp.getId());
            if (null != meterNoList && meterNoList.size() > 0) {
                electricMeterReadingReq.setMeterNoList(meterNoList);
                EnergyResp<ElectricMeterReadingResp> electricMeterReadingRespEnergyResp = countService.getSumByMonth(electricMeterReadingReq);
                if (!electricMeterReadingRespEnergyResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
                    return electricMeterReadingRespEnergyResp;
                }
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("name", unitResp.getName());
                resultMap.put("electricMeterReadingRespEnergyResp", getRmiSamplDataRespListResp(electricMeterReadingRespEnergyResp.getData()));
                resultList.add(resultMap);
            }
        }
        resp.ok(resultList);
        return resp;
    }

    @RequestMapping(value = "/boderElectricStackImage", method = RequestMethod.POST)
    @ApiOperation(value = "获取月能源看板-各生产线分时用电量统计", notes = "获取月能源看板-各生产线分时用电量统计")
    public EnergyResp boderElectricStackImage(@RequestBody @Valid WorkshopReq workshopReq, BindingResult result) {
        EnergyResp resp = new EnergyResp();
        List resultList = new ArrayList();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        ElectricMeterReadingReq electricMeterReadingReq = new ElectricMeterReadingReq();
        String startTime = DateUtil.getLastMonthOfYear(DateUtil.getMonth());
        String endTime = DateUtil.getLastMonthOfYear(DateUtil.getMonth());
        electricMeterReadingReq.setStartTime(startTime);
        electricMeterReadingReq.setEndTime(endTime);
        List<UnitResp> unitRespList = getLineListByCustId(Long.parseLong(workshopReq.getId()));
        if (null == unitRespList || unitRespList.size() <= 0) {
            resp.faile(StatusCode.C.getCode(), "该公司下未查到生产线信息");
            return resp;
        }
        for (UnitResp unitResp : unitRespList) {
            List<String> meterNoList = getMeterNoList(unitResp.getId());
            if (null != meterNoList && meterNoList.size() > 0) {
                electricMeterReadingReq.setMeterNoList(meterNoList);
                EnergyResp<ElectricMeterReadingResp> electricMeterReadingRespEnergyResp = countService.getSumByMonth(electricMeterReadingReq);
                if (!electricMeterReadingRespEnergyResp.getCode().equals(StatusCode.SUCCESS.getCode())) {
                    return electricMeterReadingRespEnergyResp;
                }
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("name", unitResp.getName());
                resultMap.put("electricMeterReadingRespEnergyResp", getRmiSamplDataRespListResp(electricMeterReadingRespEnergyResp.getData()));
                resultList.add(resultMap);
            }
        }
        resp.ok(resultList);
        return resp;
    }

    /**
     * 处理两位小数
     *
     * @param electricMeterReadingResp
     * @return
     */
    private ElectricMeterReadingResp getRmiSamplDataRespListResp(ElectricMeterReadingResp electricMeterReadingResp) {
        if (null != electricMeterReadingResp) {
            electricMeterReadingResp.setSumQuantity(MathUtils.twoDecimal(electricMeterReadingResp.getSumQuantity()));
            electricMeterReadingResp.setTipQuantity(MathUtils.twoDecimal(electricMeterReadingResp.getTipQuantity()));
            electricMeterReadingResp.setPeakQuantity(MathUtils.twoDecimal(electricMeterReadingResp.getPeakQuantity()));
            electricMeterReadingResp.setFlatQuantity(MathUtils.twoDecimal(electricMeterReadingResp.getFlatQuantity()));
            electricMeterReadingResp.setValleyQuantity(MathUtils.twoDecimal(electricMeterReadingResp.getValleyQuantity()));
            electricMeterReadingResp.setSumFees(MathUtils.twoDecimal(electricMeterReadingResp.getSumFees()));
            electricMeterReadingResp.setTipFees(MathUtils.twoDecimal(electricMeterReadingResp.getTipFees()));
            electricMeterReadingResp.setPeakFees(MathUtils.twoDecimal(electricMeterReadingResp.getPeakFees()));
            electricMeterReadingResp.setFlatFees(MathUtils.twoDecimal(electricMeterReadingResp.getFlatFees()));
            electricMeterReadingResp.setValleyFees(MathUtils.twoDecimal(electricMeterReadingResp.getValleyFees()));
        }
        return electricMeterReadingResp;
    }

    /**
     * 根据车间ID查询生产线数据
     *
     * @param workshopId
     * @return
     */
    private List<UnitResp> getLineListByCustId(Long workshopId) {
        AccountUnitReq accountUnitReq = new AccountUnitReq();
        accountUnitReq.setId(workshopId);
        accountUnitReq.setAccountingType(Constant.ACCOUNTING_TYPE_03);
        accountUnitReq.setIsAccount(true);
        EnergyResp<List<UnitResp>> unitRespListEnergyResp = accountUnitService.queryAccountList(accountUnitReq);
        if (null == unitRespListEnergyResp
                || null == unitRespListEnergyResp.getData()
                || unitRespListEnergyResp.getData().size() <= 0) {
            return null;
        }
        return unitRespListEnergyResp.getData();
    }

    /**
     * 根据生产线查询计量表
     *
     * @param lineId
     * @return
     */
    private List<String> getMeterNoList(Long lineId) {
        DefaultReq de = new DefaultReq();
        de.setId(lineId);
        EnergyResp<List<MeterResp>> meterRespListEnergyResp = accountUnitService.queryMeterListByAccount(de);
        if (null == meterRespListEnergyResp
                || null == meterRespListEnergyResp.getData()
                || meterRespListEnergyResp.getData().size() <= 0) {
            return null;
        }
        List<String> meterNoList = new ArrayList<String>();
        for (MeterResp meterResp : meterRespListEnergyResp.getData()) {
            if (meterResp.getEnergyType().equals(EnergyTypeEnum.ENERGY_ELECTRICITY.getValue())) {
                meterNoList.add(meterResp.getLoopNumber());
            }
        }
        return meterNoList;
    }
}
