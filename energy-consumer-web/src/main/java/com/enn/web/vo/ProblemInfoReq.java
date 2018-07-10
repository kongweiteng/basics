package com.enn.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

@ApiModel("专家诊断信息参数")
public class ProblemInfoReq {

    @ApiModelProperty("客户邮箱")
    @NotBlank(message = "邮箱不能为空！")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$",message = "邮箱不符合规则")
    private String email;

    @ApiModelProperty("客户手机号")
    @NotBlank(message="手机号不能为空！")
    @Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}" , message = "手机号格式不正确")
    private String phone;

    @ApiModelProperty("信息")
    @NotBlank(message = "信息不能为空！")
    private String problem;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }
}