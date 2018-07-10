package com.enn.web.controller;


import com.enn.constant.Constant;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.ExcelUtils;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.req.*;
import com.enn.vo.energy.business.resp.*;
import com.enn.vo.energy.business.upload.UploadResp;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 车间用电统计接口
 *
 * @Author: 肖明玉
 * @Date:
 */
@RequestMapping("/workshop")
@RestController
@Api(value = "用电报表--车间用电统计接口", description = "用电报表--车间用电统计接口", tags = "用电报表--	车间用电统计")
public class WorkshopElectricityStatisticsController extends BaseController {

	@RequestMapping(value = "/getWorkshop", method = RequestMethod.POST)
	@ApiOperation(value = "获取车间下拉列表", notes = "获取车间下拉列表")
	public EnergyResp<List<UnitResp>> getWorkshop(@RequestBody @Valid CustReq custReq, BindingResult result) {
		EnergyResp<List<UnitResp>> energyResp = new EnergyResp<>();
		if (result.hasErrors()) {
			energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
			return energyResp;
		}
		AccountUnitReq accountUnitReq = new AccountUnitReq();
		accountUnitReq.setId(Long.parseLong(custReq.getCustID()));
		accountUnitReq.setAccountingType(Constant.ACCOUNTING_TYPE_02);
		accountUnitReq.setIsAccount(false);
		logger.info("调取车间的下拉列表，请求参数{}", getJsonString(accountUnitReq));
		EnergyResp<List<UnitResp>> listEnergyResp = accountUnitService.queryAccountList(accountUnitReq);
		if (!listEnergyResp.getMsg().equals(StatusCode.SUCCESS.getMsg())) {
			energyResp.faile(listEnergyResp.getCode(), listEnergyResp.getMsg(), listEnergyResp.getError());
			return energyResp;
		}
		if (CollectionUtils.isEmpty(listEnergyResp.getData())) {
			energyResp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), "该企业下未找到车间");
			return energyResp;
		}
		energyResp.faile(listEnergyResp.getCode(), listEnergyResp.getMsg());
		energyResp.setData(listEnergyResp.getData());
		return energyResp;
	}


	@RequestMapping(value = "/getProductionLine", method = RequestMethod.POST)
	@ApiOperation(value = "获取生产线下拉列表", notes = "获取生产线下拉列表")
	public EnergyResp<List<UnitResp>> getProductionLine(@RequestBody @Valid WorkshopReq workshopReq, BindingResult result) {
		EnergyResp<List<UnitResp>> energyResp = new EnergyResp<>();
		if (result.hasErrors()) {
			energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
			return energyResp;
		}
		AccountUnitReq accountUnitReq = new AccountUnitReq();
		accountUnitReq.setId(Long.parseLong(workshopReq.getId()));
		accountUnitReq.setAccountingType(Constant.ACCOUNTING_TYPE_03);
		accountUnitReq.setIsAccount(true);
		logger.info("调取车间的下拉列表，请求参数{}", getJsonString(accountUnitReq));
		EnergyResp<List<UnitResp>> listEnergyResp = accountUnitService.queryAccountList(accountUnitReq);
		if (!listEnergyResp.getMsg().equals(StatusCode.SUCCESS.getMsg())) {
			energyResp.faile(listEnergyResp.getCode(), listEnergyResp.getMsg(), listEnergyResp.getError());
			return energyResp;
		}
		energyResp.faile(listEnergyResp.getCode(), listEnergyResp.getMsg());
		//取电表的数据
		List<UnitResp> unitRespList;
		if (CollectionUtils.isEmpty(listEnergyResp.getData())) {
			energyResp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), "该车间下无生产线");
			return energyResp;
		} else {
			unitRespList = Lists.newArrayList();
			List<MeterResp> meterRespList;
			//循环取生产线的电表
			for (UnitResp unitResp : listEnergyResp.getData()) {
				meterRespList = Lists.newArrayList();
				for (MeterResp meter : unitResp.getMeters()) {
					if (meter.getEnergyType().equals("01")) {
						meterRespList.add(meter);
					}
				}
				unitResp.setMeters(meterRespList);
				unitRespList.add(unitResp);
			}
		}
		if (!CollectionUtils.isEmpty(unitRespList)) {
			energyResp.setData(unitRespList);
		} else {
			energyResp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), "该生产线未找到电表");
		}
		energyResp.setData(listEnergyResp.getData());
		return energyResp;
	}

	@RequestMapping(value = "/getElectricity", method = RequestMethod.POST)
	@ApiOperation(value = "车间用电统计查询曲线", notes = "车间用电统计查询曲线")
	public EnergyResp<MeterReadingResp> getSelectElectricity(@RequestBody @Valid SelectElectricityReq selectElectricityReq, BindingResult result) {
		EnergyResp<MeterReadingResp> energyResp = new EnergyResp<>();
		if (result.hasErrors()) {
			energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
			return energyResp;
		}
		if (selectElectricityReq.getStartTime().compareTo(selectElectricityReq.getEndTime()) > 0) {
			energyResp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
			return energyResp;
		}
		DefaultReq defaultReq = new DefaultReq();
		defaultReq.setId(Long.parseLong(selectElectricityReq.getId()));
		logger.info("根据核算单元id获取所关联的计量表,请求参数{}",getJsonString(defaultReq));
		EnergyResp<List<MeterResp>> listEnergyResp = accountUnitService.queryMeterListByAccount(defaultReq);
		logger.info("根据核算单元id获取所关联的计量表接口返回，请求参数{}",getJsonString(listEnergyResp));
		List<String> list = Lists.newArrayList();
		for (MeterResp meterResp : listEnergyResp.getData()) {
			if (meterResp.getEnergyType().equals("01")) {
				list.add(meterResp.getLoopNumber());
			}
		}
		if (CollectionUtils.isEmpty(list)) {
			energyResp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), "该生产线下未配电计量仪表");
			return energyResp;
		} else {
			selectElectricityReq.setEquipID(list);
		}
		ElectricMeterReadingReq req = null;
		if (selectElectricityReq.getDownsample().equals("month")) {
			//封装月查询的请求参数
			req = new ElectricMeterReadingReq();
			req.setStartTime(selectElectricityReq.getStartTime());
			req.setEndTime(selectElectricityReq.getEndTime());
			req.setMeterNoList(selectElectricityReq.getEquipID());
			try {
				logger.info("尖峰平谷用电曲线(月),请求参数{}",getJsonString(req));
				EnergyResp<MeterReadingResp> meterByDay = countService.getMeterByDay(req);
				logger.info("尖峰平谷用电曲线(月),返回{}",getJsonString(meterByDay));
				if (meterByDay.getCode().equals(StatusCode.SUCCESS.getCode())) {
					energyResp.ok(meterByDay.getData());
				}
			} catch (Exception e) {
				logger.error("查询月统计用电曲线异常" + e);
			}
		} else if (selectElectricityReq.getDownsample().equals("year")) {
			req = new ElectricMeterReadingReq();
			req.setStartTime(selectElectricityReq.getStartTime());
			req.setEndTime(selectElectricityReq.getEndTime());
			req.setMeterNoList(selectElectricityReq.getEquipID());
			try {
				logger.info("尖峰平谷用电曲线(年),请求参数{}",getJsonString(req));
				EnergyResp<MeterReadingResp> meterByMonth = countService.getMeterByMonth(req);
				logger.info("尖峰平谷用电曲线(年),返回数据{}",getJsonString(meterByMonth));
				if (meterByMonth.getCode().equals(StatusCode.SUCCESS.getCode())) {
					energyResp.ok(meterByMonth.getData());
				}
			} catch (Exception e) {
				logger.error("请求年统计用电曲线异常" + e);
			}
		} else {
			energyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "查询的时间类型不存在，提示: month或year");
		}
		return energyResp;
	}

	@RequestMapping(value = "/getExcel", method = RequestMethod.POST)
	@ApiOperation(value = "车间用电统计表格", notes = "车间用电统计表格")
	public EnergyResp<List<WorkshopResp>> selectExcel(@RequestBody @Valid SelectElectricityReq selectElectricityReq) {
		EnergyResp<List<WorkshopResp>> energyResp=new EnergyResp<>();
		EnergyResp<List<ElectricMeterExcelResp>> excelRespEnergyResp = new EnergyResp<>();
		List<ElectricMeterExcelResp> electricMeterExcelResp;
		ElectricMeterExcelResp electricMeterExcel;

		/*if (result.hasErrors()) {
			excelRespEnergyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
			return excelRespEnergyResp;
		}*/
		if (selectElectricityReq.getStartTime().compareTo(selectElectricityReq.getEndTime()) > 0) {
			energyResp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
			return energyResp;
		}
		DefaultReq defaultReq = new DefaultReq();
		defaultReq.setId(Long.parseLong(selectElectricityReq.getId()));
		logger.info("根据核算单元id获取所关联的计量表,请求参数{}",getJsonString(defaultReq));
		EnergyResp<List<MeterResp>> listEnergyResp = accountUnitService.queryMeterListByAccount(defaultReq);
		List<String> list = Lists.newArrayList();
		for (MeterResp meterResp : listEnergyResp.getData()) {
			if (meterResp.getEnergyType().equals("01")) {
				list.add(meterResp.getLoopNumber());
			}
		}
		if (CollectionUtils.isEmpty(list)) {
			energyResp.faile(StatusCode.E_B.getCode(), StatusCode.E_B.getMsg(), "该生产线下未配电计量仪表");
			return energyResp;
		} else {
			selectElectricityReq.setEquipID(list);
		}
		ElectricMeterReadingReq req = null;
		if (selectElectricityReq.getDownsample().equals("month")) {
			int day = DateUtil.getDateSpace(selectElectricityReq.getStartTime(), selectElectricityReq.getEndTime());
			if (day == 0) {
				req = new ElectricMeterReadingReq();
				req.setStartTime(selectElectricityReq.getStartTime());
				req.setEndTime(selectElectricityReq.getEndTime());
				req.setMeterNoList(selectElectricityReq.getEquipID());
				EnergyResp<ElectricMeterReadingResp> electricMeterReadingResp = countService.getSumByDay(req);
				if (electricMeterReadingResp.getMsg().equals(StatusCode.SUCCESS.getMsg())) {
					electricMeterExcelResp = Lists.newArrayList();
					electricMeterExcel = new ElectricMeterExcelResp();
					electricMeterExcel.setDateTime(req.getStartTime() + "--" + req.getEndTime());
					electricMeterExcel.setElectricMeterReadingResp(electricMeterReadingResp.getData());
					electricMeterExcelResp.add(electricMeterExcel);
					excelRespEnergyResp.ok(electricMeterExcelResp);
				}
			} else {
				electricMeterExcelResp = Lists.newArrayList();
				req = new ElectricMeterReadingReq();
				req.setMeterNoList(selectElectricityReq.getEquipID());
				req.setStartTime(selectElectricityReq.getStartTime());
				req.setEndTime(selectElectricityReq.getEndTime());
				EnergyResp<ElectricMeterReadingResp> sum = countService.getSumByDay(req);
				electricMeterExcel = new ElectricMeterExcelResp();
				electricMeterExcel.setDateTime(selectElectricityReq.getStartTime() + "--" + selectElectricityReq.getEndTime());
				electricMeterExcel.setElectricMeterReadingResp(sum.getData());
				electricMeterExcelResp.add(electricMeterExcel);
				String nextDate = null;
				for (int i = 0; i < day + 1; i++) {
					nextDate = DateUtil.getAddDay(selectElectricityReq.getStartTime(), i);
					req.setStartTime(nextDate);
					req.setEndTime(nextDate);
					electricMeterExcel = new ElectricMeterExcelResp();
					EnergyResp<ElectricMeterReadingResp> electricMeterReadingResp = countService.getSumByDay(req);
					electricMeterExcel.setDateTime(nextDate);
					electricMeterExcel.setElectricMeterReadingResp(electricMeterReadingResp.getData());
					electricMeterExcelResp.add(electricMeterExcel);
				}
				excelRespEnergyResp.ok(electricMeterExcelResp);
			}
		} else if (selectElectricityReq.getDownsample().equals("year")) {
			//判断开始日期是不是本月
			int month = DateUtil.getMonthDiff(selectElectricityReq.getEndTime(), selectElectricityReq.getStartTime());
			if (DateUtil.isThisMonth(selectElectricityReq.getStartTime())) {
				req = new ElectricMeterReadingReq();
				req.setMeterNoList(selectElectricityReq.getEquipID());
				req.setStartTime(DateUtil.getMouthOneDay());
				req.setEndTime(DateUtil.getLastOneday());
				electricMeterExcelResp = Lists.newArrayList();
				electricMeterExcel = new ElectricMeterExcelResp();
				EnergyResp<ElectricMeterReadingResp> electricMeterReadingResp = countService.getSumByDay(req);
				electricMeterExcel.setDateTime(req.getStartTime().substring(0, 7));
				electricMeterExcel.setElectricMeterReadingResp(electricMeterReadingResp.getData());
				electricMeterExcelResp.add(electricMeterExcel);
				excelRespEnergyResp.ok(electricMeterExcelResp);
			} else {
				//判断结束时间是不是本月
				if (DateUtil.isThisMonth(selectElectricityReq.getEndTime())) {
					req = new ElectricMeterReadingReq();
					req.setMeterNoList(selectElectricityReq.getEquipID());
					req.setStartTime(selectElectricityReq.getStartTime());
					req.setEndTime(selectElectricityReq.getEndTime());
					electricMeterExcelResp = Lists.newArrayList();
					electricMeterExcel = new ElectricMeterExcelResp();
					EnergyResp<ElectricMeterReadingResp> electricMeterReadingResp = countService.getSumByMonth(req);
					electricMeterExcel.setElectricMeterReadingResp(electricMeterReadingResp.getData());
					electricMeterExcel.setDateTime(req.getStartTime() + "--" + req.getEndTime());
					electricMeterExcelResp.add(electricMeterExcel);
					String addMonth = null;
					//取两个时间段中的每个月
					for (int i = 0; i < month; i++) {
						addMonth = DateUtil.getAddMonth(selectElectricityReq.getStartTime(), i);
						req.setStartTime(addMonth);
						req.setEndTime(addMonth);
						electricMeterExcel = new ElectricMeterExcelResp();
						electricMeterReadingResp = countService.getSumByMonth(req);
						electricMeterExcel.setDateTime(addMonth);
						electricMeterExcel.setElectricMeterReadingResp(electricMeterReadingResp.getData());
						electricMeterExcelResp.add(electricMeterExcel);
					}
					//取本月1号到昨天的数据
					req.setStartTime(DateUtil.getMouthOneDay());
					req.setEndTime(DateUtil.getLastOneday());
					electricMeterReadingResp = countService.getSumByDay(req);
					electricMeterExcel = new ElectricMeterExcelResp();
					electricMeterExcel.setDateTime(req.getStartTime().substring(0, 7));
					electricMeterExcel.setElectricMeterReadingResp(electricMeterReadingResp.getData());
					electricMeterExcelResp.add(electricMeterExcel);
					excelRespEnergyResp.ok(electricMeterExcelResp);
				} else {
					req = new ElectricMeterReadingReq();
					req.setMeterNoList(selectElectricityReq.getEquipID());
					req.setStartTime(selectElectricityReq.getStartTime());
					req.setEndTime(selectElectricityReq.getEndTime());
					electricMeterExcelResp = Lists.newArrayList();
					electricMeterExcel = new ElectricMeterExcelResp();
					EnergyResp<ElectricMeterReadingResp> electricMeterReadingResp = countService.getSumByMonth(req);
					electricMeterExcel.setElectricMeterReadingResp(electricMeterReadingResp.getData());
					electricMeterExcel.setDateTime(req.getStartTime() + "--" + req.getEndTime());
					electricMeterExcelResp.add(electricMeterExcel);
					String nextMonth = null;
					for (int i = 0; i < month + 1; i++) {
						nextMonth = DateUtil.getAddMonth(selectElectricityReq.getStartTime(), i);
						req.setStartTime(nextMonth);
						req.setEndTime(nextMonth);
						electricMeterExcel = new ElectricMeterExcelResp();
						electricMeterReadingResp = countService.getSumByMonth(req);
						electricMeterExcel.setDateTime(nextMonth);
						electricMeterExcel.setElectricMeterReadingResp(electricMeterReadingResp.getData());
						electricMeterExcelResp.add(electricMeterExcel);
					}
					excelRespEnergyResp.ok(electricMeterExcelResp);
				}
			}
		} else {
			excelRespEnergyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "查询的时间类型不存在，提示: month或year");
		}
		List<ElectricMeterExcelResp> excelRespList = excelRespEnergyResp.getData();
		WorkshopResp workshopResp;
		List<WorkshopResp> workshopRespList=Lists.newArrayList();
		for (ElectricMeterExcelResp excelResp:excelRespList) {
			workshopResp=new WorkshopResp();
			workshopResp.setDateTime(excelResp.getDateTime());
			workshopResp.setFlatFees(MathUtils.towDecimal(excelResp.getElectricMeterReadingResp().getFlatFees()));
			workshopResp.setFlatQuantity(MathUtils.towDecimal(excelResp.getElectricMeterReadingResp().getFlatQuantity()));
			workshopResp.setPeakFees(MathUtils.towDecimal(excelResp.getElectricMeterReadingResp().getPeakFees()));
			workshopResp.setPeakQuantity(MathUtils.towDecimal(excelResp.getElectricMeterReadingResp().getPeakQuantity()));
			workshopResp.setTipFees(MathUtils.towDecimal(excelResp.getElectricMeterReadingResp().getTipFees()));
			workshopResp.setTipQuantity(MathUtils.towDecimal(excelResp.getElectricMeterReadingResp().getTipQuantity()));
			workshopResp.setValleyFees(MathUtils.towDecimal(excelResp.getElectricMeterReadingResp().getValleyFees()));
			workshopResp.setValleyQuantity(MathUtils.towDecimal(excelResp.getElectricMeterReadingResp().getValleyQuantity()));
			workshopResp.setSumFees(MathUtils.towDecimal(excelResp.getElectricMeterReadingResp().getSumFees()));
			workshopResp.setSumQuantity(MathUtils.towDecimal(excelResp.getElectricMeterReadingResp().getSumQuantity()));
			workshopRespList.add(workshopResp);
		}

		energyResp.ok(workshopRespList);
		return energyResp;
	}

	@RequestMapping(value = "/getUploadExcel", method = RequestMethod.POST)
	@ApiOperation(value = "报表EXCEL导出", notes = "报表EXCEL导出")
	public EnergyResp<UploadResp> getUploadExcel(@RequestBody @Valid SelectElectricityReq selectElectricityReq, BindingResult result) {
		EnergyResp<UploadResp> uploadRespEnergyResp = new EnergyResp<>();
		if (result.hasErrors()) {
			uploadRespEnergyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
			return uploadRespEnergyResp;
		}
		if (selectElectricityReq.getStartTime().compareTo(selectElectricityReq.getEndTime()) > 0) {
			uploadRespEnergyResp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
			return uploadRespEnergyResp;
		}
		EnergyResp<List<WorkshopResp>> energyResp = this.selectExcel(selectElectricityReq);
		List<WorkshopResp> results = energyResp.getData();
		List<LinkedHashMap<String, String>> rows = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(results)) {
			rows = results.stream().map(item -> {
				LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap();
				linkedHashMap.put("1", item.getDateTime());
				linkedHashMap.put("2", item.getSumQuantity().toString());
				linkedHashMap.put("3", item.getSumFees().toString());
				linkedHashMap.put("4", item.getTipQuantity().toString());
				linkedHashMap.put("5", item.getTipFees().toString());
				linkedHashMap.put("6", item.getPeakQuantity().toString());
				linkedHashMap.put("7", item.getPeakFees().toString());
				linkedHashMap.put("8", item.getFlatQuantity().toString());
				linkedHashMap.put("9", item.getFlatFees().toString());
				linkedHashMap.put("10", item.getValleyQuantity().toString());
				linkedHashMap.put("11", item.getValleyFees().toString());
				return linkedHashMap;

			}).collect(Collectors.toList());
		}
		String[] tableHeaders = {"统计日期", "总用电量(kWh)", "总电费(元)", "尖用电量(kWh)", "尖电费(元)", "峰用电量(kWh)", "峰电费(元)", "平用电量(kWh)", "平电费(元)", "谷用电量(kWh)", "谷费用(元)"};
		ResponseEntity<byte[]> responseEntity = ExcelUtils.exportExcel(tableHeaders, rows, System.currentTimeMillis() + "", ExcelUtils.ExcelSuffix.xls);
		Map map = new HashMap() {{
			put("obj", responseEntity.getBody());
		}};
		logger.info("将导出文件上传文件服务器");
		UploadResp uploadResp = uploadService.upObj(map, ExcelUtils.ExcelSuffix.xls.toString());
		if (uploadResp.getRetCode() == 0) {
			String path = baseFileUrl + uploadResp.getPath() + "?filename=" + "用电报表-车间用电" +
					selectElectricityReq.getStartTime() + "-" + selectElectricityReq.getEndTime() + ".xls";
			uploadResp.setPath(path);
			uploadRespEnergyResp.ok(uploadResp);
		} else {
			logger.error("上传文件服务器异常");
		}
		return uploadRespEnergyResp;
	}


}
