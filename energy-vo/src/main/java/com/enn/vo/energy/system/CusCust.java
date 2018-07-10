package com.enn.vo.energy.system;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 客户基本信息表 tag：CustomerInfo
 * </p>
 *
 * @author kongweiteng
 * @since 2018-05-29
 */
@TableName("cus_cust")
@ApiModel(value = "cus_cust")
public class CusCust extends Model<CusCust> {

    //public static final long serialVersionUID = 1L;

    /**
     * 客户ID；自增长
     */
	public String id;
	@TableField("agent_id")
    @ApiModelProperty(value = "", example = "1")
	public String agentId;
    /**
     * 用户编号
     */
	@TableField("c_cons")
    @ApiModelProperty(value = "用户编号", example = "1")
	public String cCons;
    /**
     * 客户代码；规则：CONS + 客户ID（ID格式化成12位，前面补0）
     */
	@TableField("cus_no")
    @ApiModelProperty(value = "客户代码；规则：CONS + 客户ID（ID格式化成12位，前面补0）", example = "1")
	public String cusNo;
    /**
     * 客户名称
     */
	public String name;
    /**
     * 客户简称
     */
	@TableField("short_name")
    @ApiModelProperty(value = "客户简称", example = "1")
	public String shortName;
    /**
     * 所在区域id
     */
	@TableField("area_id")
    @ApiModelProperty(value = "所在区域id", example = "1")
	public String areaId;
    /**
     * 省码 ；行政字典表
     */
	@TableField("province_code")
    @ApiModelProperty(value = "省码 ；行政字典表", example = "1")
	public String provinceCode;
	@TableField("office_id")
    @ApiModelProperty(value = "", example = "1")
	public String officeId;
	@TableField("is_master")
    @ApiModelProperty(value = "", example = "1")
	public String isMaster;
    /**
     * 市码；行政字典表
     */
	@TableField("city_code")
    @ApiModelProperty(value = "市码；行政字典表", example = "1")
	public String cityCode;
    /**
     * 市码；行政字典表
     */
	@TableField("district_code")
    @ApiModelProperty(value = "市码；行政字典表", example = "1")
	public String districtCode;
    /**
     * 企业地址
     */
	public String address;
    /**
     * 法人代表
     */
	@TableField("legal_person")
    @ApiModelProperty(value = "法人代表", example = "1")
	public String legalPerson;
    /**
     * 经营范围
     */
	@TableField("bus_scope")
    @ApiModelProperty(value = "经营范围", example = "1")
	public String busScope;
    /**
     * 客户状态；标准代码 custStatus ，正常/破产
     */
	public String status;
    /**
     * 签约状态
     */
	@TableField("contract_status")
    @ApiModelProperty(value = "签约状态", example = "1")
	public String contractStatus;
	public String industry;
	@TableField("register_no")
    @ApiModelProperty(value = "", example = "1")
	public String registerNo;
	@TableField("reg_capital")
    @ApiModelProperty(value = "", example = "1")
	public BigDecimal regCapital;
    /**
     * 联系人
     */
	@TableField("contact_person")
    @ApiModelProperty(value = "联系人", example = "1")
	public String contactPerson;
	public String telephone;
	public String fax;
    /**
     * 企业类型 ；标准代码
     */
	@TableField("bus_type")
    @ApiModelProperty(value = "企业类型 ；标准代码", example = "1")
	public String busType;
	@TableField("bus_scale")
    @ApiModelProperty(value = "", example = "1")
	public String busScale;
	@TableField("bus_detail")
    @ApiModelProperty(value = "", example = "1")
	public String busDetail;
    /**
     * 企业性质：标准代码
     */
	@TableField("bus_property")
    @ApiModelProperty(value = "企业性质：标准代码", example = "1")
	public String busProperty;
    /**
     * 营业执照
     */
	@TableField("bus_licence")
    @ApiModelProperty(value = "营业执照", example = "1")
	public String busLicence;
    /**
     * 主要原料
     */
	public String material;
	public String prodect;
	@TableField("pur_channel")
    @ApiModelProperty(value = "", example = "1")
	public String purChannel;
	@TableField("sale_channel")
    @ApiModelProperty(value = "", example = "1")
	public String saleChannel;
	@TableField("create_by")
    @ApiModelProperty(value = "", example = "1")
	public String createBy;
	@TableField("create_date")
    @ApiModelProperty(value = "", example = "1")
	public Date createDate;
	@TableField("update_by")
    @ApiModelProperty(value = "", example = "1")
	public String updateBy;
    /**
     * 更新时间
     */
	@TableField("update_date")
    @ApiModelProperty(value = "更新时间", example = "1")
	public Date updateDate;
    /**
     * 备注
     */
	public String remarks;
	@TableField("del_flag")
    @ApiModelProperty(value = "", example = "1")
	public String delFlag;
    /**
     * 是否监控
     */
	@TableField("is_monitor")
    @ApiModelProperty(value = "是否监控", example = "1")
	public String isMonitor;
    /**
     * 代理商合同id
     */
	@TableField("agent_contract_id")
    @ApiModelProperty(value = "代理商合同id", example = "1")
	public String agentContractId;
	@TableField("credit_no")
    @ApiModelProperty(value = "", example = "1")
	public String creditNo;
	@TableField("power_wiring_diagram")
    @ApiModelProperty(value = "", example = "1")
	public String powerWiringDiagram;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getcCons() {
		return cCons;
	}

	public void setcCons(String cCons) {
		this.cCons = cCons;
	}

	public String getCusNo() {
		return cusNo;
	}

	public void setCusNo(String cusNo) {
		this.cusNo = cusNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(String isMaster) {
		this.isMaster = isMaster;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getBusScope() {
		return busScope;
	}

	public void setBusScope(String busScope) {
		this.busScope = busScope;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getRegisterNo() {
		return registerNo;
	}

	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}

	public BigDecimal getRegCapital() {
		return regCapital;
	}

	public void setRegCapital(BigDecimal regCapital) {
		this.regCapital = regCapital;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public String getBusScale() {
		return busScale;
	}

	public void setBusScale(String busScale) {
		this.busScale = busScale;
	}

	public String getBusDetail() {
		return busDetail;
	}

	public void setBusDetail(String busDetail) {
		this.busDetail = busDetail;
	}

	public String getBusProperty() {
		return busProperty;
	}

	public void setBusProperty(String busProperty) {
		this.busProperty = busProperty;
	}

	public String getBusLicence() {
		return busLicence;
	}

	public void setBusLicence(String busLicence) {
		this.busLicence = busLicence;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getProdect() {
		return prodect;
	}

	public void setProdect(String prodect) {
		this.prodect = prodect;
	}

	public String getPurChannel() {
		return purChannel;
	}

	public void setPurChannel(String purChannel) {
		this.purChannel = purChannel;
	}

	public String getSaleChannel() {
		return saleChannel;
	}

	public void setSaleChannel(String saleChannel) {
		this.saleChannel = saleChannel;
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

	public String getIsMonitor() {
		return isMonitor;
	}

	public void setIsMonitor(String isMonitor) {
		this.isMonitor = isMonitor;
	}

	public String getAgentContractId() {
		return agentContractId;
	}

	public void setAgentContractId(String agentContractId) {
		this.agentContractId = agentContractId;
	}

	public String getCreditNo() {
		return creditNo;
	}

	public void setCreditNo(String creditNo) {
		this.creditNo = creditNo;
	}

	public String getPowerWiringDiagram() {
		return powerWiringDiagram;
	}

	public void setPowerWiringDiagram(String powerWiringDiagram) {
		this.powerWiringDiagram = powerWiringDiagram;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CusCust{" +
			"id=" + id +
			", agentId=" + agentId +
			", cCons=" + cCons +
			", cusNo=" + cusNo +
			", name=" + name +
			", shortName=" + shortName +
			", areaId=" + areaId +
			", provinceCode=" + provinceCode +
			", officeId=" + officeId +
			", isMaster=" + isMaster +
			", cityCode=" + cityCode +
			", districtCode=" + districtCode +
			", address=" + address +
			", legalPerson=" + legalPerson +
			", busScope=" + busScope +
			", status=" + status +
			", contractStatus=" + contractStatus +
			", industry=" + industry +
			", registerNo=" + registerNo +
			", regCapital=" + regCapital +
			", contactPerson=" + contactPerson +
			", telephone=" + telephone +
			", fax=" + fax +
			", busType=" + busType +
			", busScale=" + busScale +
			", busDetail=" + busDetail +
			", busProperty=" + busProperty +
			", busLicence=" + busLicence +
			", material=" + material +
			", prodect=" + prodect +
			", purChannel=" + purChannel +
			", saleChannel=" + saleChannel +
			", createBy=" + createBy +
			", createDate=" + createDate +
			", updateBy=" + updateBy +
			", updateDate=" + updateDate +
			", remarks=" + remarks +
			", delFlag=" + delFlag +
			", isMonitor=" + isMonitor +
			", agentContractId=" + agentContractId +
			", creditNo=" + creditNo +
			", powerWiringDiagram=" + powerWiringDiagram +
			"}";
	}
}
