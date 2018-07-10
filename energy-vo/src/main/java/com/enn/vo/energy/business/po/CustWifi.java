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
 * @since 2018-06-13
 */
@TableName("cust_wifi")
@ApiModel(value = "cust_wifi")
public class CustWifi extends Model<CustWifi> {

    
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 采集网段
     */
	@TableField("network_segment")
    @ApiModelProperty(value = "采集网段", example = "1")
	private String networkSegment;
    /**
     * 站地址（网关）
     */
	private String staId;
    /**
     * 状态
     */
	private String status;
    /**
     * 在线时长
     */
	@TableField("online_time")
    @ApiModelProperty(value = "在线时长", example = "1")
	private BigDecimal onlineTime;
    /**
     * 掉线次数统计
     */
	@TableField("drop_line_number")
    @ApiModelProperty(value = "掉线次数统计", example = "1")
	private Integer dropLineNumber;
    /**
     * 最后一次掉线时间
     */
	@TableField("drop_line_time")
    @ApiModelProperty(value = "最后一次掉线时间", example = "1")
	private Date dropLineTime;
    /**
     * 上线时间
     */
	private Date uptime;
    /**
     * 掉线持续时长
     */
	@TableField("drop_length")
    @ApiModelProperty(value = "掉线持续时长", example = "1")
	private BigDecimal dropLength;
    /**
     * 流量统计
     */
	@TableField("flow_statistics")
    @ApiModelProperty(value = "流量统计", example = "1")
	private BigDecimal flowStatistics;
    /**
     * SIM卡号
     */
	@TableField("card_number")
    @ApiModelProperty(value = "SIM卡号", example = "1")
	private String cardNumber;
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

	public String getNetworkSegment() {
		return networkSegment;
	}

	public void setNetworkSegment(String networkSegment) {
		this.networkSegment = networkSegment;
	}

	public String getStaId() {
		return staId;
	}

	public void setStaId(String staId) {
		this.staId = staId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(BigDecimal onlineTime) {
		this.onlineTime = onlineTime;
	}

	public Integer getDropLineNumber() {
		return dropLineNumber;
	}

	public void setDropLineNumber(Integer dropLineNumber) {
		this.dropLineNumber = dropLineNumber;
	}

	public Date getDropLineTime() {
		return dropLineTime;
	}

	public void setDropLineTime(Date dropLineTime) {
		this.dropLineTime = dropLineTime;
	}

	public Date getUptime() {
		return uptime;
	}

	public void setUptime(Date uptime) {
		this.uptime = uptime;
	}

	public BigDecimal getDropLength() {
		return dropLength;
	}

	public void setDropLength(BigDecimal dropLength) {
		this.dropLength = dropLength;
	}

	public BigDecimal getFlowStatistics() {
		return flowStatistics;
	}

	public void setFlowStatistics(BigDecimal flowStatistics) {
		this.flowStatistics = flowStatistics;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
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
		return "CustWifi{" +
			"id=" + id +
			", networkSegment=" + networkSegment +
			", staId=" + staId +
			", status=" + status +
			", onlineTime=" + onlineTime +
			", dropLineNumber=" + dropLineNumber +
			", dropLineTime=" + dropLineTime +
			", uptime=" + uptime +
			", dropLength=" + dropLength +
			", flowStatistics=" + flowStatistics +
			", cardNumber=" + cardNumber +
			", createBy=" + createBy +
			", createDate=" + createDate +
			", updateBy=" + updateBy +
			", updateDate=" + updateDate +
			", delFlag=" + delFlag +
			"}";
	}
}
