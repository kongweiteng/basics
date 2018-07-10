package com.enn.vo.energy.business.po;


import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel("cust_meter计量仪表信息")
@TableName("cust_meter")
public class CustMeter extends Model<CustMeter> {

	@ApiModelProperty("计量仪表id")
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
	@ApiModelProperty("能源类型")
	@TableField("energy_type")
	private String energyType;
	@ApiModelProperty("计量级别")
	@TableField("level")
	private String level;
	@ApiModelProperty("安装位置")
	@TableField("distribution_id")
	private Long distributionId;
	@ApiModelProperty("回路编号")
	@TableField("loop_number")
	private String loopNumber;
	@ApiModelProperty("设备名称")
	@TableField("name")
	private String name;
	@ApiModelProperty("型号")
	@TableField("model")
	private String model;
	@ApiModelProperty("倍率")
	@TableField("rate")
	private Integer rate;
	@ApiModelProperty("归属的无线模块id")
	@TableField("wifi_id")
	@TableLogic
	private Long wifiId;
	@ApiModelProperty("站地址")
	@TableField("station")
	private String station;
	@ApiModelProperty("属性")
	@TableField("property")
	private String property;
	@ApiModelProperty("上级计量点")
	@TableField("parent_id")
	private Long parentId;


	@ApiModelProperty("是否参数核算表")
	@TableField("is_accoun")
	private String isAccoun;

	@ApiModelProperty("关联核算单元id")
	@TableField("accounting_id")
	private Long accountingId;


	@ApiModelProperty("del_flag")
	@TableField("del_flag")
	private String delFlag;


	@Override
	protected Serializable pkVal() {
		return this.id=id;
	}


}
