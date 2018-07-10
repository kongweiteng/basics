package com.enn.energy.business.rest;

import com.enn.constant.StatusCode;
import com.enn.energy.business.service.IProductDataService;
import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.po.ProductDataPo;
import com.enn.vo.energy.business.req.ProductDataReq;
import com.enn.vo.energy.business.resp.ProductDataResp;
import com.enn.vo.energy.business.vo.Base;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 生产数据Controller
 *
 * @Author: 张洪源
 * @Date: 2018-06-05 09:49
 */
@Api(tags = {"生产数据管理"})
@RestController
@RequestMapping("/productData")
public class ProductDataController {

    @Autowired
    private IProductDataService productDataService;

    @ApiOperation(value = "添加生产数据")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public EnergyResp insert(@RequestBody @Valid ProductDataPo productDataPo, BindingResult result) {
        EnergyResp resp = new EnergyResp();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (productDataPo.getStartDate().compareTo(productDataPo.getEndDate()) > 0) {
            resp.faile(StatusCode.C.getCode(), "开始时间不能大于结束时间");
            return resp;
        }
        resp.ok(productDataService.insert(productDataPo));
        return resp;
    }

    @ApiOperation(value = "修改生产数据")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public EnergyResp update(@RequestBody ProductDataPo productDataPo) {
        EnergyResp resp = new EnergyResp();
        if (null == productDataPo || null == productDataPo.getId()) {
            resp.faile(StatusCode.C.getCode(), "生产数据ID不能为空！");
            return resp;
        }
        if (StringUtils.isEmpty(productDataPo.getStartDate())) {
            productDataPo.setStartDate(null);
        }
        if (StringUtils.isEmpty(productDataPo.getEndDate())) {
            productDataPo.setEndDate(null);
        }
        resp.ok(productDataService.update(productDataPo));
        return resp;
    }

    @ApiOperation(value = "删除生产数据")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public EnergyResp delete(@RequestBody Long id) {
        EnergyResp resp = new EnergyResp();
        if (null == id) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg());
            return resp;
        }
        resp.ok(productDataService.delete(id));
        return resp;
    }

    @ApiOperation(value = "查询生产数据")
    @RequestMapping(value = "/getProductDataRespById", method = RequestMethod.POST)
    public EnergyResp<ProductDataResp> getProductDataRespById(@RequestBody Long id) {
        EnergyResp<ProductDataResp> resp = new EnergyResp<ProductDataResp>();
        if (null == id) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg());
            return resp;
        }
        resp.ok(productDataService.getProductDataRespById(id));
        return resp;
    }

    @ApiOperation(value = "查询生产数据")
    @RequestMapping(value = "/getRespPageList", method = RequestMethod.POST)
    public EnergyResp<PagedList<ProductDataResp>> getRespPageList(@RequestBody @Valid ProductDataReq productDataReq, BindingResult result) {
        EnergyResp resp = new EnergyResp();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (null == productDataReq.getPageSize()) {
            productDataReq.setPageSize(Base.DEFAULT_PAGE_SIZE);
        }
        if (null == productDataReq.getPageNum()) {
            productDataReq.setPageNum(Base.DEFAULT_PAGE_NUM);
        }
        resp.ok(productDataService.getRespPageList(productDataReq));
        return resp;
    }
}
