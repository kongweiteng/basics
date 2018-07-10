package com.enn.vo.energy.business.po;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author kongweiteng
 * @since 2018-06-06
 */
@TableName("energy_equip")
@ApiModel(value = "energy_equip")
public class EnergyEquip extends Model<EnergyEquip> {

    
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 设备名称
     */
	@TableField("equip_name")
    @ApiModelProperty(value = "设备名称", example = "1")
	private String equipName;
    /**
     * 设备规格
     */
	private String specification;
    /**
     * 额定参数
     */
	@TableField("rated_param")
    @ApiModelProperty(value = "额定参数", example = "1")
	private BigDecimal ratedParam;
    /**
     * 设备类型
     */
	@TableField("equip_type")
    @ApiModelProperty(value = "设备类型", example = "1")
	private String equipType;
    /**
     * 归属核算单元id
     */
	@TableField("accounting_id")
    @ApiModelProperty(value = "归属核算单元id", example = "1")
	private Long accountingId;
	@TableField("create_by")
    @ApiModelProperty(value = "", example = "1")
	private String createBy;
	@TableField("create_date")
    @ApiModelProperty(value = "", example = "1")
	private Date createDate;
	@TableField("update_by")
    @ApiModelProperty(value = "", example = "1")
	private String updateBy;
	@TableField("update_date")
    @ApiModelProperty(value = "", example = "1")
	private Date updateDate;
	@TableField("del_flag")
    @ApiModelProperty(value = "", example = "1")
	private String delFlag;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public BigDecimal getRatedParam() {
		return ratedParam;
	}

	public void setRatedParam(BigDecimal ratedParam) {
		this.ratedParam = ratedParam;
	}

	public String getEquipType() {
		return equipType;
	}

	public void setEquipType(String equipType) {
		this.equipType = equipType;
	}

	public Long getAccountingId() {
		return accountingId;
	}

	public void setAccountingId(Long accountingId) {
		this.accountingId = accountingId;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "EnergyEquip{" +
			"id=" + id +
			", equipName=" + equipName +
			", specification=" + specification +
			", ratedParam=" + ratedParam +
			", equipType=" + equipType +
			", accountingId=" + accountingId +
			", createBy=" + createBy +
			", createDate=" + createDate +
			", updateBy=" + updateBy +
			", updateDate=" + updateDate +
			", delFlag=" + delFlag +
			"}";
	}
}
