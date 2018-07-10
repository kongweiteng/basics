package com.enn.energy.business.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.enn.vo.energy.business.condition.ProductDataConditon;
import com.enn.vo.energy.business.po.ProductDataPo;
import com.enn.vo.energy.business.req.ProductDataQueryReq;
import com.enn.vo.energy.business.resp.ProductDataResp;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 生产数据Mapper
 *
 * @Author: 张洪源
 * @Date: 2018-06-05 09:45
 */
@Repository
public interface ProductDataMapper extends BaseMapper<ProductDataPo> {

    /**
     * 查询生产数据
     *
     * @param productDataQueryReq
     * @return
     */
    List<ProductDataResp> getList(ProductDataQueryReq productDataQueryReq);

    /**
     * 查询生产数据
     *
     * @param id
     * @return
     */
    ProductDataResp getRespById(@Param("id") Long id);

    /**
     * 根据产品id查询时间区间的数据
     * @param startTime
     * @param endTime
     * @param id
     * @return
     */
    List<ProductDataPo> findLineByProductId(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("id") long id);
    
    
    /**
     * 查询生产数据
     *
     * @param productDataPo
     * @return
     */
    List<ProductDataResp> getListForProductDataStatistics(ProductDataConditon condition);
    
    
    /**
     * 统计生产数据
     * @param condition
     * @return
     */
    Integer totalForProductDataStatistics(ProductDataConditon condition);
    
}
