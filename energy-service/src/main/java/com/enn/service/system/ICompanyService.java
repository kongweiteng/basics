package com.enn.service.system;

import com.enn.vo.energy.DefaultReq;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import com.enn.vo.energy.system.CompanyCust;
import com.enn.vo.energy.system.NodeTree;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 字典数据
 */
@FeignClient(value = "energy-zuul-gateway")
public interface ICompanyService {

    /**
     * 根据id查询企业信息
     * @param de
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/companyCust/one")
    EnergyResp<CompanyCust> getOne(DefaultReq de);

    /**
     * 根据企业id查询组织机构树
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/companyCust/tree")
    EnergyResp<NodeTree> getCompanyTree(DefaultReq de);

    /**
     * 根据id查询企业信息，以及旗下所有子公司的信息
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/companyCust/getAllCompany")
    EnergyResp<ListResp<CompanyCust>> getAllCompany(DefaultReq de);
}
