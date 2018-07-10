package com.enn.web.controller;

import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.PagedList;
import com.enn.service.system.IProductService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.ProductInfo;
import com.enn.vo.energy.system.req.ProductInfoReq;
import com.enn.vo.energy.system.req.WebReq;
import com.enn.web.vo.WebReqId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/productInfo")
@Api(tags = {"产品信息管理"})
public class ProductInfoController {

    protected static final Logger logger = LoggerFactory.getLogger(ProductInfoController.class);
    @Autowired
    private IProductService productInfoService;

    @ApiOperation(value = "查询公司下所有的产品列表（分页）", notes = "查询公司下所有的产品列表(分页）")
    @RequestMapping(value = "/findList", method = RequestMethod.POST)
    public EnergyResp<PagedList<ProductInfo>> findList(@RequestBody @Valid ProductInfoReq productInfoReq, BindingResult result) {
        logger.info("查询所有产品信息 --- start");
        EnergyResp resp = new EnergyResp();
        if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
        EnergyResp<PagedList<ProductInfo>> energyResp = productInfoService.findList(productInfoReq);
        if (StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
            resp.setData(energyResp.getData());
        } else {
            resp.setCode(energyResp.getCode());
            resp.setMsg(energyResp.getMsg());
        }
        logger.info("查询所有产品信息 --- end");
        return energyResp;
    }

    @ApiOperation(value = "通过产品名称查询产品信息", notes = "通过产品名称查询产品信息")
    @RequestMapping(value = "/findByName", method = RequestMethod.POST)
    public EnergyResp<ProductInfo> findByName(@RequestBody @Valid WebReq webReq, BindingResult result) {

        logger.info("查询所有产品信息 --- start");
        EnergyResp<ProductInfo> resp = new EnergyResp();
        if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
        EnergyResp<ProductInfo> energyResp = productInfoService.findByName(webReq);
        if (StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
            resp.setData(energyResp.getData());
        } else {
            resp.setCode(energyResp.getCode());
            resp.setMsg(energyResp.getMsg());
        }
        logger.info("查询单个产品信息 --- end");
        return energyResp;
    }

    @ApiOperation(value = "添加或修改产品信息", notes = "添加或修改产品信息")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public EnergyResp<Boolean> saveOrUpdate(@RequestBody @Valid ProductInfo productInfo, BindingResult result) {
        EnergyResp<Boolean> resp = new EnergyResp();
        if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
        EnergyResp<Boolean> energyResp = new EnergyResp<>();
        if (productInfo.getId() == null) {
            logger.info("添加产品信息");
            energyResp = productInfoService.insertProductInfo(productInfo);
        } else {
            logger.info("修改所有产品信息");
            energyResp = productInfoService.updateProductInfo(productInfo);
        }
        if (StatusCode.SUCCESS.getCode().equals(energyResp.getCode())) {
            resp.ok(energyResp.getData());
        } else {
            resp.setCode(energyResp.getCode());
            resp.setMsg(energyResp.getMsg());
        }
        return resp;
    }

    @ApiOperation(value = "删除产品信息", notes = "删除产品信息")
    @RequestMapping(value = "/deleteProductInfo", method = RequestMethod.POST)
    public EnergyResp<Boolean> deleteProductInfo(@RequestBody @Valid ProductInfo productInfo, BindingResult result) {
        EnergyResp<Boolean> resp = new EnergyResp();
        if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
        EnergyResp<Boolean> energyResp = productInfoService.deleteProductInfo(productInfo);
        if (StatusCode.SUCCESS.getCode().equals(energyResp.getCode())) {
            resp.ok(energyResp.getData());
        } else {
            resp.setCode(energyResp.getCode());
            resp.setMsg(energyResp.getMsg());
        }
        return resp;
    }


    @ApiOperation(value = "通过ID查询具体商品", notes = "通过ID查询具体商品")
    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public EnergyResp<ProductInfo> findById(@RequestBody @Valid Long id, BindingResult result) {

        logger.info("查询所有产品信息 --- start");
        EnergyResp<ProductInfo> resp = new EnergyResp();
        if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
        EnergyResp<ProductInfo> energyResp = productInfoService.findById(id);
        if (StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
            resp.setData(energyResp.getData());
        } else {
            resp.setCode(energyResp.getCode());
            resp.setMsg(energyResp.getMsg());
        }
        logger.info("查询单个产品信息 --- end");
        return energyResp;
    }

    @RequestMapping(value = "/findListNoPage", method = RequestMethod.POST)
    @ApiOperation(value = "查询公司下所有的产品列表（无分页）", notes = "查询公司下所有的产品列表（无分页用于获取产品名称）")
    public EnergyResp<List<ProductInfo>> findListNoPage(@RequestBody @Valid Long custId, BindingResult result) {

        logger.info("查询所有产品信息 --- start");
        EnergyResp<List<ProductInfo>> resp = new EnergyResp();
        if (result.hasErrors()) {
            resp.setCode(StatusCode.C.getCode());
            resp.setMsg(result.getFieldError().getDefaultMessage());
            return resp;
        }
        EnergyResp<List<ProductInfo>> energyResp = productInfoService.findListNoPage(custId);
        if (StatusCode.SUCCESS.getCode().equals(resp.getCode())) {
            resp.setData(energyResp.getData());
        } else {
            resp.setCode(energyResp.getCode());
            resp.setMsg(energyResp.getMsg());
        }
        logger.info("查询单个产品信息 --- end");
        return resp;
    }


}
