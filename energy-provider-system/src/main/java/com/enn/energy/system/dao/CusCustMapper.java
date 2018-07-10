package com.enn.energy.system.dao;

import com.enn.vo.energy.system.CusCust;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
  * 客户基本信息表 tag：CustomerInfo Mapper 接口
 * </p>
 *
 * @author kongweiteng
 * @since 2018-05-29
 */
public interface CusCustMapper extends BaseMapper<CusCust> {

    public List<CusCust> getAll();
}