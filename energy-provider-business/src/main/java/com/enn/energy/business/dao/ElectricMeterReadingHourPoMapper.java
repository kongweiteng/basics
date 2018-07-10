package com.enn.energy.business.dao;

import java.util.List;

import com.enn.vo.energy.business.bo.ElectricMeterReadingHourStatisticsBo;
import com.enn.vo.energy.business.condition.ElectricMeterReadingHourCondition;
import com.enn.vo.energy.business.po.ElectricMeterReadingHourPo;
import com.enn.vo.energy.business.req.ElectricMeterReadingReq;
import com.enn.vo.energy.business.resp.StatisticsDataResp;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectricMeterReadingHourPoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ElectricMeterReadingHourPo record);

    int insertSelective(ElectricMeterReadingHourPo record);

    ElectricMeterReadingHourPo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ElectricMeterReadingHourPo record);

    int updateByPrimaryKey(ElectricMeterReadingHourPo record);
    
    /**
     * 根据指定条件获取指定hour段内的电量统计信息
     * @param condition
     * @return
     */
    List<ElectricMeterReadingHourPo> queryElectricMeterReadingHourByAssignedConditon(ElectricMeterReadingHourCondition condition);
    
    /**
     * 获取指定条件下指定设备集合的用点信息
     * @param condition
     * @return
     */
    ElectricMeterReadingHourStatisticsBo countElectricMeterReadingHourByAssignedConditon(ElectricMeterReadingHourCondition condition);

    List<StatisticsDataResp> getElecSumByHour(ElectricMeterReadingReq req);
}