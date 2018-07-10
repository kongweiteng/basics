package com.enn.vo.energy.business.req;

import com.enn.vo.energy.DefaultReq;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@ApiModel("请求企业下的表")
@Getter
@Setter
public class CompanyMeteReq extends DefaultReq {
    private String energyType;
}
