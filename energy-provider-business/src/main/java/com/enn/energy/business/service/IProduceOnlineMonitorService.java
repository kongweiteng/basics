package com.enn.energy.business.service;

import java.math.BigDecimal;
import java.util.List;

import com.enn.vo.energy.business.bo.ElectricMeterReadingMinuteBo;
import com.enn.vo.energy.business.bo.ElectricMeterReadingMinuteStatisticsBo;
import com.enn.vo.energy.business.bo.ProduceOnlineMonitorBo;
import com.enn.vo.energy.business.bo.SteamMeterReadingMinuteBo;
import com.enn.vo.energy.business.bo.SteamMeterReadingMinuteStatisticsBo;
import com.enn.vo.energy.business.dto.DepartUnitDto;
import com.enn.vo.energy.business.dto.EnergyCostDto;
import com.enn.vo.energy.business.dto.ProduceOnlineElectricRealtimeSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineElectricSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineStreamRealtimeSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineStreamSampleDto;

/**
 * 生产在线监测
 * @author kai.guo
 *
 */
public interface IProduceOnlineMonitorService {
	
	/**
	 * 查询指定企业id下的车间信息
	 * @param obj
	 * @return
	 */
	List<DepartUnitDto> queryProductionDepartment(Long companyId);
	
	/**
	 * 查询电力系统  功率曲线
	 * @return
	 */
	ProduceOnlineElectricSampleDto queryPowerCurveForWebElectricSystem(ProduceOnlineMonitorBo onlineMonitorBo);
	
	
	/**
	 * 查询热力系统蒸汽流量曲线
	 * @param object
	 * @return
	 */
	ProduceOnlineStreamSampleDto queryFlowCurveForWebThermodynamicSystem(ProduceOnlineMonitorBo onlineMonitorBo);
	
	
	
	/**
	 * 实时用电情况
	 * @param object
	 * @return
	 */
	ProduceOnlineElectricRealtimeSampleDto queryRealTimeElectricityStatus(ProduceOnlineMonitorBo onlineMonitorBo);
	
	
	
	/**
	 * 实时蒸汽情况
	 * @param object
	 * @return
	 */
	ProduceOnlineStreamRealtimeSampleDto queryRealTimeStreamStatus(ProduceOnlineMonitorBo onlineMonitorBo);
	
	
	/**
	 * 生产在线监测能源消耗
	 * @param onlineMonitorBo
	 * @return
	 */
	EnergyCostDto queryEnergyCostData(ProduceOnlineMonitorBo onlineMonitorBo);
	
	/**
	 * 查询24小时总用电量
	 */
	ElectricMeterReadingMinuteStatisticsBo countElectricMeterReadingMinuteDataFor24Hour(ElectricMeterReadingMinuteBo electricMeterReadingMinuteBo);
	
	/**
	 * 查询24小时总用汽量
	 */
	SteamMeterReadingMinuteStatisticsBo countSteamMeterReadingMinuteDataFor24Hour(SteamMeterReadingMinuteBo steamMeterReadingMinuteBo);
	
	
	BigDecimal calculateSteamFeesFromAssignedDateRange(SteamMeterReadingMinuteBo meterReadingMinuteBo);

}
