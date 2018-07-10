package com.enn.service.system;


import com.enn.energy.system.common.util.PagedList;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.ElectricInfo;
import com.enn.vo.energy.system.SteamInfo;
import com.enn.vo.energy.system.req.ElectricInfoReq;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 能源基础信息管理接口
 * @author zxj
 * @since 2018-06-06
 */
@FeignClient(value = "energy-zuul-gateway")
public interface IEnergyBasicsService {


    /**
     * 新增或修改电基础信息
     * @param electricInfo
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/energyBasics/addElectric")
    public EnergyResp<Integer> addElectric(@RequestBody ElectricInfo electricInfo);

    /**
     * 删除电基础信息
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/energyBasics/delElectric")
    public EnergyResp<Integer> delElectric(@RequestBody String id);


    /**
     * 查询一条电基础信息
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/energyBasics/getElectricOne")
    public EnergyResp<ElectricInfo> getElectricOne(@RequestBody String id);


    /**
     * 查询多条电基础信息
     * @param infoReq
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/energyBasics/getElectricList")
    public EnergyResp<PagedList<ElectricInfo>> getElectricList(@RequestBody ElectricInfoReq infoReq);

    /**
     * 新增或修改蒸汽基础信息
     * @param steamInfo
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/energyBasics/addSteam")
    public EnergyResp<Integer> addSteam(@RequestBody SteamInfo steamInfo);

    /**
     * 删除蒸汽基础信息
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/energyBasics/delSteam")
    public EnergyResp<Integer> delSteam(@RequestBody String id);

    /**
     * 查询一条蒸汽基础信息
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/energyBasics/getSteamOne")
    public EnergyResp<SteamInfo> getSteamOne(@RequestBody String id);

    /**
     * 查询多条蒸汽基础信息
     * @param infoReq
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/energyBasics/getSteamInfoList")
    public EnergyResp<PagedList<SteamInfo>> getSteamInfoList(@RequestBody ElectricInfoReq infoReq);

    /**
     * 根据客户id及日期，查询电基础信息
     * @param infoReq
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/energyBasics/getElectricInfo")
    public EnergyResp<ElectricInfo> getElectricInfo(@RequestBody ElectricInfoReq infoReq);
}
