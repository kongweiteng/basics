package com.enn.energy.business.dao;

import com.enn.vo.energy.business.po.DataResp;
import com.enn.vo.energy.business.req.DemandReq;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaxDemandMapper {
    List<DataResp> findMaxDemand(DemandReq demandReq);
}
