package com.enn.energy.business.service.impl;

import com.enn.energy.business.dao.*;
import com.enn.constant.Constant;
import com.enn.energy.business.service.ICustElectricCountService;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.po.DataResp;
import com.enn.vo.energy.business.req.ElectricMeterReadingReq;
import com.enn.vo.energy.business.resp.ElectricMeterReadingResp;
import com.enn.vo.energy.business.resp.MeterReadingResp;
import com.enn.vo.energy.business.resp.StatisticsDataResp;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 企业用电统计ServiceImpl
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 16:10
 */
@Service
public class CustElectricCountServiceImpl implements ICustElectricCountService {
	protected static final Logger logger = LoggerFactory.getLogger(CustElectricCountServiceImpl.class);

	@Autowired
	private ElectricMeterTipReadingDayMapper tipDayMapper;
	@Autowired
	private ElectricMeterTipReadingMonthMapper tipMonthMapper;
	@Autowired
	private ElectricMeterPeakReadingDayMapper peakDayMapper;
	@Autowired
	private ElectricMeterPeakReadingMonthMapper peakMonthMapper;
	@Autowired
	private ElectricMeterFlatReadingDayMapper flatDayMapper;
	@Autowired
	private ElectricMeterFlatReadingMonthMapper flatMonthMapper;
	@Autowired
	private ElectricMeterValleyReadingDayMapper valleyDayMapper;
	@Autowired
	private ElectricMeterValleyReadingMonthMapper valleyMonthMapper;
	@Autowired
	private ElectricMeterReadingMinutePoMapper minuteMapper;
	@Autowired
	private ElectricMeterReadingDayPoMapper readingDayPoMapper;
	@Autowired
	private ElectricMeterReadingMonthPoMapper readingMonthPoMapper;

	@Autowired
	private ElectricMeterReadingHourPoMapper meterReadingHourPoMapper;


	/**
	 * 查询企业分时电费占比(分钟)
	 *
	 * @param req
	 * @return
	 */
	@Override
	public ElectricMeterReadingResp getSumByMinute(ElectricMeterReadingReq req) {
		ElectricMeterReadingResp resp = minuteMapper.getSum(req);
		if (null == resp) {
			return resp;
		}
		resp.setSumQuantity(resp.sumQuantity());
		resp.setSumFees(resp.sumFees());
		return resp;
	}

	/**
	 * 查询企业分时电费占比(日)
	 *
	 * @param req
	 * @return
	 */
	@Override
	public ElectricMeterReadingResp getSumByDay(ElectricMeterReadingReq req) {
		ElectricMeterReadingResp resp = new ElectricMeterReadingResp();
		resp = addValue(tipDayMapper.getSum(req), resp);
		resp = addValue(peakDayMapper.getSum(req), resp);
		resp = addValue(flatDayMapper.getSum(req), resp);
		resp = addValue(valleyDayMapper.getSum(req), resp);
		resp.setSumQuantity(resp.sumQuantity());
		resp.setSumFees(resp.sumFees());
		return resp;
	}

	/**
	 * 查询企业分时电费占比(月)
	 *
	 * @param req
	 * @return
	 */
	@Override
	public ElectricMeterReadingResp getSumByMonth(ElectricMeterReadingReq req) {
		ElectricMeterReadingResp resp = new ElectricMeterReadingResp();
		resp = addValue(tipMonthMapper.getSum(req), resp);
		resp = addValue(peakMonthMapper.getSum(req), resp);
		resp = addValue(flatMonthMapper.getSum(req), resp);
		resp = addValue(valleyMonthMapper.getSum(req), resp);
		resp.setSumQuantity(resp.sumQuantity());
		resp.setSumFees(resp.sumFees());
		return resp;
	}

	/**
	 * 复制实体值到目标实体值
	 *
	 * @param value
	 * @param target
	 * @return
	 */
	private ElectricMeterReadingResp addValue(ElectricMeterReadingResp value, ElectricMeterReadingResp target) {
		target.setTipQuantity(target.getTipQuantity().add(value.getTipQuantity()));
		target.setPeakQuantity(target.getPeakQuantity().add(value.getPeakQuantity()));
		target.setFlatQuantity(target.getFlatQuantity().add(value.getFlatQuantity()));
		target.setValleyQuantity(target.getValleyQuantity().add(value.getValleyQuantity()));
		target.setTipFees(target.getTipFees().add(value.getTipFees()));
		target.setPeakFees(target.getPeakFees().add(value.getPeakFees()));
		target.setFlatFees(target.getFlatFees().add(value.getFlatFees()));
		target.setValleyFees(target.getValleyFees().add(value.getValleyFees()));
		return target;
	}

	@Override
	public MeterReadingResp getMeterByDay(ElectricMeterReadingReq req) {
		MeterReadingResp meterReadingResp = new MeterReadingResp();
		List<DataResp> dataResps;
		DataResp dataResp;
		/*//判断开始时间是否是今天
		if (DateUtil.compareDate(req.getStartTime(), DateUtil.getDay(new Date()))) {
			req.setStartTime(DateUtil.getNowDay());
			req.setEndTime(DateUtil.getTime());
			try {
				//尖的集合
				dataResps = Lists.newArrayList();
				dataResp = minuteMapper.getMeterMinute(req, Constant.ELECTRIC_TYPE_01);
				dataResps.add(dataResp);
				meterReadingResp.setTipList(dataResps);
				//峰的集合
				dataResps = Lists.newArrayList();
				dataResp = minuteMapper.getMeterMinute(req, Constant.ELECTRIC_TYPE_02);
				dataResps.add(dataResp);
				meterReadingResp.setPeakList(dataResps);
				//平的集合
				dataResps = Lists.newArrayList();
				dataResp = minuteMapper.getMeterMinute(req, Constant.ELECTRIC_TYPE_03);
				dataResps.add(dataResp);
				meterReadingResp.setFlatList(dataResps);
				//谷
				dataResps = Lists.newArrayList();
				dataResp = minuteMapper.getMeterMinute(req, Constant.ELECTRIC_TYPE_04);
				dataResps.add(dataResp);
				meterReadingResp.setValleyList(dataResps);
				//总量
				dataResps = Lists.newArrayList();
				dataResp = minuteMapper.getSumMinute(req);
				dataResps.add(dataResp);
				meterReadingResp.setSumList(dataResps);
			} catch (Exception e) {
				logger.error("查询分钟表的尖峰平谷数据异常");
			}
		} else {
			if (DateUtil.compareDate(req.getEndTime(), DateUtil.getDay(new Date()))) {
				req.setEndTime(DateUtil.getLastDay(req.getEndTime()));
				//尖时刻(日表)
				List<DataResp> dataRespTipList = tipDayMapper.getMeterTipDay(req);
				//峰时刻(日表)
				List<DataResp> dataRespPeakList = peakDayMapper.getMeterPeakDay(req);
				//平时刻(日表)
				List<DataResp> dataRespsFlatList = flatDayMapper.getMeterFlatDay(req);
				//谷时刻(日表)
				List<DataResp> dataRespsValleyList = valleyDayMapper.getMeterValleyDay(req);
				//总量(日表)
				List<DataResp> dataRespSumList = readingDayPoMapper.getMeterReadingDay(req);
				//拼装分钟表查询参数
				req.setStartTime(DateUtil.getNowDay());
				req.setEndTime(DateUtil.getTime());
				//尖时刻(分钟表)
				dataResp = minuteMapper.getMeterMinute(req, Constant.ELECTRIC_TYPE_01);
				dataRespTipList.add(dataResp);
				meterReadingResp.setTipList(dataRespTipList);
				//峰时刻(分钟表)
				dataResp = minuteMapper.getMeterMinute(req, Constant.ELECTRIC_TYPE_02);
				dataRespPeakList.add(dataResp);
				meterReadingResp.setPeakList(dataRespPeakList);
				//平时刻(分钟表)
				dataResp = minuteMapper.getMeterMinute(req, Constant.ELECTRIC_TYPE_03);
				dataRespsFlatList.add(dataResp);
				meterReadingResp.setFlatList(dataRespsFlatList);
				//谷时刻(分钟表)
				dataResp = minuteMapper.getMeterMinute(req, Constant.ELECTRIC_TYPE_04);
				dataRespsValleyList.add(dataResp);
				meterReadingResp.setValleyList(dataRespsValleyList);
				//总量(分钟表)
				dataResp = minuteMapper.getSumMinute(req);
				dataRespSumList.add(dataResp);
				meterReadingResp.setSumList(dataRespSumList);
			} else {
				//尖时刻(日表)
				List<DataResp> dataRespTipList = tipDayMapper.getMeterTipDay(req);
				//峰时刻(日表)
				List<DataResp> dataRespPeakList = peakDayMapper.getMeterPeakDay(req);
				//平时刻(日表)
				List<DataResp> dataRespsFlatList = flatDayMapper.getMeterFlatDay(req);
				//谷时刻(日表)
				List<DataResp> dataRespsValleyList = valleyDayMapper.getMeterValleyDay(req);
				//总量(日表)
				List<DataResp> dataRespSumList = readingDayPoMapper.getMeterReadingDay(req);
				meterReadingResp.setTipList(dataRespTipList);
				meterReadingResp.setPeakList(dataRespPeakList);
				meterReadingResp.setFlatList(dataRespsFlatList);
				meterReadingResp.setValleyList(dataRespsValleyList);
				meterReadingResp.setSumList(dataRespSumList);
			}
		}*/

		if (DateUtil.compareDate(req.getStartTime(), DateUtil.getDay(new Date()))) {
			req.setStartTime(DateUtil.getLastOneday());
			req.setEndTime(DateUtil.getLastOneday());
			List<DataResp> listTip = yearOfMonth(req.getStartTime(),req.getEndTime());
			List<DataResp> listPeak = yearOfMonth(req.getStartTime(),req.getEndTime());
			List<DataResp> listFlat = yearOfMonth(req.getStartTime(),req.getEndTime());
			List<DataResp> listValley = yearOfMonth(req.getStartTime(),req.getEndTime());
			List<DataResp> listSum = yearOfMonth(req.getStartTime(),req.getEndTime());
			List<DataResp> list = monthOfDay(DateUtil.getLastOneday(),DateUtil.getLastOneday());
			//尖时刻(日表)
			List<DataResp> dataRespTipList = tipDayMapper.getMeterTipDay(req);
			//峰时刻(日表)
			List<DataResp> dataRespPeakList = peakDayMapper.getMeterPeakDay(req);
			//平时刻(日表)
			List<DataResp> dataRespsFlatList = flatDayMapper.getMeterFlatDay(req);
			//谷时刻(日表)
			List<DataResp> dataRespsValleyList = valleyDayMapper.getMeterValleyDay(req);
			//总量(日表)
			List<DataResp> dataRespSumList = readingDayPoMapper.getMeterReadingDay(req);
			dataRespTipList.forEach(e -> {
				listTip.remove(e);
			});
			dataRespTipList.addAll(listTip);
			Collections.sort(dataRespTipList);

			dataRespPeakList.forEach(e -> {
				listPeak.remove(e);
			});
			dataRespPeakList.addAll(listPeak);
			Collections.sort(dataRespPeakList);

			dataRespsFlatList.forEach(e -> {
				listFlat.remove(e);
			});
			dataRespsFlatList.addAll(listFlat);
			Collections.sort(dataRespsFlatList);

			dataRespsValleyList.forEach(e -> {
				listValley.remove(e);
			});
			dataRespsValleyList.addAll(listValley);
			Collections.sort(dataRespsValleyList);

			dataRespSumList.forEach(e -> {
				listSum.remove(e);
			});
			dataRespSumList.addAll(listSum);
			Collections.sort(dataRespSumList);
			meterReadingResp.setTipList(dataRespTipList);
			meterReadingResp.setPeakList(dataRespPeakList);
			meterReadingResp.setFlatList(dataRespsFlatList);
			meterReadingResp.setValleyList(dataRespsValleyList);
			meterReadingResp.setSumList(dataRespSumList);
		} else {
			if (DateUtil.compareDate(req.getEndTime(), DateUtil.getDay(new Date()))) {
				req.setEndTime(DateUtil.getLastOneday());
				//尖时刻(日表)
				List<DataResp> dataRespTipList = tipDayMapper.getMeterTipDay(req);
				//峰时刻(日表)
				List<DataResp> dataRespPeakList = peakDayMapper.getMeterPeakDay(req);
				//平时刻(日表)
				List<DataResp> dataRespsFlatList = flatDayMapper.getMeterFlatDay(req);
				//谷时刻(日表)
				List<DataResp> dataRespsValleyList = valleyDayMapper.getMeterValleyDay(req);
				//总量(日表)
				List<DataResp> dataRespSumList = readingDayPoMapper.getMeterReadingDay(req);
				List<DataResp> listTip = monthOfDay(req.getStartTime(),DateUtil.getLastOneday());
				dataRespTipList.forEach(e -> {
					listTip.remove(e);
				});
				dataRespTipList.addAll(listTip);
				Collections.sort(dataRespTipList);

				List<DataResp> listPeak = monthOfDay(req.getStartTime(),DateUtil.getLastOneday());
				dataRespPeakList.forEach(e -> {
					listPeak.remove(e);
				});
				dataRespPeakList.addAll(listPeak);
				Collections.sort(dataRespPeakList);

				List<DataResp> listFlat = monthOfDay(req.getStartTime(),DateUtil.getLastOneday());
				dataRespsFlatList.forEach(e -> {
					listFlat.remove(e);
				});
				dataRespsFlatList.addAll(listFlat);
				Collections.sort(dataRespsFlatList);

				List<DataResp> listValley = monthOfDay(req.getStartTime(),DateUtil.getLastOneday());
				dataRespsValleyList.forEach(e -> {
					listValley.remove(e);
				});
				dataRespsValleyList.addAll(listValley);
				Collections.sort(dataRespsValleyList);

				List<DataResp> listSum = monthOfDay(req.getStartTime(),DateUtil.getLastOneday());
				dataRespSumList.forEach(e -> {
					listSum.remove(e);
				});
				dataRespSumList.addAll(listSum);
				Collections.sort(dataRespSumList);

				meterReadingResp.setTipList(dataRespTipList);
				meterReadingResp.setPeakList(dataRespPeakList);
				meterReadingResp.setFlatList(dataRespsFlatList);
				meterReadingResp.setValleyList(dataRespsValleyList);
				meterReadingResp.setSumList(dataRespSumList);
			} else {
				//尖时刻(日表)
				List<DataResp> dataRespTipList = tipDayMapper.getMeterTipDay(req);
				//峰时刻(日表)
				List<DataResp> dataRespPeakList = peakDayMapper.getMeterPeakDay(req);
				//平时刻(日表)
				List<DataResp> dataRespsFlatList = flatDayMapper.getMeterFlatDay(req);
				//谷时刻(日表)
				List<DataResp> dataRespsValleyList = valleyDayMapper.getMeterValleyDay(req);
				//总量(日表)
				List<DataResp> dataRespSumList = readingDayPoMapper.getMeterReadingDay(req);

				List<DataResp> listTip = monthOfDay(req.getStartTime(),req.getEndTime());
				dataRespTipList.forEach(e -> {
					listTip.remove(e);
				});
				dataRespTipList.addAll(listTip);
				Collections.sort(dataRespTipList);

				List<DataResp> listPeak = monthOfDay(req.getStartTime(),req.getEndTime());
				dataRespPeakList.forEach(e -> {
					listPeak.remove(e);
				});
				dataRespPeakList.addAll(listPeak);
				Collections.sort(dataRespPeakList);

				List<DataResp> listFlat = monthOfDay(req.getStartTime(),req.getEndTime());
				dataRespsFlatList.forEach(e -> {
					listFlat.remove(e);
				});
				dataRespsFlatList.addAll(listFlat);
				Collections.sort(dataRespsFlatList);

				List<DataResp> listValley = monthOfDay(req.getStartTime(),req.getEndTime());
				dataRespsValleyList.forEach(e -> {
					listValley.remove(e);
				});
				dataRespsValleyList.addAll(listValley);
				Collections.sort(dataRespsValleyList);

				List<DataResp> listSum = monthOfDay(req.getStartTime(),req.getEndTime());
				dataRespSumList.forEach(e -> {
					listSum.remove(e);
				});
				dataRespSumList.addAll(listSum);
				Collections.sort(dataRespSumList);

				meterReadingResp.setTipList(dataRespTipList);
				meterReadingResp.setPeakList(dataRespPeakList);
				meterReadingResp.setFlatList(dataRespsFlatList);
				meterReadingResp.setValleyList(dataRespsValleyList);
				meterReadingResp.setSumList(dataRespSumList);
			}
		}
		return meterReadingResp;
	}

	@Override
	public MeterReadingResp getMeterByMonth(ElectricMeterReadingReq req) {
		MeterReadingResp meterReadingResp = new MeterReadingResp();
		//如果开始时间是当前月
		if (DateUtil.isThisMonth(req.getStartTime())) {
			//如果当前日期为1号则没有数据
			if (DateUtil.getDay().substring(8, 10).equals("01")) {
				meterReadingResp = null;
			} else {
				List<DataResp> listTip = yearOfMonth(req.getStartTime(),req.getEndTime());
				List<DataResp> listPeak = yearOfMonth(req.getStartTime(),req.getEndTime());
				List<DataResp> listFlat = yearOfMonth(req.getStartTime(),req.getEndTime());
				List<DataResp> listValley = yearOfMonth(req.getStartTime(),req.getEndTime());
				List<DataResp> listSum = yearOfMonth(req.getStartTime(),req.getEndTime());
				//开始时间为本月的1号，结束时间为昨天
				req.setStartTime(DateUtil.getMouthOneDay());
				req.setEndTime(DateUtil.getLastOneday());
				//尖时刻(日表)
				List<DataResp> dataRespTipList = Lists.newArrayList();
				DataResp tipSumDay = tipDayMapper.getMeterSumTipDay(req);
				if (null!=tipSumDay){
					dataRespTipList.add(tipSumDay);
				}
				//峰时刻(日表)
				List<DataResp> dataRespPeakList = Lists.newArrayList();
				DataResp peakSumDay = peakDayMapper.getMeterSumPeakDay(req);
				if (null!=peakSumDay){
					dataRespPeakList.add(peakSumDay);
				}
				//平时刻(日表)
				List<DataResp> dataRespsFlatList = Lists.newArrayList();
				DataResp flatSumDay = flatDayMapper.getMeterSumFlatDay(req);
				if (null!=flatSumDay){
					dataRespsFlatList.add(flatSumDay);
				}
				//谷时刻(日表)
				List<DataResp> dataRespsValleyList = Lists.newArrayList();
				DataResp valleySumDay = valleyDayMapper.getMeterSumValleyDay(req);
				if (null!=valleySumDay){
					dataRespsValleyList.add(valleySumDay);
				}
				//总量(日表)
				List<DataResp> dataRespSumList = Lists.newArrayList();
				DataResp readingSumDay = readingDayPoMapper.getMeterSunmReadingDay(req);
				if (null!=readingSumDay){
					dataRespSumList.add(readingSumDay);
				}
				dataRespTipList.forEach(e -> {
					listTip.remove(e);
				});
				dataRespTipList.addAll(listTip);
				Collections.sort(dataRespTipList);

				dataRespPeakList.forEach(e -> {
					listPeak.remove(e);
				});
				dataRespPeakList.addAll(listPeak);
				Collections.sort(dataRespPeakList);

				dataRespsFlatList.forEach(e -> {
					listFlat.remove(e);
				});
				dataRespsFlatList.addAll(listFlat);
				Collections.sort(dataRespsFlatList);

				dataRespsValleyList.forEach(e -> {
					listValley.remove(e);
				});
				dataRespsValleyList.addAll(listValley);
				Collections.sort(dataRespsValleyList);

				dataRespSumList.forEach(e -> {
					listSum.remove(e);
				});
				dataRespSumList.addAll(listSum);
				Collections.sort(dataRespSumList);
				meterReadingResp.setTipList(dataRespTipList);
				meterReadingResp.setPeakList(dataRespPeakList);
				meterReadingResp.setFlatList(dataRespsFlatList);
				meterReadingResp.setValleyList(dataRespsValleyList);
				meterReadingResp.setSumList(dataRespSumList);
			}
		} else {
			if (DateUtil.isThisMonth(req.getEndTime())) {
				List<DataResp> listTip = yearOfMonth(req.getStartTime(),req.getEndTime());
				List<DataResp> listPeak = yearOfMonth(req.getStartTime(),req.getEndTime());
				List<DataResp> listFlat = yearOfMonth(req.getStartTime(),req.getEndTime());
				List<DataResp> listValley = yearOfMonth(req.getStartTime(),req.getEndTime());
				List<DataResp> listSum = yearOfMonth(req.getStartTime(),req.getEndTime());

				req.setEndTime(DateUtil.getLastMonthOfYear(req.getEndTime()));
				//尖时段(月)
				List<DataResp> dataRespTipList = tipMonthMapper.getMeterTipMonth(req);
				//峰时段(月)
				List<DataResp> dataRespPeakList = peakMonthMapper.getMeterPeakMonth(req);
				//平时段
				List<DataResp> dataRespFlatList = flatMonthMapper.getMeterFlatMonth(req);
				//谷时段
				List<DataResp> dataRespValleyList = valleyMonthMapper.getMeterValleyMonth(req);
				//总量
				List<DataResp> dataRespSumList = readingMonthPoMapper.getMeterReadingMonth(req);

				//拼装日表的请求参数
				req.setStartTime(DateUtil.format(DateUtil.getOneDay(), "yyyy-MM-dd"));
				req.setEndTime(DateUtil.getLastOneday());
				//尖
				DataResp dataRespTip = tipDayMapper.getMeterSumTipDay(req);
				if(null!=dataRespTip){
					dataRespTipList.add(dataRespTip);
				}
				//峰
				DataResp dataRespPeak = peakDayMapper.getMeterSumPeakDay(req);
				if(null!=dataRespPeak){
					dataRespPeakList.add(dataRespPeak);
				}
				//平
				DataResp dataRespFlat = flatDayMapper.getMeterSumFlatDay(req);
				if(null!=dataRespFlat){
					dataRespFlatList.add(dataRespFlat);
				}
				//谷
				DataResp dataRespValley = valleyDayMapper.getMeterSumValleyDay(req);
				if(null!=dataRespValley){
					dataRespValleyList.add(dataRespValley);
				}
				//总量
				DataResp dataRespReading = readingDayPoMapper.getMeterSunmReadingDay(req);
				if(null!=dataRespReading){
					dataRespSumList.add(dataRespReading);
				}
				//与每个月对比

				dataRespTipList.forEach(e -> {
					listTip.remove(e);
				});
				dataRespTipList.addAll(listTip);
				Collections.sort(dataRespTipList);

				dataRespPeakList.forEach(e -> {
					listPeak.remove(e);
				});
				dataRespPeakList.addAll(listPeak);
				Collections.sort(dataRespPeakList);

				dataRespFlatList.forEach(e -> {
					listFlat.remove(e);
				});
				dataRespFlatList.addAll(listFlat);
				Collections.sort(dataRespFlatList);

				dataRespValleyList.forEach(e -> {
					listValley.remove(e);
				});
				dataRespValleyList.addAll(listValley);
				Collections.sort(dataRespValleyList);

				dataRespSumList.forEach(e -> {
					listSum.remove(e);
				});
				dataRespSumList.addAll(listSum);
				Collections.sort(dataRespSumList);
				//封装数据
				meterReadingResp.setTipList(dataRespTipList);
				meterReadingResp.setPeakList(dataRespPeakList);
				meterReadingResp.setFlatList(dataRespFlatList);
				meterReadingResp.setValleyList(dataRespValleyList);
				meterReadingResp.setSumList(dataRespSumList);
			} else {
				//结束日期和开始日期都不是本月
				//尖时段(月)
				List<DataResp> dataRespTipList = tipMonthMapper.getMeterTipMonth(req);
				//峰时段(月)
				List<DataResp> dataRespPeakList = peakMonthMapper.getMeterPeakMonth(req);
				//平时段
				List<DataResp> dataRespFlatList = flatMonthMapper.getMeterFlatMonth(req);
				//谷时段
				List<DataResp> dataRespValleyList = valleyMonthMapper.getMeterValleyMonth(req);
				//总量
				List<DataResp> dataRespSumList = readingMonthPoMapper.getMeterReadingMonth(req);

				//与每个月对比
				List<DataResp> listTip = yearOfMonth(req.getStartTime(),req.getEndTime());
				dataRespTipList.forEach(e -> {
					listTip.remove(e);
				});
				dataRespTipList.addAll(listTip);
				Collections.sort(dataRespTipList);

				List<DataResp> listPeak = yearOfMonth(req.getStartTime(),req.getEndTime());
				dataRespPeakList.forEach(e -> {
					listPeak.remove(e);
				});
				dataRespPeakList.addAll(listPeak);
				Collections.sort(dataRespPeakList);

				List<DataResp> listFlat = yearOfMonth(req.getStartTime(),req.getEndTime());
				dataRespFlatList.forEach(e -> {
					listFlat.remove(e);
				});
				dataRespFlatList.addAll(listFlat);
				Collections.sort(dataRespFlatList);

				List<DataResp> listValley = yearOfMonth(req.getStartTime(),req.getEndTime());
				dataRespValleyList.forEach(e -> {
					listValley.remove(e);
				});
				dataRespValleyList.addAll(listValley);
				Collections.sort(dataRespValleyList);

				List<DataResp> listSum = yearOfMonth(req.getStartTime(),req.getEndTime());
				dataRespSumList.forEach(e -> {
					listSum.remove(e);
				});
				dataRespSumList.addAll(listSum);
				Collections.sort(dataRespSumList);

				//封装数据
				meterReadingResp.setTipList(dataRespTipList);
				meterReadingResp.setPeakList(dataRespPeakList);
				meterReadingResp.setFlatList(dataRespFlatList);
				meterReadingResp.setValleyList(dataRespValleyList);
				meterReadingResp.setSumList(dataRespSumList);
			}
		}
		return meterReadingResp;
	}

	@Override
	public List<StatisticsDataResp> getElecSumByDay(ElectricMeterReadingReq req) {
		return readingDayPoMapper.getElecSumByDay(req);
	}

	@Override
	public List<StatisticsDataResp> getElecSumByMonth(ElectricMeterReadingReq req) {
		return readingMonthPoMapper.getElecSumByMonth(req);
	}

	@Override
	public EnergyResp<StatisticsDataResp> getMonthElectricity(String start, String end, List<String> device) {
		EnergyResp<StatisticsDataResp> energyResp = new EnergyResp<>();
		StatisticsDataResp statisticsDataResp = new StatisticsDataResp();
		ElectricMeterReadingReq req = new ElectricMeterReadingReq();
		req.setStartTime(start);
		req.setEndTime(end);
		req.setMeterNoList(device);
		List<StatisticsDataResp> statisticsList = readingDayPoMapper.getElecSumByDay(req);
		//计算电量的和
		BigDecimal quantitySum = null;
		BigDecimal feeSum = null;
		for (StatisticsDataResp statistics : statisticsList) {
			BigDecimal useQuantity = statistics.getSumQuantity();
			BigDecimal useFee = statistics.getSumFees();
			quantitySum = MathUtils.add(useQuantity, quantitySum);
			feeSum = MathUtils.add(useFee, feeSum);
		}
		statisticsDataResp.setSumQuantity(MathUtils.towDecimal(quantitySum));
		statisticsDataResp.setSumFees(MathUtils.towDecimal(feeSum));
		energyResp.ok(statisticsDataResp);
		return energyResp;
	}

	@Override
	public EnergyResp<StatisticsDataResp> getDayElectricity(String start, String end, List<String> device) {
		EnergyResp<StatisticsDataResp> energyResp = new EnergyResp<>();
		ElectricMeterReadingReq req = new ElectricMeterReadingReq();
		StatisticsDataResp statisticsDataResp = new StatisticsDataResp();
		req.setStartTime(start);
		req.setEndTime(end);
		req.setMeterNoList(device);
		List<StatisticsDataResp> meterReadingHours = meterReadingHourPoMapper.getElecSumByHour(req);
		//计算电量的和
		BigDecimal quantitySum = new BigDecimal(0);
		BigDecimal feeSum = new BigDecimal(0);
		for (StatisticsDataResp statistics : meterReadingHours) {
			BigDecimal useQuantity = statistics.getSumQuantity();
			BigDecimal useFee = statistics.getSumFees();
			quantitySum = MathUtils.add(useQuantity, quantitySum);
			feeSum = MathUtils.add(useFee, feeSum);
		}
		statisticsDataResp.setSumQuantity(MathUtils.towDecimal(quantitySum));
		statisticsDataResp.setSumFees(MathUtils.towDecimal(feeSum));
		energyResp.ok(statisticsDataResp);
		return energyResp;
	}

	@Override
	public EnergyResp<StatisticsDataResp> getHourElectricity(String start, String end, List<String> device) {
		EnergyResp<StatisticsDataResp> energyResp = new EnergyResp<>();
		StatisticsDataResp statisticsDataResp = new StatisticsDataResp();
		ElectricMeterReadingReq req = new ElectricMeterReadingReq();
		req.setStartTime(start);
		req.setEndTime(end);
		req.setMeterNoList(device);
		List<StatisticsDataResp> meterReadingMinutes = minuteMapper.getElecSumByMinute(req);
		//计算电量的和
		BigDecimal quantitySum = new BigDecimal(0);
		BigDecimal feeSum = new BigDecimal(0);
		for (StatisticsDataResp statistics : meterReadingMinutes) {
			BigDecimal useQuantity = statistics.getSumQuantity();
			BigDecimal useFee = statistics.getSumFees();
			quantitySum = MathUtils.add(useQuantity, quantitySum);
			feeSum = MathUtils.add(useFee, feeSum);
		}
		statisticsDataResp.setSumQuantity(MathUtils.towDecimal(quantitySum));
		statisticsDataResp.setSumFees(MathUtils.towDecimal(feeSum));
		energyResp.ok(statisticsDataResp);
		return energyResp;
	}

	@Override
	public EnergyResp<StatisticsDataResp> getYearElectricity(String start, String end, List<String> device) {
		EnergyResp<StatisticsDataResp> energyResp = new EnergyResp<>();
		StatisticsDataResp statisticsDataResp = new StatisticsDataResp();
		ElectricMeterReadingReq req = new ElectricMeterReadingReq();
		req.setStartTime(start);
		req.setEndTime(end);
		req.setMeterNoList(device);
		List<StatisticsDataResp> statisticsList = readingMonthPoMapper.getElecSumByMonth(req);
		//计算电量的和
		BigDecimal quantitySum = new BigDecimal(0);
		BigDecimal feeSum = new BigDecimal(0);
		for (StatisticsDataResp statistics : statisticsList) {
			BigDecimal useQuantity = statistics.getSumQuantity();
			BigDecimal useFee = statistics.getSumFees();
			quantitySum = MathUtils.add(useQuantity, quantitySum);
			feeSum = MathUtils.add(useFee, feeSum);
		}
		statisticsDataResp.setSumQuantity(MathUtils.towDecimal(quantitySum));
		statisticsDataResp.setSumFees(MathUtils.towDecimal(feeSum));
		energyResp.ok(statisticsDataResp);
		return energyResp;
	}

	public List<DataResp> monthOfDay(String startTime, String endTime) {
		int day = DateUtil.getDateSpace(startTime, endTime);
		DataResp dataResp;
		List<DataResp> dataRespList = Lists.newArrayList();
		for (int i=0;i<=day;i++) {
			dataResp = new DataResp();
			dataResp.setDateTime(DateUtil.getAddDay(startTime, i));
			dataResp.setDateValue(null);
			dataRespList.add(dataResp);
		}
		return dataRespList;
	}

	public List<DataResp> yearOfMonth(String startTime, String endTime) {
		int month = DateUtil.getMonthDiff(endTime,startTime);
		DataResp dataResp;
		List<DataResp> dataRespList = Lists.newArrayList();
		for (int i = 0; i <= month; i++) {
			dataResp = new DataResp();
			dataResp.setDateTime(DateUtil.getAddMonth(startTime, i));
			dataResp.setDateValue(null);
			dataRespList.add(dataResp);
		}
		return dataRespList;
	}


}
