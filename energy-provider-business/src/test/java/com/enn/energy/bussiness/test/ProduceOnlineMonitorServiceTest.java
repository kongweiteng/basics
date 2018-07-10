package com.enn.energy.bussiness.test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.enn.BusinessApplication;
import com.enn.energy.business.service.IAccountingUnitService;
import com.enn.energy.business.service.IProduceOnlineMonitorService;
import com.enn.energy.business.service.impl.ProduceOnlineMonitorServiceImpl;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.bo.DepartUnitBo;
import com.enn.vo.energy.business.bo.ProduceOnlineMonitorBo;
import com.enn.vo.energy.business.bo.ProduceOnlineMonitorSampleBo;
import com.enn.vo.energy.business.bo.ProduceOnlineRealtimeSampleBo;
import com.enn.vo.energy.business.bo.SteamMeterReadingMinuteBo;
import com.enn.vo.energy.business.dto.DepartUnitDto;
import com.enn.vo.energy.business.dto.EnergyCostDto;
import com.enn.vo.energy.business.dto.ProduceOnlineElectricRealtimeSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineElectricSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineStreamRealtimeSampleDto;
import com.enn.vo.energy.business.dto.ProduceOnlineStreamSampleDto;
import com.enn.vo.energy.business.dto.ProductOnlineMonitorDto;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;

/**
* @author kai.guo
* @version 创建时间：2018年6月6日 下午6:59:41
* @Description 类描述
*/
@SpringBootTest( classes=BusinessApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProduceOnlineMonitorServiceTest {
	
	@Autowired
	private ProduceOnlineMonitorServiceImpl iProduceOnlineMonitorService;
	
	
	@Test
	public void testQueryPowerCurveForElectricSystem(){
		
		Date currentDate=new Date();
		String date =DateUtil.format(currentDate, DateUtil.BASIC_PATTEN);
		System.out.println("date:===="+date);
		String beforeDate=DateUtil.getBeforeHourTime(currentDate, -12,DateUtil.BASIC_PATTEN);
		System.out.println("beforeDate:===="+beforeDate);
		ProduceOnlineMonitorBo onlineMonitorBo=new ProduceOnlineMonitorBo();
		onlineMonitorBo.setDepartmentId(13L);
//		onlineMonitorBo.setDepartmentId(34L);
		onlineMonitorBo.setSampleStartTime(beforeDate);
		onlineMonitorBo.setSampleEndTime(date);
		
		ProduceOnlineElectricSampleDto dto=iProduceOnlineMonitorService.queryPowerCurveForWebElectricSystem(onlineMonitorBo);
		
		System.out.println("msg:====="+JSON.toJSONString(dto));
		
	}
	
	
	@Test
	public void testQueryFlowCurveForWebThermodynamicSystem(){
		
		Date currentDate=new Date();
		String date =DateUtil.format(currentDate, DateUtil.BASIC_PATTEN);
		System.out.println("date:===="+date);
		String beforeDate=DateUtil.getBeforeHourTime(currentDate, -6,DateUtil.BASIC_PATTEN);
		System.out.println("beforeDate:===="+beforeDate);
		ProduceOnlineMonitorBo onlineMonitorBo=new ProduceOnlineMonitorBo();
		onlineMonitorBo.setDepartmentId(34L);
		onlineMonitorBo.setSampleStartTime(beforeDate);
		onlineMonitorBo.setSampleEndTime(date);
		
		ProduceOnlineStreamSampleDto dto=iProduceOnlineMonitorService.queryFlowCurveForWebThermodynamicSystem(onlineMonitorBo);
		
		System.out.println("msg:====="+JSON.toJSONString(dto));
		
	}
	
	@Test
	public void testQueryRealTimeElectricityStatus(){
		
		Date currentDate=new Date();
		String date =DateUtil.format(currentDate, DateUtil.BASIC_PATTEN);
		System.out.println("date:===="+date);
		String beforeDate=DateUtil.getBeforeHourTime(currentDate, -12,DateUtil.BASIC_PATTEN);
		System.out.println("beforeDate:===="+beforeDate);
		ProduceOnlineMonitorBo onlineMonitorBo=new ProduceOnlineMonitorBo();
		onlineMonitorBo.setDepartmentId(13L);
		onlineMonitorBo.setSampleStartTime(beforeDate);
		onlineMonitorBo.setSampleEndTime(date);
		
		ProduceOnlineElectricRealtimeSampleDto dto=iProduceOnlineMonitorService.queryRealTimeElectricityStatus(onlineMonitorBo);
		
		System.out.println("msg:==="+JSON.toJSONString(dto));
		
	}
	
	
	@Test
	public void testQueryRealTimeStreamStatus(){
		
		Date currentDate=new Date();
		String date =DateUtil.format(currentDate, DateUtil.BASIC_PATTEN);
		System.out.println("date:===="+date);
		String beforeDate=DateUtil.getBeforeHourTime(currentDate, -12,DateUtil.BASIC_PATTEN);
		System.out.println("beforeDate:===="+beforeDate);
		ProduceOnlineMonitorBo onlineMonitorBo=new ProduceOnlineMonitorBo();
		onlineMonitorBo.setDepartmentId(36L);
		onlineMonitorBo.setSampleStartTime(beforeDate);
		onlineMonitorBo.setSampleEndTime(date);
		
		ProduceOnlineStreamRealtimeSampleDto dto=iProduceOnlineMonitorService.queryRealTimeStreamStatus(onlineMonitorBo);
		
		System.out.println("msg:==="+JSON.toJSONString(dto));
		
	}
	
	@Test
	public void test(){
		
		List<DepartUnitDto> departUnitBos=iProduceOnlineMonitorService.queryProductionDepartment(1L);
		System.out.println("msg:====="+JSON.toJSONString(departUnitBos));
		
	}
	
	@Test
	public void testQueryEquipInfo(){
		
		Long departmentId=23L;
		/*Map<String,ProduceOnlineMonitorSampleBo> map=iProduceOnlineMonitorService.queryEquipInfo(departmentId);
		
		System.out.println("msg:===="+JSON.toJSONString(map));*/
	}
	
	@Test
	public void testOnlineMonitor(){
//		Date currentDate=new Date();
		String currentDates="2018-07-03 12:28:00";
		Date currentDate=DateUtil.parse(currentDates, "yyyy-MM-dd HH:mm:00");
		String date =DateUtil.format(currentDate, DateUtil.BASIC_PATTEN);
		System.out.println("date:===="+date);
		String beforeDate=DateUtil.getBeforeHourTime(currentDate, -12,DateUtil.BASIC_PATTEN);
		System.out.println("beforeDate:===="+beforeDate);
		ProduceOnlineMonitorBo onlineMonitorBo=new ProduceOnlineMonitorBo();
		onlineMonitorBo.setDepartmentId(36L);
		onlineMonitorBo.setSampleStartTime(beforeDate);
		onlineMonitorBo.setSampleEndTime(date);
		
		EnergyCostDto dto=iProduceOnlineMonitorService.queryEnergyCostData(onlineMonitorBo);
		System.out.println("dto:==="+JSON.toJSONString(dto));
	}
	
	@Test
	public void testMinuteSteamFees(){
		SteamMeterReadingMinuteBo meterReadingMinuteBo=new SteamMeterReadingMinuteBo();
		meterReadingMinuteBo.setStart("2018-06-15 09:08:00");
		meterReadingMinuteBo.setEnd("2018-06-16 10:08:00");
		meterReadingMinuteBo.setEquipID(Lists.newArrayList("LHDD"));
		
		iProduceOnlineMonitorService.calculateSteamFeesFromAssignedDateRange(meterReadingMinuteBo);
	}
	

}
