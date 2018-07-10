package com.enn.vo.energy.business.req;

import com.enn.vo.energy.business.vo.Base;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 生产数据请求实体
 *
 * @Author: 张洪源
 * @Date: 2018-06-05 11:01
 */
@Data
public class ProductDataReq extends Base {

    @ApiModelProperty(value = "生产日期", name = "time", example = "yyyy-MM-dd")
    private String time;

    @ApiModelProperty(value = "企业ID", name = "custId")
    @NotNull(message = "企业ID不能为空！")
    private Long custId;
}