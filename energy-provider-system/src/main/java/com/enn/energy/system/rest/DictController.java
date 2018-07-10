package com.enn.energy.system.rest;

import com.enn.energy.system.service.DictService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.Dict;
import com.enn.vo.energy.system.DictTemp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: 苏盼盼
 * @Date: 2018/4/11
 * @Description:字典接口
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/dict")
public class DictController {
    private static final Logger logger = LoggerFactory.getLogger(DictController.class);
    @Resource
    private DictService dictService;

    /**
     * 根据id查询字典数据
     *
     * @param id
     * @return
     */
    @PostMapping("/get")
    public EnergyResp<Dict> get(@RequestBody String id) {
        EnergyResp<Dict> resp = new EnergyResp<>();
        try {
            resp = dictService.findOne(id);
            return resp;
        } catch (Exception e) {
            logger.error("根据id查询字典数据出错，id为 {}", id);
            resp.null_obj(new Dict());
        }
        return resp;
    }

    /**
     * 根据id查询单条数据
     *
     * @param dict
     * @return
     */
    @PostMapping("/findOne")
    public EnergyResp<Dict> findOne(@RequestBody Dict dict) {
        EnergyResp<Dict> resp = new EnergyResp<>();
        try {
            resp = dictService.findOne(dict);
        } catch (Exception e) {
            logger.error("根据id查询字典数据出错，id为 {}", dict.getId());
            resp.null_obj(new Dict());
        }
        return resp;
    }

    /**
     * 根据字典类型和字典值查询字典标签
     * @return
     */
    @PostMapping("/getLabelByTypeAndValue")
    public EnergyResp<Dict> getDictLabelByTypeAndValue(@RequestBody DictTemp temp) {
        EnergyResp<Dict> resp = new EnergyResp<>();
        try {
            if (StringUtils.isNotBlank(temp.getType()) && StringUtils.isNotBlank(temp.getValue())) {
                resp = dictService.findDictLabelByTypeAndValue(temp.getType(), temp.getValue());
                return resp;
            }
        } catch (Exception e) {
            logger.error("根据类型和值查询字典数据出错，类型为 {}，值为 {}", temp.getType(), temp.getValue());
            resp.null_obj(new Dict());
        }
        return resp;
    }

    /**
     * 根据字典类型和字典标签查询字典值
     * @return
     */
    @PostMapping("/getValueByTypeAndLabel")
    public EnergyResp<Dict> getDictValueByTypeAndLabel(@RequestBody DictTemp temp) {
        EnergyResp<Dict> resp = new EnergyResp<>();
        try {
            if (StringUtils.isNotBlank(temp.getType()) && StringUtils.isNotBlank(temp.getLabel())) {
                resp = dictService.findDictValueByTypeAndLabel(temp.getType(), temp.getLabel());
                return resp;
            }
        } catch (Exception e) {
            logger.error("根据类型和标签查询字典数据， 类型为 {}，标签为 {}", temp.getType(), temp.getLabel());
            resp.null_obj(new Dict());
        }
        return resp;
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param type 字典类型
     * @return
     */
    @PostMapping("/getDictList")
    public EnergyResp<List<Dict>> getDictList(@RequestParam("type")  String type) {
        return dictService.findDictByType(type);
    }
}
