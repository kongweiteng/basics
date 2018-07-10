package com.enn.energy.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enn.energy.business.dao.EnergyPlanPoMapper;
import com.enn.energy.business.service.IEnergyProgramManageService;
import com.enn.energy.system.common.util.CommonConverter;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.business.bo.EnergyPlanBo;
import com.enn.vo.energy.business.condition.EnergyPlanCondition;
import com.enn.vo.energy.business.dto.EnergyPlanDto;
import com.enn.vo.energy.business.po.EnergyPlanPo;
import com.enn.vo.energy.common.enums.EnergyTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

/**
 * 能源计划管理
 * @author kai.guo
 *
 */
@Service
public class IEnergyProgramManageServiceImpl implements IEnergyProgramManageService {
	
	@Autowired
	private EnergyPlanPoMapper energyPlanPoMapper;

	@Override
	public PagedList<EnergyPlanDto> queryEnergyProgramsByAssignedCondition(EnergyPlanBo energyPlanBo) {
		
		
		PageHelper.startPage(energyPlanBo.getPageNum(), energyPlanBo.getPageSize());
		
		EnergyPlanCondition energyPlanCondition=CommonConverter.map(energyPlanBo, EnergyPlanCondition.class);
		List<EnergyPlanPo>  energyPlanPos=energyPlanPoMapper.queryEnergyPlanByCondition(energyPlanCondition);
		
		PageInfo<EnergyPlanPo> pageInfo = new PageInfo<>(energyPlanPos);
        PagedList<EnergyPlanPo> poPagedList = PagedList.newMe(pageInfo.getPageNum(), pageInfo.getPageSize(),
                pageInfo.getTotal(), energyPlanPos);
        
        List<EnergyPlanDto> energyPlanDtos=Lists.newArrayList();
        poPagedList.getData().stream().forEach(p->{
        	EnergyPlanDto energyPlanDto=CommonConverter.map(p, EnergyPlanDto.class);
        	if(p.getTransportationDate() !=null){
        		energyPlanDto.setTransportationDates(DateUtil.format(p.getTransportationDate(),"yyyy-MM-dd"));
        	}
        	energyPlanDto.setEnergyTypeDesc(EnergyTypeEnum.getEnergyTypeDesc(p.getEnergyType()));
        	energyPlanDto.setCapacityUnit(EnergyTypeEnum.getEnergyType(p.getEnergyType()).getCapacityUnit());
        	energyPlanDto.setContractPriceUnit(EnergyTypeEnum.getEnergyType(p.getEnergyType()).getPriceUnit());
        	energyPlanDto.setPurchaseUnit(EnergyTypeEnum.getEnergyType(p.getEnergyType()).getPurchaseUnit());
        	energyPlanDtos.add(energyPlanDto);
        });
		
        PagedList<EnergyPlanDto> energyPlanPageList=PagedList.newMe(pageInfo.getPageNum(), pageInfo.getPageSize(),
                pageInfo.getTotal(), energyPlanDtos);
        
		return energyPlanPageList;
	}

	@Override
	public Boolean addEnergyProgram(EnergyPlanBo energyPlanBo) {
		
		EnergyPlanPo energyPlanPo=CommonConverter.map(energyPlanBo, EnergyPlanPo.class);
		
		return energyPlanPoMapper.insertSelective(energyPlanPo)>0?true:false;
	} 

	@Override
	public Boolean modifyEnergyProgram(EnergyPlanBo energyPlanBo) {
		
		EnergyPlanPo energyPlanPo=CommonConverter.map(energyPlanBo, EnergyPlanPo.class);
		
		return energyPlanPoMapper.updateByPrimaryKeySelective(energyPlanPo)>0?true:false;
	}

	@Override
	public EnergyPlanDto queryEnergyProgramsByPrimaryKey(Long id) {
		
		EnergyPlanPo energyPlanPo=energyPlanPoMapper.selectByPrimaryKey(id);
		
		EnergyPlanDto energyPlanDto=CommonConverter.map(energyPlanPo, EnergyPlanDto.class);
		if(null != energyPlanPo.getTransportationDate()){
			energyPlanDto.setTransportationDates(DateUtil.format(energyPlanPo.getTransportationDate(),"yyyy-MM-dd"));
		}
		energyPlanDto.setEnergyTypeDesc(EnergyTypeEnum.getEnergyTypeDesc(energyPlanPo.getEnergyType()));
		energyPlanDto.setCapacityUnit(EnergyTypeEnum.getEnergyType(energyPlanPo.getEnergyType()).getCapacityUnit());
		energyPlanDto.setContractPriceUnit(EnergyTypeEnum.getEnergyType(energyPlanPo.getEnergyType()).getPriceUnit());
		energyPlanDto.setPurchaseUnit(EnergyTypeEnum.getEnergyType(energyPlanPo.getEnergyType()).getPurchaseUnit());
		return energyPlanDto;
	}

}
