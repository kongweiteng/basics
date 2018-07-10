package com.enn.vo.energy.business.resp;

import lombok.Data;

import java.util.List;

@Data
public class ProductConsumptionListResp {

    private List<ProduceLoadResp> produceLoadRespList;

    private List<ProduceLineResp> produceLineRespList;
}
