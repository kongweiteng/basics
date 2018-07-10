package com.enn.energy.business.rest;

import com.enn.constant.StatusCode;
import com.enn.energy.business.service.IMonthEnergyBoardService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.req.MonthBoardParamRep;
import com.enn.vo.energy.business.resp.MonthEnergyBoardResp;
import com.enn.vo.energy.business.resp.SteamMeterReadingResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * 月能源看板Controller
 *
 * @Author: sl
 * @Date: 2018-06-07 16:39
 */
@Api(tags = {"月能源看板"})
@RestController
@RequestMapping("/monthEnergy")
public class MonthEnergyBoardController {
    private static Logger logger = LoggerFactory.getLogger(MonthEnergyBoardController.class);

    @Autowired
    private IMonthEnergyBoardService monthEnergyBoardService;

    @RequestMapping(value = "/boderData", method = RequestMethod.POST)
    @ApiOperation(value = "月能源看板", notes = "月能源看板")
    public EnergyResp<MonthEnergyBoardResp> boderData(@RequestBody MonthBoardParamRep req, BindingResult result) {

        EnergyResp<MonthEnergyBoardResp> resp = new EnergyResp<MonthEnergyBoardResp>();
        // param 校验，error back
        if (req.getCustID() == null) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "企业id不能为空");
            return resp;
        }
        MonthEnergyBoardResp data = null;
        try {
            data = monthEnergyBoardService.boderData(req);
        } catch (Exception e) {
            logger.error("月能源看板 error: {}", e.getMessage());
            resp.faile(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMsg(), e.getMessage());
            return resp;
        }

        resp.ok(data);
        return resp;
    }

    @RequestMapping(value = "/boderSteamCurve", method = RequestMethod.POST)
    @ApiOperation(value = "月能源看板-总-用气量曲线", notes = "月能源看板-总-用气量曲线")
    public EnergyResp<List<SteamMeterReadingResp>> boderSteamCurve(@RequestBody MonthBoardParamRep req, BindingResult result) {

        EnergyResp<List<SteamMeterReadingResp>> resp = new EnergyResp<List<SteamMeterReadingResp>>();
        // param 校验，error back
        if (req.getCustID() == null) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "企业id不能为空");
            return resp;
        }
        List<SteamMeterReadingResp> data = null;
        try {
            data = monthEnergyBoardService.boderSteamCurve(req);

            if (data != null && data.size() >0) {
                List<SteamMeterReadingResp> list = lastMonthDay();

                data.forEach(e -> {
                    list.remove(e);
                });
                data.addAll(list);

                Collections.sort(data);
            }
        } catch (Exception e) {
            logger.error("月能源看板-总-用气量曲线 error: {}", e.getMessage());
            resp.faile(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMsg(), e.getMessage());
            return resp;
        }

        resp.ok(data);
        return resp;
    }

    /**
     *
     * <上月天数><功能具体实现>
     *
     * @create：2018/6/16 下午8:43
     * @author：sl
     * @param
     * @return java.util.List<com.enn.vo.energy.business.resp.SteamMeterReadingResp>
     */
    public List<SteamMeterReadingResp> lastMonthDay() {
        List<SteamMeterReadingResp> list = new ArrayList();

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
            SteamMeterReadingResp o = new SteamMeterReadingResp();
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

            o.setReadTime(aDate);
            list.add(o);
        }
        return list;
    }

}
