package com.enn.vo.energy.business.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Setter
@Getter
@ApiModel("获取负荷率实体")
public class RealTimeLoadRateReq {

	@ApiModelProperty("企业ID")
	@NotBlank( message = "企业ID不能为空")
	private String custID;
	@ApiModelProperty("全厂负荷标准")
	@NotBlank(message = "全厂负荷标准不能为空")
	private String sumRatedPower;

	public String getCustID() {
		return custID;
	}

	public void setCustID(String custID) {
		this.custID = custID;
	}

	public String getSumRatedPower() {
		return sumRatedPower;
	}

	public void setSumRatedPower(String sumRatedPower) {
		this.sumRatedPower = sumRatedPower;
	}
}
