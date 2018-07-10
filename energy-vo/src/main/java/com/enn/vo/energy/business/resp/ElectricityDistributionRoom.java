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
import java.util.List;

/**
 * electricity_distribution_room配电室信息
 *
 */
@Getter
@Setter
@ApiModel("electricity_distribution_room配电室信息")
@TableName("electricity_distribution_room")
public class ElectricityDistributionRoom extends Model<ElectricityDistributionRoom> {

	@ApiModelProperty("配电室ID")
	@TableField("id")
	private Long id;
	@ApiModelProperty("配电室名称")
	@TableField("name")
	private String name;
	@ApiModelProperty("上级配电室")
	@TableField("parent_id")
	private Long parentId;
	@ApiModelProperty("归属企业ID")
	@TableField("cust_id")
	private String custId;
	@ApiModelProperty("电压等级")
	@TableField("voltage_level")
	private String voltageLevel;
	@ApiModelProperty("删除状态")
	@TableField("del_flag")
	@TableLogic
	private String flag;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	@Override
	public String toString() {
		return "ElectricityDistributionRoom{" +
				"id=" + id +
				", name='" + name + '\'' +
				", parentId=" + parentId +
				", custId=" + custId +
				", voltageLevel='" + voltageLevel + '\'' +
				'}';
	}
}
