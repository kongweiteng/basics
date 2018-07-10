package com.enn.energy.business.service;


import com.enn.vo.energy.business.ProblemInfo;

public interface IProblemInfoService {


	/**
	 * 保存问题信息
	 * @param problemInfo
	 * @return
	 */
	public int saveProblemInfo(ProblemInfo problemInfo);


}
