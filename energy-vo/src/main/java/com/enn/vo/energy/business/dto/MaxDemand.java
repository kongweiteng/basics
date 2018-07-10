package com.enn.vo.energy.business.dto;

import com.enn.vo.energy.business.resp.DataResp;

import java.util.List;

public class MaxDemand {
    private List<DataResp> dataResp;

    public List<DataResp> getDataResp() {
        return dataResp;
    }

    public void setDataResp(List<DataResp> dataResp) {
        this.dataResp = dataResp;
    }

}
