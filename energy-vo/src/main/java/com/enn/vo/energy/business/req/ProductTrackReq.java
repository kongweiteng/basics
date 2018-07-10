package com.enn.vo.energy.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel("采样数据请求实体")
public class ProductTrackReq {
    @ApiModelProperty("开始日期")
    @NotBlank(message = "startTime 不能为空！")
    @Pattern(regexp = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d$",message = "开始日期格式为yyyy-MM-dd HH:mm")
    private String startTime;
    @ApiModelProperty("结束日期")
    @NotBlank(message = "endTime 不能为空！")
    @Pattern(regexp = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d$",message = "结束日期格式为yyyy-MM-dd HH:mm")
    private String endTime;

    @ApiModelProperty("生产线id")
    @NotNull(message = "生产线id 不能为空")
    private Long proLineId;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getProLineId() {
        return proLineId;
    }

    public void setProLineId(Long proLineId) {
        this.proLineId = proLineId;
    }
}
