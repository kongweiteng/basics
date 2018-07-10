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

/**
 * electricity_distribution_room配电室信息
 *
 */
@Getter
@Setter
@ApiModel("electricity_distribution_room配电室信息")
public class ElectricityDistributionRoomResp {

	@ApiModelProperty("配电室ID")
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
	@ApiModelProperty("设备ID")
	private String equipID;
	@ApiModelProperty("采集数据的集合")
	private RmiSamplDataResp rmiSamplDataRespList;



}
