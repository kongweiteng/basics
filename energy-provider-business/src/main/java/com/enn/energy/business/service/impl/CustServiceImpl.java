package com.enn.energy.business.service.impl;

import com.enn.energy.business.dao.CustMapper;
import com.enn.energy.business.service.ICustService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CustServiceImpl implements ICustService {

    @Resource
    private CustMapper custMapper;

    @Override
    public String getCustId(String relationId) {
        return custMapper.getCustId(relationId);
    }
}
