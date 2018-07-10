package com.enn.service.system;

import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.Dict;
import com.enn.vo.energy.system.DictTemp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 字典数据
 */
@FeignClient(value = "energy-zuul-gateway")
public interface IDictService {

    /**
     * 根据id查询字典数据
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/dict/get")
    EnergyResp<Dict> get(@RequestParam("id") String id);

    /**
     * 根据id查询单条数据
     *
     * @param dict
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/dict/findOne")
    EnergyResp<Dict> findOne(Dict dict);

    /**
     * 根据字典类型和字典值查询字典标签
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/dict/getLabelByTypeAndValue")
    EnergyResp<Dict> getDictLabelByTypeAndValue(DictTemp temp);

    /** t
     * 根据字典类型和字典标签查询字典值
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/dict/getValueByTypeAndLabel")
    EnergyResp<Dict> getDictValueByTypeAndLabel(DictTemp temp);

    /**
     * 根据字典类型查询字典数据
     * @param type 字典类型
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/energy-proxy/energy-provider-system/dict/getDictList")
    EnergyResp<List<Dict>> getDictList(@RequestParam("type") String type);
}
