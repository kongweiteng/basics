package com.enn.vo.energy.business.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 产品单耗请求实体
 *
 */
@Data
public class ProduceCurveListReq {

    @ApiModelProperty(value="根据生产线查负荷曲线请求实体")
    @NotEmpty(message="生产线信息不能为空！")
    private List<ProduceCurveReq> produceCurveReq;

}