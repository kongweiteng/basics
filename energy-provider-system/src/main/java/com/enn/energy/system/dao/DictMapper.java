package com.enn.energy.system.dao;

import com.enn.vo.energy.system.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 苏盼盼
 * @Date: 2018/4/11
 * @Description:
 * @version: 1.0.0
 */
public interface DictMapper {
    /**
     * 根据id查询字典数据
     * @param dict
     * @return
     */
    Dict findOne(Dict dict);

    /**
     * 根据id查询字典数据
     * @param id
     * @return
     */
    Dict findOne(String id);

    /**
     * 根据字典类型和字典值查询字典标签
     *
     * @param type
     * @param value
     * @return
     */
    List<Dict> findDictLabelByTypeAndValue(@Param("type") String type, @Param("value") String value);

    /**
     * 根据字典类型和字典标签查询字典值
     *
     * @param type  字典类型
     * @param label 字典标签
     * @return
     */
    List<Dict> findDictValueByTypeAndLabel(@Param("type") String type, @Param("label") String label);

    /**
     * 根据字典类型查询字典数据
     *
     * @param type 字典类型
     * @return
     */
    List<Dict> findDictByType(String type);
}
