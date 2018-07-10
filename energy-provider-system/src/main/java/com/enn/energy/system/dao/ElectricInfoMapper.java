package com.enn.energy.system.dao;

import com.enn.vo.energy.system.ElectricInfo;
import com.enn.vo.energy.system.ElectricPriceTime;
import com.enn.vo.energy.system.req.ElectricInfoReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ElectricInfoMapper {


    /**
     * 新增电基础信息
     * @param electricInfo
     * @return
     */
    int addElectricInfo(ElectricInfo electricInfo);

    /**
     *  新增电基础信息价格起始时间
     * @param electricPriceTime
     * @return
     */
    int addElectricPriceTime(ElectricPriceTime electricPriceTime);

    /**
     * 修改电基础信息
     * @param electricInfo
     * @return
     */
    int updateElectricInfo(ElectricInfo electricInfo);

    /**
     * 修改电基础信息价格起始时间
     * @param electricPriceTime
     * @return
     */
    int updateElectricPriceTime(ElectricPriceTime electricPriceTime);

    /**
     * 删除电基础信息
     * @param id
     * @return
     */
    int delElectricInfo(@Param("id") String id);

    /**
     * 删除电基础信息价格起始时间
     * @param electricInfoId
     * @return
     */
    int delElectricPriceTime(@Param("electricInfoId") String electricInfoId);

    /**
     * 查询一条电基础信息
     * @param id
     * @return
     */
    ElectricInfo getElectricOne(@Param("id") String id);

    /**
     * 查询多条电基础信息
     * @param infoReq
     * @return
     */
    List<ElectricInfo> getElectricList(ElectricInfoReq infoReq);

    /**
     * 查询多条电基础信息价格起始时间
     * @param electricInfoId 电基础信息id
     * @return
     */
    List<ElectricPriceTime> getElectricPriceTimes(@Param("electricInfoId") String electricInfoId);
}
