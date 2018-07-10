package com.enn.vo.energy.business.req;

import com.enn.vo.energy.DefaultReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel("请求企业下所有计量表请求参数")
@Getter
@Setter
public class MeterListReq extends DefaultReq {
    @ApiModelProperty(value="能源类型（01电、02蒸汽、03水、04冷、05热、06压缩空气）",name="energyType",example="01")
    @NotNull(message="energyType 不能为空！")
    private String energyType;
    @ApiModelProperty(value="计量表名称（模糊查询条件）",name="meterName",example="你好")
    private String meterName;

}
