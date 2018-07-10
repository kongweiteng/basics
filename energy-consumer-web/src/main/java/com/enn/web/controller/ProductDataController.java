package com.enn.web.controller;

import com.enn.constant.Constant;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.po.TeamInfoPo;
import com.enn.vo.energy.business.req.AccountUnitReq;
import com.enn.vo.energy.business.req.ProductDataReq;
import com.enn.vo.energy.business.resp.ProductDataResp;
import com.enn.vo.energy.business.resp.UnitResp;
import com.enn.vo.energy.business.vo.Base;
import com.enn.vo.energy.system.ProductInfo;
import com.enn.web.vo.ProductDataAddReq;
import com.enn.web.vo.ProductDataUpdateReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 生产数据接口
 *
 * @Author: 张洪源
 * @Date: 2018-06-05 20:36
 */
@Api(tags = {"生产数据管理"})
@RestController
@RequestMapping(value = "/web/productData")
public class ProductDataController extends BaseController {

    @ApiOperation(value = "查询车间信息")
    @RequestMapping(value = "/getWorkshopByCustId", method = RequestMethod.POST)
    public EnergyResp<List<UnitResp>> getWorkshopByCustId(Long custId) {
        EnergyResp<List<UnitResp>> resp = new EnergyResp<List<UnitResp>>();
        if (null == custId) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg());
            return resp;
        }
        AccountUnitReq req = new AccountUnitReq();
        req.setId(custId);
        req.setAccountingType(Constant.ACCOUNTING_TYPE_02);
        req.setIsAccount(false);
        return accountUnitService.queryAccountList(req);
    }

    @ApiOperation(value = "查询生产线信息")
    @RequestMapping(value = "/getLineByWorkshopId", method = RequestMethod.POST)
    public EnergyResp<List<UnitResp>> getLineByWorkshopId(Long workshopId) {
        EnergyResp<List<UnitResp>> resp = new EnergyResp<List<UnitResp>>();
        if (null == workshopId) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg());
            return resp;
        }
        AccountUnitReq req = new AccountUnitReq();
        req.setId(workshopId);
        req.setAccountingType(Constant.ACCOUNTING_TYPE_03);
        req.setIsAccount(true);
        return accountUnitService.queryAccountList(req);
    }

    @ApiOperation(value = "查询班组信息")
    @RequestMapping(value = "/getTeamInfoByLineId", method = RequestMethod.POST)
    public EnergyResp<List<TeamInfoPo>> getTeamInfoByLineId(Long lineId) {
        EnergyResp<List<TeamInfoPo>> resp = new EnergyResp<List<TeamInfoPo>>();
        if (null == lineId) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg());
            return resp;
        }
        return teamInfoService.getListByLineId(lineId);
    }

    @ApiOperation(value = "查询产品信息")
    @RequestMapping(value = "/getProductInfoByLineId", method = RequestMethod.POST)
    public EnergyResp<List<ProductInfo>> getProductInfoByLineId(Long custId) {
        EnergyResp<List<ProductInfo>> resp = new EnergyResp<List<ProductInfo>>();
        if (null == custId) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg());
            return resp;
        }
        return productService.findListNoPage(custId);
    }

    @ApiOperation(value = "添加生产数据")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public EnergyResp insert(@RequestBody @Valid ProductDataAddReq productDataAddReq, BindingResult result) {
        EnergyResp resp = new EnergyResp();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        return productDataService.insert(ProductDataAddReq.trans(productDataAddReq));
    }

    @ApiOperation(value = "修改生产数据")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public EnergyResp update(@RequestBody @Valid ProductDataUpdateReq productDataUpdateReq, BindingResult result) {
        EnergyResp resp = new EnergyResp();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        return productDataService.update(ProductDataUpdateReq.trans(productDataUpdateReq));
    }

    @ApiOperation(value = "删除生产数据")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public EnergyResp delete(Long id) {
        EnergyResp resp = new EnergyResp();
        if (null == id) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg());
            return resp;
        }
        return productDataService.delete(id);
    }

    @ApiOperation(value = "查询生产数据")
    @RequestMapping(value = "/getProductDataRespById", method = RequestMethod.POST)
    public EnergyResp<ProductDataResp> getProductDataRespById(Long id) {
        EnergyResp<ProductDataResp> resp = new EnergyResp<ProductDataResp>();
        if (null == id) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg());
            return resp;
        }
        return productDataService.getProductDataRespById(id);
    }

    @ApiOperation(value = "查询生产数据")
    @RequestMapping(value = "/getRespPageList", method = RequestMethod.POST)
    public EnergyResp<PagedList<ProductDataResp>> getRespPageList(@RequestBody @Valid ProductDataReq productDataReq, BindingResult result) {
        EnergyResp<PagedList<ProductDataResp>> resp = new EnergyResp<PagedList<ProductDataResp>>();
        if (result.hasErrors()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), result.getFieldError().getDefaultMessage());
            return resp;
        }
        if (StringUtils.isNotEmpty(productDataReq.getTime())) {
            if (!DateUtil.isValidDate(productDataReq.getTime())) {
                resp.faile(StatusCode.C.getCode(), "时间格式为：yyyy-MM-dd！！！");
            }
        }
        if (null == productDataReq.getPageSize()) {
            productDataReq.setPageSize(Base.DEFAULT_PAGE_SIZE);
        }
        if (null == productDataReq.getPageNum()) {
            productDataReq.setPageNum(Base.DEFAULT_PAGE_NUM);
        }
        String reg = "^[0-9]*[1-9][0-9]*$";
        if (!Pattern.compile(reg).matcher(productDataReq.getPageSize().toString()).find()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "pageSize 格式错误，只能是数字");
            return resp;
        }
        if (!Pattern.compile(reg).matcher(productDataReq.getPageNum().toString()).find()) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg(), "pageNum 格式错误，只能是数字");
            return resp;
        }
        return productDataService.getRespPageList(productDataReq);
    }
}
