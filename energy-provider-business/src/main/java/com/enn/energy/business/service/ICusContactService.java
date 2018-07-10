package com.enn.energy.business.service;

import com.enn.vo.energy.business.CusContact;
import com.baomidou.mybatisplus.service.IService;
import com.enn.vo.energy.EnergyResp;

/**
 * <p>
 * 客户联系信息表，（基本信息表同客户联系表是1对多关系） tag：ConsContact 服务类
 * </p>
 *
 * @author kongweiteng
 * @since 2018-05-30
 */
public interface ICusContactService extends IService<CusContact> {

    /**
     * 查询单条数据
     *
     */
    public EnergyResp<CusContact> getOne(String id);

}
