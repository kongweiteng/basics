package com.enn.vo.energy.user;

import com.enn.vo.energy.app.login.AccRole;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class EnnBossCrm {

    private boolean mainAccount;
    private boolean entExit;
    private UserInfoResp userInfo;
    private EntInfo ent;
    private List<AccRole> roleList;

}
