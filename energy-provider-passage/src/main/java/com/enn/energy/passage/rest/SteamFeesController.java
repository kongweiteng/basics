package com.enn.energy.passage.rest;

import com.alibaba.fastjson.JSON;
import com.enn.constant.StatusCode;
import com.enn.energy.passage.service.ISteamFeesTaskService;
import com.enn.energy.passage.vo.CalculateParam;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.passage.req.SteamFeesCalculateReq;
import com.enn.vo.energy.passage.resp.SteamFeesCalculateResp;
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

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;


/**
 * 蒸汽用量费用计算Controller
 *
 * @Author: sl
 * @Date: 2018-06-05 09:49
 */
@Api(tags = {"蒸汽用量费用计算"})
@RestController
@RequestMapping("/steamFees")
public class SteamFeesController {
    private static Logger logger = LoggerFactory.getLogger(SteamFeesController.class);

    @Autowired
    private ISteamFeesTaskService steamFeesTaskService;

    @ApiOperation(value = "计算蒸汽费用")
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public EnergyResp calculate() {
        EnergyResp resp = new EnergyResp();
        try {
            logger.info("计算蒸汽费用 -- start");
            steamFeesTaskService.steamFeesTaskForHour();
            logger.info("计算蒸汽费用 -- end");
            resp.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("计算蒸汽费用-calculate error: {}", e.getMessage());
            resp.faile(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMsg(), e.getMessage());
            return resp;
        }
        return resp;
    }

    @ApiOperation(value = "计算蒸汽费用-天-手动")
    @RequestMapping(value = "/calculateDay", method = RequestMethod.POST)
    public EnergyResp calculateDay() {
        EnergyResp resp = new EnergyResp();
        try {
            logger.info("计算蒸汽费用-天-手动 -- start");
            steamFeesTaskService.steamFeesJobForDay();
            logger.info("计算蒸汽费用-天-手动 -- end");
            resp.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("计算蒸汽费用-天-手动-calculateDay error: {}", e.getMessage());
            resp.faile(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMsg(), e.getMessage());
            return resp;
        }
        return resp;
    }

    @ApiOperation(value = "计算蒸汽费用-天(条件)-手动")
    @RequestMapping(value = "/calculateDayByCondition", method = RequestMethod.POST)
    public EnergyResp calculateDayByCondition(@RequestBody CalculateParam param) {
        EnergyResp resp = new EnergyResp();
        try {
            logger.info("计算蒸汽费用-天(条件)-手动 -- start");
            steamFeesTaskService.steamFeesJobForDayByCondition(param);
            logger.info("计算蒸汽费用-天(条件)-手动 -- end");
            resp.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("计算蒸汽费用-天(条件)-手动-calculateDay error: {}", e.getMessage());
            resp.faile(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMsg(), e.getMessage());
            return resp;
        }
        return resp;
    }

    @ApiOperation(value = "计算蒸汽费用-月-手动")
    @RequestMapping(value = "/calculateMonth", method = RequestMethod.POST)
    public EnergyResp calculateMonth() {
        EnergyResp resp = new EnergyResp();
        try {
            logger.info("计算蒸汽费用-月-手动 -- start");
            steamFeesTaskService.steamFeesJobForMonth();
            logger.info("计算蒸汽费用-月-手动 -- end");
            resp.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("计算蒸汽费用-月-手动-calculateDay error: {}", e.getMessage());
            resp.faile(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMsg(), e.getMessage());
            return resp;
        }
        return resp;
    }

    @ApiOperation(value = "计算蒸汽费用-月(条件)-手动")
    @RequestMapping(value = "/calculateMonthByCondition", method = RequestMethod.POST)
    public EnergyResp calculateMonthByCondition(@RequestBody CalculateParam param) {
        EnergyResp resp = new EnergyResp();
        try {
            logger.info("计算蒸汽费用-月-手动 -- start");
            steamFeesTaskService.steamFeesJobForMonthByCondition(param);
            logger.info("计算蒸汽费用-月-手动 -- end");
            resp.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("计算蒸汽费用-月-手动-calculateDay error: {}", e.getMessage());
            resp.faile(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMsg(), e.getMessage());
            return resp;
        }
        return resp;
    }

    @ApiOperation(value = "计算蒸汽费用-小时(符合条件)-手动")
    @RequestMapping(value = "/calculateHour", method = RequestMethod.POST)
    public EnergyResp calculateHour(@RequestBody CalculateParam param) {
        EnergyResp resp = new EnergyResp();
        try {
            logger.info("计算蒸汽费用-小时-手动 -- start");
            steamFeesTaskService.steamFeesJobForHour(param);
            logger.info("计算蒸汽费用-小时-手动 -- end");
            resp.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("计算蒸汽费用-小时-手动-calculateDay error: {}", e.getMessage());
            resp.faile(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMsg(), e.getMessage());
            return resp;
        }
        return resp;
    }

    @ApiOperation(value = "计算蒸汽费用api")
    @RequestMapping(value = "/calculateApi", method = RequestMethod.POST)
    public EnergyResp<SteamFeesCalculateResp> calculateApi(@RequestBody @Valid SteamFeesCalculateReq req, BindingResult result) {

        logger.info("计算蒸汽费用api params: {}", JSON.toJSONString(req));
        EnergyResp<SteamFeesCalculateResp> resp = new EnergyResp<SteamFeesCalculateResp>();
        try {
            // param check
            if (result.hasErrors()) {
                resp.setCode(StatusCode.C.getCode());
                resp.setMsg(result.getFieldError().getDefaultMessage());
                return resp;
            }

            // 计算费用
            BigDecimal fees = steamFeesTaskService.steamFeesApi(req);
            logger.info("计算蒸汽费用api fees result: {}", fees);
            SteamFeesCalculateResp o = new SteamFeesCalculateResp();

            // 费用
            o.setFees(fees);
            resp.ok(o);
        } catch (Exception e) {
            logger.error("计算蒸汽费用api-calculateApi error: {}", e.getMessage());
            resp.faile(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMsg(), e.getMessage());
            return resp;
        }

        return resp;
    }
    
    
    @ApiOperation(value = "计算蒸汽费用api")
    @RequestMapping(value = "/calculateCollectionApi", method = RequestMethod.POST)
    public EnergyResp<List<SteamFeesCalculateResp>> calculateCollectionApi(@RequestBody @Valid List<SteamFeesCalculateReq> req, BindingResult result) {

        logger.info("计算蒸汽费用api params: {}", JSON.toJSONString(req));
        EnergyResp<List<SteamFeesCalculateResp>> resp = new EnergyResp<List<SteamFeesCalculateResp>>();
        try {
            // param check
            if (result.hasErrors()) {
                resp.setCode(StatusCode.C.getCode());
                resp.setMsg(result.getFieldError().getDefaultMessage());
                return resp;
            }

            // 计算费用
            List<SteamFeesCalculateResp> fees = steamFeesTaskService.steamFeesApi(req);
            resp.ok(fees);
        } catch (Exception e) {
            logger.error("计算蒸汽费用api-calculateApi error: {}", e.getMessage());
            resp.faile(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMsg(), e.getMessage());
            return resp;
        }

        return resp;
    }
}
