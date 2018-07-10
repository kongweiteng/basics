package com.enn.vo.energy.system.req;


import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

@ApiModel("条件查询日志信息参数")
@Data
public class SysLogCondition {

    @ApiModelProperty("错误代码")
    private Integer code;

    @ApiModelProperty("错误信息（模糊查询）")
    private String msg;
    @ApiModelProperty("项目（模糊查询）")
    private String project;
}
