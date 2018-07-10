package com.enn.energy.business.service.impl;

import ch.qos.logback.core.joran.action.IADataForComplexProperty;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.enn.constant.Constant;
import com.enn.energy.business.dao.CustMeterMapper;
import com.enn.energy.business.dao.CustWifiMapper;
import com.enn.energy.business.service.IAccountingUnitService;
import com.enn.energy.business.service.ICustMeterService;
import com.enn.energy.business.service.ICustWifiService;
import com.enn.service.system.ICompanyService;
import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.CustMeter;
import com.enn.vo.energy.business.po.CustWifi;
import com.enn.vo.energy.business.po.DistributionMeter;
import com.enn.vo.energy.business.req.MeterListReq;
import com.enn.vo.energy.business.resp.MeterResp;
import com.enn.vo.energy.system.CompanyCust;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CustMeterServiceImpl extends ServiceImpl<CustMeterMapper, CustMeter> implements ICustMeterService {

	@Resource
	private CustMeterMapper custMeterMapper;

	@Autowired
	private ICompanyService companyService;
	@Autowired
	private IAccountingUnitService accountingUnitService;
	@Autowired
	private ICustWifiService custWifiService;

	@Autowired
	private CustWifiMapper custWifiMapper;

	@Override
	public List<DistributionMeter> getCustMeterByDistributionId(Long[] ids) {
		EntityWrapper<CustMeter> wrapper = new EntityWrapper<>();
		wrapper.in("distribution_id", ids);
		wrapper.eq("is_accoun",1);
		wrapper.eq("level",1);
		wrapper.eq("del_flag", '0');
		//List<CustMeter> custMeterList1 =custMeterMapper.selectList(wrapper);
		List<CustMeter> custMeterList =
				custMeterMapper.selectList(wrapper);
		List<DistributionMeter> distributionMeterList = custMeterList.stream().map(s -> {
			DistributionMeter distributionMeter = new DistributionMeter();
			distributionMeter.setMeterId(s.getId());
			distributionMeter.setLoopNumber(s.getLoopNumber());
			distributionMeter.setMeterName(s.getName());
			distributionMeter.setDistributionId(s.getDistributionId());
			distributionMeter.setIsAccoun(s.getIsAccoun());
			distributionMeter.setEnergyType(s.getEnergyType());
			distributionMeter.setWifiId(s.getWifiId());
			return distributionMeter;
		}).collect(Collectors.toList());
		List<Long> list = custMeterList.stream().map(s -> s.getWifiId()).collect(Collectors.toList());
		List<CustWifi> custWifiList = custWifiService.selectList(Condition.create().in("id", list));
		for (DistributionMeter distributionMeter : distributionMeterList) {
			for (CustWifi custWifi : custWifiList) {
				if (custWifi.getId().equals(distributionMeter.getWifiId())) {
					distributionMeter.setStaId(custWifi.getStaId());
				}
			}
		}
		return distributionMeterList;
	}

	@Override
	public EnergyResp<CustMeter> getAccountMeter(Long id) {
		EnergyResp<CustMeter> resp = new EnergyResp<>();
		CustMeter meter = new CustMeter();
		meter.setIsAccoun("0");
		meter.setAccountingId(id);
		resp.ok(custMeterMapper.selectOne(meter));
		return resp;
	}

	/**
	 * 根据企业id查询所有的计量表信息
	 */
	@Override
	public EnergyResp<List<MeterResp>> getAllMeter(MeterListReq meterListReq) {
		EnergyResp<List<MeterResp>> resp = new EnergyResp<>();
		//根据企业id获取到list
		DefaultReq defaultReq = new DefaultReq();
		defaultReq.setId(meterListReq.getId());
		EnergyResp<ListResp<CompanyCust>> allCompany = companyService.getAllCompany(defaultReq);
		//根据企业查询所有的核算单元
		List<Long> ids = new ArrayList<>();
		if (allCompany.getData() != null) {
			List<CompanyCust> list = allCompany.getData().getList();
			for (CompanyCust companyCust : list) {
				ids.add(companyCust.getId());
			}
		}
		//根据ids查询出所有的核算单元列表
		EnergyResp<List<AccountingUnit>> unitByCompanyisd = accountingUnitService.getUnitByCompanyisd(ids);
		//组装核算单元ids
		List<Long> unitIds = new ArrayList<>();
		if (unitByCompanyisd.getData() != null) {
			for (AccountingUnit accountingUnit : unitByCompanyisd.getData()) {
				unitIds.add(accountingUnit.getId());
			}
		}
		//使用核算单元ids查询出所有的计量表信息
		EntityWrapper<CustMeter> wrapper = new EntityWrapper<>();

		wrapper.eq("del_flag", "0").in("accounting_id", unitIds).like("name", meterListReq.getMeterName()).eq("energy_type", meterListReq.getEnergyType());
		List<CustMeter> custMeters = custMeterMapper.selectList(wrapper);
		List<MeterResp> meterResps = new ArrayList<>();
		MeterResp meterResp = null;
		for (CustMeter meters : custMeters) {
			meterResp = new MeterResp();
			//BeanUtils.copyProperties(meters, meterResp);
			meterResp.setEnergyType(meters.getEnergyType());
			meterResp.setMeterName(meters.getName());
			if(meters.getEnergyType().equals("01")){
				meterResp.setLoopNumber(meters.getLoopNumber());
			}else{
				meterResp.setLoopNumber(meters.getLoopNumber());
			}
			meterResp.setIsAccoun(meters.getIsAccoun());
			meterResp.setMeterId(meters.getId());
			//查询到站点id
			CustWifi custWifi = custWifiMapper.selectById(meters.getWifiId());
			if(custWifi!=null){
				meterResp.setStaId(custWifi.getStaId());
			}
			meterResps.add(meterResp);
		}
		resp.ok(meterResps);
		return resp;
	}

	@Override
	public EnergyResp<Integer> save(CustMeter meter) {
		EnergyResp<Integer> en = new EnergyResp<>();
		Integer insert = null;
		if (meter.getId() == null || meter.getId().equals("")) {//insert
			meter.setDelFlag("0");
			insert = custMeterMapper.insert(meter);
		} else {//updata
			if (meter.getDelFlag() == null || meter.getDelFlag().equals("")) {
				meter.setDelFlag("0");
			}
			insert = custMeterMapper.updateById(meter);
		}
		en.ok(insert);
		return en;
	}

	@Override
	public List<DistributionMeter> getCustMetersByDistributionId(long distributionId) {
		List<DistributionMeter> distributionMeterList = new ArrayList<>();
		EntityWrapper<CustMeter> wrapper = new EntityWrapper<>();
		wrapper.eq("distribution_id", distributionId);
		wrapper.eq("del_flag", '0');
		List<CustMeter> custMeterList =	custMeterMapper.selectList(wrapper);
		for(CustMeter custMeter:custMeterList){
			DistributionMeter distributionMeter = new DistributionMeter();
			distributionMeter.setLoopNumber(custMeter.getLoopNumber());
			distributionMeter.setMeterName(custMeter.getName());
			distributionMeter.setMeterId(custMeter.getId());
			distributionMeterList.add(distributionMeter);
		}
		return distributionMeterList;
	}
}
