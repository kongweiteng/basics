package com.enn.vo.energy.business.req;

public class QueriesLastReq {
    private  String metric;
    private  TagsReq tags;

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public TagsReq getTags() {
        return tags;
    }

    public void setTags(TagsReq tags) {
        this.tags = tags;
    }
}
