package com.enn.energy.business.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.enn.vo.energy.business.po.DataResp;
import com.enn.vo.energy.business.po.ElectricMeterValleyReadingMonthPo;
import com.enn.vo.energy.business.req.ElectricMeterReadingReq;
import com.enn.vo.energy.business.resp.ElectricMeterReadingResp;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 月用电谷时刻用电量、用电费用
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 16:00
 */
@Repository
public interface ElectricMeterValleyReadingMonthMapper extends BaseMapper<ElectricMeterValleyReadingMonthPo> {

    /**
     * 查询用电统计
     *
     * @param req
     * @return
     */
    ElectricMeterReadingResp getSum(ElectricMeterReadingReq req);
    /**
     * 查询时间段每一月的谷值数据
     */
    List<DataResp> getMeterValleyMonth(ElectricMeterReadingReq req);
}
