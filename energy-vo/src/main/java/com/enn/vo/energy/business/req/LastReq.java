package com.enn.vo.energy.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.util.List;
@ApiModel("最后一条数据请求实体")
@Data
public class LastReq {
    @ApiModelProperty(value="测点",name="metric",example="UES.TsPreOut")
    //@Pattern(regexp = "(?i)^ETS.*$|^UES.*$|^PVS.*$", message = "metric中必须包含业务域！！！")
    @NotBlank(message="metric 不能为空！")
    private  String metric;

    @ApiModelProperty(value="设备列表",name="equipID")
    @NotEmpty(message="equipID 不能为空！")
    private List<String> equipID;

    @ApiModelProperty(value="设备类型",name="equipMK",example="GSB")
    @NotBlank(message="equipMK 不能为空！")
    private String equipMK;


    @ApiModelProperty(value="站点",name="staId",example="GSB02")
    @NotBlank(message="start 不能为空！")
    private String staId;

}
