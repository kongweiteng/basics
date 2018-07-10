package com.enn.energy.business.service;

import com.baomidou.mybatisplus.service.IService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.po.CustMeter;
import com.enn.vo.energy.business.po.DistributionMeter;
import com.enn.vo.energy.business.req.MeterListReq;
import com.enn.vo.energy.business.resp.MeterResp;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface ICustMeterService extends IService<CustMeter> {

	List<DistributionMeter> getCustMeterByDistributionId(Long[] ids);

	/**
	 * 根据核算单元id查询 该核算单元下参与核算的表
	 */
	EnergyResp<CustMeter> getAccountMeter(Long id);

	/**
	 * 根据企业id查询所有的计量表信息
	 */

	public EnergyResp<List<MeterResp>> getAllMeter( MeterListReq meterListReq);

    /**
     * 保存 计量表信息
     */
    public EnergyResp<Integer> save(CustMeter meter);


	List<DistributionMeter> getCustMetersByDistributionId(long distributionId);

}
