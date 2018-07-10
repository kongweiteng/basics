package com.enn.service.business;

import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.po.TeamInfoPo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 班组信息管理Service
 *
 * @Author: 张洪源
 * @Date: 2018-06-08 10:06
 */
@FeignClient(value = "energy-zuul-gateway")
public interface ITeamInfoService {

    /**
     * 根据生产线ID查询班组信息
     *
     * @param lineId
     * @return
     */
    @RequestMapping(value = "/energy-proxy/energy-provider-business/teamInfo/getListByLineId", method = RequestMethod.POST)
    EnergyResp<List<TeamInfoPo>> getListByLineId(@RequestBody Long lineId);
}
