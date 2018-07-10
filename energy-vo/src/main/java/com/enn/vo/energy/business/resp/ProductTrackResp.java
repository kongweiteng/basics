package com.enn.vo.energy.business.resp;

import java.util.List;

public class ProductTrackResp {

    private String indexType;
    private List<MetricAnalysisResp> metericAnalysisData;

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public List<MetricAnalysisResp> getMetericAnalysisData() {
        return metericAnalysisData;
    }

    public void setMetericAnalysisData(List<MetricAnalysisResp> metericAnalysisData) {
        this.metericAnalysisData = metericAnalysisData;
    }
}
