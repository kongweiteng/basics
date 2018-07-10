package com.enn.vo.energy.business.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.enn.vo.energy.passage.resp.SteamFeesCalculateResp;

import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月14日 下午8:17:24
* @Description 类描述
*/
@Data
public class SteamMeterReadingMinuteFeesBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3895270640568021647L;
	
	private String meterNo;
	
	BigDecimal totalSteamFees=BigDecimal.ZERO;
	
	List<SteamFeesCalculateResp> meterMinuteFees;
	
}
