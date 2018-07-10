package com.enn.energy.business.service.impl;

import com.enn.energy.business.dao.MaxDemandMapper;
import com.enn.energy.business.service.IMaxDemandService;
import com.enn.vo.energy.business.po.DataResp;
import com.enn.vo.energy.business.req.DemandReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaxDemandServiceImpl implements IMaxDemandService {

    @Autowired
    private MaxDemandMapper maxDemandMapper;
    @Override
    public List<DataResp> findMaxDemand(DemandReq req) {
        return null;
    }
}
