package com.enn.vo.energy.business.po;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 班组信息
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 11:10
 */
@Data
@TableName("team_info")
public class TeamInfoPo {

    @ApiModelProperty(value = "主键ID", name = "id")
    @TableField("id")
    private Long id;

    @ApiModelProperty(value = "班组名称", name = "name")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "负责人", name = "monitor")
    @TableField("monitor")
    private String monitor;

    @ApiModelProperty(value = "生产线ID", name = "lineId")
    @TableField("line_id")
    private Long lineId;
}
