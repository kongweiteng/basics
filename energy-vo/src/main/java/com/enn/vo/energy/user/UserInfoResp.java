package com.enn.vo.energy.user;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户信息
 */
@Getter
@Setter
public class UserInfoResp {

    private String ticket;
    private String loginname;
    private String openid;
    private String mobile;
    private String email;
    private String realName;
}
