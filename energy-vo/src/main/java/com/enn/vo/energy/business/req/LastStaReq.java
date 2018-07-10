package com.enn.vo.energy.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@ApiModel("最后一条数据请求实体-不同站点id")
@Data
public class LastStaReq {
    @ApiModelProperty(value="测点",name="metric",example="UES.TsPreOut")
    //@Pattern(regexp = "(?i)^ETS.*$|^UES.*$|^PVS.*$", message = "metric中必须包含业务域！！！")
    @NotBlank(message="metric 不能为空！")
    private  String metric;

    @NotEmpty(message="equips 不能为空！")
    @ApiModelProperty(value="设备信息请求实体类")
    private List<Equip> equips;
}
