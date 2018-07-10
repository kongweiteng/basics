package com.enn.vo.energy.business.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 基础参数类
 * @author kai.guo
 *
 */
@Data
public class Base implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8466822314486433187L;


	/**
	 * 分页默认每页显示数据量
	 */
	public static final Integer DEFAULT_PAGE_SIZE=20;
	
	
	/**
	 * 分页默认显示页数
	 */
	public static final Integer DEFAULT_PAGE_NUM=1;
	

	private Integer pageSize;
	
	private Integer pageNum;
	

}
