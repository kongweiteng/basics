package com.enn.web.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("图线返回数据")
public class LineResp {
    private List<EnergyBoardLineResp> seatStatistics;

    private String tile;
}
