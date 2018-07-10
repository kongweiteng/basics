package com.enn.web.controller;

import com.enn.constant.Constant;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.ExcelUtils;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.service.business.IAccountUnitService;
import com.enn.service.business.IOpentsdbService;
import com.enn.service.business.ISteamMeterReadingService;
import com.enn.service.upload.IUploadService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.req.*;
import com.enn.vo.energy.business.resp.MeterResp;
import com.enn.vo.energy.business.resp.ProductionLineData;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.business.upload.UploadResp;
import com.enn.vo.energy.web.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * 企业用汽统计接口
 * @author zxj
 * @since 2018-06-07
 *
 */
@RestController
@RequestMapping("/steamStatistics")
@Api(value = "企业用汽统计接口",tags = {"用汽报表-企业用汽"})
public class SteamStatisticsController {


    protected static final Logger logger = LoggerFactory.getLogger(SteamStatisticsController.class);

    @Autowired
    private IAccountUnitService accountUnitService;
    @Autowired
    private ISteamMeterReadingService steamMeterReadingService;
    @Autowired
    private IOpentsdbService opentsdbService;
    @Autowired
    protected IUploadService uploadService;

    @Value("${file.domain}")
    protected String baseFileUrl;

    /**
     * 企业用汽详情
     * @param electricity
     * @param result
     * @return
     */
    @PostMapping("/findSteam")
    @ApiOperation(value = "企业用汽详情", notes = "企业用汽详情")
    public EnergyResp<List<SteamMonthNum>> addElectric(@RequestBody @Valid Electricity electricity, BindingResult result){
        EnergyResp resp = Verification(electricity,result);
        if(resp.getCode() != null){
            return resp;
        }
        return getSteam(electricity);
    }

    @PostMapping("/getElectricExcel")
    @ApiOperation(value = "企业用汽excel导出", notes = "企业用汽excel导出")
    public EnergyResp<String> getElectricExcel(@RequestBody @Valid Electricity electricity, BindingResult result){
        EnergyResp resp = Verification(electricity,result);
        if(resp.getCode() != null){
            return resp;
        }
        EnergyResp<String> str = new EnergyResp<>();
        EnergyResp<List<SteamMonthNum>> listEnergyResp = getSteam(electricity);
        //导出Excel
        String[] headers = {"核算单元", "总用汽量(t)", "总蒸汽费用(元)"};
        String exportExcelName = "企业用汽详情";
        List<LinkedHashMap<String, String>> rows = new ArrayList<>();
        if(StatusCode.SUCCESS.getCode().equals(listEnergyResp.getCode())){
            for(SteamMonthNum num:listEnergyResp.getData()){
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("1",num.getName());
                map.put("2",num.getSteamAmount() == null ? "" : num.getSteamAmount().toString());
                map.put("3",num.getSteamMonth() == null ? "" : num.getSteamMonth().toString());
                rows.add(map);
            }
        }
        ResponseEntity<byte[]> responseEntity = ExcelUtils.exportExcel(headers, rows, exportExcelName, ExcelUtils.ExcelSuffix.xls);
        Map map = new HashMap() {{
            put("obj", responseEntity.getBody());
        }};
        logger.info("将导出文件上传文件服务器");
        UploadResp uploadResp = uploadService.upObj(map, ExcelUtils.ExcelSuffix.xls.toString());
        if (uploadResp.getRetCode() == 0) {
            str.ok(baseFileUrl + uploadResp.getPath() + "?filename=" + exportExcelName + ".xls");
        } else {
            logger.error("上传文件服务器异常");
            str.faile(StatusCode.E_C.getCode(),StatusCode.E_C.getMsg());
        }
        return str;
    }


    /**
     * 各车间用汽量占比
     * @param electricity
     * @param result
     * @return
     */
    @PostMapping("/getWorkshopRatio")
    @ApiOperation(value = "各车间用汽量占比", notes = "各车间用汽量占比")
    public EnergyResp<EnterpriseSteam> getWorkshopRatio(@RequestBody @Valid Electricity electricity, BindingResult result){
        EnergyResp<EnterpriseSteam> energyResp = new EnergyResp<>();
        EnergyResp resp = Verification(electricity,result);
        if(resp.getCode() != null){
            return resp;
        }
        EnterpriseSteam enterpriseSteam = new EnterpriseSteam();
        SteamMeterReadingReq readingReq = new SteamMeterReadingReq();
        readingReq.setCustId(electricity.getCustId());
        readingReq.setStartTime(electricity.getStartDate());
        readingReq.setEndTime(electricity.getEndDate());
        readingReq.setDateType(electricity.getDownsample());
        readingReq.setAccountingType("02");
        EnergyResp<List<SteamMonthNum>> energyResp1 = steamMeterReadingService.getSteamUnitDay(readingReq);
        if(StatusCode.SUCCESS.getCode().equals(energyResp1.getCode())){
            enterpriseSteam.setSteamMonthNumList(energyResp1.getData());
        }
        List<SteamTimeNum> steamTimeNums = new ArrayList<>();
        EnergyResp<List<SteamTimeNum>> energyResp2 = steamMeterReadingService.getSteamTimeMonthDay(readingReq);
        if (StatusCode.SUCCESS.getCode().equals(energyResp2.getCode())) {
            steamTimeNums = energyResp2.getData();
        }
        BigDecimal db = null;
        for(SteamTimeNum steamTimeNum:steamTimeNums){
            db = MathUtils.add(db,steamTimeNum.getValue());
        }
        enterpriseSteam.setCountPows(db);
        energyResp.ok(enterpriseSteam);
        return energyResp;
    }


    /**
     * 用汽曲线
     * @param electricity
     * @param result
     * @return
     */
    @PostMapping("/getVapourCurve")
    @ApiOperation(value = "用汽曲线", notes = "用汽曲线")
    public EnergyResp<List<SteamTimeNum>> getVapourCurve(@RequestBody @Valid Electricity electricity, BindingResult result){
        EnergyResp<List<SteamTimeNum>> energyResp = new EnergyResp<>();
        EnergyResp resp = Verification(electricity,result);
        if(resp.getCode() != null){
            return resp;
        }
        List<SteamTimeNum> steamTimeNums = new ArrayList<>();
        SteamMeterReadingReq readingReq = new SteamMeterReadingReq();
        readingReq.setCustId(electricity.getCustId());
        readingReq.setStartTime(electricity.getStartDate());
        readingReq.setEndTime(electricity.getEndDate());
        readingReq.setDateType(electricity.getDownsample());
        if(electricity.getDownsample()>1) {
            EnergyResp<List<SteamTimeNum>> energyResp2 = steamMeterReadingService.getSteamTimeMonthDay(readingReq);
            if (StatusCode.SUCCESS.getCode().equals(energyResp2.getCode())) {
                steamTimeNums = energyResp2.getData();
            }
        }
        energyResp.ok(steamTimeNums);
        return energyResp;
    }


    /**
     * 蒸汽负荷情况
     * @param electricity
     * @param result
     * @return
     */
    @PostMapping("/getsteamLoad")
    @ApiOperation(value = "蒸汽负荷情况", notes = "蒸汽负荷情况")
    public EnergyResp<SteamSte> getsteamLoad(@RequestBody @Valid Electricity electricity, BindingResult result){
        EnergyResp resp = Verification(electricity,result);
        if(resp.getCode() != null){
            return resp;
        }
        EnergyResp<SteamSte> energyResp = new EnergyResp<>();
        SteamSte steamSte = new SteamSte();

        SamplDataStaReq staReq = new SamplDataStaReq();
        List<Equip> equips = new ArrayList<>();
        CompanyMeteReq de = new CompanyMeteReq();
        de.setId(Long.valueOf(electricity.getCustId()));
        de.setEnergyType("02");
        EnergyResp<List<MeterResp>> listEnergyResp = accountUnitService.queryMeterListByCompanyAndType(de);
        if(StatusCode.SUCCESS.getCode().equals(listEnergyResp.getCode()) && listEnergyResp.getData().size()>0){
            for(MeterResp meterResp:listEnergyResp.getData()){
                Equip equip = new Equip();
                equip.setEquipMK("STE");
                equip.setEquipID(meterResp.getLoopNumber());
                equip.setStaId(meterResp.getStaId());
                equips.add(equip);
            }
        }
        staReq.setEquips(equips);
        staReq.setDownsample(Constant.DOWNSMPLE_1M);
        staReq.setStart(electricity.getStartDate());
        staReq.setEnd(electricity.getEndDate());
        staReq.setMetric("EMS.FsLP"); //蒸汽流量
        staReq.setPoint(2);
        EnergyResp<ListResp<RmiSamplDataResp>> rmifs = opentsdbService.getSamplDataStaReq(staReq);
        if(StatusCode.SUCCESS.getCode().equals(rmifs.getCode()) && rmifs.getData().getList().size()>0){
            List<ProductionLineData> dataRespFs = new ArrayList<>();
            for(RmiSamplDataResp rmi:rmifs.getData().getList()){
                ProductionLineData productionLineData = new ProductionLineData();
                EnergyResp<List<AccountingUnit>> account = accountUnitService.getUnitByMete(rmi.getEquipID());
                if(account.getData().size()>0){
                    productionLineData.setName(account.getData().get(0).getName());
                }
                productionLineData.setDataResp(rmi.getDataResp());
                dataRespFs.add(productionLineData);
            }
            steamSte.setDataRespFs(dataRespFs);
        }
//        samplDataReq.setMetric("EMS.PAsLP"); //蒸汽压力
//        EnergyResp<ListResp<RmiSamplDataResp>> rmipa = opentsdbService.getSamplData(samplDataReq);
//        if( StatusCode.SUCCESS.getCode().equals(rmipa.getCode()) && rmipa.getData().getList().size()>0){
//            steamSte.setDataRespPa(rmipa.getData().getList().get(0).getDataResp());
//        }
        energyResp.ok(steamSte);
        return energyResp;
    }


    private EnergyResp<List<SteamMonthNum>> getSteam(Electricity electricity){
        SteamMeterReadingReq readingReq = new SteamMeterReadingReq();
        readingReq.setCustId(electricity.getCustId());
        readingReq.setStartTime(electricity.getStartDate());
        readingReq.setEndTime(electricity.getEndDate());
        readingReq.setDateType(electricity.getDownsample());
        return steamMeterReadingService.getSteamUnitDay(readingReq);
    }

    /**
     * 参数验证
     * @param electricity
     * @param result
     * @return
     */
    private EnergyResp Verification(Electricity electricity,BindingResult result){
        EnergyResp energyResp = new EnergyResp<>();
        if(result.hasErrors()){
            energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        if(electricity.getStartDate().compareTo(electricity.getEndDate())>0){
            energyResp.faile(StatusCode.C.getCode(),"开始时间不能大于结束时间!");
            return energyResp;
        }
        if(electricity.getDownsample() == 1) {
            try {
                SimpleDateFormat ft1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String start = DateUtil.getTime(DateUtil.add(ft1.parse(electricity.getStartDate()), Calendar.HOUR, 72));
                if(electricity.getEndDate().compareTo(start) > 0){
                    energyResp.faile(StatusCode.C.getCode(),"开始时间与结束时间相隔不能超过72小时!");
                    return energyResp;
                }
            } catch (ParseException e) {
                throw new EnergyException(StatusCode.C.getCode(), "时间格式不正确！", e.getMessage());
            }
        }
        return energyResp;
    }
}
