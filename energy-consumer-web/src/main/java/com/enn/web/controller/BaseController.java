package com.enn.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.enn.service.business.*;
import com.enn.service.system.IProductService;
import com.enn.service.upload.IUploadService;
import com.enn.service.user.IEntService;
import com.enn.service.user.IUacService;
import com.enn.vo.energy.EnergyResp;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class BaseController<T> {

	protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	/**
	 * 接口返回500常量
	 */
	protected static final Integer CODE=500;
	/**
	 * 查询类型月
	 */
	protected static final String MONTH="month";
	/**
	 * 接口返回500常量
	 */
	protected static final String YEAR="year";

	@Autowired
	protected IElectricityDistributionRoomService electricityDistributionRoomService;

	@Autowired
	protected ICustTransService custTransService;

	@Autowired
	protected IOpentsdbService opentsdbService;

	@Autowired
	protected ICustMeterService custMeterService;

	@Autowired
	protected IAccountUnitService accountUnitService;

	@Autowired
	protected IProductDataService productDataService;

	@Autowired
	protected ITeamInfoService teamInfoService;

	@Autowired
	protected IProductService productService;

	@Autowired
	protected ICustElectricCountService countService;

	@Autowired
	protected IUploadService uploadService;

	@Autowired
	protected IUacService uacService;

	@Autowired
	protected IEntService entService;

	@Autowired
	protected ICustService custService;

	@Autowired
	private Environment env;

	@Value("${file.domain}")
	protected String baseFileUrl;

	protected String getJsonString(T obj) {
		return JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);
	}
	/**
	 * 对象转json字符串，空对象转为null，空字符串转""
	 *
	 * @param energyRespn
	 * @return
	 */
	protected String getJsonString(EnergyResp energyRespn) {
		return JSONObject.toJSONString(energyRespn, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);
	}

	protected String getReadString(Object energyRespn) {
		return JSONObject.toJSONString(energyRespn);
	}

	/**
	 * 对象转json字符串，空对象转为"-"
	 */
	protected String getJsonStringNull(EnergyResp energyRespn) {
		return JSONObject.toJSONString(energyRespn,filter, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);
	}

	protected static ValueFilter filter = new ValueFilter() {
		@Override
		public Object process(Object obj, String s, Object v) {
			if (v == null){
				return "-";
				}
			return v;
		}
	};

	/**
	 * 导出Excel
	 * @param response
	 * @param sheetName
	 * @param wb
	 */
	protected void uploadExecd(HttpServletResponse response, String sheetName, HSSFWorkbook wb){
		String filename = sheetName+".xls";//设置下载时客户端Excel的名称
		try {
			response.setHeader("Content-Type","application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=" + new String(filename.getBytes("GB2312"), "ISO_8859_1"));
			response.setContentType("application/octet-stream");
			response.setCharacterEncoding("UTF-8");
			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * 拼装EXCEL导出的地址
	 */
	public String excelPath(String uploadRespPath,String fileName,String startTime,String endTime){
		String path = baseFileUrl + uploadRespPath + "?filename=" + fileName +
				startTime + "-" + endTime + ".xls";
		return path;
	}

}
