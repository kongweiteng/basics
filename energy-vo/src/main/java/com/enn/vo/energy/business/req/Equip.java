package com.enn.vo.energy.business.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("com.enn.vo.energy.business.req.Equip")
@Data
public class Equip {

    @ApiModelProperty(value="站点",name="staId",example="GSB02")
    @NotBlank(message="staId 不能为空！")
    private String staId;//站点id

    @ApiModelProperty(value="设备类型",name="equipMK",example="GSB")
    @NotBlank(message="equipMK 不能为空！")
    private String equipMK;//设备类型

    @ApiModelProperty(value="设备id",name="equipID")
    @NotBlank(message="equipID 不能为空！")
    private String equipID;
    
}
