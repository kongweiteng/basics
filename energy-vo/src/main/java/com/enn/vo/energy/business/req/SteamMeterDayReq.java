package com.enn.vo.energy.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel("车间用汽统计-用汽详情")
public class SteamMeterDayReq implements Serializable {

    @ApiModelProperty("车间id")
    @NotNull(message = "车间id 不能为空")
    private Integer unitId;

    @ApiModelProperty("开始日期")
    private String startTime;

    @ApiModelProperty("结束日期")
    private String endTime;

    @ApiModelProperty(value = "每页显示数", required = false)
    @Min(value=1,message = "每页显示数必须大于0！")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "第几页", required = false)
    @Min(value=1,message = "第几页必须大于0！")
    private Integer pageNum = 1;

    @ApiModelProperty("时间维度（1=day，2=month，3=year）")
    private Integer dateType;

    @ApiModelProperty(hidden = true)
    private int flag;

    @ApiModelProperty(hidden = true)
    private int exist;

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

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

    public Integer getDateType() {
        return dateType;
    }

    public void setDateType(Integer dateType) {
        this.dateType = dateType;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getExist() {
        return exist;
    }

    public void setExist(int exist) {
        this.exist = exist;
    }
}
