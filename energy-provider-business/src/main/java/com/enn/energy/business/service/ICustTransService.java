package com.enn.energy.business.service;

import com.baomidou.mybatisplus.service.IService;
import com.enn.vo.energy.business.resp.CustTransResp;

import java.util.List;

public interface ICustTransService extends IService<CustTransResp> {

	List<CustTransResp> getCustTransByDistributionId(Long[] ids);

}
