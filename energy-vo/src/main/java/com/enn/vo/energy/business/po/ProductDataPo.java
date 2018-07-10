package com.enn.vo.energy.business.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 生产数据实体
 *
 * @Author: 张洪源
 * @Date: 2018-06-05 09:28
 */
@Data
@TableName("product_data")
public class ProductDataPo {

    @ApiModelProperty(value = "主键ID", name = "id")
    @TableId(value="id", type= IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "开始时间", name = "startDate")
    @NotBlank(message = "开始时间不能为空！")
    @Length(min = 19, max = 19, message = "开始时间格式为：yyyy-MM-dd HH:mm:ss！！！")
    @TableField("start_date")
    private String startDate;

    @ApiModelProperty(value = "结束时间", name = "endDate")
    @NotBlank(message = "结束时间不能为空！")
    @Length(min = 19, max = 19, message = "结束时间格式为：yyyy-MM-dd HH:mm:ss！！！")
    @TableField("end_date")
    private String endDate;

    @ApiModelProperty(value = "企业ID", name = "custId")
    @NotNull(message = "企业ID不能为空！")
    @TableField("cust_id")
    private Long custId;

    @ApiModelProperty(value = "车间ID", name = "workshopId")
    @NotNull(message = "车间ID不能为空！")
    @TableField("workshop_id")
    private Long workshopId;

    @ApiModelProperty(value = "生产线ID", name = "lineId")
    @NotNull(message = "生产线ID不能为空！")
    @TableField("line_id")
    private Long lineId;

    @ApiModelProperty(value = "班组ID", name = "teamId")
    @NotNull(message = "班组ID不能为空！")
    @TableField("team_id")
    private Long teamId;

    @ApiModelProperty(value = "产品ID", name = "productId")
    @NotNull(message = "产品ID不能为空！")
    @TableField("product_id")
    private Long productId;

    @ApiModelProperty(value = "数量", name = "number")
    @NotNull(message = "数量不能为空！")
    @TableField("number")
    private BigDecimal number;

    @ApiModelProperty(value = "删除标识", name = "delFlag")
    @TableField("del_flag")
    private Integer delFlag;
}
