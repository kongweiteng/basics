package com.enn.vo.energy.business.resp;

import com.enn.vo.energy.business.MonitorNum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by kexing on 2018/4/18.
 */
@ApiModel(value = "com.enn.vo.energy.business.resp.EventResp",description = "首页",subTypes={EventResp.class})
public class EventResp {
    @ApiModelProperty(value="安全用电时间",name="useTime",example="7")
    private String useTime;

    @ApiModelProperty(value="事件信息",name="monitor")
    private List<MonitorNum> monitor;

//    @ApiModelProperty(value="分析报告信息",name="report")
//    private ReportResp report;

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

//    public ReportResp getReport() {
//        return report;
//    }
//
//    public void setReport(ReportResp report) {
//        this.report = report;
//    }

    public List<MonitorNum> getMonitor() {
        return monitor;
    }

    public void setMonitor(List<MonitorNum> monitor) {
        this.monitor = monitor;
    }



}
