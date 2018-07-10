package com.enn.energy.business.service;


import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.bo.*;
import com.enn.vo.energy.business.po.ElectricMeterReadingDayPo;
import com.enn.vo.energy.business.po.ElectricMeterReadingMonthPo;
import com.enn.vo.energy.business.req.ElectricMeterReadingReq;
import com.enn.vo.energy.business.resp.YesterdayBoardUnitResp;

import java.util.List;

/**
* @author kai.guo
* @version 创建时间：2018年6月7日 上午9:56:08
* @Description 用电费用，用电量综合统计服务
*/
public interface IElectricMeterReadingService {
	
	/**
	 * 查询指定条件下的Hour用电统计
	 * @return
	 */
	public ElectricMeterReadingHourStatisticsBo countElectricMeterReadingHourByAssignedConditon(ElectricMeterReadingHourBo meterReadingHourBo );
	
	
	/**
	 * 查询指定条件下的Minute用电统计
	 * @return
	 */
	public ElectricMeterReadingMinuteStatisticsBo countElectricMeterReadingMinuteByAssignedConditon(ElectricMeterReadingMinuteBo meterReadingMinuteBo );


	/**
	 * 根据车间id查询各个生产线的用电情况（昨日数值）
	 */
	public EnergyResp<List<YesterdayBoardUnitResp>> getYesterdayBoardUnit(DefaultReq defaultReq);


	/**
	 *  时间段内的电量和
	 */
	public ElecMeterReadingDayStatisticsBo countElecMeterReadingDayByAssignedConditon(ElecMeterReadingDayBo elecMeterReadingDayBo);
	public Integer getCount(ElecMeterReadingDayBo elecMeterReadingDayBo);

	/**
	 * 查询日总用电量和总费用
	 *
	 * @param req
	 * @return
	 */
	ElectricMeterReadingDayPo getElecSumByD(ElectricMeterReadingReq req);

	/**
	 * 查询月总用电量和总费用
	 *
	 * @param req
	 * @return
	 */
	ElectricMeterReadingMonthPo getElecSumByM(ElectricMeterReadingReq req);
}
