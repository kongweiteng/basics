package com.enn.energy.business.rest;


import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.enn.energy.business.service.IEnergyProgramManageService;
import com.enn.energy.system.common.util.CommonConverter;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.bo.EnergyPlanBo;
import com.enn.vo.energy.business.dto.EnergyPlanDto;
import com.enn.vo.energy.business.vo.Base;
import com.enn.vo.energy.business.vo.EnergyPlanEntityParams;
import com.enn.vo.energy.business.vo.EnergyPlanQueryParams;
import com.enn.vo.energy.business.vo.EnergyPlanSavingEntityParams;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 能源计划管理
 * @author kai.guo
 *
 */
@RestController
@RequestMapping("/energyProgramManage")
@Api( tags="能源计划管理")
public class EnergyProgramManageController {
	
	private static final Logger logger=LoggerFactory.getLogger(EnergyProgramManageController.class);
	
	@Autowired
	private IEnergyProgramManageService iEnergyProgramManageService;
	
	@RequestMapping(value="/queryEnergyPrograms",method=RequestMethod.POST)
	@ApiOperation( value="查询指定条件下的能源计划", notes="查询参数")
	public EnergyResp<PagedList<EnergyPlanDto>> queryEnergyProgramsByAssignedCondition(@RequestBody @Valid EnergyPlanQueryParams energyPlanParams){
		
		logger.info("【能源计划管理】,能源计划查询, method:[queryEnergyProgramsByAssignedCondition],reqParams:{}",JSON.toJSON(energyPlanParams));
		
		if(null ==energyPlanParams.getPageSize()){
			energyPlanParams.setPageSize(Base.DEFAULT_PAGE_SIZE);
		}
		if(null ==energyPlanParams.getPageNum()){
			energyPlanParams.setPageNum(Base.DEFAULT_PAGE_NUM);
		}
		
		EnergyPlanBo energyPlanBo=CommonConverter.map(energyPlanParams, EnergyPlanBo.class);
		PagedList<EnergyPlanDto> list= iEnergyProgramManageService.queryEnergyProgramsByAssignedCondition(energyPlanBo);
		EnergyResp<PagedList<EnergyPlanDto>> resp = new EnergyResp<>();
		resp.ok(list);
		return resp;
	}
	
	
	/**
	 * 新增能源计划
	 * @return
	 */
	@RequestMapping(value="/addEnergyProgram",method=RequestMethod.POST)
	@ApiOperation( value="新增能源计划", notes="根据入参添加能源计划数据")
	public EnergyResp<Boolean> addEnergyProgram(@RequestBody  @Valid EnergyPlanSavingEntityParams  energyPlanParams){
		
		logger.info("【能源计划管理】,新增能源计划, method:[addEnergyProgram],reqParams:{}",JSON.toJSON(energyPlanParams));
		
		EnergyPlanBo energyPlanBo=CommonConverter.map(energyPlanParams, EnergyPlanBo.class);
		if(StringUtils.isNotEmpty(energyPlanParams.getTransportationDates())){
			energyPlanBo.setTransportationDate(DateUtil.parseDate(energyPlanParams.getTransportationDates()));
		}
		Boolean result= iEnergyProgramManageService.addEnergyProgram(energyPlanBo);
		EnergyResp<Boolean> resp=new EnergyResp<>();
		resp.ok(result);
		return resp;
	}
	
	/**
	 * 修改能源计划
	 * @return
	 */
	@RequestMapping(value="/mofigyEnergyProgram",method=RequestMethod.POST)
	@ApiOperation( value="修改能源计划", notes="修改能源计划数据参数")
	public EnergyResp<Boolean> modifyEnergyProgram(@RequestBody  @Valid EnergyPlanEntityParams energyPlanParams){
		
		logger.info("【能源计划管理】,修改能源计划, method:[modifyEnergyProgram],reqParams:{}",JSON.toJSON(energyPlanParams));
		
		EnergyPlanBo energyPlanBo=CommonConverter.map(energyPlanParams, EnergyPlanBo.class);
		if(StringUtils.isNotEmpty(energyPlanParams.getTransportationDates())){
			energyPlanBo.setTransportationDate(DateUtil.parseDate(energyPlanParams.getTransportationDates()));
		}
		Boolean result=  iEnergyProgramManageService.modifyEnergyProgram(energyPlanBo);
		EnergyResp<Boolean> resp=new EnergyResp<>();
		resp.ok(result);
		return resp;
	}
	
	/**
	 * 查询指定主键对于的能源计划
	 * @return
	 */
	@RequestMapping(value="/queryEnergyProgramById",method=RequestMethod.POST)
	@ApiOperation( value="查询指定能源计划", notes="根据主键id查询对于的能源计划")
	public EnergyResp<EnergyPlanDto> queryEnergyProgramById(@RequestParam("id") @Valid Long id){
		
		logger.info("【能源计划管理】,查询指定能源计划, method:[queryEnergyProgramById],id:{}",id);
		
		EnergyPlanDto energyPlanDto= iEnergyProgramManageService.queryEnergyProgramsByPrimaryKey(id);
		EnergyResp<EnergyPlanDto> resp=new EnergyResp<>();
		resp.ok(energyPlanDto);
		return resp;
	}
	
	
	

}
