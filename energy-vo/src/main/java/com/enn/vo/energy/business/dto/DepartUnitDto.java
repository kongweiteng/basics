package com.enn.vo.energy.business.dto;

import java.io.Serializable;

import lombok.Data;


/**
* @author kai.guo
* @version 创建时间：2018年6月7日 下午4:12:01
* @Description 类描述
*/
@Data
public class DepartUnitDto implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3355100994640944619L;
	/**
	 * 车间id
	 */
	private Long id;
    /**
     * 核算单元名称
     */
	private String name;
	/**
	 * 核算单元类型
	 */
	private String accountingType;

}
