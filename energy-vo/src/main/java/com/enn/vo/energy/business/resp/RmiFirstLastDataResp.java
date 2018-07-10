package com.enn.vo.energy.business.resp;

/**
 * 返回第一条和最后一条实体
 */
public class RmiFirstLastDataResp {
    private DataResp first;
    private DataResp last;

    public DataResp getFirst() {
        return first;
    }

    public void setFirst(DataResp first) {
        this.first = first;
    }

    public DataResp getLast() {
        return last;
    }

    public void setLast(DataResp last) {
        this.last = last;
    }
}
