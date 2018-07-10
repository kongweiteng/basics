package com.enn.energy.business.service.impl;

import com.enn.constant.DelFlag;
import com.enn.energy.business.dao.ProductDataMapper;
import com.enn.energy.business.service.IProductDataService;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.PageUtil;
import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.business.bo.ProductDataStatisticsQueryBo;
import com.enn.vo.energy.business.condition.ProductDataConditon;
import com.enn.vo.energy.business.po.ProductDataPo;
import com.enn.vo.energy.business.req.ProductDataQueryReq;
import com.enn.vo.energy.business.req.ProductDataReq;
import com.enn.vo.energy.business.resp.ProductDataResp;
import com.enn.vo.energy.common.enums.DateDimensionEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 生产数据ServiceImpl
 *
 * @Author: 张洪源
 * @Date: 2018-06-05 09:48
 */
@Service
public class ProductDataServiceImpl implements IProductDataService {

    @Autowired
    private ProductDataMapper productDataMapper;
    
    /**
     * 添加生产数据
     *
     * @param productDataPo
     * @return
     */
    @Override
    public Integer insert(ProductDataPo productDataPo) {
        if (null == productDataPo) {
            return 0;
        }
        return productDataMapper.insert(productDataPo);
    }

    /**
     * 修改生产数据
     *
     * @param productDataPo
     * @return
     */
    @Override
    public Integer update(ProductDataPo productDataPo) {
        if (null == productDataPo || null == productDataPo.getId()) {
            return 0;
        }
        return productDataMapper.updateById(productDataPo);
    }

    /**
     * 删除生产数据
     *
     * @param id
     * @return
     */
    @Override
    public Integer delete(Long id) {
        if (null == id) {
            return 0;
        }
        ProductDataPo productData = new ProductDataPo();
        productData.setId(id);
        //删除标识
        productData.setDelFlag(DelFlag.DELETE.getCode());
        return productDataMapper.updateById(productData);
    }

    /**
     * 查询生产数据
     *
     * @param id
     * @return
     */
    @Override
    public ProductDataResp getProductDataRespById(Long id) {
        if (null == id) {
            return null;
        }
        return productDataMapper.getRespById(id);
    }

    /**
     * 查询生产数据
     *
     * @param productDataReq
     * @return
     */
    @Override
    public PagedList<ProductDataResp> getRespPageList(ProductDataReq productDataReq) {
        ProductDataQueryReq productDataQueryReq = new ProductDataQueryReq();
        productDataQueryReq.setPageNum(productDataReq.getPageNum());
        productDataQueryReq.setPageSize(productDataReq.getPageSize());
        productDataQueryReq.setCustId(productDataReq.getCustId());
        if (null != productDataReq && StringUtils.isNotEmpty(productDataReq.getTime())) {
            productDataQueryReq.setStartDate(productDataReq.getTime() + " 23:59:59");
            productDataQueryReq.setEndDate(productDataReq.getTime() + " 00:00:00");
        }
        PageHelper.startPage(productDataQueryReq.getPageNum(), productDataQueryReq.getPageSize());
        List<ProductDataResp> productDataRespList = productDataMapper.getList(productDataQueryReq);
        PageInfo<ProductDataResp> pageInfo = new PageInfo<>(productDataRespList);
        PagedList<ProductDataResp> pagedList = PagedList.newMe(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), productDataRespList);
        return pagedList;
    }

	@Override
	public PagedList<ProductDataResp> getRespPageListForProductDataStatistics(ProductDataStatisticsQueryBo queryBo) {
		
		ProductDataConditon condition=new ProductDataConditon();
		
		if(queryBo.getTimeDimension().equals(DateDimensionEnum.DAY_DATE_DIMENSITON.getTimeDemension())){
			Date startDate=DateUtil.parse(queryBo.getStartDate(), "yyyy-MM-dd HH:mm");
			Date endDate=DateUtil.parse(queryBo.getEndDate(), "yyyy-MM-dd HH:mm");
			condition.setStartDate(DateUtil.format(startDate, "yyyy-MM-dd HH:mm"));
			condition.setEndDate(DateUtil.format(endDate, "yyyy-MM-dd HH:mm"));
			condition.setFormatHour("%Y-%m-%d %H:%i");
		}else if(queryBo.getTimeDimension().equals(DateDimensionEnum.MONTH_DATE_DIMENSION.getTimeDemension())){
			Date startDate=DateUtil.parse(queryBo.getStartDate(), "yyyy-MM-dd");
			Date endDate=DateUtil.parse(queryBo.getEndDate(), "yyyy-MM-dd");
			condition.setStartDate(DateUtil.format(startDate, "yyyy-MM-dd"));
			condition.setEndDate(DateUtil.format(endDate, "yyyy-MM-dd"));
			condition.setFormatHour("%Y-%m-%d");
		}else if(queryBo.getTimeDimension().equals(DateDimensionEnum.YEAR_DATE_DIMENSION.getTimeDemension())){
			Date startDate=DateUtil.parse(queryBo.getStartDate(), "yyyy-MM");
			Date endDate=DateUtil.parse(queryBo.getEndDate(), "yyyy-MM");
			condition.setStartDate(DateUtil.format(startDate, "yyyy-MM"));
			condition.setEndDate(DateUtil.format(endDate, "yyyy-MM"));
			condition.setFormatHour("%Y-%m");
		}
		condition.setWorkshopId(queryBo.getWorkShopId());
		condition.setCustId(queryBo.getCustId());
		
		Integer count=productDataMapper.totalForProductDataStatistics(condition);
		
		/*Integer pageStart=PageUtil.calculateLimitStart(queryBo.getPageNum(), queryBo.getPageSize());
		condition.setPageStart(pageStart);
		condition.setPageCustomSize(queryBo.getPageSize());*/
		List<ProductDataResp> productDataRespList = productDataMapper.getListForProductDataStatistics(condition);
		
        PagedList<ProductDataResp> pagedList = PagedList.newMe(1, count, count, productDataRespList);
        return pagedList;
	}

}
