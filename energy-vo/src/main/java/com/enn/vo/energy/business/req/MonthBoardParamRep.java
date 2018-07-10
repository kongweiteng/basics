package com.enn.vo.energy.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by sl
 * User: sl
 * Date: 2018/6/15
 * Time: 下午5:23
 */
@Setter
@Getter
@ApiModel("月能源看板请求参数")
public class MonthBoardParamRep implements Serializable {
    @ApiModelProperty("企业ID")
    @NotBlank( message = "企业ID不能为空")
    private Integer custID;
}