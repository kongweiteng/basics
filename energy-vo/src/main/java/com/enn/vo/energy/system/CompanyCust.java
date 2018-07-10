package com.enn.vo.energy.system;

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
 * @since 2018-06-05
 */
@TableName("company_cust")
@ApiModel(value = "company_cust")
public class CompanyCust extends Model<CompanyCust> {

    
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 企业关联id（关联平台）
     */
	@TableField("relation_id")
    @ApiModelProperty(value = "企业关联id（关联平台）", example = "1")
	private String relationId;
    /**
     * 企业用户名称
     */
	@TableField("company_name")
    @ApiModelProperty(value = "企业用户名称", example = "1")
	private String companyName;
    /**
     * 企业用户属性
     */
	@TableField("company_attribute")
    @ApiModelProperty(value = "企业用户属性", example = "1")
	private String companyAttribute;
    /**
     * 行业
     */
	private String industry;
    /**
     * 归属区域
     */
	@TableField("area_id")
    @ApiModelProperty(value = "归属区域", example = "1")
	private Long areaId;
    /**
     * 日核算开始时间
     */
	@TableField("day_start")
    @ApiModelProperty(value = "日核算开始时间", example = "1")
	private String dayStart;
    /**
     * 日核算结束时间
     */
	@TableField("day_end")
    @ApiModelProperty(value = "日核算结束时间", example = "1")
	private String dayEnd;
    /**
     * 月核算开始时间
     */
	@TableField("month_start")
    @ApiModelProperty(value = "月核算开始时间", example = "1")
	private String monthStart;
    /**
     * 月核算结束时间
     */
	@TableField("month_end")
    @ApiModelProperty(value = "月核算结束时间", example = "1")
	private String monthEnd;
    /**
     * 年核算开始时间
     */
	@TableField("year_start")
    @ApiModelProperty(value = "年核算开始时间", example = "1")
	private String yearStart;
    /**
     * 年核算结束时间
     */
	@TableField("year_end")
    @ApiModelProperty(value = "年核算结束时间", example = "1")
	private String yearEnd;
    /**
     * 归属的上级企业
     */
	@TableField("parent_id")
    @ApiModelProperty(value = "归属的上级企业", example = "1")
	private Long parentId;
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

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAttribute() {
		return companyAttribute;
	}

	public void setCompanyAttribute(String companyAttribute) {
		this.companyAttribute = companyAttribute;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getDayStart() {
		return dayStart;
	}

	public void setDayStart(String dayStart) {
		this.dayStart = dayStart;
	}

	public String getDayEnd() {
		return dayEnd;
	}

	public void setDayEnd(String dayEnd) {
		this.dayEnd = dayEnd;
	}

	public String getMonthStart() {
		return monthStart;
	}

	public void setMonthStart(String monthStart) {
		this.monthStart = monthStart;
	}

	public String getMonthEnd() {
		return monthEnd;
	}

	public void setMonthEnd(String monthEnd) {
		this.monthEnd = monthEnd;
	}

	public String getYearStart() {
		return yearStart;
	}

	public void setYearStart(String yearStart) {
		this.yearStart = yearStart;
	}

	public String getYearEnd() {
		return yearEnd;
	}

	public void setYearEnd(String yearEnd) {
		this.yearEnd = yearEnd;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
		return "CompanyCust{" +
			"id=" + id +
			", relationId=" + relationId +
			", companyName=" + companyName +
			", companyAttribute=" + companyAttribute +
			", industry=" + industry +
			", areaId=" + areaId +
			", dayStart=" + dayStart +
			", dayEnd=" + dayEnd +
			", monthStart=" + monthStart +
			", monthEnd=" + monthEnd +
			", yearStart=" + yearStart +
			", yearEnd=" + yearEnd +
			", parentId=" + parentId +
			", createBy=" + createBy +
			", createDate=" + createDate +
			", updateBy=" + updateBy +
			", updateDate=" + updateDate +
			", delFlag=" + delFlag +
			"}";
	}
}
