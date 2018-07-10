package com.enn.vo.energy.app.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@ApiModel("登出请求实体")
public class Logout {

    @ApiModelProperty(value = "token令牌",name="token",example="08f9045a9b16436194c63b449dbf28b1")
    @NotBlank(message="token 不能为空！")
    private String token;

    @ApiModelProperty(value="设备id",name="equipment",example="08f9045a9b16436194c63b449dbf28b1")
    @NotBlank(message="equipment 不能为空！")
    @Length(min=32, max=64, message = "设备标识长度限制在32-64")
    private String equipment;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }
}
