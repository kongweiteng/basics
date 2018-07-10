package com.enn.energy.business.dao;

import com.enn.vo.energy.business.bo.ElecMeterReadingDayBo;
import com.enn.vo.energy.business.bo.ElecMeterReadingDayStatisticsBo;
import com.enn.vo.energy.business.bo.SteamMeterReadingDayStatisticsBo;
import com.enn.vo.energy.business.condition.SteamMeterReadingDayCondition;
import com.enn.vo.energy.business.po.DataResp;
import com.enn.vo.energy.business.po.ElectricMeterReadingDayPo;
import com.enn.vo.energy.business.req.ElectricMeterReadingReq;
import com.enn.vo.energy.business.resp.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectricMeterReadingDayPoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ElectricMeterReadingDayPo record);

    int insertSelective(ElectricMeterReadingDayPo record);

    ElectricMeterReadingDayPo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ElectricMeterReadingDayPo record);

    int updateByPrimaryKey(ElectricMeterReadingDayPo record);

    List<DataResp> getMeterReadingDay(ElectricMeterReadingReq req);

    List<StatisticsDataResp> getElecSumByDay(ElectricMeterReadingReq req);

    DataResp getMeterSunmReadingDay(ElectricMeterReadingReq req);

    /**
     * 根据 表号、时间  查询能源用量和  用费和
     */
    public YesterdayBoardUnitResp getYesterdayBoardUnit(@Param("time") String time, @Param("meters") List<String> meters);


    /**
     * 查寻时间段内的和
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
}