package com.enn.vo.energy.business.bo;

import java.util.Date;
import java.util.List;

import com.enn.vo.energy.business.vo.Base;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 能源计划管理service层BO实体类
 * @author kai.guo
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class EnergyPlanBo  extends Base  {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8101110241139719277L;

    /**
     * 能源计划主键id
     */
    private Long id;

	/**
	 * 能源类型
	 */
	private String energyType;
	
	/**
	 * 能源类型集合
	 */
	private List<String> energyTypes;
	
	/**
	 * 企业id
	 */
    private Long custId;

    /**
     * 查询年度
     */
    private Date transportationDate;
    
    /**
     * 年度
     */
    private String yearPeriod;
    

    /**
     * 能源提供商
     */
    private String energyProvider;

    /**
     * 容量
     */
    private String capacity;

    /**
     * 合同价格
     */
    private String contractPrice;
    
    /**
     * 本年度采购量
     */
    private String yearPurchaseVolume;

    /**
     * 商务联系人
     */
    private String businessPerson;

    /**
     * 商务联系人联系方式
     */
    private String businessMobile;

    /**
     * 技术联系人
     */
    private String techPerson;

    /**
     * 技术联系人联系方式
     */
    private String techMobile;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;
    
}
