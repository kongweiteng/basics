package com.enn.vo.energy.business.resp;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.enn.vo.energy.business.po.DataResp;

import java.util.List;
@Data
@ApiModel("尖峰平谷，总量的集合")
public class MeterReadingResp {

	@ApiModelProperty("尖时段")
	private List<DataResp> tipList;
	@ApiModelProperty("峰时段")
	private List<DataResp> peakList;
	@ApiModelProperty("平时段")
	private List<DataResp> flatList;
	@ApiModelProperty("谷时段")
	private List<DataResp> valleyList;
	@ApiModelProperty("总电量")
	private List<DataResp> sumList;

}
