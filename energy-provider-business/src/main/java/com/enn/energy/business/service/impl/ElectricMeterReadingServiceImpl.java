package com.enn.energy.business.service.impl;


import com.enn.energy.business.service.IAccountingUnitService;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.bo.*;
import com.enn.vo.energy.business.po.ElectricMeterReadingDayPo;
import com.enn.vo.energy.business.po.ElectricMeterReadingMonthPo;
import com.enn.vo.energy.business.req.AccountUnitReq;
import com.enn.vo.energy.business.req.ElectricMeterReadingReq;
import com.enn.vo.energy.business.resp.MeterResp;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.business.resp.YesterdayBoardUnitResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enn.energy.business.dao.ElectricMeterReadingDayPoMapper;
import com.enn.energy.business.dao.ElectricMeterReadingHourPoMapper;
import com.enn.energy.business.dao.ElectricMeterReadingMinutePoMapper;
import com.enn.energy.business.dao.ElectricMeterReadingMonthPoMapper;
import com.enn.energy.business.service.IElectricMeterReadingService;
import com.enn.energy.system.common.util.CommonConverter;
import com.enn.vo.energy.business.condition.ElectricMeterReadingHourCondition;
import com.enn.vo.energy.business.condition.ElectricMeterReadingMinuteCondition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author kai.guo
* @version 创建时间：2018年6月7日 上午9:57:26
* @Description 用电费用，用电量综合统计服务
*/
@Service
public class ElectricMeterReadingServiceImpl implements IElectricMeterReadingService {
	
	@Autowired
	private ElectricMeterReadingMinutePoMapper meterReadingMinutePoMapper;
	
	@Autowired
	private ElectricMeterReadingHourPoMapper meterReadingHourPoMapper;
	
	@Autowired
	private ElectricMeterReadingDayPoMapper	meterReadingDayPoMapper;
	
	@Autowired
	private ElectricMeterReadingMonthPoMapper meterReadingMonthPoMapper;

	@Autowired
	private IAccountingUnitService accountingUnitService;

	@Override
	public ElectricMeterReadingHourStatisticsBo countElectricMeterReadingHourByAssignedConditon(
			ElectricMeterReadingHourBo meterReadingHourBo) {
		
		ElectricMeterReadingHourCondition electricMeterReadingHourCondition=CommonConverter.map(meterReadingHourBo, ElectricMeterReadingHourCondition.class);
		
		ElectricMeterReadingHourStatisticsBo readingHourStatisticsBo=meterReadingHourPoMapper.countElectricMeterReadingHourByAssignedConditon(electricMeterReadingHourCondition);
		
		return readingHourStatisticsBo;
	}

	@Override
	public ElectricMeterReadingMinuteStatisticsBo countElectricMeterReadingMinuteByAssignedConditon(
			ElectricMeterReadingMinuteBo meterReadingMinuteBo) {
		
		ElectricMeterReadingMinuteCondition electricMeterReadingMinuteCondition=CommonConverter.map(meterReadingMinuteBo, ElectricMeterReadingMinuteCondition.class);
		
		ElectricMeterReadingMinuteStatisticsBo readingMinuteStatisticsBo=meterReadingMinutePoMapper.countElectricMeterReadingMinuteByAssignedConditon(electricMeterReadingMinuteCondition);
		
		return readingMinuteStatisticsBo;
	}



	/**
	 * 根据车间id查询各个生产线的用电情况（昨日数值）
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
					if (me.getEnergyType().equals("01")) {//拿到电表号
						metersParam.add(me.getLoopNumber());
					}
				}
				//定义时间
				String yesteday = DateUtil.formatDate(DateUtil.parseTime(DateUtil.getLastDay(DateUtil.formatDateTime(new Date()))), "yyyy-MM-dd");
				//拿到电表号后，使用表号请求数据
				YesterdayBoardUnitResp yesterdayBoardUnit1 =null;
				if(metersParam.size()>0){
					 yesterdayBoardUnit1 = meterReadingDayPoMapper.getYesterdayBoardUnit(yesteday, metersParam);
				}
				if(yesterdayBoardUnit1 !=null){
					if(yesterdayBoardUnit1.getFees()!=null){
						yesterdayBoardUnit1.setFees(MathUtils.point(yesterdayBoardUnit1.getFees(),2));
					}
					if(yesterdayBoardUnit1.getUseQuantity()!=null){
						yesterdayBoardUnit1.setUseQuantity(MathUtils.point(yesterdayBoardUnit1.getUseQuantity(),2));
					}
					yesterdayBoardUnit1.setName(unitResp.getName());
					yesterdayBoardUnits.add(yesterdayBoardUnit1);
				}
			}

			resp.ok(yesterdayBoardUnits);
		}
		return resp;
	}

	@Override
	public ElecMeterReadingDayStatisticsBo countElecMeterReadingDayByAssignedConditon(ElecMeterReadingDayBo elecMeterReadingDayBo) {
		ElecMeterReadingDayStatisticsBo elecMeterReadingDayStatisticsBo = meterReadingDayPoMapper.countElecMeterReadingDayByAssignedConditon(elecMeterReadingDayBo);
		return elecMeterReadingDayStatisticsBo;
	}

	@Override
	public Integer getCount(ElecMeterReadingDayBo elecMeterReadingDayBo) {
		Integer count = meterReadingDayPoMapper.getCount(elecMeterReadingDayBo);
		return count;
	}

	/**
	 * 查询日总用电量和总费用
	 *
	 * @param req
	 * @return
	 */
	@Override
	public ElectricMeterReadingDayPo getElecSumByD(ElectricMeterReadingReq req) {
		return meterReadingDayPoMapper.getElecSumByD(req);
	}

	/**
	 * 查询日总用电量和总费用
	 *
	 * @param req
	 * @return
	 */
	@Override
	public ElectricMeterReadingMonthPo getElecSumByM(ElectricMeterReadingReq req) {
		return meterReadingMonthPoMapper.getElecSumByM(req);
	}
}
