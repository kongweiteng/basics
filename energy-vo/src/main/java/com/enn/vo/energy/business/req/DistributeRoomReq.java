package com.enn.vo.energy.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel("根据配电室请求实体")
public class DistributeRoomReq {


    @ApiModelProperty(value="配电室ID",name="配电室ID",example="123")
    @NotNull(message="配电室ID 不能为空！")
    private Long distributeRoomId;

    public Long getDistributeRoomId() {
        return distributeRoomId;
    }

    public void setDistributeRoomId(Long distributeRoomId) {
        this.distributeRoomId = distributeRoomId;
    }
}
