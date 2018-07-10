package com.enn.energy.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.system.ProductInfo;
import com.enn.vo.energy.system.req.ProductInfoReq;
import com.enn.vo.energy.system.req.WebReq;

import java.util.List;

public interface IProductInfoService{

    public PagedList<ProductInfo> selectAll(ProductInfoReq productInfoReq);

    ProductInfo selectByName(WebReq webReq);

    boolean insertProductInfo(ProductInfo productInfo);

    boolean updateProductInfoById(ProductInfo productInfo);

    boolean deleteProductInfo(ProductInfo productInfo);

    List<ProductInfo> findListNoPage(Long custId);

    ProductInfo selectById(Long id);
}
