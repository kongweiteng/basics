package test;


import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.enn.energy.system.common.util.HttpClientUtils;
import com.enn.vo.energy.business.vo.ProductDataStatisticsQueryParams;
import com.enn.web.EnergyWebApplication;


@SpringBootTest( classes=EnergyWebApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductOnlineMonitorWebTest {
	
	
	@Test
	public void testQueryEnergyProgramsByAssignedCondition(){
		
		String reqURL="http://localhost:8083/web/productDataStatistics/getUploadExcel";
		
		ProductDataStatisticsQueryParams queryParams=new ProductDataStatisticsQueryParams();
		
		queryParams.setPageNum(1);
		queryParams.setPageSize(10);
		queryParams.setStartDate("2018-06-01 10:27");
		queryParams.setEndDate("2018-06-23 15:00");
		queryParams.setTimeDimension("3");
		queryParams.setWorkShopId(35L);
		queryParams.setCustId(4L);
		
		String sendData=JSON.toJSONString(queryParams);
		
		Header header=new Header() {
			
			@Override
			public String getValue() {
				// TODO Auto-generated method stub
				return "123456";
			}
			
			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return "ticket";
			}
			
			@Override
			public HeaderElement[] getElements() throws ParseException {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		String msg=HttpClientUtils.httpPostStr(reqURL, sendData,header);
		System.out.println("msg:==="+msg);
		
	}
	
	

}
