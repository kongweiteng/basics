package com.enn.vo.energy.business.resp;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DetailedResp {


    /**
     * 车间下生产线总用汽量、总金额
     */
    private List<ProductionLineResp> productionLineResps;

    /**
     * 车间每日用汽量
     */
    private List<ProductionLineData> productionLineDatas;
}
