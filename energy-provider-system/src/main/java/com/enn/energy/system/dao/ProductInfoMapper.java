package com.enn.energy.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.enn.vo.energy.system.ProductInfo;
import com.enn.vo.energy.system.req.ProductInfoReq;
import com.enn.vo.energy.system.req.WebReq;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

public interface ProductInfoMapper{
    List<ProductInfo> selectAll(Long custId);

    ProductInfo selectByName(WebReq webReq);

    boolean insertProductInfo(ProductInfo productInfo);

    boolean updateProductInfoById(ProductInfo productInfo);

    boolean deleteProductInfo(ProductInfo productInfo);

    ProductInfo selectById(Long id);
}
