package com.enn.web.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
@ApiModel("昨日能源看板车间展开图")
public class EnergyUnitOutResp {

    /**
     * 用能明细
     */
    private UnitEnergyInfoResp info;


    /**
     * 用电统计图
     */
    private List<ElectricTypeQuantityResp> elecStatistics;


    /**
     * 用汽统计
     */
    private LineResp seatStatistics;

}
