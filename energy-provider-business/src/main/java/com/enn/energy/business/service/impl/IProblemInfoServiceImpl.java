package com.enn.energy.business.service.impl;

import com.enn.energy.business.dao.ProblemInfoMapper;
import com.enn.energy.business.service.IProblemInfoService;
import com.enn.vo.energy.business.ProblemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class IProblemInfoServiceImpl implements IProblemInfoService {

	
	@Autowired
	private ProblemInfoMapper problemInfoMapper;


	//保存问题信息
	@Override
	public int saveProblemInfo(ProblemInfo problemInfo) {
		int result = problemInfoMapper.insert(problemInfo);
		return result;
	}
}
