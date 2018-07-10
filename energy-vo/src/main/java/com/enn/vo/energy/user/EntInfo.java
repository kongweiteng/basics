package com.enn.vo.energy.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntInfo {

    private String entId;
    private String openid;
    private String entName;
    private String entShortName;
    private String entFaxNum;
    private int applyStatus;
}
