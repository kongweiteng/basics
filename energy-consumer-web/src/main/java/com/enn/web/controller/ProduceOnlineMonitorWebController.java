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
import com.enn.service.business.IProduceOnlineMonitorFacade;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.dto.DepartUnitDto;
import com.enn.vo.energy.business.dto.EnergyCostDto;
import com.enn.vo.energy.business.dto.ProduceOnlineElectricRealtimeSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineElectricSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineStreamRealtimeSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineStreamSampleDto;
import com.enn.vo.energy.business.vo.ProduceOnlineMonitorParams;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
* @author kai.guo
* @version 创建时间：2018年6月7日 下午4:49:11
* @Description 生产在线监测
*/
@RestController
@RequestMapping("/web/produce")
@Api( tags="生产在线监测")
public class ProduceOnlineMonitorWebController {
	
	@Autowired
	private IProduceOnlineMonitorFacade produceOnlineMonitorFacade;
	
	/**
	 * 查询指定企业id的生产车间列表
	 * @param companyId
	 */
	@RequestMapping(value="/queryProductionDepartment",method=RequestMethod.POST)
	@ApiOperation(value="查询指定企业的生产车间列表", notes="根据企业id查询生产车间列表")
	public EnergyResp<List<DepartUnitDto>> queryProductionDepartment(@RequestParam("companyId") @Valid Long companyId){
		EnergyResp<List<DepartUnitDto>> resp = new EnergyResp();
		if (null == companyId || "".equals(companyId)) {
			resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg());
            return resp;
        }
		
		return produceOnlineMonitorFacade.queryProductionDepartment(companyId);
	}
	
	/**
	 * 查询指定部门的电力采样数据
	 * @param onlineMonitorParams
	 * @return
	 */
	@ApiOperation( value="电力系统功率曲线",notes="根据车间id查询指定时间段内的电力采样数据")
	@RequestMapping( value="/queryPowerCurveForWebElectricSystem",method=RequestMethod.POST)
	public EnergyResp<ProduceOnlineElectricSampleDto> queryPowerCurveForWebElectricSystem(@RequestBody @Valid ProduceOnlineMonitorParams onlineMonitorParams, BindingResult result){
		EnergyResp<ProduceOnlineElectricSampleDto> resp = new EnergyResp();
		if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
		return produceOnlineMonitorFacade.queryPowerCurveForWebElectricSystem(onlineMonitorParams);
	}
	
	/**
	 * 查询指定部门的蒸汽采样数据
	 * @param onlineMonitorParams
	 * @return
	 */
	@ApiOperation( value="蒸汽系统负荷曲线",notes="根据车间id查询指定时间段内的蒸汽采样数据")
	@RequestMapping( value="/queryFlowCurveForWebThermodynamicSystem",method=RequestMethod.POST)
	public EnergyResp<ProduceOnlineStreamSampleDto> queryFlowCurveForWebThermodynamicSystem(@RequestBody @Valid ProduceOnlineMonitorParams onlineMonitorParams, BindingResult result){
		EnergyResp<ProduceOnlineStreamSampleDto> resp = new EnergyResp();
		if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
		return produceOnlineMonitorFacade.queryFlowCurveForWebThermodynamicSystem(onlineMonitorParams);
	}
	
	
	/**
	 * 查询指定部门的电力实时用电情况
	 * @param onlineMonitorParams
	 * @return
	 */
	@ApiOperation( value="查询实时用电情况",notes="根据车间id查询指定部门的电力实时用电情况")
	@RequestMapping( value="/queryRealTimeElectricityStatus",method=RequestMethod.POST)
	public EnergyResp<ProduceOnlineElectricRealtimeSampleDto> queryRealTimeElectricityStatus(@RequestBody @Valid ProduceOnlineMonitorParams onlineMonitorParams, BindingResult result){
		EnergyResp<ProduceOnlineElectricRealtimeSampleDto> resp = new EnergyResp();
		if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
		return produceOnlineMonitorFacade.queryRealTimeElectricityStatus(onlineMonitorParams);
	}
	
	
	/**
	 * 查询指定部门的蒸汽实时使用情况
	 * @param onlineMonitorParams
	 * @return
	 */
	@ApiOperation( value="查询实时用汽情况",notes="根据车间id查询指定部门的蒸汽实时使用情况")
	@RequestMapping( value="/queryRealTimeStreamStatus",method=RequestMethod.POST)
	public EnergyResp<ProduceOnlineStreamRealtimeSampleDto> queryRealTimeStreamStatus(@RequestBody @Valid ProduceOnlineMonitorParams onlineMonitorParams, BindingResult result){
		EnergyResp<ProduceOnlineStreamRealtimeSampleDto> resp = new EnergyResp();
		if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
		return produceOnlineMonitorFacade.queryRealTimeStreamStatus(onlineMonitorParams);
	}
	
	
	/**
	 * 查询指定部门的能源费用占比
	 * @param onlineMonitorParams
	 * @return
	 */
	@ApiOperation( value="查询指定部门的能源费用占比",notes="根据车间id查询指定部门的能源费用占比")
	@RequestMapping( value="/queryEnergyCostData",method=RequestMethod.POST)
	public EnergyResp<EnergyCostDto> queryEnergyCostData(@RequestBody @Valid ProduceOnlineMonitorParams onlineMonitorParams, BindingResult result){
		EnergyResp<EnergyCostDto> resp = new EnergyResp();
		if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
		return produceOnlineMonitorFacade.queryEnergyCostData(onlineMonitorParams);
	}

}
