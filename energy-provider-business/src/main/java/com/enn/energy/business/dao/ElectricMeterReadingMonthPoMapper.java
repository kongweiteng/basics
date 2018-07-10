package com.enn.energy.business.dao;

import com.enn.vo.energy.business.po.DataResp;
import com.enn.vo.energy.business.po.ElectricMeterReadingMonthPo;
import com.enn.vo.energy.business.req.ElectricMeterReadingReq;
import com.enn.vo.energy.business.resp.StatisticsDataResp;
import com.enn.vo.energy.business.vo.BoardMonthVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectricMeterReadingMonthPoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ElectricMeterReadingMonthPo record);

    int insertSelective(ElectricMeterReadingMonthPo record);

    ElectricMeterReadingMonthPo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ElectricMeterReadingMonthPo record);

    int updateByPrimaryKey(ElectricMeterReadingMonthPo record);

    /**
     * 查询时间段每一月的总量数据
     */
    List<DataResp> getMeterReadingMonth(ElectricMeterReadingReq req);

    List<StatisticsDataResp> getElecSumByMonth(ElectricMeterReadingReq req);


    BoardMonthVo electricMonthTotalByWorkShop(@Param("workShopId") Integer workShopId, @Param("dateFlag") Integer dateFlag);

    BoardMonthVo electricMonthTotal(@Param("loopNumbers") List<String> loopNumbers, @Param("dateFlag") Integer dateFlag);

    List<BoardMonthVo> electricMonthData(@Param("loopNumbers") List<String> loopNumbers);

    BoardMonthVo monthTotalVo(@Param("loopNumber") String loopNumber, @Param("dateFlag") Integer dateFlag);

    /**
     * 查询月总用电量和总费用
     *
     * @param req
     * @return
     */
    ElectricMeterReadingMonthPo getElecSumByM(ElectricMeterReadingReq req);
}