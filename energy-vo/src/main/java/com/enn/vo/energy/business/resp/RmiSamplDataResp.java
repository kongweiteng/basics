package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 采集数据返回实体
 */
@ApiModel("采样数据返回实体")
@Data
public class RmiSamplDataResp implements Serializable {
    @ApiModelProperty(value="测点",name="metric",example="UES.TsPreOut")
    private String metric;

    @ApiModelProperty(value="设备id",name="equipID",example="GSB02")
    private String equipID;

    @ApiModelProperty(value="设备类型",name="equipMK",example="GSB02")
    private String equipMK;

    @ApiModelProperty(value="站点id",name="equipID",example="GSB02")
    private String staId;

    @ApiModelProperty(value="列表数据",name="dataResp")
    private List<DataResp> dataResp;

}
