package com.enn.vo.energy.system.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel("电基础信息请求")
public class ElectricInfoReq {

    @ApiModelProperty("企业用户id")
    @NotNull(message = "custId 不能为空")
    private long custId;

    @ApiModelProperty("开始执行日期")
    @Length(min = 10, max = 10, message = "开始执行日期格式为：yyyy-MM-dd！")
    private String startExecuteDate;

    @ApiModelProperty("每页显示数")
    @Min(value=1,message = "每页显示数必须大于0！")
    private Integer pageSize = 10;

    @ApiModelProperty("第几页")
    @Min(value=1,message = "第几页必须大于0！")
    private Integer pageNum = 1;

    public long getCustId() {
        return custId;
    }

    public void setCustId(long custId) {
        this.custId = custId;
    }

    public String getStartExecuteDate() {
        return startExecuteDate;
    }

    public void setStartExecuteDate(String startExecuteDate) {
        this.startExecuteDate = startExecuteDate;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
