package com.enn.web.controller;


import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.service.business.ICustMeterService;
import com.enn.service.business.IOpentsdbService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.req.Equip;
import com.enn.vo.energy.business.req.MeterListReq;
import com.enn.vo.energy.business.req.SamplDataStaReq;
import com.enn.vo.energy.business.resp.DataResp;
import com.enn.vo.energy.business.resp.MeterResp;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;
import com.enn.web.service.ExportService;
import com.enn.web.vo.ReportReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/export")
@Api(tags = {"导出"})
@Slf4j
public class ExportController {
    @Autowired
    private IOpentsdbService opentsdbService;
    @Autowired
    private ICustMeterService custMeterService;
    @Autowired
    private ExportService exportService;

    private BufferedWriter bw = null;

    /**
     * 将数据为null的写入文件
     *
     * @param samplDataStaReq
     * @param result
     * @return
     */
    @ApiOperation(value = "根据表号查询表计为空的值")
    @RequestMapping(value = "getByEquip", method = RequestMethod.POST)
    public EnergyResp<Map<String, String>> getsb(@RequestBody @Valid SamplDataStaReq samplDataStaReq, BindingResult result) throws Exception {
        List<SamplDataStaReq> params = new ArrayList<>();
        params.add(samplDataStaReq);
        EnergyResp<Map<String, String>> getsb = exportService.getsb(params);
        return getsb;
    }


    /**
     * 将所有数据写入文件
     */
    @ApiOperation(value = "根据客户id查询表计为空的值")
    @RequestMapping(value = "getByCompany", method = RequestMethod.POST)
    public EnergyResp<Map<String, String>> getCompany(@RequestBody @Valid ReportReq reportReq, BindingResult result) throws Exception {
        List<String> list = exportService.getList();
        List<SamplDataStaReq> params = new ArrayList<>();
        //获取企业下所有的表号
        MeterListReq meterListReq = new MeterListReq();
        meterListReq.setEnergyType("01");
        meterListReq.setId(reportReq.getId());
        EnergyResp<List<MeterResp>> allMeter = custMeterService.getAllMeter(meterListReq);
        //组装参数
        for (String metric : list) {
            SamplDataStaReq samplDataStaReq = new SamplDataStaReq();
            samplDataStaReq.setStart(reportReq.getStart());
            samplDataStaReq.setEnd(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            samplDataStaReq.setMetric(metric);
            samplDataStaReq.setDownsample("1m-first-nan");
            Equip equip = null;
            List<Equip> equips = new ArrayList<>();
            if (allMeter != null && allMeter.getData().size() > 0) {
                for (MeterResp meterResp : allMeter.getData()) {
                    equip = new Equip();
                    equip.setStaId(meterResp.getStaId());
                    equip.setEquipID(meterResp.getLoopNumber());
                    equip.setEquipMK("METE");
                    equips.add(equip);
                }
            }
            samplDataStaReq.setEquips(equips);
            if (equips.size() <= 0) {
                throw new EnergyException(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), "该客户id下没有计量电表！！！" + reportReq.getId());
            }

            params.add(samplDataStaReq);
        }

        //请求数据平台，将文件生成到本地中
        EnergyResp<Map<String, String>> getsb = exportService.getsb(params);

        return getsb;
    }


    /**
     * 将所有数据写入文件
     */
    @ApiOperation(value = "根据客户id查询表计数据")
    @RequestMapping(value = "getCompanyAll", method = RequestMethod.POST)
    public EnergyResp<Map<String, String>> getCompanyAll(@RequestBody @Valid ReportReq reportReq, BindingResult result) throws Exception {
        List<String> list = exportService.getList();
        List<SamplDataStaReq> params = new ArrayList<>();
        //获取企业下所有的表号
        MeterListReq meterListReq = new MeterListReq();
        meterListReq.setEnergyType("01");
        meterListReq.setId(reportReq.getId());
        EnergyResp<List<MeterResp>> allMeter = custMeterService.getAllMeter(meterListReq);
        for (String metric : list) {
            //组装参数
            SamplDataStaReq samplDataStaReq = new SamplDataStaReq();
            samplDataStaReq.setStart(reportReq.getStart());
            samplDataStaReq.setEnd(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            samplDataStaReq.setMetric(metric);
            samplDataStaReq.setDownsample("1m-first-nan");
            Equip equip = null;
            List<Equip> equips = new ArrayList<>();
            if (allMeter != null && allMeter.getData().size() > 0) {
                for (MeterResp meterResp : allMeter.getData()) {
                    equip = new Equip();
                    equip.setStaId(meterResp.getStaId());
                    equip.setEquipID(meterResp.getLoopNumber());
                    equip.setEquipMK("METE");
                    equips.add(equip);
                }
            }
            samplDataStaReq.setEquips(equips);
            if (equips.size() <= 0) {
                throw new EnergyException(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), "该客户id下没有计量电表！！！" + reportReq.getId());
            }
            params.add(samplDataStaReq);
            //请求数据平台，将文件生成到本地中
        }
        EnergyResp<Map<String, String>> getsb = exportService.getsb2(params);

        return getsb;
    }


}


