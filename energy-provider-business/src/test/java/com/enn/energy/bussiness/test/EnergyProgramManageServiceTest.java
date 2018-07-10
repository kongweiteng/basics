package com.enn.energy.bussiness.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.enn.BusinessApplication;
import com.enn.energy.business.service.IEnergyProgramManageService;
import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.business.bo.EnergyPlanBo;
import com.enn.vo.energy.business.dto.EnergyPlanDto;


@SpringBootTest( classes=BusinessApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EnergyProgramManageServiceTest {
	
	@Autowired
	private IEnergyProgramManageService energyProgramManageService;
	
	@Test
	public void test(){
		
		EnergyPlanBo energyPlanBo=new EnergyPlanBo();
		energyPlanBo.setYearPeriod("2018");
		energyPlanBo.setEnergyType("01");
		energyPlanBo.setCustId(1L);
		energyPlanBo.setPageNum(2);
		energyPlanBo.setPageSize(3);
		
		PagedList<EnergyPlanDto> dtos=energyProgramManageService.queryEnergyProgramsByAssignedCondition(energyPlanBo);
		System.out.println("msg:===="+JSON.toJSONString(dtos));
		
	}
	
	/**
	 * 根据主键查询指定能源计划
	 */
	@Test
	public void queryEnergyPlanByIdTest(){
		
		Long id=1L;
		EnergyPlanDto energyPlanDto=energyProgramManageService.queryEnergyProgramsByPrimaryKey(id);
		System.out.println("msg:===="+JSON.toJSONString(energyPlanDto));
	}
	
	@Test
	public void addEnergyPlanTest(){
		
		EnergyPlanBo energyPlanBo=new EnergyPlanBo();
		energyPlanBo.setBusinessMobile("18518417383");
		energyPlanBo.setBusinessPerson("商务联系人");
		energyPlanBo.setTechMobile("18518417382");
		energyPlanBo.setTechPerson("技术联系人");
		energyPlanBo.setEnergyType("02");
		energyPlanBo.setEnergyProvider("新奥集团");
		energyPlanBo.setCustId(1000L);
		energyPlanBo.setContractPrice("1000000");
		energyPlanBo.setCapacity("20000");
		energyPlanBo.setTransportationDate(new Date());
		energyPlanBo.setYearPeriod("2018");
		energyPlanBo.setYearPurchaseVolume("1000");
		
		energyProgramManageService.addEnergyProgram(energyPlanBo);
	}
	
	
	@Test
	public void modifyEnergyPlanTest(){
		
		EnergyPlanBo energyPlanBo=new EnergyPlanBo();
		energyPlanBo.setBusinessMobile("");
		energyPlanBo.setBusinessPerson("");
		energyPlanBo.setTechMobile("");
		energyPlanBo.setTechPerson("");
		energyPlanBo.setEnergyType("01");
		energyPlanBo.setEnergyProvider("");
		energyPlanBo.setCustId(22L);
		energyPlanBo.setContractPrice("");
		energyPlanBo.setCapacity("");
		energyPlanBo.setId(83l);
		energyPlanBo.setTransportationDate(new Date());
		energyPlanBo.setYearPeriod("2018");
		energyPlanBo.setYearPurchaseVolume("777");
		
		energyProgramManageService.modifyEnergyProgram(energyPlanBo);
	}
	
}
