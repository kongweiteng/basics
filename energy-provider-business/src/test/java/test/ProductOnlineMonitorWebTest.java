package test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.enn.BusinessApplication;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.HttpClientUtils;
import com.enn.energy.system.common.util.MathUtils;
import com.enn.vo.energy.CollectItemEnum;
import com.enn.vo.energy.business.bo.EnergyPlanBo;
import com.enn.vo.energy.business.vo.EnergyPlanEntityParams;
import com.enn.vo.energy.business.vo.EnergyPlanQueryParams;
import com.enn.vo.energy.business.vo.EnergyPlanSavingEntityParams;
import com.enn.vo.energy.business.vo.ProduceOnlineMonitorParams;
import com.google.common.collect.Lists;


@SpringBootTest( classes=BusinessApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductOnlineMonitorWebTest {
	
	
	@Test
	public void testQueryEnergyProgramsByAssignedCondition(){
		
		String reqURL="http://localhost:8081/produce/queryPowerCurveForWebElectricSystem";
		
		ProduceOnlineMonitorParams onlineMonitorParams=new ProduceOnlineMonitorParams();
		onlineMonitorParams.setDepartmentId("23");
		onlineMonitorParams.setSampleCurrentTime("2018-06-12 14:38:00");
		
		String sendData=JSON.toJSONString(onlineMonitorParams);
		String msg=HttpClientUtils.httpPostStr(reqURL, sendData);
		System.out.println("msg:==="+msg);
		
	}
	
	
	@Test
	public void test(){
		
		String reqURL="http://localhost:8081/produce/queryEnergyCostData";
		
		ProduceOnlineMonitorParams onlineMonitorParams=new ProduceOnlineMonitorParams();
		onlineMonitorParams.setDepartmentId("23");
		onlineMonitorParams.setSampleCurrentTime("2018-06-20 15:28:00");
		
		String sendData=JSON.toJSONString(onlineMonitorParams);
		String msg=HttpClientUtils.httpPostStr(reqURL, sendData);
		System.out.println("msg:==="+msg);
		
	}
	
	

}
