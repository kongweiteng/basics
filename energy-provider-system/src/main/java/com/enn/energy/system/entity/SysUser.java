package com.enn.energy.system.entity;

import lombok.Data;

@Data
public class SysUser {

    private Long id;
    private String openId;
    private Long companyId;
    private Long officeId;
}
