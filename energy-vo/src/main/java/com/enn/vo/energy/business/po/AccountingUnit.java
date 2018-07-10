package com.enn.vo.energy.business.po;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.enums.IdType;
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
@TableName("accounting_unit")
@ApiModel(value = "accounting_unit")
public class AccountingUnit extends Model<AccountingUnit> {

    
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 核算单元名称
     */
	private String name;
    /**
     * 核算单元类型
     */
	@TableField("accounting_type")
    @ApiModelProperty(value = "核算单元类型", example = "1")
	private String accountingType;
    /**
     * 归属的企业
     */
	@TableField("cust_id")
    @ApiModelProperty(value = "归属的企业", example = "1")
	private Long custId;
    /**
     * 上级核算单元id
     */
	@TableField("parent_id")
    @ApiModelProperty(value = "上级核算单元id", example = "1")
	private Long parentId;
    /**
     * 所有上级核算单元id
     */
	@TableField("parent_ids")
    @ApiModelProperty(value = "所有上级核算单元id", example = "1")
	private String parentIds;
    /**
     * 核算公式
     */
	private String formula;
    /**
     * 是否是临时核算单元，0为否，1为是
     */
	@TableField("is_temp")
    @ApiModelProperty(value = "是否是临时核算单元，0为否，1为是", example = "1")
	private String isTemp;
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

	@TableField("is_use")
	@ApiModelProperty(value = "isUse", example = "1")
	private String isUse;

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountingType() {
		return accountingType;
	}

	public void setAccountingType(String accountingType) {
		this.accountingType = accountingType;
	}

	public Long getCustId() {
		return custId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getIsTemp() {
		return isTemp;
	}

	public void setIsTemp(String isTemp) {
		this.isTemp = isTemp;
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
		return "AccountingUnit{" +
			"id=" + id +
			", name=" + name +
			", accountingType=" + accountingType +
			", custId=" + custId +
			", parentId=" + parentId +
			", parentIds=" + parentIds +
			", formula=" + formula +
			", isTemp=" + isTemp +
			", createBy=" + createBy +
			", createDate=" + createDate +
			", updateBy=" + updateBy +
			", updateDate=" + updateDate +
			", delFlag=" + delFlag +
			"}";
	}
}
