package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sl
 * User: sl
 * Date: 2018/6/12
 * Time: 下午4:53
 */
@ApiModel("月能源看板返回")
@Getter
@Setter
public class MonthEnergyBoardResp implements Serializable {


    @ApiModelProperty(value="企业名称",name="custName",example="龙辉电镀分公司")
    private String custName;

    @ApiModelProperty(value="月能源总费用",name="energyTotal",example="1500.00")
    private BigDecimal energyTotal;

    @ApiModelProperty(value="月用电总量",name="monthTotalElectricity",example="3000.00")
    private BigDecimal monthTotalElectricity;

    @ApiModelProperty(value="月用汽总量",name="monthTotalSteam",example="2000.00")
    private BigDecimal monthTotalSteam;

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

    @ApiModelProperty(value="车间list",name="workShopList")
    private List<MonthWorkShopEnergyBoardResp> workShopList;

}
