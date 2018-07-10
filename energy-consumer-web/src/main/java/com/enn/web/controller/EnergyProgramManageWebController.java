package com.enn.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.PagedList;
import com.enn.service.business.IEnergyProgramManageFacade;
import com.enn.service.system.IDictService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.dto.EnergyPlanDto;
import com.enn.vo.energy.business.dto.EnergyTypeDto;
import com.enn.vo.energy.business.vo.EnergyPlanEntityParams;
import com.enn.vo.energy.business.vo.EnergyPlanQueryParams;
import com.enn.vo.energy.business.vo.EnergyPlanSavingEntityParams;
import com.enn.vo.energy.common.enums.EnergyTypeEnum;
import com.enn.vo.energy.system.Dict;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping( value="/web/energyProgramManage")
@Api( tags="能源计划管理")
public class EnergyProgramManageWebController {
	
	@Autowired
	private IDictService dictService;
	
	@Autowired
	private IEnergyProgramManageFacade energyProgramManageFacade;
	
	
	@RequestMapping(value="/queryEnergyPrograms",method=RequestMethod.POST)
	@ApiOperation( value="查询指定条件下的能源计划", notes="查询参数")
	public EnergyResp<PagedList<EnergyPlanDto>> queryEnergyProgramsByAssignedCondition(@RequestBody @Valid EnergyPlanQueryParams energyPlanParams, BindingResult result){
		EnergyResp<PagedList<EnergyPlanDto>> resp = new EnergyResp();
		if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
		return energyProgramManageFacade.queryEnergyProgramsByAssignedCondition(energyPlanParams);
	}
	
	
	/**
	 * 新增能源计划
	 * @return
	 */
	@RequestMapping(value="/addEnergyProgram",method=RequestMethod.POST)
	@ApiOperation( value="新增能源计划", notes="根据入参添加能源计划数据")
	public EnergyResp<Boolean> addEnergyProgram(@RequestBody @Valid EnergyPlanSavingEntityParams  energyPlanParams, BindingResult result){
		EnergyResp<Boolean> resp = new EnergyResp();
		if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
		return energyProgramManageFacade.addEnergyProgram(energyPlanParams);
	}
	
	/**
	 * 修改能源计划
	 * @return
	 */
	@RequestMapping(value="/mofigyEnergyProgram",method=RequestMethod.POST)
	@ApiOperation( value="修改能源计划", notes="修改能源计划数据参数")
	public EnergyResp<Boolean> modifyEnergyProgram(@RequestBody @Valid EnergyPlanEntityParams energyPlanParams, BindingResult result){
		EnergyResp<Boolean> resp = new EnergyResp();
		if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
		return  energyProgramManageFacade.modifyEnergyProgram(energyPlanParams);
	}
	
	/**
	 * 查询指定主键对于的能源计划
	 * @return
	 */
	@RequestMapping(value="/queryEnergyProgramById",method=RequestMethod.POST)
	@ApiOperation( value="查询指定能源计划", notes="根据主键id查询对于的能源计划")
	public EnergyResp<EnergyPlanDto> queryEnergyProgramById(@RequestParam("id") @Valid Long id){
		EnergyResp<EnergyPlanDto> resp = new EnergyResp();
		if (null == id || "".equals(id)) {
			resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg());
            return resp;
        }
		return energyProgramManageFacade.queryEnergyProgramById(id);
	}
	
	/**
	 * 查询指定类型的字典信息
	 * @param type
	 * @return
	 */
	@RequestMapping(value="/queryEnergyTypeByDictTable",method=RequestMethod.POST)
	@ApiOperation( value="查询指定类型的字典信息", notes="根据能源类型，查询对应字典信息")
	public EnergyResp<List<EnergyTypeDto>> queryEnergyTypesByDictTable() {
		
		String type="energy_type";
		EnergyResp<List<Dict>> energyResp=dictService.getDictList(type);
		List<EnergyTypeDto> energyTypeDtos=Lists.newArrayList();
		energyResp.getData().stream().forEach(p->{
			EnergyTypeEnum energyTypeEnum=EnergyTypeEnum.getEnergyType(p.getValue());
			EnergyTypeDto dto=new EnergyTypeDto();
			dto.setLabel(p.getLabel());
			dto.setValue(p.getValue());
			dto.setPriceUnit(energyTypeEnum.getPriceUnit());
			dto.setCapacityUnit(energyTypeEnum.getCapacityUnit());
			dto.setPurchaseUnit(energyTypeEnum.getPurchaseUnit());
			energyTypeDtos.add(dto);
		});
		EnergyResp<List<EnergyTypeDto>> resp= new EnergyResp<>();
		resp.ok(energyTypeDtos);
		return resp;
	}
	

}
