package com.enn.vo.energy.business.req;

import java.util.List;

/**
 * 请求opentsdb的必要请求参数
 */
public class QueriesReq {
    private String metric;
    private String downsample;
    private String aggregator;
    private List<FilterReq> filters ;
    private TagsReq tags ;


    public TagsReq getTags() {
        return tags;
    }

    public void setTags(TagsReq tags) {
        this.tags = tags;
    }

    public String getAggregator() {
        return aggregator;
    }

    public void setAggregator(String aggregator) {
        this.aggregator = aggregator;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getDownsample() {
        return downsample;
    }

    public void setDownsample(String downsample) {
        this.downsample = downsample;
    }

    public List<FilterReq> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterReq> filters) {
        this.filters = filters;
    }
}
