package com.enn.vo.energy.business.bo;

import java.io.Serializable;
import java.util.List;

import com.enn.vo.energy.business.req.Equip;

import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月6日 下午2:32:51
* @Description 生产在线监测 采样BO实体
*/
@Data
public class ProduceOnlineMonitorSampleBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8791970459474533180L;
	
	/**
	 * 采样开始时间
	 */
	private String start;

	/**
	 * 采样结束时间
	 */
    private String end;

    /**
     * 采样格式
     */
    private String downsample;


    /**
     * 测点
     */
    private String metric;
    
    
    /**
     * 设备对象
     */
    private List<Equip> equips;
    
}
