package com.enn.vo.energy.business.req;

import java.util.List;

/**
 * opentsdb 请求最后一条数据 实体类
 */
public class LastDataReq {
    private String userKey;
    private String dataSource;
    private List<QueriesLastReq> queries;
    private  boolean resolveNames;
    private  Integer backScan;

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public List<QueriesLastReq> getQueries() {
        return queries;
    }

    public void setQueries(List<QueriesLastReq> queries) {
        this.queries = queries;
    }

    public boolean isResolveNames() {
        return resolveNames;
    }

    public void setResolveNames(boolean resolveNames) {
        this.resolveNames = resolveNames;
    }

    public Integer getBackScan() {
        return backScan;
    }

    public void setBackScan(Integer backScan) {
        this.backScan = backScan;
    }
}
