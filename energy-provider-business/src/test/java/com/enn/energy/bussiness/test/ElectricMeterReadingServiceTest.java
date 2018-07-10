package com.enn.energy.bussiness.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.enn.BusinessApplication;
import com.enn.energy.business.service.IElectricMeterReadingService;
import com.enn.vo.energy.business.bo.ElectricMeterReadingHourBo;
import com.enn.vo.energy.business.bo.ElectricMeterReadingHourStatisticsBo;
import com.enn.vo.energy.business.bo.ElectricMeterReadingMinuteBo;
import com.enn.vo.energy.business.bo.ElectricMeterReadingMinuteStatisticsBo;
import com.google.common.collect.Lists;

/**
* @author kai.guo
* @version 创建时间：2018年6月7日 上午11:21:12
* @Description 类描述
*/
@SpringBootTest( classes=BusinessApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ElectricMeterReadingServiceTest {
	
	@Autowired
	private IElectricMeterReadingService electricMeterReadingService;
	
	@Test
	public void testHour(){
		ElectricMeterReadingHourBo meterReadingHourBo=new ElectricMeterReadingHourBo();
		meterReadingHourBo.setEquipID(Lists.newArrayList("METE340340002281"));
		meterReadingHourBo.setStart("2018-05-18 11:00:00");
		meterReadingHourBo.setEnd("2018-06-01 05:00:00");
		ElectricMeterReadingHourStatisticsBo hourStatisticsBo=electricMeterReadingService.countElectricMeterReadingHourByAssignedConditon(meterReadingHourBo);
		System.out.println("msg:===="+JSON.toJSONString(hourStatisticsBo));
	}
	
	@Test
	public void testMinute(){
		ElectricMeterReadingMinuteBo meterReadingMinuteBo=new ElectricMeterReadingMinuteBo();
		meterReadingMinuteBo.setEquipID(Lists.newArrayList("METE340340002281"));
		meterReadingMinuteBo.setStart("2018-05-18 11:30:00");
		meterReadingMinuteBo.setEnd("2018-05-19 05:27:00");
		ElectricMeterReadingMinuteStatisticsBo hourStatisticsBo=electricMeterReadingService.countElectricMeterReadingMinuteByAssignedConditon(meterReadingMinuteBo);
		System.out.println("msg:===="+JSON.toJSONString(hourStatisticsBo));
	}
	

}
