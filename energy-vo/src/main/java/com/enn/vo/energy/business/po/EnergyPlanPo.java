package com.enn.vo.energy.business.po;

import java.util.Date;

import lombok.Data;

/**
 * energy_plan 对于的数据层PO实体类
 * @author kai.guo
 *
 */
@Data
public class EnergyPlanPo {
    private Long id;

    private String energyType;

    private String energyProvider;

    private Long custId;

    private Date transportationDate;

    private String yearPeriod;

    private String capacity;

    private String contractPrice;

    private String yearPurchaseVolume;

    private String businessPerson;

    private String businessMobile;

    private String techPerson;

    private String techMobile;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    private String delFlag;

}