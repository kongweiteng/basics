package com.enn.energy.business.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.enn.vo.energy.business.po.DataResp;
import com.enn.vo.energy.business.po.ElectricMeterTipReadingMonthPo;
import com.enn.vo.energy.business.req.ElectricMeterReadingReq;
import com.enn.vo.energy.business.resp.ElectricMeterReadingResp;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 月用电尖时刻用电量、用电费用
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 15:56
 */
@Repository
public interface ElectricMeterTipReadingMonthMapper extends BaseMapper<ElectricMeterTipReadingMonthPo> {

    /**
     * 查询用电统计
     *
     * @param req
     * @return
     */
    ElectricMeterReadingResp getSum(ElectricMeterReadingReq req);
    /**
     * 查询时间段每一月的尖值数据
     */
    List<DataResp> getMeterTipMonth(ElectricMeterReadingReq req);


}
