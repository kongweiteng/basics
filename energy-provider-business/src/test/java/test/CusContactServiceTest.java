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
import com.google.common.collect.Lists;


@SpringBootTest( classes=BusinessApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CusContactServiceTest {
	
	@Test
	public void test(){
		
		String reqURL="http://localhost:8081/energyProgramManage/queryEnergyProgramById";
		Map<String, Object> params=new HashMap<String,Object>();
		params.put("id", "1");
		String sendData=JSON.toJSONString(params);
		String responseMsg=HttpClientUtils.httpPostStr(reqURL, sendData);
		System.out.println("responseMsg:==="+responseMsg);
		
		try {
			Thread.sleep(100000l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testQueryEnergyProgramsByAssignedCondition(){
		
		String reqURL="http://localhost:8081/energyProgramManage/queryEnergyPrograms";
		
		List<String> energyTypes=Lists.newArrayList();
		energyTypes.add("01");
		energyTypes.add("02");
		
		EnergyPlanQueryParams energyPlanQueryParams=new EnergyPlanQueryParams();
		energyPlanQueryParams.setEnergyTypes(energyTypes);
		energyPlanQueryParams.setYearPeriod("2018");
		energyPlanQueryParams.setPageNum(1);
		energyPlanQueryParams.setPageSize(10);
		String sendData=JSON.toJSONString(energyPlanQueryParams);
		String msg=HttpClientUtils.httpPostStr(reqURL, sendData);
		System.out.println("msg:==="+msg);
		
	}
	
	
	@Test
	public void testAddEnergyPlan(){
		
		String reqURL="http://localhost:8081/energyProgramManage/addEnergyProgram";
		EnergyPlanSavingEntityParams  energyPlanBo=new EnergyPlanSavingEntityParams();
		energyPlanBo.setBusinessMobile("18518417383");
		energyPlanBo.setBusinessPerson("商务联系人");
		energyPlanBo.setTechMobile("18518417382");
		energyPlanBo.setTechPerson("技术联系人");
		energyPlanBo.setEnergyType("02");
		energyPlanBo.setEnergyProvider("新奥集团");
		energyPlanBo.setContractPrice("1000000");
		energyPlanBo.setCapacity("20000瓦");
		energyPlanBo.setTransportationDates("2018-06-11");
		energyPlanBo.setYearPeriod("2018");
		energyPlanBo.setYearPurchaseVolume("1000");
		
		String sendData=JSON.toJSONString(energyPlanBo);
		String msg=HttpClientUtils.httpPostStr(reqURL, sendData);
		System.out.println("msg:==="+msg);
	}
	
	@Test
	public void testQueryEnergyPlanById(){
		
		String reqURL="http://localhost:8081/energyProgramManage/queryEnergyProgramById?id=18";
		Long id=18L;
		String sendData=JSON.toJSONString(id);
		String msg=HttpClientUtils.httpPostStr(reqURL, sendData);
		System.out.println("msg:==="+msg);
	}
	
	
	public static void main(String[] args) {
		
		Date currentDate=new Date();
		String date =DateUtil.format(currentDate, DateUtil.BASIC_PATTEN);
		System.out.println("date:===="+date);
		String beforeDate=DateUtil.getBeforeHourTime(currentDate, -6,DateUtil.BASIC_PATTEN);
		System.out.println("beforeDate:===="+beforeDate);

		/*BigDecimal a=new BigDecimal("20");
		BigDecimal b=new BigDecimal("100");
		String result=MathUtils.divide(a, b).multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
		String percent=result+"%";
		System.out.println("c:===="+percent);*/
		
		/*String p=CollectItemEnum.P.name();
		System.out.println("p:===>"+p);*/
		
	}

}
