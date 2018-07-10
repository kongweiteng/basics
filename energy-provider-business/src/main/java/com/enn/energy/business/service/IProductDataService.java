package com.enn.energy.business.service;

import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.business.bo.ProductDataStatisticsQueryBo;
import com.enn.vo.energy.business.po.ProductDataPo;
import com.enn.vo.energy.business.req.ProductDataReq;
import com.enn.vo.energy.business.resp.ProductDataResp;

import java.util.List;

/**
 * 生产数据Service
 *
 * @Author: 张洪源
 * @Date: 2018-06-05 09:46
 */
public interface IProductDataService {

    /**
     * 添加生产数据
     *
     * @param productDataPo
     * @return
     */
    Integer insert(ProductDataPo productDataPo);

    /**
     * 修改生产数据
     *
     * @param productDataPo
     * @return
     */
    Integer update(ProductDataPo productDataPo);

    /**
     * 删除生产数据
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 查询生产数据
     *
     * @param id
     * @return
     */
    ProductDataResp getProductDataRespById(Long id);

    /**
     * 查询生产数据
     *
     * @param productDataReq
     * @return
     */
    PagedList<ProductDataResp> getRespPageList(ProductDataReq productDataReq);


    /**
     * 查询生产数据
     *
     * @param queryBo
     * @return
     */
    PagedList<ProductDataResp> getRespPageListForProductDataStatistics(ProductDataStatisticsQueryBo queryBo);

}