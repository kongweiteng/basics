package com.enn.web.vo;

import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用电量变化曲线返回实体
 *
 * @Author: 张洪源
 * @Date: 2018-06-13 09:38
 */
@Data
@ApiModel("用电量变化曲线")
public class CustElectricCountLineResp {

    @ApiModelProperty("生产线名称")
    private String name;

    @ApiModelProperty("曲线数据")
    private ListResp<RmiSamplDataResp> list;
}
