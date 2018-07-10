package com.enn.vo.energy.app.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * 登录请求参数
 */
@ApiModel("登录请求参数实体")
public class LoginReq {
    @ApiModelProperty(value="用户名",name="userName",example="daliyuan")
    @NotBlank(message="userName 不能为空！")
    private String userName;

    @ApiModelProperty(value="密码",name="password",example="daliyuan")
    @NotBlank(message="password 不能为空！")
    private String password;

    @ApiModelProperty(value="设备id",name="equipment",example="08f9045a9b16436194c63b449dbf28b1")
    @NotBlank(message="equipment 不能为空！")
    @Length(min=32, max=64, message = "设备标识长度限制在32-64")
    private String equipment;

    @ApiModelProperty(value="设备类型",name="equipType",example="ios")
    @NotBlank(message="equipType 不能为空！")
    @Pattern(regexp = "(?i)^ios$|^android$", message = "设备类型不正确")
    private String equipType;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getEquipType() {
        return equipType;
    }

    public void setEquipType(String equipType) {
        this.equipType = equipType;
    }
}
