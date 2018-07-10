package com.enn.energy.business.service;

import com.enn.vo.energy.business.po.DataResp;
import com.enn.vo.energy.business.req.DemandReq;

import java.util.List;

public interface IMaxDemandService {

    public List<DataResp> findMaxDemand(DemandReq req);
}
