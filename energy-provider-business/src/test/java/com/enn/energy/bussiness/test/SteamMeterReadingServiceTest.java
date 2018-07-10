package com.enn.energy.bussiness.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.enn.BusinessApplication;
import com.enn.energy.business.service.ISteamMeterReadingService;
import com.enn.vo.energy.business.bo.SteamMeterReadingHourBo;
import com.enn.vo.energy.business.bo.SteamMeterReadingHourStatisticsBo;
import com.enn.vo.energy.business.bo.SteamMeterReadingMinuteBo;
import com.enn.vo.energy.business.bo.SteamMeterReadingMinuteStatisticsBo;
import com.google.common.collect.Lists;

/**
* @author kai.guo
* @version 创建时间：2018年6月7日 下午1:37:29
* @Description 类描述
*/
@SpringBootTest( classes=BusinessApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SteamMeterReadingServiceTest {
	
	@Autowired
	private ISteamMeterReadingService steamMeterReadingService;
	
	@Test
	public void testHour(){
		SteamMeterReadingHourBo meterReadingHourBo=new SteamMeterReadingHourBo();
		meterReadingHourBo.setEquipID(Lists.newArrayList("METE340340002281"));
		meterReadingHourBo.setStart("2018-05-18 11:00:00");
		meterReadingHourBo.setEnd("2018-06-01 05:00:00");
		SteamMeterReadingHourStatisticsBo hourStatisticsBo=steamMeterReadingService.countSteamMeterReadingHourByAssignedConditon(meterReadingHourBo);
		System.out.println("msg:===="+JSON.toJSONString(hourStatisticsBo));
		
	}
	
	
	@Test
	public void testMinute(){
		SteamMeterReadingMinuteBo meterReadingMinuteBo=new SteamMeterReadingMinuteBo();
		meterReadingMinuteBo.setEquipID(Lists.newArrayList("METE340340002281"));
		meterReadingMinuteBo.setStart("2018-05-18 11:00:00");
		meterReadingMinuteBo.setEnd("2018-06-01 05:00:00");
		SteamMeterReadingMinuteStatisticsBo hourStatisticsBo=steamMeterReadingService.countSteamMeterReadingMinuteByAssignedConditon(meterReadingMinuteBo);
		System.out.println("msg:===="+JSON.toJSONString(hourStatisticsBo));
		
	}

}
