package com.enn.web.vo;

import com.enn.vo.energy.business.resp.YesterdayBoardUnitResp;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@ApiModel("昨日看板车间用能明细返回实体")
@Getter
@Setter
public class UnitEnergyInfoResp   implements Serializable {
    private String electricTitle;
    private String steamTitle;
    private List<YesterdayBoardUnitResp> electric;
    private List<YesterdayBoardUnitResp> steam;
}
