package com.enn.vo.energy.business.resp;

import java.util.List;

public class MetricAnalysisResp {
    private String metricName;
    private String metric;
    private List<DataResp> metricData;

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public List<DataResp> getMetricData() {
        return metricData;
    }

    public void setMetricData(List<DataResp> metricData) {
        this.metricData = metricData;
    }
}
