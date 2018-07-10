package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ApiModel("核算单元列表返回")
@Getter
@Setter
public class UnitResp {
    @ApiModelProperty(value="核算单元名称",name="name",example="厂房一")
    private String name;

    @ApiModelProperty(value="核算单元id",name="id",example="13456")
    private Long id;

    @ApiModelProperty(value="核算单元公式",name="formula",example="7547")
    private String formula;

    @ApiModelProperty(value="设备信息",name="loopNumber")
    private List<MeterResp> meters;

    @ApiModelProperty(value="核算单元类型",name="accountingType")
    private String accountingType;


}
