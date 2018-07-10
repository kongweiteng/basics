package com.enn.energy.system.service.impl;

import com.enn.energy.system.dao.DictMapper;
import com.enn.energy.system.service.DictService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.Dict;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: 苏盼盼
 * @Date: 2018/4/11
 * @Description:
 * @version: 1.0.0
 */
@Service
public class DictServiceImpl implements DictService {
    @Resource
    private DictMapper dictMapper;
    private static final Logger logger = LoggerFactory.getLogger(DictServiceImpl.class);

    /**
     * 根据id查询字典数据
     *
     * @param dict
     * @return
     */
    @Override
    public EnergyResp<Dict> findOne(Dict dict) {
        Dict param = dictMapper.findOne(dict);
        EnergyResp<Dict> resp = EnergyResp.getRespObj(param);
        return resp;
    }

    /**
     * 根据id查询字典数据
     *
     * @param id
     * @return
     */
    @Override
    public EnergyResp<Dict> findOne(String id) {
        Dict param = dictMapper.findOne(id);
        EnergyResp<Dict> resp = EnergyResp.getRespObj(param);
        return resp;
    }

    /**
     * 根据字典类型和字典值查询字典标签
     *
     * @param type  字典类型
     * @param value 字典值
     * @return
     */
    @Override
    public EnergyResp<Dict> findDictLabelByTypeAndValue(String type, String value) {
        EnergyResp<Dict> resp = new EnergyResp<>();
        List<Dict> list = dictMapper.findDictLabelByTypeAndValue(type, value);
        if (list != null && list.size() > 0) {
            resp = EnergyResp.getRespObj(list.get(0));
        } else {
            resp.null_obj(null);
        }
        return resp;
    }

    /**
     * 根据字典类型和字典标签查询字典值
     *
     * @param type  字典类型
     * @param label 字典标签
     * @return
     */
    @Override
    public EnergyResp<Dict> findDictValueByTypeAndLabel(String type, String label) {
        EnergyResp<Dict> resp = new EnergyResp<>();
        List<Dict> list = dictMapper.findDictValueByTypeAndLabel(type, label);
        if (list != null && list.size() > 0) {
            resp = EnergyResp.getRespObj(list.get(0));
        } else {
            resp.null_obj(null);
        }
        return resp;
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param type 字典类型
     * @return
     */
    @Override
    public EnergyResp<List<Dict>> findDictByType(String type) {
        EnergyResp<List<Dict>> resp = new EnergyResp<>();
        List<Dict> list = Lists.newArrayList();
        try {
            list = dictMapper.findDictByType(type);
        } catch (Exception e) {
            logger.error("根据类型查询字典列表信息错误，类型为 {}", type);
        }
        resp = EnergyResp.getRespList(list);
        return resp;
    }

}
