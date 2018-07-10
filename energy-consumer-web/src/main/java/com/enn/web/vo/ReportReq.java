package com.enn.web.vo;

import com.enn.vo.energy.DefaultReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("文件导出请求参数")
@Data
public class ReportReq extends DefaultReq {
    @ApiModelProperty(value="start",name="start",example="123")
    @NotNull(message="start 不能为空！")
    private String start;

}
