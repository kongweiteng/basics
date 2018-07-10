package com.enn.web.vo;


import com.enn.vo.energy.business.resp.DataResp;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Data
@ApiModel("昨日能源看板公路曲线图返回")
public class EnergyBoardLineResp implements Serializable {
    private String name;
    private String equipId;
    private List<DataResp> data;

}
