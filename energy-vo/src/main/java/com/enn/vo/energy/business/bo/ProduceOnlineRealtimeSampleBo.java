package com.enn.vo.energy.business.bo;

import java.io.Serializable;
import java.util.List;

import com.enn.vo.energy.business.req.Equip;

import lombok.Data;


/**
* @author kai.guo
* @version 创建时间：2018年6月6日 下午2:42:22
* @Description 生产在线实时采样BO实体
*/
@Data
public class ProduceOnlineRealtimeSampleBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2221055428162715645L;
	
	
	/**
	 * 测点
	 */
	private  String metric;
	
	/**
	 * 设备列表
	 */
	private List<Equip> equips;

	/**
	 * 设备列表
	 */
//    private List<String> equipID;

    /**
     * 设备类型
     */
//    private String equipMK;

    /**
     * 站点
     */
//    private String staId;

}
