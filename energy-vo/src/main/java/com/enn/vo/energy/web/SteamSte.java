package com.enn.vo.energy.web;

import com.enn.vo.energy.business.resp.DataResp;
import com.enn.vo.energy.business.resp.ProductionLineData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SteamSte {

    @ApiModelProperty(value="压力",name="dataRespPa")
    private List<ProductionLineData> dataRespPa;

    @ApiModelProperty(value="瞬时流量",name="dataRespFs")
    private List<ProductionLineData> dataRespFs;
}
