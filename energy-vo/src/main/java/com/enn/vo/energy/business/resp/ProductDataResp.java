package com.enn.vo.energy.business.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 生产数据返回实体
 *
 * @Author: 张洪源
 * @Date: 2018-06-05 10:22
 */
@Data
@ApiModel("生产数据")
public class ProductDataResp {

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private Long id;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date startDate;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endDate;

    /**
     * 车间ID
     */
    @ApiModelProperty("车间ID")
    private Long workshopId;

    /**
     * 车间名称
     */
    @ApiModelProperty("车间名称")
    private String workshopName;

    /**
     * 生产线ID
     */
    @ApiModelProperty("生产线ID")
    private Long lineId;

    /**
     * 生产线名称
     */
    @ApiModelProperty("生产线名称")
    private String lineName;

    /**
     * 班组ID
     */
    @ApiModelProperty("班组ID")
    private Long teamId;

    /**
     * 班组名称
     */
    @ApiModelProperty("班组名称")
    private String teamName;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private String monitor;

    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    private Long productId;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private BigDecimal number;
    
    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String specification;
}
