package com.enn.vo.energy.business.req;

import java.util.List;

/**
 * 请求opentsdb的请求参数
 */
public class OpentsdbReq {
    private String userKey;
    private String dataSource;
    private long start;
    private long end;
    private List<QueriesReq> queries;
    /*
     * 时区
     */
    private String timezone ;
    private boolean useCalendar ;

    public boolean isUseCalendar() {
        return useCalendar;
    }

    public void setUseCalendar(boolean useCalendar) {
        this.useCalendar = useCalendar;
    }

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

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public List<QueriesReq> getQueries() {
        return queries;
    }

    public void setQueries(List<QueriesReq> queries) {
        this.queries = queries;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
