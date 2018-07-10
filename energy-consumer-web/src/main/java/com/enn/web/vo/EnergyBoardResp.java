package com.enn.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Data
@ApiModel("昨日能源看板返回实体")
public class EnergyBoardResp implements Serializable {
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("日能源总费用")
    private String totalCost;

    @ApiModelProperty("日用电量")
    private String dayEleQuantity;

    @ApiModelProperty("日用电量环比")
    private String dayEleQuantityRatio;


    @ApiModelProperty("日用汽量")
    private String daySeatQuantity;

    @ApiModelProperty("日用汽量环比")
    private String daySeatQuantityRatio;

    @ApiModelProperty("电日均量")
    private String dayEleAveQuantity;

    @ApiModelProperty("电日偏差")
    private String dayEleDeviation;

    @ApiModelProperty("电警告灯")
    private Boolean dayEleAlarm =true;

    @ApiModelProperty("电警告灯上名称")
    private String dayEleAlarmName;

    @ApiModelProperty("汽日均量")
    private String daySeatAveQuantity;

    @ApiModelProperty("汽日偏差")
    private String daySeatDeviation;

    @ApiModelProperty("汽警告灯")
    private Boolean daySeatAlarm=true;


    @ApiModelProperty("汽警告灯上名称")
    private String daySeatAlarmName;

    @ApiModelProperty("是否展开")
    private Boolean open;






}
