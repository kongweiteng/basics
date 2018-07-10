package com.enn.energy.business.rest;

import com.enn.constant.Constant;
import com.enn.constant.StatusCode;
import com.enn.energy.business.service.IOpentsdbService;
import com.enn.energy.business.service.IPowerFactorService;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.CommonConverter;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.vo.energy.CollectItemEnum;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.bo.StatisticsDataBo;
import com.enn.vo.energy.business.dto.PowerFactor;
import com.enn.vo.energy.business.po.ElectricMeterPowerFactorDayPo;
import com.enn.vo.energy.business.req.SamplDataReq;
import com.enn.vo.energy.business.req.StatisticsDataReq;
import com.enn.vo.energy.business.resp.DataResp;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = {"功率因数查询"})
@RestController
@RequestMapping("/powerFactor")
public class PowerFactorController {

    private static final Logger logger = LoggerFactory.getLogger(PowerFactorController.class);
    @Autowired
    private IPowerFactorService powerFactorService;
    @Autowired
    private IOpentsdbService opentsdbService;

    @RequestMapping(value = "/getPowerFactorByDay",method = RequestMethod.POST)
    @ApiOperation(value = "通过回路编号（表号）获取按天获取功率因数之和", notes = "通过回路编号（表号）获取按天获取功率因数之和")
    public EnergyResp<BigDecimal> getPowerFactorByDay(@RequestBody StatisticsDataReq statisticsDataReq) {
        StatisticsDataBo statisticsDataBo = CommonConverter.map(statisticsDataReq, StatisticsDataBo.class);
        EnergyResp<BigDecimal> energyResp = new EnergyResp<>();
        try {
            BigDecimal daySum = powerFactorService.findPowerFactorByDay(statisticsDataBo);
            energyResp.ok(daySum);
        } catch (Exception e) {
            energyResp.setCode(StatusCode.A.getCode());
            energyResp.setMsg("查询信息出现异常");
            logger.info("----查询信息出错");
            throw new EnergyException(StatusCode.ERROR.getCode(), "查询信息出现异常！", e.getMessage());
        }

        return energyResp;
    }

    @RequestMapping(value = "/getPowerFactorByHour",method = RequestMethod.POST)
    @ApiOperation(value = "通过回路编号（表号）获取按小时获取功率因数之和", notes = "通过回路编号（表号）获取按小时获取功率因数之和")
    public EnergyResp<BigDecimal> getPowerFactorByHour(@RequestBody StatisticsDataReq statisticsDataReq) {
        StatisticsDataBo statisticsDataBo = CommonConverter.map(statisticsDataReq, StatisticsDataBo.class);
        EnergyResp<BigDecimal> energyResp = new EnergyResp<>();
        try {
            BigDecimal hourSum = powerFactorService.findPowerFactorByHour(statisticsDataBo);
            energyResp.setData(hourSum);
        } catch (Exception e) {
            energyResp.setCode(StatusCode.A.getCode());
            energyResp.setMsg("查询信息出现异常");
            logger.info("----查询信息出错");
            throw new EnergyException(StatusCode.ERROR.getCode(), "查询信息出现异常！", e.getMessage());
        }
        return energyResp;
    }


    /**
     * 计算  时间段内  的需量的最大值 -以及发生时间--多设备的需量相加
     */
    @RequestMapping("/min")
    @ApiOperation(value = "获取一段时间内的最小值", notes = "获取一段时间内的最小值")
    public DataResp getMin(@RequestBody StatisticsDataReq statisticsDataReq) {
        DataResp resp = new DataResp();
        //获取所有设备的需量值
        SamplDataReq samplDataReq = new SamplDataReq();
        samplDataReq.setStart(statisticsDataReq.getStart());
        samplDataReq.setEnd(statisticsDataReq.getEnd());
        samplDataReq.setDownsample(Constant.DOWNSMPLE_1M);
        samplDataReq.setMetric("EST."+CollectItemEnum.COSavg);
        samplDataReq.setEquipID(statisticsDataReq.getMeters());
        EnergyResp<ListResp<RmiSamplDataResp>> samplData = opentsdbService.getSamplData(samplDataReq);
        if (samplData.getCode().equals(StatusCode.SUCCESS.getCode())) {
            BigDecimal min = new BigDecimal(0);
            String minTime = null;
            List<RmiSamplDataResp> data = samplData.getData().getList();
            //定义一个map放置对应时间的值
            Map<String, BigDecimal> dataMap = new HashMap<>();
            //遍历多个设备的采集数据
            for (RmiSamplDataResp rmiSamplDataResp : data) {
                //拿到第一个值进行累计
                List<DataResp> dataResp = rmiSamplDataResp.getDataResp();
                for (DataResp da : dataResp) {
                    String time = da.getTime();
                    BigDecimal value = da.getValue();
                    //将拿到的数据往map中放置
                    if (dataMap.get(time) == null) {
                        dataMap.put(time, value);
                    } else {
                        dataMap.put(time, MathUtils.add(dataMap.get(time), value));
                    }
                }
            }
            //将数据放进map后，根据map中的value比较大小找到最大的一个，并且将时间记录下来
            for (String key : dataMap.keySet()) {
                if (dataMap.get(key)!=null && MathUtils.compare(min, dataMap.get(key))) {//max<value
                    min = dataMap.get(key);
                    minTime = key;
                }
            }
            resp.setTime(minTime);
            resp.setValue(min);
        }
        return resp;
    }
}
