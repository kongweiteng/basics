package com.enn.vo.energy.business.req;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ElectricMeterNosReq {

    /**
     * 表号
     */
    @NotNull(message = "meterNoList 不能为空！")
    private List<String> meterNoList;

    public List<String> getMeterNoList() {
        return meterNoList;
    }

    public void setMeterNoList(List<String> meterNoList) {
        this.meterNoList = meterNoList;
    }
}
