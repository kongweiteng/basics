package com.enn.energy.business.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.enn.vo.energy.business.po.DataResp;
import com.enn.vo.energy.business.po.ElectricMeterPeakReadingDayPo;
import com.enn.vo.energy.business.req.ElectricMeterReadingReq;
import com.enn.vo.energy.business.resp.ElectricMeterReadingResp;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 日用电峰时刻用电量、用电费用
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 15:51
 */
@Repository
public interface ElectricMeterPeakReadingDayMapper extends BaseMapper<ElectricMeterPeakReadingDayPo> {

    /**
     * 查询用电统计
     *
     * @param req
     * @return
     */
    ElectricMeterReadingResp getSum(ElectricMeterReadingReq req);

    /**
     * 查询时间段每一天的峰值数据
     */
    List<DataResp> getMeterPeakDay(ElectricMeterReadingReq req);

    /**
     * 获取一天的峰时段的总和(按年查询，日期选择本月)
     */
    DataResp getMeterSumPeakDay(ElectricMeterReadingReq req);

}
