package com.enn.vo.energy.business.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 能源字典类型
 * @author kai.guo
 *
 */
@Data
public class EnergyTypeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5869665240163018308L;
	
	private String value;
	
    private String label;
    
	/**
	 * 单价单位
	 */
	private String priceUnit;
	
	/**
	 * 容量单位
	 */
	private String capacityUnit;
	
	/**
	 * 采购量单位
	 */
	private String purchaseUnit;
    
}
