package com.enn.energy.bussiness.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.enn.BusinessApplication;
import com.enn.energy.business.service.IProductDataService;
import com.enn.energy.business.service.IProductDataStatisticsService;
import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.business.bo.ProductDataStatisticsQueryBo;
import com.enn.vo.energy.business.dto.ProductDataStatisticsDto;
import com.enn.vo.energy.business.req.ProductDataReq;

/**
* @author kai.guo
* @version 创建时间：2018年6月11日 下午4:22:34
* @Description 类描述
*/
@SpringBootTest( classes=BusinessApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductDataStatisticsServiceTest {
	
	@Autowired
	private IProductDataStatisticsService productDataStatisticsService;
	
	@Autowired
	private IProductDataService iProductDataService;
	
	@Test
	public void test(){
		
		ProductDataStatisticsQueryBo queryBo=new ProductDataStatisticsQueryBo();
		queryBo.setPageNum(1);
		queryBo.setPageSize(10);
		queryBo.setStartDate("2018-06-01 10:27");
		queryBo.setEndDate("2018-06-23 15:00");
		queryBo.setTimeDimension("3");
//		queryBo.setWorkShopId(29L);
//		queryBo.setCustId(1L);
//		queryBo.setWorkShopId("");
		queryBo.setCustId(4L);
		
		PagedList<ProductDataStatisticsDto> pagedList=productDataStatisticsService.queryProductDataStatisticsInfoByAssignedCondition(queryBo);
		
		System.out.println("msg:==="+JSON.toJSONString(pagedList));
	
	}
	
	@Test
	public void test2(){
		ProductDataReq productDataReq=new ProductDataReq();
		productDataReq.setPageNum(1);
		productDataReq.setPageSize(20);
		productDataReq.setTime("2018-06-14");
		
		iProductDataService.getRespPageList(productDataReq);
	}
	

}
