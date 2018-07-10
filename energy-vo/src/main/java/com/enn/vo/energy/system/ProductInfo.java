package com.enn.vo.energy.system;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@TableName("product_info")
@ApiModel(value = "product_info")
public class ProductInfo implements Serializable {
    private Long id;//ID
    @TableField("name")
    @ApiModelProperty(value = "名称", example = "1")
    private String name;//名称
    @TableField("specification")
    @ApiModelProperty(value = "规格", example = "1")
    private String specification;//规格
    @TableField("cust_id")
    @ApiModelProperty(value = "企业用户id", example = "1")
    @NotNull(message = "custId 不能为空")
    private Long custId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }
}
