package com.enn.service.business;

import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.po.ProductDataPo;
import com.enn.vo.energy.business.po.TeamInfoPo;
import com.enn.vo.energy.business.req.ProductDataReq;
import com.enn.vo.energy.business.resp.ProductDataResp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 生产数据服务
 *
 * @Author: 张洪源
 * @Date: 2018-06-05 20:32
 */
@FeignClient(value = "energy-zuul-gateway")
public interface IProductDataService {

    /**
     * 添加生产数据
     *
     * @param productDataPo
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/productData/insert", method = RequestMethod.POST)
    EnergyResp insert(@RequestBody ProductDataPo productDataPo);

    /**
     * 修改生产数据
     *
     * @param productDataPo
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/productData/update", method = RequestMethod.POST)
    EnergyResp update(@RequestBody ProductDataPo productDataPo);

    /**
     * 删除生产数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/productData/delete", method = RequestMethod.POST)
    EnergyResp delete(@RequestBody Long id);

    /**
     * 查询生产数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/productData/getProductDataRespById", method = RequestMethod.POST)
    EnergyResp<ProductDataResp> getProductDataRespById(@RequestBody Long id);

    /**
     * 查询生产数据
     *
     * @param productDataReq
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/productData/getRespPageList", method = RequestMethod.POST)
    EnergyResp<PagedList<ProductDataResp>> getRespPageList(@RequestBody ProductDataReq productDataReq);
}
