package com.enn.energy.business.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.enn.constant.Constant;
import com.enn.constant.StatusCode;
import com.enn.energy.business.service.IAccountingUnitService;
import com.enn.energy.business.service.IElectricMeterReadingService;
import com.enn.energy.business.service.IOpentsdbService;
import com.enn.energy.business.service.IProduceOnlineMonitorService;
import com.enn.energy.business.service.ISteamMeterReadingService;
import com.enn.energy.system.common.util.CommonConverter;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.service.passage.ISteamFeesCalculateService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.bo.ElectricMeterReadingMinuteBo;
import com.enn.vo.energy.business.bo.ElectricMeterReadingMinuteStatisticsBo;
import com.enn.vo.energy.business.bo.ProduceOnlineMonitorBo;
import com.enn.vo.energy.business.bo.ProduceOnlineMonitorSampleBo;
import com.enn.vo.energy.business.bo.ProduceOnlineMonitorTransferBo;
import com.enn.vo.energy.business.bo.ProduceOnlineRealtimeSampleBo;
import com.enn.vo.energy.business.bo.SteamMeterReadingMinuteBo;
import com.enn.vo.energy.business.bo.SteamMeterReadingMinuteFeesBo;
import com.enn.vo.energy.business.bo.SteamMeterReadingMinuteResultBo;
import com.enn.vo.energy.business.bo.SteamMeterReadingMinuteStatisticsBo;
import com.enn.vo.energy.business.dto.DepartUnitDto;
import com.enn.vo.energy.business.dto.DevicePowerDto;
import com.enn.vo.energy.business.dto.EnergyCostDto;
import com.enn.vo.energy.business.dto.ProduceOnlineElectricRealtimeSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineElectricSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineStreamRealtimeSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineStreamSampleDto;
import com.enn.vo.energy.business.dto.RmiSamplDataDto;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.req.AccountUnitReq;
import com.enn.vo.energy.business.req.Equip;
import com.enn.vo.energy.business.req.LastStaReq;
import com.enn.vo.energy.business.req.SamplDataStaReq;
import com.enn.vo.energy.business.resp.DataResp;
import com.enn.vo.energy.business.resp.LastResp;
import com.enn.vo.energy.business.resp.MeterResp;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.common.enums.EnergyTypeEnum;
import com.enn.vo.energy.common.enums.SteamMetricEnum;
import com.enn.vo.energy.passage.req.SteamFeesCalculateReq;
import com.enn.vo.energy.passage.resp.SteamFeesCalculateResp;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
* @author kai.guo
* @version 创建时间：2018年6月6日 上午10:44:13
* @Description 生产在线监测
*/
@Service
public class ProduceOnlineMonitorServiceImpl implements IProduceOnlineMonitorService {
	
	
	private static final Integer BASIC_PRECISE_DIGITS=2;
	
	private static final Integer PRECISE_DIGITS=5;
	
	private static final Long ANXIAN_CUSTID=2L;
	
	@Resource
    private IOpentsdbService opentsdbService;
	
	@Autowired
	private IElectricMeterReadingService electricMeterReadingService;
	
	@Autowired
	private ISteamMeterReadingService steamMeterReadingService;
	
	
	@Autowired
    private IAccountingUnitService accountingUnitService;
	
	@Autowired
	private ISteamFeesCalculateService steamFeesCalculateService;
	
	
	private static final Logger logger=LoggerFactory.getLogger(IProduceOnlineMonitorService.class);

	@Override
	public List<DepartUnitDto> queryProductionDepartment(Long companyId) {
		
		AccountUnitReq accountUnitReq=new AccountUnitReq();
		accountUnitReq.setIsAccount(false);
		accountUnitReq.setAccountingType(Constant.ACCOUNTING_TYPE_02);
		accountUnitReq.setId(companyId);
		//获取核算单元数据
		
		EnergyResp<List<UnitResp>> energyResp=accountingUnitService.queryAccountListT(accountUnitReq);
		List<DepartUnitDto> departUnitDtos=Lists.newArrayList();
		if(energyResp.getData() !=null){
			departUnitDtos=CommonConverter.mapList(energyResp.getData(), DepartUnitDto.class);
		}
		
		return departUnitDtos;
	}
	
	
	/**
	 * 构建设备信息
	 * TODO 此处电表暂时取出 METE
	 * @param arg0
	 * @return
	 */
	private Equip buildEquip(MeterResp arg0,String energyType){
		Equip equip=CommonConverter.map(arg0, Equip.class);
		if(energyType.equals(EnergyTypeEnum.ENERGY_ELECTRICITY.getValue())){
//			equip.setEquipID("METE"+arg0.getLoopNumber());
			equip.setEquipID(arg0.getLoopNumber());
			equip.setEquipMK(Constant.ELEC_EQUIPMK);
		}else if(energyType.equals(EnergyTypeEnum.ENERGY_STEAM.getValue())){
			equip.setEquipID(arg0.getLoopNumber());
			equip.setEquipMK(Constant.STEAM_EQUIPMK);
		}
		return equip;
	}
	
	/**
	 * 构建采样对象
	 * @param meterResps
	 * @param onlineMonitorBo
	 * @return
	 */
	private ProduceOnlineMonitorSampleBo buildProduceOnlineMonitorSampleBo(List<MeterResp> meterResps,ProduceOnlineMonitorBo onlineMonitorBo,String metric,String downsample){
		
		if(CollectionUtils.isEmpty(meterResps)){
			return null;
		}
		ProduceOnlineMonitorSampleBo monitorSampleBo=new ProduceOnlineMonitorSampleBo();
		List<Equip> equips=Lists.transform(meterResps, new Function<MeterResp,Equip>(){
			public Equip apply(MeterResp arg0) {
				return buildEquip(arg0,arg0.getEnergyType());
			}
		});
		monitorSampleBo.setEquips(equips);
		monitorSampleBo.setStart(onlineMonitorBo.getSampleStartTime());
		monitorSampleBo.setEnd(onlineMonitorBo.getSampleEndTime());
		monitorSampleBo.setDownsample(downsample);
		monitorSampleBo.setMetric(metric);
		return monitorSampleBo;
	}
	
	public Map<String,ProduceOnlineMonitorSampleBo> queryEquipInfo2(Long departmentId,ProduceOnlineMonitorBo onlineMonitorBo){
		
		
		List<MeterResp> meterResps = accountingUnitService.getAccountMeterT(departmentId);
		logger.info("【生产在线监测】,获取车间departmentId:{}的设备信息,method:[queryEquipInfo2],  respData:{}",departmentId,JSON.toJSONString(meterResps));
		
		Map<String,ProduceOnlineMonitorSampleBo> map=Maps.newHashMap();
		
		Map<String,List<MeterResp>> groupMeterResp=meterResps.stream().collect(Collectors.groupingBy(MeterResp::getEnergyType));
		if(MapUtils.isNotEmpty(groupMeterResp)){
			Set<Entry<String,List<MeterResp>>> entries=groupMeterResp.entrySet();
			for(Entry<String,List<MeterResp>> entry: entries){
				String energyType=entry.getKey();
				EnergyTypeEnum energyTypeEnum=EnergyTypeEnum.getEnergyType(energyType);
				switch (energyTypeEnum) {
				case ENERGY_ELECTRICITY:
					ProduceOnlineMonitorSampleBo electricSampleBo= this.buildProduceOnlineMonitorSampleBo(entry.getValue(), onlineMonitorBo,Constant.ELECTRIC_METRIC_EMS,Constant.DOWNSMPLE_1M);
					if(null != electricSampleBo){
						map.put(EnergyTypeEnum.ENERGY_ELECTRICITY.getValue(), electricSampleBo);
					}
					break;
				case ENERGY_STEAM:
					ProduceOnlineMonitorSampleBo steamSampleBo= this.buildProduceOnlineMonitorSampleBo(entry.getValue(), onlineMonitorBo,SteamMetricEnum.STEAM_FLOW.getMetric(),Constant.DOWNSMPLE_1M);
					if(null != steamSampleBo){
						map.put(EnergyTypeEnum.ENERGY_STEAM.getValue(), steamSampleBo);
					}
					break;
				default:
					break;
				}
				
			}
		}
		
		return map;
	}
	
	/**
	 * 获取指定生产车间的 设备信息列表
	 * @param departmentId
	 * @return
	 */
	public Map<String,MeterResp> queryEquipInfo(Long departmentId){
			
			
			List<MeterResp> meterResps = accountingUnitService.getAccountMeterT(departmentId);
			logger.info("【生产在线监测】,获取车间departmentId:{}的设备信息,method:[queryEquipInfo2],  respData:{}",departmentId,JSON.toJSONString(meterResps));
			
			Map<String,MeterResp> map=meterResps.stream().collect(Collectors.toMap(meter->meter.getLoopNumber(), meter->meter));
	        
			return map;
		}
	
	/**
	 * 构建采样对象
	 * @return
	 */
	private ProduceOnlineMonitorSampleBo buildProduceOnlineMonitorSampleBoForStreamPressure(ProduceOnlineMonitorSampleBo monitorSampleBo,String metric){
		
		ProduceOnlineMonitorSampleBo monitorSampleBo2=CommonConverter.map(monitorSampleBo, ProduceOnlineMonitorSampleBo.class);
		monitorSampleBo2.setMetric(metric);
		logger.info("【生产在线监测】,reqParams:"+JSON.toJSONString(monitorSampleBo2));
		return monitorSampleBo2;
	}
	
	
	/**
	 * 计算指定数值在总量中的占比
	 * @return
	 */
	private String calculatePercent(BigDecimal calculateData,BigDecimal totalData){
		
		String result=MathUtils.divide(calculateData, totalData).multiply(new BigDecimal("100")).setScale(BASIC_PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP).toString();
		
		String percent=result+"%";
		return percent;
		
	}
	
	/**
	 * 计算能源费用占比
	 */
	private void buildEnergyConstDto(EnergyCostDto energyCostDto){
		
		BigDecimal totalEnergyFees=energyCostDto.getTotalElectricFees().add(energyCostDto.getTotalSteamFees());
		
		if(totalEnergyFees.compareTo(BigDecimal.ZERO)>0){
			
			String electricFeesPercert= calculatePercent(energyCostDto.getTotalElectricFees(), totalEnergyFees);
			String steamFeesPercent= calculatePercent(energyCostDto.getTotalSteamFees(), totalEnergyFees);
			
			energyCostDto.setEnergyFees(totalEnergyFees.setScale(BASIC_PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP));
			energyCostDto.setElectricFeesPercert(electricFeesPercert);
			energyCostDto.setSteamFeesPercent(steamFeesPercent);
			energyCostDto.setTotalElectricFees(energyCostDto.getTotalElectricFees().setScale(BASIC_PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP));
			energyCostDto.setTotalSteamFees(energyCostDto.getTotalSteamFees().setScale(BASIC_PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP));
		}
	}
	
	/**
	 * 判断是否是需要过滤的企业id
	 * @return
	 */
	private Boolean judgeCustIdForFiltering(Long departmentId){
		
		boolean result=false;
		
		DefaultReq de=new DefaultReq();
		de.setId(departmentId);
		EnergyResp<AccountingUnit> energyResp=accountingUnitService.getOne(de);
		AccountingUnit accountingUnit=energyResp.getData();
		if(null!=accountingUnit && accountingUnit.getCustId()==ANXIAN_CUSTID){
			result=true;
		}
		return result;
	}
	
	
	/**
	 * 获取指定能源类型的采样数据
	 */
	private ListResp<RmiSamplDataDto> querySampleDataFromAssignedEnergyType(ProduceOnlineMonitorSampleBo monitorSampleBo,Map<String,MeterResp> meterRespMap,ProduceOnlineMonitorTransferBo transferBo) {
		
		SamplDataStaReq samplDataStaReq=CommonConverter.map(monitorSampleBo, SamplDataStaReq.class);
		logger.info("【生产在线监测】,energyType:{}, method:[querySampleDataFromAssignedEnergyType],    samplDataStaReq:{}",transferBo.getEnergyTypeLabel(),JSON.toJSON(samplDataStaReq));
		EnergyResp<ListResp<RmiSamplDataResp>> energyResp = null;
		try {
			energyResp = opentsdbService.getSamplData(samplDataStaReq);
		} catch (Exception e) {
			logger.error("【生产在线监测】,查询[{}]采样数据异常,异常原因:{}",transferBo.getEnergyTypeLabel(),e.getMessage());
			return null;
		}
		List<RmiSamplDataDto> dataDtos=Lists.newArrayList();
		ListResp<RmiSamplDataResp> listResp=energyResp.getData();
		listResp.getList().stream().forEach(p->{
			RmiSamplDataDto samplDataDto=CommonConverter.map(p, RmiSamplDataDto.class);
			
			List<DataResp> dataResp=samplDataDto.getDataResp();
			if(judgeCustIdForFiltering(transferBo.getDepartmentId())&&p.getMetric().equals(Constant.ELECTRIC_METRIC_EMS)){
				dataResp.stream().filter(m->null!=m.getValue()).forEach(m->{
						m.setValue(MathUtils.divide(m.getValue(), new BigDecimal("1000")).setScale(BASIC_PRECISE_DIGITS, BigDecimal.ROUND_HALF_UP));
				});
			}else{
				dataResp.stream().filter(m->null!=m.getValue()).forEach(m->{
						m.setValue(m.getValue().setScale(BASIC_PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP));
				});
			}
			samplDataDto.setDataResp(dataResp);
			MeterResp meterResp=meterRespMap.get(p.getEquipID());
			samplDataDto.setEquipName(meterResp.getMeterName());
			dataDtos.add(samplDataDto);
		});
		ListResp<RmiSamplDataDto> listResp2=new ListResp<>();
		listResp2.setList(dataDtos);
		
		return listResp2;
	}


	/**
	 * 实时用电情况采集
	 */
	private void queryRealTimeSampleDataByAssignedEnergyType(ProduceOnlineRealtimeSampleBo realtimeSampleBo,Map<String,MeterResp> meterRespMap,ProduceOnlineElectricRealtimeSampleDto realtimeSampleDto,ProduceOnlineMonitorTransferBo transferBo) {
		
		LastStaReq lastReq=CommonConverter.map(realtimeSampleBo, LastStaReq.class);
		EnergyResp<ListResp<LastResp>> lastDatas = null;
		try {
			logger.info("【生产在线监测】,查询电力实时采样数据,method:[queryRealTimeSampleDataByAssignedEnergyType],req:{}",JSON.toJSONString(lastReq));
			lastDatas = opentsdbService.getLastDatas(lastReq);
		} catch (Exception e) {
			logger.error("【生产在线监测】,查询[用电]realTime采样数据异常,异常原因:{}",e.getMessage());
			return ;
		}
		List<LastResp> listResp=lastDatas.getData().getList();
		System.out.println("msg:===="+JSON.toJSONString(listResp));
		List<DevicePowerDto> devicePowerDtos=Lists.newArrayList();
		
		//计算电量的和
		BigDecimal sum=listResp.stream().filter(p->StringUtils.isNotEmpty(p.getValue())).map(m->new BigDecimal(m.getValue()))
				.reduce(BigDecimal.ZERO,BigDecimal::add);
		
		BigDecimal calculateTotalRealTimePower=sum.setScale(PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP);
		listResp.stream().forEach(p->{
			DevicePowerDto devicePowerDto=new DevicePowerDto();
			devicePowerDto.setValue(p.getValue());
			BigDecimal calculateRealTimePower=new BigDecimal(p.getValue()).setScale(PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP);
			String percent=calculatePercent(calculateRealTimePower,calculateTotalRealTimePower);
			devicePowerDto.setPercent(percent);
			devicePowerDto.setEquipID(p.getEquipID());
			MeterResp meterResp=meterRespMap.get(p.getEquipID());
			devicePowerDto.setEquipName(meterResp.getMeterName());
			devicePowerDtos.add(devicePowerDto);
		});
		
		/**
		 * 判断是否是龙辉电镀 P采点
		 */
		if(judgeCustIdForFiltering(transferBo.getDepartmentId())&&realtimeSampleBo.getMetric().equals(Constant.ELECTRIC_METRIC_EMS)){
			devicePowerDtos.stream().filter(p->StringUtils.isNotEmpty(p.getValue())).forEach(p->{
				p.setValue(MathUtils.divide(new BigDecimal(p.getValue()), new BigDecimal("1000")).setScale(BASIC_PRECISE_DIGITS, BigDecimal.ROUND_HALF_UP).toString());
			});
			realtimeSampleDto.setElectricRealTimePower(MathUtils.divide(sum, new BigDecimal("1000")).setScale(BASIC_PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP));
		}else{
			devicePowerDtos.stream().filter(p->StringUtils.isNotEmpty(p.getValue())).forEach(p->{
				p.setValue(new BigDecimal(p.getValue()).setScale(BASIC_PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP).toString());
			});
			realtimeSampleDto.setElectricRealTimePower(sum.setScale(BASIC_PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP));
		}
		
		realtimeSampleDto.setDevicePowerDtos(devicePowerDtos);
		
	}
	
	/**
	 * 实时用蒸汽情况采集
	 */
	private void queryRealTimeStreamStatus(ProduceOnlineRealtimeSampleBo realtimeSampleBo,Map<String,MeterResp> meterRespMap,ProduceOnlineStreamRealtimeSampleDto realtimeSampleDto) {
		
		LastStaReq lastReq=CommonConverter.map(realtimeSampleBo, LastStaReq.class);
		EnergyResp<ListResp<LastResp>> lastDatas = null;
		try {
			logger.info("【生产在线监测】,查询蒸汽实时采样数据,method:[queryRealTimeStreamStatus],req:{}",JSON.toJSONString(lastReq));
			lastDatas = opentsdbService.getLastDatas(lastReq);
		} catch (Exception e) {
			logger.error("【生产在线监测】,查询[蒸汽]realTime采样数据异常,异常原因:{}",e.getMessage());
			return ;
		}
		List<LastResp> listResp=lastDatas.getData().getList();
		
		List<DevicePowerDto> devicePowerDtos=Lists.newArrayList();
		
		//计算电量的和
		BigDecimal sum=listResp.stream().filter(p->StringUtils.isNotEmpty(p.getValue())).map(m->new BigDecimal(m.getValue()))
				.reduce(BigDecimal.ZERO,BigDecimal::add);
		
		realtimeSampleDto.setStreamRealTimePower(sum.setScale(BASIC_PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP));
		
		BigDecimal calculateTotalRealTimePower=sum.setScale(PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP);
		
		listResp.stream().forEach(p->{
			DevicePowerDto devicePowerDto=new DevicePowerDto();
			devicePowerDto.setValue(new BigDecimal(p.getValue()).setScale(BASIC_PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP).toString());
			BigDecimal calculateRealTimePower=new BigDecimal(p.getValue()).setScale(PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP);
			String percent=calculatePercent(calculateRealTimePower,calculateTotalRealTimePower);
			devicePowerDto.setPercent(percent);
			devicePowerDto.setEquipID(p.getEquipID());
			MeterResp meterResp=meterRespMap.get(p.getEquipID());
			devicePowerDto.setEquipName(meterResp.getMeterName());
			devicePowerDtos.add(devicePowerDto);
		});
		realtimeSampleDto.setDevicePowerDtos(devicePowerDtos);
		
	}

	/**
	 * 指定时间段内总用电量，用电费用统计
	 */
	@Override
	public ElectricMeterReadingMinuteStatisticsBo countElectricMeterReadingMinuteDataFor24Hour(ElectricMeterReadingMinuteBo electricMeterReadingMinuteBo) {
		
		return electricMeterReadingService.countElectricMeterReadingMinuteByAssignedConditon(electricMeterReadingMinuteBo);
		
	}

	/**
	 * 指定时间段内总蒸汽量，蒸汽费用统计
	 */
	@Override
	public SteamMeterReadingMinuteStatisticsBo countSteamMeterReadingMinuteDataFor24Hour(SteamMeterReadingMinuteBo steamMeterReadingMinuteBo) {
		
		return steamMeterReadingService.countSteamMeterReadingMinuteByAssignedConditon(steamMeterReadingMinuteBo);
		
	}
	
	
	private ProduceOnlineMonitorTransferBo buildProduceOnlineMonitorTransferBo(Long departmentId,String energyTypeLabel){
		ProduceOnlineMonitorTransferBo transferBo=new ProduceOnlineMonitorTransferBo();
		transferBo.setDepartmentId(departmentId);
		transferBo.setEnergyTypeLabel(energyTypeLabel);
		return transferBo;
	}

	/**
	 * 获取电力系统---->功率采样数据
	 */
	@Override
	public ProduceOnlineElectricSampleDto queryPowerCurveForWebElectricSystem(ProduceOnlineMonitorBo onlineMonitorBo) {
		
		ProduceOnlineElectricSampleDto electricSampleDto=new ProduceOnlineElectricSampleDto();
		
		Map<String,ProduceOnlineMonitorSampleBo> map=queryEquipInfo2(onlineMonitorBo.getDepartmentId(),onlineMonitorBo);
		
		Map<String,MeterResp> meterRespMap=this.queryEquipInfo(onlineMonitorBo.getDepartmentId());
		
		ProduceOnlineMonitorSampleBo electricMonitorSampleBo=map.get(EnergyTypeEnum.ENERGY_ELECTRICITY.getValue());
		if(null != electricMonitorSampleBo){
			ProduceOnlineMonitorTransferBo transferBo=this.buildProduceOnlineMonitorTransferBo(onlineMonitorBo.getDepartmentId(), EnergyTypeEnum.ENERGY_ELECTRICITY.getLabel());
			ListResp<RmiSamplDataDto> electricSampleData= this.querySampleDataFromAssignedEnergyType(electricMonitorSampleBo,meterRespMap,transferBo);
			electricSampleDto.setElectricSampleData(electricSampleData);
		}
		return electricSampleDto;
	}

	/**
	 * 获取蒸汽系统----->蒸汽流量 ，蒸汽压力采样数据
	 */
	@Override
	public ProduceOnlineStreamSampleDto queryFlowCurveForWebThermodynamicSystem(
			ProduceOnlineMonitorBo onlineMonitorBo) {
		
		ProduceOnlineStreamSampleDto streamSampleDto=new ProduceOnlineStreamSampleDto();
		
		Map<String,ProduceOnlineMonitorSampleBo> map=queryEquipInfo2(onlineMonitorBo.getDepartmentId(),onlineMonitorBo);
		
		Map<String,MeterResp> meterRespMap=this.queryEquipInfo(onlineMonitorBo.getDepartmentId());
		
		ProduceOnlineMonitorSampleBo streamMonitorSampleBo=map.get(EnergyTypeEnum.ENERGY_STEAM.getValue());
		if(null != streamMonitorSampleBo){
			ProduceOnlineMonitorTransferBo transferBo=this.buildProduceOnlineMonitorTransferBo(onlineMonitorBo.getDepartmentId(), EnergyTypeEnum.ENERGY_STEAM.getLabel());
			
			ListResp<RmiSamplDataDto> streamSampleData=this.querySampleDataFromAssignedEnergyType(streamMonitorSampleBo,meterRespMap,transferBo);
			streamSampleDto.setStreamFlowSampleData(streamSampleData);;
			
			ProduceOnlineMonitorSampleBo pressureMonitorSampleBo=this.buildProduceOnlineMonitorSampleBoForStreamPressure(streamMonitorSampleBo, SteamMetricEnum.STEAM_PRESSURE.getMetric());
			ListResp<RmiSamplDataDto> streamPressureSampleData=this.querySampleDataFromAssignedEnergyType(pressureMonitorSampleBo,meterRespMap,transferBo);
			streamSampleDto.setStreamPressureSampleData(streamPressureSampleData);
		}
		return streamSampleDto;
	}

	/**
	 * 获取---->实时用电情况
	 */
	@Override
	public ProduceOnlineElectricRealtimeSampleDto queryRealTimeElectricityStatus(
			ProduceOnlineMonitorBo onlineMonitorBo) {
		
		ProduceOnlineElectricRealtimeSampleDto electricRealtimeSampleDto=new ProduceOnlineElectricRealtimeSampleDto();
		Map<String,ProduceOnlineMonitorSampleBo> map=queryEquipInfo2(onlineMonitorBo.getDepartmentId(),onlineMonitorBo);
		
		Map<String,MeterResp> meterRespMap=this.queryEquipInfo(onlineMonitorBo.getDepartmentId());
		
		ProduceOnlineMonitorSampleBo electricMonitorSampleBo=map.get(EnergyTypeEnum.ENERGY_ELECTRICITY.getValue());
		if(null != electricMonitorSampleBo){
			ProduceOnlineMonitorTransferBo transferBo=this.buildProduceOnlineMonitorTransferBo(onlineMonitorBo.getDepartmentId(), EnergyTypeEnum.ENERGY_ELECTRICITY.getLabel());
			
			ProduceOnlineRealtimeSampleBo electricRealtimeSampleBo=CommonConverter.map(electricMonitorSampleBo, ProduceOnlineRealtimeSampleBo.class);
			this.queryRealTimeSampleDataByAssignedEnergyType(electricRealtimeSampleBo,meterRespMap,electricRealtimeSampleDto,transferBo);
			
			/*
			 * 统计指定时间段内总用电量
			 */
			List<String> equipIds=electricMonitorSampleBo.getEquips().stream().map(p->p.getEquipID()).collect(Collectors.toList());
			ElectricMeterReadingMinuteBo electricMeterReadingHourBo=CommonConverter.map(electricMonitorSampleBo, ElectricMeterReadingMinuteBo.class);
			electricMeterReadingHourBo.setEquipID(equipIds);
			ElectricMeterReadingMinuteStatisticsBo countElectricStaticsBo=this.countElectricMeterReadingMinuteDataFor24Hour(electricMeterReadingHourBo);
			if(null != countElectricStaticsBo){
				electricRealtimeSampleDto.setElectric24HourTotalPower(countElectricStaticsBo.getTotalElectricPower().setScale(BASIC_PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP));
			}
		}
		return electricRealtimeSampleDto;
	}

	/**
	 * 获取----->实时用汽情况
	 */
	@Override
	public ProduceOnlineStreamRealtimeSampleDto queryRealTimeStreamStatus(ProduceOnlineMonitorBo onlineMonitorBo) {
		
		ProduceOnlineStreamRealtimeSampleDto streamRealtimeSampleDto=new ProduceOnlineStreamRealtimeSampleDto();
		Map<String,ProduceOnlineMonitorSampleBo> map=queryEquipInfo2(onlineMonitorBo.getDepartmentId(),onlineMonitorBo);
		
		Map<String,MeterResp> meterRespMap=this.queryEquipInfo(onlineMonitorBo.getDepartmentId());
		
		ProduceOnlineMonitorSampleBo streamMonitorSampleBo=map.get(EnergyTypeEnum.ENERGY_STEAM.getValue());
		if(null != streamMonitorSampleBo){
			
			ProduceOnlineRealtimeSampleBo streamRealtimeSampleBo=CommonConverter.map(streamMonitorSampleBo, ProduceOnlineRealtimeSampleBo.class);
			this.queryRealTimeStreamStatus(streamRealtimeSampleBo,meterRespMap,streamRealtimeSampleDto);
			
			/*
			 * 统计指定时间段内总蒸汽量
			 */
			List<String> equipIds=streamMonitorSampleBo.getEquips().stream().map(p->p.getEquipID()).collect(Collectors.toList());
			SteamMeterReadingMinuteBo steamMeterReadingHourBo=CommonConverter.map(streamMonitorSampleBo, SteamMeterReadingMinuteBo.class);
			steamMeterReadingHourBo.setEquipID(equipIds);
			SteamMeterReadingMinuteStatisticsBo countSteamStaticsBo=this.countSteamMeterReadingMinuteDataFor24Hour(steamMeterReadingHourBo);
			if(null != countSteamStaticsBo){
				streamRealtimeSampleDto.setStream24HourTotalPower(countSteamStaticsBo.getTotalSteamPower().setScale(BASIC_PRECISE_DIGITS,BigDecimal.ROUND_HALF_UP));
			}
		}
		return streamRealtimeSampleDto;
	}

	/**
	 * 获取能源费用占比
	 */
	@Override
	public EnergyCostDto queryEnergyCostData(ProduceOnlineMonitorBo onlineMonitorBo) {
		
		EnergyCostDto energyCostDto=new EnergyCostDto();
		
		Map<String,ProduceOnlineMonitorSampleBo> map=queryEquipInfo2(onlineMonitorBo.getDepartmentId(),onlineMonitorBo);
		
		/*
		 * 统计指定时间段内总用电费用
		 */
		ProduceOnlineMonitorSampleBo electricMonitorSampleBo=map.get(EnergyTypeEnum.ENERGY_ELECTRICITY.getValue());
		if(null != electricMonitorSampleBo){
			
			List<String> equipIds=electricMonitorSampleBo.getEquips().stream().map(p->p.getEquipID()).collect(Collectors.toList());
			ElectricMeterReadingMinuteBo electricMeterReadingHourBo=CommonConverter.map(electricMonitorSampleBo, ElectricMeterReadingMinuteBo.class);
			electricMeterReadingHourBo.setEquipID(equipIds);
			ElectricMeterReadingMinuteStatisticsBo countElectricStaticsBo=this.countElectricMeterReadingMinuteDataFor24Hour(electricMeterReadingHourBo);
			if(null != countElectricStaticsBo){
				energyCostDto.setTotalElectricFees(countElectricStaticsBo.getTotalElectricFees());
			}
		}
		
		/*
		 * 统计指定时间段内总蒸汽费用
		 */
		ProduceOnlineMonitorSampleBo streamMonitorSampleBo=map.get(EnergyTypeEnum.ENERGY_STEAM.getValue());
		if(null != streamMonitorSampleBo){
			
			List<String> equipIds=streamMonitorSampleBo.getEquips().stream().map(p->p.getEquipID()).collect(Collectors.toList());
			SteamMeterReadingMinuteBo steamMeterReadingMinuteBo=CommonConverter.map(streamMonitorSampleBo, SteamMeterReadingMinuteBo.class);
			steamMeterReadingMinuteBo.setEquipID(equipIds);
			
			BigDecimal totalSteamFees=this.calculateSteamFeesFromAssignedDateRange(steamMeterReadingMinuteBo);
			
			energyCostDto.setTotalSteamFees(totalSteamFees);
//			SteamMeterReadingMinuteStatisticsBo countSteamStaticsBo=this.countSteamMeterReadingMinuteDataFor24Hour(steamMeterReadingMinuteBo);
//			if(null != countSteamStaticsBo){
//				energyCostDto.setTotalSteamFees(countSteamStaticsBo.getTotalSteamFees());
//			}
		}
		
		this.buildEnergyConstDto(energyCostDto);
		
		return energyCostDto;
	}
	
	/**
	 * 构建蒸汽算费请求对象
	 */
	private SteamFeesCalculateReq buildSteamFeesCalculateReq(SteamMeterReadingMinuteResultBo p){
		SteamFeesCalculateReq req=new SteamFeesCalculateReq();
		req.setMeterNo(p.getMeterNo());
		req.setUseVol(p.getUseQuantity());
		req.setBeforeVol(p.getQuantity().subtract(p.getUseQuantity()));
		return req;
	}
	
	
	/**
	 * 获取指定集合的蒸汽计算费用
	 * @param resultBos
	 * @return
	 */
	private List<SteamFeesCalculateReq> querySteamFeesByAssignedConditions(List<SteamMeterReadingMinuteResultBo> resultBos){
		
		List<SteamFeesCalculateReq> calculateResps=Lists.newArrayList();
		
		resultBos.stream().forEach(p->{
			try {
				SteamFeesCalculateReq req=this.buildSteamFeesCalculateReq(p);
				
				calculateResps.add(req);
			} catch (Exception e) {
				logger.error("【生产在线监测】,method:[calculateSteamFeesFromAssignedDateRange],查询[蒸汽minute]realTime采样数据异常,异常原因reason:{}",e.getMessage());
			}
		});
		return calculateResps;
	}
	
	/**
	 * 查询指定时间区间的蒸汽总费用
	 */
	public BigDecimal calculateSteamFeesFromAssignedDateRange(SteamMeterReadingMinuteBo meterReadingMinuteBo){
		
		List<SteamMeterReadingMinuteResultBo> minuteResultBos=steamMeterReadingService.querySteamMeterReadingMinuteByAssignedConditon(meterReadingMinuteBo);
		
		BigDecimal equipTotalFees=BigDecimal.ZERO;
        try {
        	SteamMeterReadingMinuteFeesBo minuteFeesBo=new SteamMeterReadingMinuteFeesBo();
        	
        	if(CollectionUtils.isNotEmpty(minuteResultBos)){
        		List<SteamFeesCalculateReq> calculateReqs=this.querySteamFeesByAssignedConditions(minuteResultBos);
        		logger.info("【蒸汽费用计算】,method:[queryEnergyCostData],reqParams:{}",JSON.toJSONString(calculateReqs));
        		EnergyResp<List<SteamFeesCalculateResp>> energyResp=steamFeesCalculateService.calculateCollectionApi(calculateReqs);
        		if(energyResp.getCode().equals( StatusCode.SUCCESS.getCode())){
        			List<SteamFeesCalculateResp> calculateResps=energyResp.getData();
        			if(CollectionUtils.isNotEmpty(calculateResps)){
        				minuteFeesBo.setMeterMinuteFees(calculateResps);
        				equipTotalFees=calculateResps.stream().filter(p->p.getFees()!=null).map(m->m.getFees()).reduce(BigDecimal.ZERO,BigDecimal::add);
        			}
				}
        	}
        	
        }catch (Exception e) {
        	logger.error("【生产在线监测】,method:[calculateSteamFeesFromAssignedDateRange],查询[蒸汽minute]realTime采样数据异常,异常原因reason:{}",e.getMessage());
        }
		
		return equipTotalFees;
	}

	

}
