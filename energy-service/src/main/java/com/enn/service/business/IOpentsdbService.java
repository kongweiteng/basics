package com.enn.service.business;

import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.business.req.LastReq;
import com.enn.vo.energy.business.req.LastStaReq;
import com.enn.vo.energy.business.req.SamplDataReq;
import com.enn.vo.energy.business.req.SamplDataStaReq;
import com.enn.vo.energy.business.resp.LastResp;
import com.enn.vo.energy.business.resp.RmiSamplDataResp;
import com.enn.vo.energy.system.CompanyCust;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "energy-zuul-gateway")
public interface IOpentsdbService {

    /**
     * 采样获取数据
     * @param samplDataReq
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/opentsdb/sampl/downsample")
    public EnergyResp<ListResp<RmiSamplDataResp>> getSamplData(SamplDataReq samplDataReq);
    /**
     * 采样获取数据--不同站点
     * @param samplDataStaReq
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/opentsdb/getSamplDataStaReq")
    public EnergyResp<ListResp<RmiSamplDataResp>> getSamplDataStaReq(SamplDataStaReq samplDataStaReq);

    /**
     * 最后一条数据
     * @param last
     * @return
     *
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/opentsdb/last")
    public EnergyResp<ListResp<LastResp>> getLast(LastReq last);


    /**
     * 最后一条数据---不同站点
     * @param last
     * @return
     *
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-business/opentsdb/staLast")
    public EnergyResp<ListResp<LastResp>> getLast(LastStaReq last);

}
