package com.enn.energy.business.dao;

import com.enn.vo.energy.business.CusContact;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
  * 客户联系信息表，（基本信息表同客户联系表是1对多关系） tag：ConsContact Mapper 接口
 * </p>
 *
 * @author kongweiteng
 * @since 2018-05-30
 */
@Repository
public interface CusContactMapper extends BaseMapper<CusContact> {

}