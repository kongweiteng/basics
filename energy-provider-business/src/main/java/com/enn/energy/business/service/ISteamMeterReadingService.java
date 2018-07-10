package com.enn.energy.business.service;

import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.bo.*;
import com.enn.vo.energy.business.po.SteamMeterReadingDayPo;
import com.enn.vo.energy.business.po.SteamMeterReadingMonthPo;
import com.enn.vo.energy.business.req.SteamMeterDayReq;
import com.enn.vo.energy.business.resp.StatisticsDataResp;
import com.enn.vo.energy.business.resp.SteamMeterReadingDetailResp;
import com.enn.vo.energy.business.resp.SteamMeterReadingResp;
import com.enn.vo.energy.business.resp.YesterdayBoardUnitResp;
import com.enn.vo.energy.business.upload.UploadResp;

import java.util.List;

/**
* @author kai.guo
* @version 创建时间：2018年6月7日 上午10:02:13
* @Description 蒸汽费用，用气量（日，小时，分钟）统计
*/
public interface ISteamMeterReadingService {
	
	
	/**
	 * 查询指定条件下的Hour蒸汽统计
	 * @return
	 */
	public SteamMeterReadingHourStatisticsBo countSteamMeterReadingHourByAssignedConditon(SteamMeterReadingHourBo meterReadingHourBo );
	
	
	/**
	 * 查询指定条件下的Minute蒸汽统计
	 * @return
	 */
	public SteamMeterReadingMinuteStatisticsBo countSteamMeterReadingMinuteByAssignedConditon(SteamMeterReadingMinuteBo meterReadingMinuteBo );

	/**
	 * 查询指定条件下的Day蒸汽统计
	 * @param meterReadingDayBo
	 * @return
	 */
	public SteamMeterReadingDayStatisticsBo countSteamMeterReadingDayByAssignedConditon(SteamMeterReadingDayBo meterReadingDayBo);

	/**
	 * 查询指定条件下的month蒸汽统计
	 * @param meterReadingMonthBo
	 * @return
	 */
	public SteamMeterReadingMonthStatisticsBo countSteamMeterReadingMonthByAssignedConditon(SteamMeterReadingMonthBo meterReadingMonthBo);
	
	/**
	 * 获取指定条件下的minute蒸汽信息
	 * @param meterReadingDayBo
	 * @return
	 */
	public List<SteamMeterReadingMinuteResultBo> querySteamMeterReadingMinuteByAssignedConditon(SteamMeterReadingMinuteBo meterReadingDayBo);


	public List<SteamMeterReadingDayPo> querySteamMeterReadingDayByAssignedConditon(SteamMeterReadingDayBo meterReadingDayBo);

	public List<SteamMeterReadingDayPo> querySteamDayGroup(SteamMeterReadingDayBo meterReadingDayBo);

	public List<SteamMeterReadingMonthPo> querySteamMeterReadingMonthByAssignedConditon(SteamMeterReadingMonthBo meterReadingMonthBo);

	public List<SteamMeterReadingMonthPo> querySteamMonthGroup(SteamMeterReadingMonthBo meterReadingMonthBo);

	List<StatisticsDataResp> getSteamByMonth(SteamMeterReadingMonthBo meterReadingMonthBo);

	List<StatisticsDataResp> getSteamByDay(SteamMeterReadingDayBo meterReadingMonthBo);


	/**
	 * 根据车间id查询各个生产线的用气情况（昨日数值）
	 */
	public EnergyResp<List<YesterdayBoardUnitResp>> getYesterdayBoardUnit(DefaultReq defaultReq);


    List<SteamMeterReadingResp> getSteamMeterForDay(SteamMeterDayReq req);

    PagedList<SteamMeterReadingResp> getSteamMeterDetailForDay(SteamMeterDayReq req);

    List<SteamMeterReadingResp> getSteamMeterForYear(SteamMeterDayReq req);

    PagedList<SteamMeterReadingDetailResp> getSteamMeterDetailForYear(SteamMeterDayReq req);

    EnergyResp<UploadResp> exportDetailDataForExcelDay(SteamMeterDayReq req);

    EnergyResp<UploadResp> detailDataForExcelYear(SteamMeterDayReq req);
}
