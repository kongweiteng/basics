package com.enn.energy.business.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface CustMapper {

    String getCustId(String relationId);

    String getCustName(String id);
}
