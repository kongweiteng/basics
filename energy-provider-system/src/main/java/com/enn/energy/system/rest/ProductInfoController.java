package com.enn.energy.system.rest;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.PagedList;
import com.enn.energy.system.service.IProductInfoService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.ProductInfo;
import com.enn.vo.energy.system.req.ProductInfoReq;
import com.enn.vo.energy.system.req.WebReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/productInfo")
@Api(value = "产品信息管理接口",tags = {"产品信息管理"})
public class ProductInfoController {
    @Autowired
    private IProductInfoService productInfoService;

    private static final Logger logger = LoggerFactory.getLogger(ProductInfoController.class);


    @RequestMapping(value = "/findList",method = RequestMethod.POST)
    @ApiOperation(value = "查询所有产品信息（分页）")
    public EnergyResp<PagedList<ProductInfo>> findList(@RequestBody ProductInfoReq productInfoReq) {
        EnergyResp<PagedList<ProductInfo>> resp = new EnergyResp<>();
        try {
            PagedList<ProductInfo> productInfoList = productInfoService.selectAll(productInfoReq);
            resp.ok(productInfoList);
        } catch (Exception e) {
            logger.info(productInfoReq.getCustId() + "----查询产品信息出错");
            throw new EnergyException(StatusCode.ERROR.getCode(), "查询产品信息出现异常！", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/findByName",method = RequestMethod.POST)
    @ApiOperation(value = "通过产品名称查询产品信息")
    public EnergyResp<ProductInfo> findByName(@RequestBody WebReq webReq) {
        EnergyResp<ProductInfo> energyResp = new EnergyResp<>();
        try {
            ProductInfo productInfo = productInfoService.selectByName(webReq);
            energyResp.ok(productInfo);
        } catch (Exception e) {
            logger.info(webReq.getName() + "----通过名称查询产品信息出错");
            throw new EnergyException(StatusCode.ERROR.getCode(), "通过名称查询产品信息出现异常！", e.getMessage());
        }
        return energyResp;
    }

    @RequestMapping(value = "/insertProduct", method = RequestMethod.POST)
    @ApiOperation(value = "添加产品信息")
    public EnergyResp<Boolean> insertProductInfo(@RequestBody ProductInfo productInfo) {
        EnergyResp<Boolean> resp = new EnergyResp();
        try {
            boolean result = productInfoService.insertProductInfo(productInfo);
            resp.ok(result);
        } catch (Exception e) {
            logger.info(productInfo.getName() + "----添加产品信息出错");
            throw new EnergyException(StatusCode.ERROR.getCode(), "添加产品信息出现异常！", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/updateProductInfo",method = RequestMethod.POST)
    @ApiOperation(value = "修改产品信息")
    public EnergyResp<Boolean> updateProductInfo(@RequestBody ProductInfo productInfo){
        EnergyResp<Boolean> resp = new EnergyResp();
        try {
            Boolean result = productInfoService.updateProductInfoById(productInfo);
            resp.ok(result);
        } catch (Exception e) {
            logger.info(productInfo.getName() + "----修改产品信息出错");
            throw new EnergyException(StatusCode.ERROR.getCode(), "修改产品信息出现异常！", e.getMessage());
        }
        return resp;
    }


    @RequestMapping(value = "/deleteProductInfo",method = RequestMethod.POST)
    @ApiOperation(value = "删除产品信息")
    public EnergyResp<Boolean> deleteProductInfo(@RequestBody ProductInfo productInfo) {
        EnergyResp<Boolean> resp = new EnergyResp<>();
        try {
            Boolean result = productInfoService.deleteProductInfo(productInfo);
            resp.ok(result);
        } catch (Exception e) {
            logger.info(productInfo.getName() + "----删除产品信息出错");
            throw new EnergyException(StatusCode.ERROR.getCode(), "删除产品信息出现异常！", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/findListNoPage",method = RequestMethod.POST)
    @ApiOperation(value = "查询所有产品信息（无分页）")
    public EnergyResp<List<ProductInfo>> findListNoPage(@RequestBody Long custId) {
        EnergyResp<List<ProductInfo>> resp = new EnergyResp<>();
        try {
            List<ProductInfo> productInfoList = productInfoService.findListNoPage(custId);
            resp.ok(productInfoList);
        } catch (Exception e) {
            logger.info( "----查询产品信息出错");
            throw new EnergyException(StatusCode.ERROR.getCode(), "查询产品信息出现异常！", e.getMessage());
        }
        return resp;
    }

    @RequestMapping(value = "/findById",method = RequestMethod.POST)
    @ApiOperation(value = "通过产品ID查询产品信息")
    public EnergyResp<ProductInfo> findById(@RequestBody Long id) {
        EnergyResp<ProductInfo> energyResp = new EnergyResp<>();
        try {
            ProductInfo productInfo = productInfoService.selectById(id);
            energyResp.ok(productInfo);
        } catch (Exception e) {
            logger.info("----通过id查询产品信息出错");
            throw new EnergyException(StatusCode.ERROR.getCode(), "通过id查询产品信息出现异常！", e.getMessage());
        }
        return energyResp;
    }
 }
