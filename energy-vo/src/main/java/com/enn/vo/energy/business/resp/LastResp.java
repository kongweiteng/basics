package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("最后一条数据返回实体")
@Data
public class LastResp {
    @ApiModelProperty(value="设备id",name="equipID",example="GSB02")
    private String equipID;

    @ApiModelProperty(value="时间",name="time",example="2018-04-24 23:31:22")
    private String time;

    @ApiModelProperty(value="值",name="value",example="0.059")
    private String value;
    @ApiModelProperty(value="站点id",name="staId",example="0.059")
    private String staId;


}
