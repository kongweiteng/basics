package com.enn.vo.energy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("接口返回list默认实体")
public class ListResp<T> {
    @ApiModelProperty(value="返回list",name="list")
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
