package com.enn.energy.passage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel("算力通道请求价格参数")
public class EnergyPriceReq {


        @ApiModelProperty("表号")
        @NotBlank(message = "表号不能为空！")
        private String meterNo;

        @ApiModelProperty(value = "日期" , example = "yyyy-MM-dd HH:mm:ss" , name = "date")
        @NotBlank(message="日期不能为空！")
        @Length(min = 19, max = 19, message = "时间格式为：yyyy-MM-dd HH:mm:ss！！！")
        private String date;

}
