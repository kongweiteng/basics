package com.enn.web.vo;


import com.enn.vo.energy.business.resp.SteamDayAve;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel("能源日均量返回实体")
@Data
public class EnergyDayAveResp    implements Serializable {
    private SteamDayAve steam;
    private SteamDayAve elec;
}
