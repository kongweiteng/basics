package com.enn.service.system;

import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.ProductInfo;
import com.enn.vo.energy.system.req.ProductInfoReq;
import com.enn.vo.energy.system.req.WebReq;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "energy-zuul-gateway")
public interface IProductService {
    /**
     * 查询所有产品信息（有分页）
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/productInfo/findList")
    EnergyResp<PagedList<ProductInfo>> findList(@RequestBody ProductInfoReq productInfoReq);

    /**
     * 根据产品名称查询所有产品信息
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/productInfo/findByName")
    public EnergyResp<ProductInfo> findByName(@RequestBody WebReq webReq);

    /**
     * 插入产品信息
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/productInfo/insertProduct")
    EnergyResp<Boolean> insertProductInfo(ProductInfo info);

    /**
     * 修改产品信息
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/productInfo/updateProductInfo")
    EnergyResp<Boolean> updateProductInfo(ProductInfo info);

    /**
     * 删除产品信息
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/productInfo/deleteProductInfo")
    EnergyResp<Boolean> deleteProductInfo(ProductInfo info);

    /**
     * 所有产品信息（无分页）
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/productInfo/findListNoPage")
    EnergyResp<List<ProductInfo>> findListNoPage(@RequestBody Long custId);

    /**
     * 根据产品ID查询所有产品信息
     */

    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/productInfo/findById")
    EnergyResp<ProductInfo> findById(@RequestBody Long id);

}
