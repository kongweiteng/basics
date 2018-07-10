package com.enn.web.vo;

import com.enn.vo.energy.business.resp.SteamDayAve;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("能源日用量返回实体")
@Data
public class EnergyDayResp {
    private EnergyQuantityAndFee steam;
    private EnergyQuantityAndFee elec;
}
