package com.enn.energy.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.enn.constant.Constant;
import com.enn.energy.business.dao.*;
import com.enn.energy.business.service.IMonthEnergyBoardService;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.CustMeter;
import com.enn.vo.energy.business.req.MonthBoardParamRep;
import com.enn.vo.energy.business.resp.MonthEnergyBoardResp;
import com.enn.vo.energy.business.resp.MonthWorkShopEnergyBoardResp;
import com.enn.vo.energy.business.resp.SteamMeterReadingResp;
import com.enn.vo.energy.business.vo.BoardMonthVo;
import com.enn.vo.energy.business.vo.SteamUnitVo;
import com.enn.vo.energy.common.enums.EnergyTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sl
 * @version 创建时间：2018年6月7日 上午10:03:27
 * @Description 月能源看板
 */
@Service
public class MonthEnergyBoardServiceImpl implements IMonthEnergyBoardService {

    private static Logger logger = LoggerFactory.getLogger(MonthEnergyBoardServiceImpl.class);

    @Autowired
    private AccountingUnitMapper accountingUnitMapper;

    @Autowired
    private CustMeterMapper custMeterMapper;

    @Autowired
    private SteamMeterReadingMonthPoMapper meterReadingMonthPoMapper;

    @Autowired
    private ElectricMeterReadingMonthPoMapper electricMeterReadingMonthPoMapper;

    @Autowired
    private SteamMeterReadingDayPoMapper steamMeterReadingDayPoMapper;

    @Resource
    private CustMapper custMapper;

    public MonthEnergyBoardResp boderData(MonthBoardParamRep req) {

        // 月能源看板
        MonthEnergyBoardResp totalResp = new MonthEnergyBoardResp();

        // 企业id
        totalResp.setUnitId(req.getCustID());
        logger.info("企业id: {}", req.getCustID());

        // 根据企业id获取车间ID
        // 获取核算单元-车间
        List<SteamUnitVo> workShops = workShops(req.getCustID());
        logger.info("核算单元-车间: {}", JSON.toJSONString(workShops));

        // 企业-车间-list
        List<MonthWorkShopEnergyBoardResp> workShopList = new ArrayList<>();

        // 车间用汽list
        List<BoardMonthVo> steamMonthVoList = new ArrayList<>();
        // 车间用电list
        List<BoardMonthVo> electricMonthVoList = new ArrayList<>();

        // 遍历车间
        for (SteamUnitVo vo : workShops) {
            logger.info("车间 vo：{}", JSON.toJSONString(vo));
            // 车间-生产线-电表
            List<String> elecricLoopNumbers = loopNumbersForProductLine(vo.getUnitId(), EnergyTypeEnum.ENERGY_ELECTRICITY.getValue());
            logger.info("车间-生产线-电表: {}", JSON.toJSONString(elecricLoopNumbers));
            // 车间-生产线-汽表
            List<String> steamLoopNumbers = loopNumbersForProductLine(vo.getUnitId(), EnergyTypeEnum.ENERGY_STEAM.getValue());
            logger.info("车间-生产线-汽表: {}", JSON.toJSONString(steamLoopNumbers));

            // 根据车间id获取车间电表
            List<CustMeter> elecricts = custMeterList(vo.getUnitId(), EnergyTypeEnum.ENERGY_ELECTRICITY.getValue());
            logger.info("根据车间id获取车间电表: {}", JSON.toJSONString(elecricts));

            // 根据车间id获取车间汽表
            List<CustMeter> steams = custMeterList(vo.getUnitId(), EnergyTypeEnum.ENERGY_STEAM.getValue());
            logger.info("根据车间id获取车间汽表: {}", JSON.toJSONString(steams));

            MonthWorkShopEnergyBoardResp resp = new MonthWorkShopEnergyBoardResp();

            // 电-月-总
            BoardMonthVo electricTotal = null;
            // 汽-月-总
            BoardMonthVo steamTotal = null;

            // 车间月总费用
            BigDecimal monthWorkShopFeesTotal = new BigDecimal("0");

            // 车间-月-电-总用量
            electricTotal = electricTotal(vo, elecricLoopNumbers, elecricts, 0);
            logger.info("车间-月-电-总用量: {}", JSON.toJSONString(electricTotal));

            // 车间-月-汽-总用量
            steamTotal = steamTotal(vo, steamLoopNumbers, steams, 0);
            logger.info("车间-月-汽-总用量: {}", JSON.toJSONString(steamTotal));

            // 车间-月-用电总量
            if (electricTotal != null) {
                // 车间名称
                electricTotal.setUnitName(vo.getUnitName());
                // 车间-月用电
                electricMonthVoList.add(electricTotal);
                // 车间月用电量
                resp.setMonthElectricity(electricTotal.getUseQuantity().setScale(2, RoundingMode.HALF_UP));
                // 车间月用电费用
                resp.setElectricityFees(electricTotal.getFees().setScale(2, RoundingMode.HALF_UP));

                // 获取上上个月用电总量
                BoardMonthVo lastElectricTotal = electricTotal(vo, elecricLoopNumbers, elecricts, 1);
                if (lastElectricTotal != null) {
                    // 车间用电同比
                    resp.setMonthElectricityPercent(percent(electricTotal.getUseQuantity(), lastElectricTotal.getUseQuantity()));
                }
                // 车间-月-总费用
                monthWorkShopFeesTotal = monthWorkShopFeesTotal.add(electricTotal.getFees());
            }

            // 车间-月-用汽总量
            if (steamTotal != null) {
                // 车间名称
                steamTotal.setUnitName(vo.getUnitName());
                // 车间-月用电;
                steamMonthVoList.add(steamTotal);
                // 车间月用电量
                resp.setMonthSteam(steamTotal.getUseQuantity().setScale(2, RoundingMode.HALF_UP));
                // 车间月用电费用
                resp.setSteamFees(steamTotal.getFees().setScale(2, RoundingMode.HALF_UP));

                // 获取上上个月用汽总量
                BoardMonthVo lastSteamTotal = steamTotal(vo, steamLoopNumbers, steams, 1);
                if (lastSteamTotal != null) {
                    // 车间用汽比
                    resp.setMonthSteamPercent(percent(steamTotal.getUseQuantity(), lastSteamTotal.getUseQuantity()));
                }
                // 车间-月-总费用
                monthWorkShopFeesTotal = monthWorkShopFeesTotal.add(steamTotal.getFees());
            }
            // 车间-月-总费用
            resp.setEnergyTotal(monthWorkShopFeesTotal.setScale(2, RoundingMode.HALF_UP));
            // 车间id
            resp.setUnitId(vo.getUnitId());
            // 车间名称
            resp.setWorkShopName(vo.getUnitName());

            // 车间-月-最高-生产线-电
            monthMaxElectricLine(elecricLoopNumbers, resp);

            // 车间-月-最高-生产线-汽
            monthMaxSteamLine(steamLoopNumbers, resp);

            logger.info("车间-board：{}", JSON.toJSONString(resp));

            workShopList.add(resp);
        }

        // 公司总费用、公司-月-用电总量、公司-月-用汽总量
        companyTotal(totalResp, req.getCustID());

        // 公司-月-电-最高车间
        monthMaxElectricWorkShop(totalResp, electricMonthVoList);

        // 公司-月-汽-最高车间
        monthMaxSteamWorkShop(totalResp, steamMonthVoList);

        // 总data
        totalResp.setWorkShopList(workShopList);


        totalResp.setCustName(custMapper.getCustName(req.getCustID().toString()));

        logger.info("总-board：{}", JSON.toJSONString(totalResp));

        return totalResp;
    }

    /**
     * <车间-月-最高-生产线-汽><功能具体实现>
     *
     * @param steamLoopNumbers
     * @param resp
     * @return void
     * @create：2018/6/16 上午10:38
     * @author：sl
     */
    public void monthMaxSteamLine(List<String> steamLoopNumbers, MonthWorkShopEnergyBoardResp resp) {
        if (steamLoopNumbers != null && steamLoopNumbers.size() > 0) {
            // 车间生产线是否有表
            resp.setSteamFlag(true);
            // 车间-月-最高-生产线-汽-data
            steamLineList(resp, steamLoopNumbers);
        } else {
            // 车间生产线是否有表
            resp.setSteamFlag(false);
        }
    }

    /**
     * <车间-月-最高-生产线-电><功能具体实现>
     *
     * @param elecricLoopNumbers
     * @param resp
     * @return void
     * @create：2018/6/16 上午10:38
     * @author：sl
     */
    public void monthMaxElectricLine(List<String> elecricLoopNumbers, MonthWorkShopEnergyBoardResp resp) {
        if (elecricLoopNumbers != null && elecricLoopNumbers.size() > 0) {
            // 车间生产线是否有表
            resp.setElectricFlag(true);

            // 车间-月-最高-生产线-电-data
            electricLineList(resp, elecricLoopNumbers);
        } else {
            // 车间生产线是否有表
            resp.setElectricFlag(false);
        }
    }

    /**
     * <公司总费用、公司-月-用电总量、公司-月-用汽总量><功能具体实现>
     *
     * @param totalResp
     * @return void
     * @create：2018/6/16 上午10:37
     * @author：sl
     */
    public void companyTotal(MonthEnergyBoardResp totalResp, Integer custId) {
        // 公司总费用
        BigDecimal feesTotal = new BigDecimal("0");
        // 公司-月-用电总量
        BigDecimal electricTotal = new BigDecimal("0");
        // 公司-月-用汽总量
        BigDecimal steamTotal = new BigDecimal("0");
        // 公司-月-用电-百分比
        BigDecimal electricPercent = new BigDecimal("0");
        // 公司-月-用汽-百分比
        BigDecimal steamPercent = new BigDecimal("0");

        EntityWrapper<AccountingUnit> w = new EntityWrapper<>();
        w.eq("accounting_type", Constant.ACCOUNTING_TYPE_00).eq("cust_id", custId).eq("is_temp", "0")
                .eq("is_use","1").eq("del_flag","0");
        // 车间表count
        List<AccountingUnit> units =  accountingUnitMapper.selectList(w);
        if (units != null && units.size() >0) {
            Integer cid = units.get(0).getId().intValue();
            // 获取分公司-电-总表
            List<CustMeter> electrics = custMeterList(cid, EnergyTypeEnum.ENERGY_ELECTRICITY.getValue());
            // 获取分公司-汽-总表
            List<CustMeter> steams = custMeterList(cid, EnergyTypeEnum.ENERGY_STEAM.getValue());

            // 电-总表
            if (electrics != null && electrics.size() > 0) {
                BoardMonthVo electricMonthTotalVo = electricTotalMonth(electrics, 0);
                // 获取分公司总表-月-电
                if (electricMonthTotalVo != null) {
                    // 总表-月-电-费用
                    feesTotal = feesTotal.add(electricMonthTotalVo.getFees());
                    // 总表-月-电-用量
                    electricTotal = electricTotal.add(electricMonthTotalVo.getUseQuantity());
                }

                // 上上月表总
                BoardMonthVo last = electricTotalMonth(electrics, 1);

                // 月总电量百分比
                if (last != null) {
                    if (last.getUseQuantity().doubleValue() != 0) {
                        electricPercent = (electricMonthTotalVo.getUseQuantity().subtract(last.getUseQuantity())).divide(last.getUseQuantity(), 5, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal("100"));
                    } else {
                        electricPercent = new BigDecimal("0");
                    }
                }
            }

            // 汽-总表
            if (steams != null && steams.size() > 0) {
                // 获取分公司总表-月-汽
                BoardMonthVo monthTotalVo = steamTotalMonth(steams, 0);
                if (monthTotalVo != null) {
                    // 总表-月-汽-费用
                    feesTotal = feesTotal.add(monthTotalVo.getFees());
                    // 总表-月-汽-用量
                    steamTotal = steamTotal.add(monthTotalVo.getUseQuantity());
                }

                // 上上月表总
                BoardMonthVo last = steamTotalMonth(steams, 1);

                // 月总电量百分比
                if (last != null) {
                    if (last.getUseQuantity().doubleValue() != 0) {
                        steamPercent = (monthTotalVo.getUseQuantity().subtract(last.getUseQuantity())).divide(last.getUseQuantity(), 5, BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal("100"));
                    } else {
                        steamPercent = new BigDecimal("0");
                    }
                }
            }
        }

        totalResp.setEnergyTotal(feesTotal.setScale(2, RoundingMode.HALF_UP));
        totalResp.setMonthTotalElectricity(electricTotal.setScale(2, RoundingMode.HALF_UP));
        totalResp.setMonthTotalSteam(steamTotal.setScale(2, RoundingMode.HALF_UP));
        totalResp.setMonthElectricityPercent(electricPercent.setScale(2, RoundingMode.HALF_UP));
        totalResp.setMonthSteamPercent(steamPercent.setScale(2, RoundingMode.HALF_UP));
    }

    /**
     * <分公司-汽-月-总表><功能具体实现>
     *
     * @param steams
     * @param dateFlag
     * @return com.enn.vo.energy.business.vo.BoardMonthVo
     * @create：2018/6/17 上午11:58
     * @author：sl
     */
    public BoardMonthVo steamTotalMonth(List<CustMeter> steams, Integer dateFlag) {
        return meterReadingMonthPoMapper.monthTotalVo(steams.get(0).getLoopNumber(), dateFlag);
    }

    /**
     * <分公司-电-月-总表><功能具体实现>
     *
     * @param electrics
     * @param dateFlag
     * @return com.enn.vo.energy.business.vo.BoardMonthVo
     * @create：2018/6/17 上午11:59
     * @author：sl
     */
    public BoardMonthVo electricTotalMonth(List<CustMeter> electrics, Integer dateFlag) {
        BoardMonthVo vo = electricMeterReadingMonthPoMapper.monthTotalVo(electrics.get(0).getLoopNumber(), dateFlag);
        return vo;
    }

    /**
     * <公司-月-汽-最高车间><功能具体实现>
     *
     * @param totalResp
     * @param steamMonthVoList
     * @return void
     * @create：2018/6/16 上午10:32
     * @author：sl
     */
    public void monthMaxSteamWorkShop(MonthEnergyBoardResp totalResp, List<BoardMonthVo> steamMonthVoList) {
        if (steamMonthVoList != null && steamMonthVoList.size() > 0) {
            // 最大用汽里车间
            BoardMonthVo max = Collections.max(steamMonthVoList);
            // 最大用汽量
            totalResp.setMaxMonthSteam(max.getUseQuantity().setScale(2, RoundingMode.HALF_UP));
            // 根据最大用汽生产线表获取车间信息
            SteamUnitVo steamUnitVo = getSteamUnitVo(max.getMeterNo());
            // 车间名称
            totalResp.setMaxMonthSteamName(max.getUnitName());
        }
    }

    /**
     * <公司-月-电-最高车间><功能具体实现>
     *
     * @param totalResp
     * @param electricMonthVoList
     * @return void
     * @create：2018/6/16 上午10:31
     * @author：sl
     */
    public void monthMaxElectricWorkShop(MonthEnergyBoardResp totalResp, List<BoardMonthVo> electricMonthVoList) {
        if (electricMonthVoList != null && electricMonthVoList.size() > 0) {
            // 最大用汽里车间
            BoardMonthVo max = Collections.max(electricMonthVoList);
            // 最大用汽量
            totalResp.setMaxMonthElectricity(max.getUseQuantity().setScale(2, RoundingMode.HALF_UP));
            // 车间名称
            totalResp.setMaxMonthElectricityName(max.getUnitName());
        }
    }

    /**
     * <根据车间id获取车间电、汽表><功能具体实现>
     *
     * @param custId
     * @param energyType
     * @return java.util.List<com.enn.vo.energy.business.po.CustMeter>
     * @create：2018/6/16 上午10:28
     * @author：sl
     */
    public List<CustMeter> custMeterList(Integer custId, String energyType) {
        EntityWrapper<CustMeter> wrapper = new EntityWrapper<>();
        wrapper.eq("energy_type", energyType).eq("accounting_id", custId).eq("is_accoun", "1").isNotNull("loop_number");
        // 车间表count
        return custMeterMapper.selectList(wrapper);
    }

    /**
     * <车间-月-汽-总用量><功能具体实现>
     *
     * @param vo
     * @param steamLoopNumbers
     * @param steams
     * @return com.enn.vo.energy.business.vo.BoardMonthVo
     * @create：2018/6/16 上午10:26
     * @author：sl
     */
    public BoardMonthVo steamTotal(SteamUnitVo vo, List<String> steamLoopNumbers, List<CustMeter> steams, Integer dateFlag) {
        BoardMonthVo steamTotal = null;
        if (steams != null && steams.size() > 0) {
            // 车间有表-月用电量以车间为准
            steamTotal = meterReadingMonthPoMapper.steamMonthTotalByWorkShop(vo.getUnitId(), dateFlag);
        } else {
            // 车间无表-查生产线表-月用电量以生产线为准
            if (steamLoopNumbers != null && steamLoopNumbers.size() > 0) {
                steamTotal = meterReadingMonthPoMapper.steamMonthTotal(steamLoopNumbers, dateFlag);
            }
        }
        return steamTotal;
    }

    /**
     * <车间-月-电-总用量><功能具体实现>
     *
     * @param vo
     * @param elecricLoopNumbers
     * @param elecricts
     * @return com.enn.vo.energy.business.vo.BoardMonthVo
     * @create：2018/6/16 上午10:25
     * @author：sl
     */
    public BoardMonthVo electricTotal(SteamUnitVo vo, List<String> elecricLoopNumbers, List<CustMeter> elecricts, Integer dateFlag) {
        BoardMonthVo electricTotal = null;
        if (elecricts != null && elecricts.size() > 0) {
            // 车间有表(电表+汽表)-月用电量以车间为准
            electricTotal = electricMeterReadingMonthPoMapper.electricMonthTotalByWorkShop(vo.getUnitId(), dateFlag);
        } else {
            // 车间无表-查生产线表-月用电量以生产线为准
            if (elecricLoopNumbers != null && elecricLoopNumbers.size() > 0) {
                // 月-生产线-总-电
                electricTotal = electricMeterReadingMonthPoMapper.electricMonthTotal(elecricLoopNumbers, dateFlag);
            }
        }
        return electricTotal;
    }

    /**
     * <生产线-电-最高><功能具体实现>
     *
     * @param resp
     * @param elecricLoopNumbers
     * @return void
     * @create：2018/6/16 上午10:21
     * @author：sl
     */
    public void electricLineList(MonthWorkShopEnergyBoardResp resp, List<String> elecricLoopNumbers) {
        // 根据表获取month data list-生产线
        List<BoardMonthVo> vos = electricMeterReadingMonthPoMapper.electricMonthData(elecricLoopNumbers);

        if (vos != null && vos.size() > 0) {
            // 最大用汽生产线
            //BoardMonthVo max = Collections.max(vos);
            BoardMonthVo max = vos.get(0);

            // 最大生产线用汽量
            resp.setMaxMonthElectricity(max.getUseQuantity().setScale(2, RoundingMode.HALF_UP));
            // 最大用汽生产线名称
            resp.setMaxMonthElectricityName(max.getUnitName());
        }
    }

    /**
     * <生产线-汽-最高><功能具体实现>
     *
     * @param resp
     * @param steamLoopNumbers
     * @return void
     * @create：2018/6/16 上午10:21
     * @author：sl
     */
    public void steamLineList(MonthWorkShopEnergyBoardResp resp, List<String> steamLoopNumbers) {
        // 根据表获取month data list-生产线
        List<BoardMonthVo> vos = meterReadingMonthPoMapper.steamMonthData(steamLoopNumbers);

        if (vos != null && vos.size() > 0) {
            // 最大用汽生产线
            //BoardMonthVo max = Collections.max(vos);
            BoardMonthVo max = vos.get(0);

            // 最大生产线用汽量
            resp.setMaxMonthSteam(max.getUseQuantity().setScale(2, RoundingMode.HALF_UP));
            resp.setMaxMonthSteamName(max.getUnitName());
        }
    }

    /**
     * <根据企业id获取车间信息><功能具体实现>
     *
     * @param custID
     * @return java.util.List<com.enn.vo.energy.business.vo.SteamUnitVo>
     * @create：2018/6/16 上午10:22
     * @author：sl
     */
    public List<SteamUnitVo> workShops(Integer custID) {
        return accountingUnitMapper.workShops(custID);
    }

    /**
     * <获取生产线能源类型表><功能具体实现>
     *
     * @param workShopId
     * @param energyType
     * @return java.util.List<java.lang.String>
     * @create：2018/6/16 上午10:22
     * @author：sl
     */
    public List<String> loopNumbersForProductLine(Integer workShopId, String energyType) {
        return custMeterMapper.loopNumbersForProductLine(workShopId, energyType);
    }

    /**
     * <根据最大用汽生产线表获取车间信息><功能具体实现>
     *
     * @param meterNo
     * @return com.enn.vo.energy.business.vo.SteamUnitVo
     * @create：2018/6/13 下午8:13
     * @author：sl
     */
    public SteamUnitVo getSteamUnitVo(String meterNo) {
        return custMeterMapper.custMeterByLoopNumber(meterNo);
    }

    /**
     * <月能源看板-总-用气量曲线><功能具体实现>
     *
     * @param req
     * @return java.util.List<com.enn.vo.energy.business.resp.SteamMeterReadingResp>
     * @create：2018/6/16 下午3:11
     * @author：sl
     */
    @Override
    public List<SteamMeterReadingResp> boderSteamCurve(MonthBoardParamRep req) {

        List<SteamMeterReadingResp> list = null;
        EntityWrapper<AccountingUnit> wrapper = new EntityWrapper<>();
        wrapper.eq("accounting_type", Constant.ACCOUNTING_TYPE_00).eq("cust_id", req.getCustID()).eq("is_temp", "0").eq("is_use", "1");

        List<AccountingUnit> accountingUnits = accountingUnitMapper.selectList(wrapper);
        if (accountingUnits != null && accountingUnits.size() > 0) {

            // 获取分公司-汽-总表
            List<CustMeter> steams = custMeterList(accountingUnits.get(0).getId().intValue(), EnergyTypeEnum.ENERGY_STEAM.getValue());

            if (steams != null && steams.size() > 0) {
                list = steamMeterReadingDayPoMapper.boderSteamCurve(steams.get(0).getLoopNumber());
                logger.info("月能源看板-总-用气量曲线 data: {}", JSON.toJSONString(list));
            }
        }


        // 车间id获取表
        return list;
    }

    /**
     * <百分比><功能具体实现>
     *
     * @param vol
     * @param lastVol
     * @return java.math.BigDecimal
     * @create：2018/6/16 下午3:28
     * @author：sl
     */
    public BigDecimal percent(BigDecimal vol, BigDecimal lastVol) {
        BigDecimal percent = new BigDecimal("0");
        if (lastVol.doubleValue() != 0) {
            percent = (vol.subtract(lastVol)).divide(lastVol, 5, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
        }
        return percent;
    }
}
