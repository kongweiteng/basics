package com.enn.vo.energy.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import sun.rmi.log.LogInputStream;

import java.util.List;
@ApiModel("采样数据请求实体-不同站点")
@Data
public class SamplDataStaReq {


    @ApiModelProperty(value="开始时间",name="start",example="yyyy-MM-dd HH:mm:ss")
    @NotBlank(message="start 不能为空！")
    @Length(min=19, max=19,message = "时间格式为yyyy-MM-dd HH:mm:ss！！！")
    private String start;


    @ApiModelProperty(value="结束时间",name="end",example="yyyy-MM-dd HH:mm:ss")
    @NotBlank(message="end 不能为空！")
    @Length(min=19, max=19,message = "时间格式为：yyyy-MM-dd HH:mm:ss！！！")
    private String end;

    @ApiModelProperty(value="测点",name="metric",example="UES.TsPreOut")
    //@Pattern(regexp = "(?i)^ETS.*$|^UES.*$|^PVS.*$", message = "metric中必须包含业务域！！！")
    @NotBlank(message="metric 不能为空！")
    private String metric;

    @NotEmpty(message="equips 不能为空！")
    @ApiModelProperty(value="设备信息请求实体类")
    private List<Equip> equips;

    @ApiModelProperty(value="采样格式",name="downsample",example="1m-first")
    //@NotBlank(message="downsample 不能为空！")
    private String downsample;


    @ApiModelProperty(value="结果小数位数",name="point",example="4")
    private Integer point;//结果小数位数


}
