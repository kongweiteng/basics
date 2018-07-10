package com.enn.energy.business.service.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.enn.energy.business.dao.CustTransMapper;
import com.enn.energy.business.service.ICustTransService;
import com.enn.vo.energy.business.resp.CustTransResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class CustTransServiceImpl extends ServiceImpl<CustTransMapper, CustTransResp> implements ICustTransService {

	@Resource
	private CustTransMapper custTransMapper;

	@Override
	public List<CustTransResp> getCustTransByDistributionId(Long[] ids) {
		EntityWrapper<CustTransResp> wrapper = new EntityWrapper<>();
		wrapper.in("distribution_id", ids);
		List<CustTransResp> custTransResps = custTransMapper.selectList(Condition.create().in("distribution_id", ids));
		return custTransResps;
	}
}
