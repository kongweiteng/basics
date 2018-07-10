package com.enn.vo.energy.business.req;

import com.enn.vo.energy.DefaultReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("核算单元列表请求")
public class UnitReq extends DefaultReq {
    @ApiModelProperty(value="页大小",name="pageSize",example="123")
    private Integer pageSize;

    @ApiModelProperty(value="页数量",name="pageSize",example="123")
    private Integer pageNo;

    @ApiModelProperty(value="能源类型",name="energyType",example="123")
    private String energyType;

    @ApiModelProperty(value="节点类型",name="nodeType",example="123")
    private String nodeType;


    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType;
    }
}
