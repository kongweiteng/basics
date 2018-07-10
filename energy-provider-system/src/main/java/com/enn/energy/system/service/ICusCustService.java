package com.enn.energy.system.service;

import com.enn.vo.energy.system.CusCust;
import com.baomidou.mybatisplus.service.IService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;

/**
 * <p>
 * 客户基本信息表 tag：CustomerInfo 服务类
 * </p>
 *
 * @author kongweiteng
 * @since 2018-05-29
 */
public interface ICusCustService extends IService<CusCust> {
    /**
     * 查询单条数据
     * @param id
     * @return
     */
    public EnergyResp<CusCust> selectOne(String id);
    public EnergyResp<ListResp<CusCust>> selectAll();
}
