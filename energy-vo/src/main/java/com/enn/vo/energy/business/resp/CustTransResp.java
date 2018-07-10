package com.enn.vo.energy.business.resp;


import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ApiModel("cust_trans变压器信息")
@TableName("cust_trans")
public class CustTransResp extends Model<CustTransResp> {
	@ApiModelProperty("变压器id")
	@TableField("id")
	private Long id;
	@ApiModelProperty("变压器名称")
	@TableField("name")
	private String name;
	@ApiModelProperty("铭牌容量")
	@TableField("nameplate_capacity")
	private BigDecimal nameplateCapacity;
	@ApiModelProperty("归属的配电室")
	@TableField("distribution_id")
	private Long distributionId;
	@ApiModelProperty("安装日期")
	@TableField("install_time")
	private String installTime;
	@ApiModelProperty("一次侧电压")
	@TableField("first_voltage")
	private String firstVoltage;
	@ApiModelProperty("二次侧电压")
	@TableField("second_voltage")
	private String secondVoltage;
	@ApiModelProperty("最大额定功率")
	@TableField("rated_power")
	private BigDecimal ratedPower;
	@ApiModelProperty()
	@TableField("del_flag")
	@TableLogic
	private String delFlag;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CustTransResp{" +
				"id=" + id +
				", name='" + name + '\'' +
				", nameplateCapacity=" + nameplateCapacity +
				", distributionId=" + distributionId +
				", installTime='" + installTime + '\'' +
				", firstVoltage='" + firstVoltage + '\'' +
				", secondVoltage='" + secondVoltage + '\'' +
				", ratedPower=" + ratedPower +
				", delFlag='" + delFlag + '\'' +
				'}';
	}
}
