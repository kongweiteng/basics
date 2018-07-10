package com.enn.energy.business.rest;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.enn.energy.business.service.IProduceOnlineMonitorService;
import com.enn.energy.system.common.util.CommonConverter;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.bo.ProduceOnlineMonitorBo;
import com.enn.vo.energy.business.dto.DepartUnitDto;
import com.enn.vo.energy.business.dto.EnergyCostDto;
import com.enn.vo.energy.business.dto.ProduceOnlineElectricRealtimeSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineElectricSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineStreamRealtimeSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineStreamSampleDto;
import com.enn.vo.energy.business.vo.ProduceOnlineMonitorParams;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
* @author kai.guo
* @version 创建时间：2018年6月7日 下午4:49:11
* @Description 生产在线监测
*/
@RestController
@Api( tags="生产在线监测")
@RequestMapping("/produce")
public class ProduceOnlineMonitorController {
	
	private static final Logger logger=LoggerFactory.getLogger(ProduceOnlineMonitorController.class);
	
	private static final Integer SAMPLE_TIME_DISTANCE=-12;
	
	@Autowired
	private IProduceOnlineMonitorService produceOnlineMonitorService;
	
	/**
	 * 查询指定企业id的生产车间列表
	 * @param companyId
	 */
	@ApiOperation(value="查询指定企业的生产车间列表", notes="根据企业id查询生产车间列表")
	@RequestMapping(value="/queryProductionDepartment",method=RequestMethod.POST)
	public EnergyResp<List<DepartUnitDto>> queryProductionDepartment(@RequestParam("companyId") Long companyId){
		
		logger.info("【生产在线监测】,查询指定企业的生产车间列表,method:[queryProductionDepartment],companyId:{}",companyId);
		
		List<DepartUnitDto> departUnitBos=produceOnlineMonitorService.queryProductionDepartment(companyId);
		EnergyResp<List<DepartUnitDto>> resp = new EnergyResp<>();
		resp.ok(departUnitBos);
		return resp;
	}
	
	/**
	 * 查询指定部门的电力采样数据
	 * @param onlineMonitorParams
	 * @return
	 */
	@ApiOperation( value="查询指定车间当前时间的电力采样数据",notes="根据车间id查询指定时间段内的电力采样数据")
	@RequestMapping( value="/queryPowerCurveForWebElectricSystem",method=RequestMethod.POST)
	public EnergyResp<ProduceOnlineElectricSampleDto> queryPowerCurveForWebElectricSystem(@RequestBody @Valid ProduceOnlineMonitorParams onlineMonitorParams){
		
		logger.info("【生产在线监测】,查询电力系统有功功率采样数据,method:[queryPowerCurveForWebElectricSystem],onlineMonitorParams:{}",JSON.toJSON(onlineMonitorParams));
		
		ProduceOnlineMonitorBo onlineMonitorBo=CommonConverter.map(onlineMonitorParams, ProduceOnlineMonitorBo.class);
		
		Date currentDate=DateUtil.parse(onlineMonitorParams.getSampleCurrentTime(), DateUtil.BASIC_PATTEN);
		String date =DateUtil.format(currentDate, DateUtil.BASIC_PATTEN);
		String beforeDate=DateUtil.getBeforeHourTime(currentDate, SAMPLE_TIME_DISTANCE,DateUtil.BASIC_PATTEN);
		onlineMonitorBo.setSampleStartTime(beforeDate);
		onlineMonitorBo.setSampleEndTime(date);
		
		ProduceOnlineElectricSampleDto electricSampleDto=produceOnlineMonitorService.queryPowerCurveForWebElectricSystem(onlineMonitorBo);
		
		EnergyResp<ProduceOnlineElectricSampleDto> resp = new EnergyResp<>();
		resp.ok(electricSampleDto);
		return resp;
	}
	
	/**
	 * 查询指定部门的蒸汽采样数据
	 * @param onlineMonitorParams
	 * @return
	 */
	@ApiOperation( value="查询指定车间当前时间的蒸汽采样数据",notes="根据车间id查询指定时间段内的蒸汽采样数据")
	@RequestMapping( value="/queryFlowCurveForWebThermodynamicSystem",method=RequestMethod.POST)
	public EnergyResp<ProduceOnlineStreamSampleDto> queryFlowCurveForWebThermodynamicSystem(@RequestBody @Valid ProduceOnlineMonitorParams onlineMonitorParams){
		
		logger.info("【生产在线监测】,查询蒸汽流量，蒸汽压力采样数据,method:[queryFlowCurveForWebThermodynamicSystem],onlineMonitorParams:{}",JSON.toJSON(onlineMonitorParams));
		
		ProduceOnlineMonitorBo onlineMonitorBo=CommonConverter.map(onlineMonitorParams, ProduceOnlineMonitorBo.class);
		
		Date currentDate=DateUtil.parse(onlineMonitorParams.getSampleCurrentTime(), DateUtil.BASIC_PATTEN);
		String date =DateUtil.format(currentDate, DateUtil.BASIC_PATTEN);
		String beforeDate=DateUtil.getBeforeHourTime(currentDate, SAMPLE_TIME_DISTANCE,DateUtil.BASIC_PATTEN);
		onlineMonitorBo.setSampleStartTime(beforeDate);
		onlineMonitorBo.setSampleEndTime(date);
		
		ProduceOnlineStreamSampleDto electricSampleDto=produceOnlineMonitorService.queryFlowCurveForWebThermodynamicSystem(onlineMonitorBo);
		
		EnergyResp<ProduceOnlineStreamSampleDto> resp = new EnergyResp<>();
		resp.ok(electricSampleDto);
		return resp;
	}
	
	
	/**
	 * 查询指定部门的电力实时用电情况
	 * @param onlineMonitorParams
	 * @return
	 */
	@ApiOperation( value="查询指定部门的电力实时用电情况",notes="根据车间id查询指定部门的电力实时用电情况")
	@RequestMapping( value="/queryRealTimeElectricityStatus",method=RequestMethod.POST)
	public EnergyResp<ProduceOnlineElectricRealtimeSampleDto> queryRealTimeElectricityStatus(@RequestBody @Valid ProduceOnlineMonitorParams onlineMonitorParams){
		
		logger.info("【生产在线监测】,查询实时用电情况,method:[queryRealTimeElectricityStatus],onlineMonitorParams:{}",JSON.toJSON(onlineMonitorParams));
		
		ProduceOnlineMonitorBo onlineMonitorBo=CommonConverter.map(onlineMonitorParams, ProduceOnlineMonitorBo.class);
		
		Date currentDate=DateUtil.parse(onlineMonitorParams.getSampleCurrentTime(), DateUtil.BASIC_PATTEN);
		String date =DateUtil.format(currentDate, DateUtil.BASIC_PATTEN);
		String beforeDate=DateUtil.getBeforeHourTime(currentDate, SAMPLE_TIME_DISTANCE,DateUtil.BASIC_PATTEN);
		onlineMonitorBo.setSampleStartTime(beforeDate);
		onlineMonitorBo.setSampleEndTime(date);
		
		ProduceOnlineElectricRealtimeSampleDto electricSampleDto=produceOnlineMonitorService.queryRealTimeElectricityStatus(onlineMonitorBo);
		
		EnergyResp<ProduceOnlineElectricRealtimeSampleDto> resp = new EnergyResp<>();
		resp.ok(electricSampleDto);
		return resp;
	}
	
	
	/**
	 * 查询指定部门的蒸汽实时使用情况
	 * @param onlineMonitorParams
	 * @return
	 */
	@ApiOperation( value="查询指定部门的蒸汽实时使用情况",notes="根据车间id查询指定部门的蒸汽实时使用情况")
	@RequestMapping( value="/queryRealTimeStreamStatus",method=RequestMethod.POST)
	public EnergyResp<ProduceOnlineStreamRealtimeSampleDto> queryRealTimeStreamStatus(@RequestBody @Valid ProduceOnlineMonitorParams onlineMonitorParams){
		
		logger.info("【生产在线监测】,查询实时蒸汽流量情况,method:[queryRealTimeStreamStatus],onlineMonitorParams:{}",JSON.toJSON(onlineMonitorParams));
		
		ProduceOnlineMonitorBo onlineMonitorBo=CommonConverter.map(onlineMonitorParams, ProduceOnlineMonitorBo.class);
		
		Date currentDate=DateUtil.parse(onlineMonitorParams.getSampleCurrentTime(), DateUtil.BASIC_PATTEN);
		String date =DateUtil.format(currentDate, DateUtil.BASIC_PATTEN);
		String beforeDate=DateUtil.getBeforeHourTime(currentDate, SAMPLE_TIME_DISTANCE,DateUtil.BASIC_PATTEN);
		onlineMonitorBo.setSampleStartTime(beforeDate);
		onlineMonitorBo.setSampleEndTime(date);
		
		ProduceOnlineStreamRealtimeSampleDto electricSampleDto=produceOnlineMonitorService.queryRealTimeStreamStatus(onlineMonitorBo);
		
		EnergyResp<ProduceOnlineStreamRealtimeSampleDto> resp = new EnergyResp<>();
		resp.ok(electricSampleDto);
		return resp;
	}
	
	
	/**
	 * 查询指定部门的能源费用占比
	 * @param onlineMonitorParams
	 * @return
	 */
	@ApiOperation( value="查询指定部门的能源费用占比",notes="根据车间id查询指定部门的能源费用占比")
	@RequestMapping( value="/queryEnergyCostData",method=RequestMethod.POST)
	public EnergyResp<EnergyCostDto> queryEnergyCostData(@RequestBody @Valid ProduceOnlineMonitorParams onlineMonitorParams){
		
		logger.info("【生产在线监测】,查询能源费用占比,method:[queryEnergyCostData],onlineMonitorParams:{}",JSON.toJSON(onlineMonitorParams));
		
		ProduceOnlineMonitorBo onlineMonitorBo=CommonConverter.map(onlineMonitorParams, ProduceOnlineMonitorBo.class);
		
		Date currentDate=DateUtil.parse(onlineMonitorParams.getSampleCurrentTime(), DateUtil.BASIC_PATTEN);
		String date =DateUtil.format(currentDate, DateUtil.BASIC_PATTEN);
		String beforeDate=DateUtil.getBeforeHourTime(currentDate, SAMPLE_TIME_DISTANCE,DateUtil.BASIC_PATTEN);
		onlineMonitorBo.setSampleStartTime(beforeDate);
		onlineMonitorBo.setSampleEndTime(date);
		
		EnergyCostDto electricSampleDto=produceOnlineMonitorService.queryEnergyCostData(onlineMonitorBo);
		
		EnergyResp<EnergyCostDto> resp = new EnergyResp<>();
		resp.ok(electricSampleDto);
		return resp;
	}

}
