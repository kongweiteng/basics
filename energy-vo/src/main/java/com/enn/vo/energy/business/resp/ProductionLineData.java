package com.enn.vo.energy.business.resp;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductionLineData {

    private String name;

    private List<DataResp> dataResp;
}
