package com.enn.vo.energy.business.resp;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel("昨日看板车间用能明细返回实体")
@Getter
@Setter
public class YesterdayBoardUnitResp  implements Serializable {
    private String name;
    private BigDecimal useQuantity;
    private BigDecimal fees;
}
