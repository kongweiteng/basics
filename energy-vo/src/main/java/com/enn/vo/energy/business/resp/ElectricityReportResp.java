package com.enn.vo.energy.business.resp;

import lombok.Data;

import java.util.List;

/**
 * 字典数据返回
 */
public class ElectricityReportResp {
    private List<ElectricityReportVO> electricityReportList;

    public List<ElectricityReportVO> getElectricityReportList() {
        return electricityReportList;
    }

    public void setElectricityReportList(List<ElectricityReportVO> electricityReportList) {
        this.electricityReportList = electricityReportList;
    }
}
