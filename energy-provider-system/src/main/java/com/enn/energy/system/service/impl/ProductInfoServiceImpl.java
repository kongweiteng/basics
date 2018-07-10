package com.enn.energy.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.enn.energy.system.common.util.PagedList;
import com.enn.energy.system.dao.ProductInfoMapper;
import com.enn.energy.system.service.IProductInfoService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.ElectricInfo;
import com.enn.vo.energy.system.ProductInfo;
import com.enn.vo.energy.system.req.ProductInfoReq;
import com.enn.vo.energy.system.req.WebReq;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductInfoServiceImpl implements IProductInfoService{
    @Resource
    private ProductInfoMapper productInfoMapper;

    public PagedList<ProductInfo> selectAll(ProductInfoReq productInfoReq) {

        PageHelper.startPage(productInfoReq.getPageNum(), productInfoReq.getPageSize());
        List<ProductInfo> electricInfos = productInfoMapper.selectAll(productInfoReq.getCustId());
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(electricInfos);
        PagedList<ProductInfo> poPagedList = PagedList.newMe(pageInfo.getPageNum(), pageInfo.getPageSize(),
                pageInfo.getTotal(), electricInfos);
        return poPagedList;
    }

    @Override
    public ProductInfo selectByName(WebReq webReq) {
        return productInfoMapper.selectByName(webReq);
    }

    @Override
    public boolean insertProductInfo(ProductInfo productInfo) {
        return productInfoMapper.insertProductInfo(productInfo);
    }

    @Override
    public boolean updateProductInfoById(ProductInfo productInfo) {
        return productInfoMapper.updateProductInfoById(productInfo);
    }

    @Override
    public boolean deleteProductInfo(ProductInfo productInfo) {
         return  productInfoMapper.deleteProductInfo(productInfo);
    }

    @Override
    public List<ProductInfo> findListNoPage(Long custId) {
        return productInfoMapper.selectAll(custId);
    }

    @Override
    public ProductInfo selectById(Long id) {
        return productInfoMapper.selectById(id);
    }
}
