package com.enn.vo.energy.business.resp;

import lombok.Data;

@Data
public class ProduceLoadResp {

    private Long lineId;

    private String name;

    private String equipID;

    private RmiSamplDataResp rmiSamplDataRespList;
}
