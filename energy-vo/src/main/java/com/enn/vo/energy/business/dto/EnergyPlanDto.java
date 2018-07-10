package com.enn.vo.energy.business.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 能源计划管理  响应Dto实体类
 * @author kai.guo
 *
 */
@Data
public class EnergyPlanDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3598422608836405565L;
	
	
	private Integer id;

    private String energyType;
    
    /**
     * 能源类型描述
     */
    private String energyTypeDesc;

    private String energyProvider;
    
    
    /**
     * 企业id
     */
    private Long custId;

    private String transportationDates;
    
    /**
     * 年度
     */
    private String yearPeriod;

    /**
     * 容量
     */
    private String capacity;
    
	/**
	 * 容量单位
	 */
	private String capacityUnit;

	/**
	 * 合同单价
	 */
    private String contractPrice;
    
	/**
	 * 合同单价单位
	 */
	private String contractPriceUnit;

	/**
	 * 采购量
	 */
    private String yearPurchaseVolume;
    
	/**
	 * 采购量单位
	 */
	private String purchaseUnit;

    private String businessPerson;

    private String businessMobile;

    private String techPerson;

    private String techMobile;

    private String delFlag;

}
