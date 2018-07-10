package com.enn.vo.energy.business.req;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 企业用电量及费用请求实体
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 15:38
 */
@Data
public class ElectricMeterReadingReq {

    /**
     * 开始时间
     */
    @NotBlank(message = "startTime 不能为空！")
    private String startTime;

    /**
     * 结束时间
     */
    @NotBlank(message = "endTime 不能为空！")
    private String endTime;

    /**
     * 表号
     */
    @NotNull(message = "meterNoList 不能为空！")
    private List<String> meterNoList;
}
