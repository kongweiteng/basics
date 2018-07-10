package com.enn.vo.energy.business;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 客户联系信息表，（基本信息表同客户联系表是1对多关系） tag：ConsContact
 * </p>
 *
 * @author kongweiteng
 * @since 2018-05-30
 */
@TableName("cus_contact")
@ApiModel(value = "cus_contact")
public class CusContact extends Model<CusContact> {

    //public static final long serialVersionUID = 1L;

    /**
     * 自增长
     */
	private String id;
    /**
     * 客户ID；同客户基本信息表关联字段
     */
	@TableField("cus_id")
    @ApiModelProperty(value = "客户ID；同客户基本信息表关联字段", example = "1")
	private String cusId;
    /**
     * 联系类型，标准代码 
     */
	private String type;
    /**
     * 联系人姓名
     */
	private String name;
    /**
     * 性别；标准代码 gender
     */
	private String gender;
    /**
     * 移动电话
     */
	private String telephone;
    /**
     * 移动电话
     */
	private String mobile;
    /**
     * 电子邮箱
     */
	private String email;
	private String weixin;
	private String qq;
	@TableField("create_by")
    @ApiModelProperty(value = "", example = "1")
	private String createBy;
	@TableField("create_date")
    @ApiModelProperty(value = "", example = "1")
	private Date createDate;
	@TableField("update_by")
    @ApiModelProperty(value = "", example = "1")
	private String updateBy;
    /**
     * 更新时间
     */
	@TableField("update_date")
    @ApiModelProperty(value = "更新时间", example = "1")
	private Date updateDate;
    /**
     * 联系备注
     */
	private String remarks;
	@TableField("del_flag")
    @ApiModelProperty(value = "", example = "1")
	private String delFlag;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
		return "CusContact{" +
			"id=" + id +
			", cusId=" + cusId +
			", type=" + type +
			", name=" + name +
			", gender=" + gender +
			", telephone=" + telephone +
			", mobile=" + mobile +
			", email=" + email +
			", weixin=" + weixin +
			", qq=" + qq +
			", createBy=" + createBy +
			", createDate=" + createDate +
			", updateBy=" + updateBy +
			", updateDate=" + updateDate +
			", remarks=" + remarks +
			", delFlag=" + delFlag +
			"}";
	}
}
