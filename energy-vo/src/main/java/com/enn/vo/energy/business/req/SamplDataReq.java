package com.enn.vo.energy.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;


import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 采样获取数据 -请求参数
 */
@ApiModel("采样数据请求实体")
@Data
public class SamplDataReq {
    @ApiModelProperty(value="开始时间",name="start",example="yyyy-MM-dd HH:mm:ss")
    @NotBlank(message="start 不能为空！")
    @Length(min=19, max=19,message = "时间格式为：yyyy-MM-dd HH:mm:ss！！！")
    private String start;


    @ApiModelProperty(value="结束时间",name="end",example="yyyy-MM-dd HH:mm:ss")
    @NotBlank(message="end 不能为空！")
    @Length(min=19, max=19,message = "时间格式为：yyyy-MM-dd HH:mm:ss！！！")
    private String end;


    @ApiModelProperty(value="采样格式",name="downsample",example="1m-first")
    //@NotBlank(message="downsample 不能为空！")
    private String downsample;


    @ApiModelProperty(value="设备列表",name="equipID")
    @NotEmpty(message="equipID 不能为空！")
    private List<String> equipID;


    @ApiModelProperty(value="测点",name="metric",example="UES.TsPreOut")
    //@Pattern(regexp = "(?i)^ETS.*$|^UES.*$|^PVS.*$", message = "metric中必须包含业务域！！！")
    @NotBlank(message="metric 不能为空！")
    private String metric;

    @ApiModelProperty(value="站点",name="staId",example="GSB02")
    @NotBlank(message="staId 不能为空！")
    private String staId;//站点id


    @ApiModelProperty(value="设备类型",name="equipMK",example="GSB")
    @NotBlank(message="equipMK 不能为空！")
    private String equipMK;//设备类型

    @ApiModelProperty(value="结果小数位数",name="point",example="4")
    private Integer point;//结果小数位数

}
