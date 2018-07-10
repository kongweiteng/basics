package com.enn.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.ExcelUtils;
import com.enn.energy.system.common.util.PagedList;
import com.enn.service.business.IProductDataStatisticsFacade;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.dto.DepartUnitDto;
import com.enn.vo.energy.business.dto.ProductDataStatisticsDto;
import com.enn.vo.energy.business.dto.ProductDataStatisticsItemDto;
import com.enn.vo.energy.business.req.SelectElectricityReq;
import com.enn.vo.energy.business.resp.WorkshopResp;
import com.enn.vo.energy.business.upload.UploadResp;
import com.enn.vo.energy.business.vo.ProductDataStatisticsQueryParams;
import com.google.common.collect.Maps;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
* @author kai.guo
* @version 创建时间：2018年6月12日 下午12:38:54
* @Description 生产数据统计
*/
@RestController
@RequestMapping("/web/productDataStatistics")
@Api( tags="产品数据统计-日/月/年")
public class ProductDataStatisticsWebController  extends BaseController {
	
	@Autowired
	private IProductDataStatisticsFacade iProductDataStatisticsFacade;
	
	private static final Logger logger=LoggerFactory.getLogger(ProductDataStatisticsWebController.class);
	
	
	/**
	 * 查询指定企业id的生产车间列表
	 * @param companyId
	 */
	@ApiOperation(value="查询指定企业的生产车间列表", notes="根据企业id查询生产车间列表")
	@RequestMapping(value="/queryProductionDepartment",method=RequestMethod.POST)
	public EnergyResp<List<DepartUnitDto>> queryProductionDepartment(@RequestParam("companyId") Long companyId){
		EnergyResp<List<DepartUnitDto>> resp = new EnergyResp();
		if (null == companyId || "".equals(companyId)) {
			resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg());
            return resp;
        }
		return iProductDataStatisticsFacade.queryProductionDepartment(companyId);
	}
	
	/**
	 * 生产数据统计
	 * @param queryParams
	 * @return
	 */
	@ApiOperation(value="查询指定车间的生产数据信息", notes="查询指定车间的生产数据信息")
	@RequestMapping(value="/queryProductDataStatisticsInfo",method=RequestMethod.POST)
	EnergyResp<PagedList<ProductDataStatisticsDto>>	queryProductDataStatisticsInfoByAssignedCondition(@RequestBody @Valid ProductDataStatisticsQueryParams queryParams, BindingResult result){
		EnergyResp<PagedList<ProductDataStatisticsDto>> resp = new EnergyResp();
		if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
		return iProductDataStatisticsFacade.queryProductDataStatisticsInfoByAssignedCondition(queryParams);
	}
	
	
	private LinkedHashMap<String, String> buildExcelRowStatisticsData(ProductDataStatisticsDto dataStatisticsDto){
		LinkedHashMap<String, String> linkedHashMap = Maps.newLinkedHashMap();
		linkedHashMap.put("1", "合计");
		linkedHashMap.put("2", "");
		linkedHashMap.put("3", dataStatisticsDto.getTotalNumber().toString());
		linkedHashMap.put("4", dataStatisticsDto.getTotalWeight().toString());
		return linkedHashMap;
	}
	
	private LinkedHashMap<String, String> buildExcelRowItemData(ProductDataStatisticsItemDto item){
		LinkedHashMap<String, String> linkedHashMap = Maps.newLinkedHashMap();
		linkedHashMap.put("1", item.getProductName());
		linkedHashMap.put("2", item.getWorkshopName());
		linkedHashMap.put("3", item.getNumber().toString());
		linkedHashMap.put("4", item.getWeight().toString());
		return linkedHashMap;
	}
	
	
	@RequestMapping(value = "/getUploadExcel", method = RequestMethod.POST)
	@ApiOperation(value = "报表EXCEL导出", notes = "报表EXCEL导出")
	public EnergyResp<UploadResp> getUploadExcel(@RequestBody @Valid ProductDataStatisticsQueryParams queryParams, BindingResult result) {
		EnergyResp<UploadResp> uploadRespEnergyResp = new EnergyResp<>();
		if (result.hasErrors()) {
			uploadRespEnergyResp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
			return uploadRespEnergyResp;
		}
		if (queryParams.getStartDate().compareTo(queryParams.getEndDate()) > 0) {
			uploadRespEnergyResp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
			return uploadRespEnergyResp;
		}
		EnergyResp<PagedList<ProductDataStatisticsDto>> energyResp = iProductDataStatisticsFacade.queryProductDataStatisticsInfoByAssignedCondition(queryParams);
		List<ProductDataStatisticsDto> results = energyResp.getData().getData();
		List<LinkedHashMap<String, String>> rows = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(results)) {
			results.stream().forEach(p->{
				p.getDataStatisticsItem().stream().forEach(item->{
					LinkedHashMap<String, String> linkedHashMap = buildExcelRowItemData(item);
					rows.add(linkedHashMap);
				});
				
				LinkedHashMap<String, String> linkedHashMap2 =buildExcelRowStatisticsData(p);
				rows.add(linkedHashMap2);
			});
		}
		String[] tableHeaders = {"产品名称", "车间", "产量", "核算单位"};
		ResponseEntity<byte[]> responseEntity = ExcelUtils.exportExcel(tableHeaders, rows, System.currentTimeMillis() + "", ExcelUtils.ExcelSuffix.xls);
		Map map = new HashMap() {{
			put("obj", responseEntity.getBody());
		}};
		logger.info("将导出文件上传文件服务器");
		UploadResp uploadResp = uploadService.upObj(map, ExcelUtils.ExcelSuffix.xls.toString());
		if (uploadResp.getRetCode() == 0) {
			String path = baseFileUrl + uploadResp.getPath() + "?filename=" + "产品数据统计" +
					queryParams.getStartDate() + "-" + queryParams.getEndDate() + ".xls";
			uploadResp.setPath(path);
			uploadRespEnergyResp.ok(uploadResp);
		} else {
			logger.error("上传文件服务器异常");
		}
		return uploadRespEnergyResp;
	}
	

}
