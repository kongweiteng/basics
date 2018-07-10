package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by sl
 * User: sl
 * Date: 2018/6/12
 * Time: 下午4:53
 */
@ApiModel("月能源看板返回")
@Getter
@Setter
public class MonthWorkShopEnergyBoardResp implements Serializable {

    @ApiModelProperty(value="月能源总费用",name="energyTotal",example="1500.00")
    private BigDecimal energyTotal;

    @ApiModelProperty(value="月用电量",name="monthElectricity",example="3000.00")
    private BigDecimal monthElectricity;

    @ApiModelProperty(value="月用汽量",name="monthSteam",example="2000.00")
    private BigDecimal monthSteam;

    @ApiModelProperty(value="上月用电量最高车间/生产线",name="maxMonthElectricity",example="2000.00")
    private BigDecimal maxMonthElectricity;

    @ApiModelProperty(value="上月用电量最高车间/生产线名称",name="maxMonthElectricityName",example="2000.00")
    private String maxMonthElectricityName;

    @ApiModelProperty(value="上月用汽量最高车间/生产线",name="maxMonthSteam",example="1000.00")
    private BigDecimal maxMonthSteam;

    @ApiModelProperty(value="上月用汽量最高车间/生产线名称",name="maxMonthSteamName",example="1000.00")
    private String maxMonthSteamName;

    @ApiModelProperty(value="月用电量同比",name="monthElectricityPercent",example="3.5")
    private BigDecimal monthElectricityPercent;

    @ApiModelProperty(value="月用汽量同比",name="monthSteamPercent",example="3.5")
    private BigDecimal monthSteamPercent;

    @ApiModelProperty(value="核算单元id",name="unitId",example="1")
    private Integer unitId;

    @ApiModelProperty(value="车间用汽费用",name="steamFees",example="1.00", hidden = true)
    private BigDecimal steamFees;

    @ApiModelProperty(value="车间用电费用",name="electricityFees",example="1.00", hidden = true)
    private BigDecimal electricityFees;

    @ApiModelProperty(value="车间生产线是否有表(true: 有；false：无)",name="electricFlag",example="true")
    private boolean electricFlag;

    @ApiModelProperty(value="车间生产线是否有表(true: 有；false：无)",name="steamFlag",example="true")
    private boolean steamFlag;

    @ApiModelProperty(value="车间名称",name="workShopName",example="车间")
    private String workShopName;


}
