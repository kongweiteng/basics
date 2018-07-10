package com.enn.vo.energy.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import java.math.BigInteger;

/**
 *企业ID
 */
@Setter
@Getter
@ApiModel("根据企业信息请求实体")
public class CustReq {

	@ApiModelProperty("企业ID")
	@NotBlank( message = "企业ID不能为空")
	@Min(value = 0,message = "企业ID是不小于0的整数")
	private String custID;

}
