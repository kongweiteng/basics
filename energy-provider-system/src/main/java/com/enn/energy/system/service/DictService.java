package com.enn.energy.system.service;


import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.Dict;

import java.util.List;

/**
 * @Author: 苏盼盼
 * @Date: 2018/4/11
 * @Description:
 * @version: 1.0.0
 */
public interface DictService {

    /**
     * 根据id查询字典数据
     * @param dict
     * @return
     */
    EnergyResp<Dict> findOne(Dict dict);

    /**
     * 根据id查询字典数据
     * @param id
     * @return
     */
    EnergyResp<Dict> findOne(String id);

    /**
     * 根据字典类型和字典值查询字典标签
     *
     * @param type  字典类型
     * @param value 字典值
     * @return
     */
    EnergyResp<Dict> findDictLabelByTypeAndValue(String type, String value);

    /**
     * 根据字典类型和字典标签查询字典值
     *
     * @param type  字典类型
     * @param label 字典标签
     * @return
     */
    EnergyResp<Dict> findDictValueByTypeAndLabel(String type, String label);

    /**
     * 根据字典类型查询字典数据
     *
     * @param type 字典类型
     * @return
     */
    EnergyResp<List<Dict>> findDictByType(String type);
}
