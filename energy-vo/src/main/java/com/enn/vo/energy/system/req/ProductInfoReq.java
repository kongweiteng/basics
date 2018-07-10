package com.enn.vo.energy.system.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ApiModel("产品信息管理查询请求")
public class ProductInfoReq {
    @ApiModelProperty("企业用户id")
    @NotNull(message = "custId 不能为空")
    private Long custId;

    @ApiModelProperty("每页显示数")
    @Min(value = 1, message = "每页显示数必须大于0！")
    private Integer pageSize = 10;

    @ApiModelProperty("第几页")
    @Min(value = 1, message = "第几页必须大于0！")
    private Integer pageNum = 1;

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
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
