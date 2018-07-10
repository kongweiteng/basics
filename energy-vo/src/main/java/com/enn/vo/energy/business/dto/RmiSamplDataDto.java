package com.enn.vo.energy.business.dto;

import java.util.List;

import com.enn.vo.energy.business.resp.DataResp;

import lombok.Data;

/**
* @author kai.guo
* @version 创建时间：2018年6月19日 下午12:44:24
* @Description 类描述
*/
@Data
public class RmiSamplDataDto {
	
	private String metric;

    private String equipID;

    private String equipMK;
    
    private String equipName;

    private String staId;

    private List<DataResp> dataResp;

}
