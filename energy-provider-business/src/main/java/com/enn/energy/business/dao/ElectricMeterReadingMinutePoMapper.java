package com.enn.energy.business.dao;

import com.enn.vo.energy.business.bo.ElectricMeterReadingMinuteStatisticsBo;
import com.enn.vo.energy.business.condition.ElectricMeterReadingMinuteCondition;
import com.enn.vo.energy.business.po.DataResp;
import com.enn.vo.energy.business.po.ElectricMeterReadingMinutePo;
import com.enn.vo.energy.business.req.ElectricMeterReadingReq;
import com.enn.vo.energy.business.resp.ElectricMeterReadingResp;
import com.enn.vo.energy.business.resp.StatisticsDataResp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectricMeterReadingMinutePoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ElectricMeterReadingMinutePo record);

    int insertSelective(ElectricMeterReadingMinutePo record);

    ElectricMeterReadingMinutePo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ElectricMeterReadingMinutePo record);

    int updateByPrimaryKey(ElectricMeterReadingMinutePo record);

    /**
     * 查询用电统计
     *
     * @param req
     * @return
     */
    ElectricMeterReadingResp getSum(ElectricMeterReadingReq req);
    
    /**
     * 根据指定条件获取指定minute段内的电量统计信息
     * @param condition
     * @return
     */
    List<ElectricMeterReadingMinutePo> queryElectricMeterReadingMinuteByAssignedConditon(ElectricMeterReadingMinuteCondition condition);
    
    /**
     * 获取指定条件下指定minute设备集合的用点信息
     * @param condition
     * @return
     */
    ElectricMeterReadingMinuteStatisticsBo countElectricMeterReadingMinuteByAssignedConditon(ElectricMeterReadingMinuteCondition condition);
    /**
     * 获取分钟表中的尖峰平谷数据总和
     */
    DataResp getMeterMinute(ElectricMeterReadingReq req,@Param("electrictype") String electrictype);
	/**
	 * 获取总量的数据
	 */
	DataResp getSumMinute(ElectricMeterReadingReq req);


    List<StatisticsDataResp> getElecSumByMinute(ElectricMeterReadingReq req);


}