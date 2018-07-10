package com.enn.energy.system.service.impl;

import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.PagedList;
import com.enn.energy.system.dao.ElectricInfoMapper;
import com.enn.energy.system.service.ElectricInfoService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.ElectricInfo;
import com.enn.vo.energy.system.ElectricPriceTime;
import com.enn.vo.energy.system.req.ElectricInfoReq;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ElectricInfoServiceImpl implements ElectricInfoService {

    @Resource
    private ElectricInfoMapper electricInfoMapper;

    /**
     * 新增或修改电基础信息
     * @param electricInfo
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public EnergyResp<Integer>  addElectric(ElectricInfo electricInfo) {
        EnergyResp<Integer> energyResp = new EnergyResp<>();
        List<ElectricPriceTime> electricPriceTimes = electricInfo.getElectricPriceTimes();
        if(electricInfo.getId() == 0){
            electricInfoMapper.addElectricInfo(electricInfo);
            if(electricPriceTimes != null) {
                for (ElectricPriceTime priceTime : electricPriceTimes) {
                    priceTime.setElectricInfoId(electricInfo.getId());
                    electricInfoMapper.addElectricPriceTime(priceTime);
                }
            }
        }else{
            electricInfoMapper.updateElectricInfo(electricInfo);
            if(electricPriceTimes != null) {
                for (ElectricPriceTime priceTime : electricPriceTimes) {
                    electricInfoMapper.updateElectricPriceTime(priceTime);
                }
            }
        }
        energyResp.ok(1);
        return energyResp;
    }

    /**
     * 删除电基础信息
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public EnergyResp<Integer>  delElectric(String id) {
        EnergyResp<Integer> energyResp = new EnergyResp<>();
        electricInfoMapper.delElectricInfo(id);
        electricInfoMapper.delElectricPriceTime(id);
        energyResp.ok(1);
        return energyResp;
    }

    /**
     * 查询一条电基础信息
     * @param id
     * @return
     */
    @Override
    public EnergyResp<ElectricInfo> getElectricOne(String id) {
        EnergyResp<ElectricInfo> energyResp = new EnergyResp<>();
        ElectricInfo info = electricInfoMapper.getElectricOne(id);
        if(info !=null) {
            info.setElectricPriceTimes(electricInfoMapper.getElectricPriceTimes(id));
        }
        energyResp.ok(info);
        return energyResp;
    }

    /**
     * 查询多条电基础信息
     * @param infoReq
     * @return
     */
    @Override
    public EnergyResp<PagedList<ElectricInfo>> getElectricList(ElectricInfoReq infoReq) {
        EnergyResp<PagedList<ElectricInfo>> energyResp = new EnergyResp<>();

        PageHelper.startPage(infoReq.getPageNum(), infoReq.getPageSize());
        List<ElectricInfo> electricInfos = electricInfoMapper.getElectricList(infoReq);
        PageInfo<ElectricInfo> pageInfo = new PageInfo<>(electricInfos);
        PagedList<ElectricInfo> poPagedList = PagedList.newMe(pageInfo.getPageNum(), pageInfo.getPageSize(),
                pageInfo.getTotal(), electricInfos);
        energyResp.ok(poPagedList);
        return energyResp;
    }

    @Override
    public EnergyResp<ElectricInfo> getElectricInfo(ElectricInfoReq infoReq) {
        EnergyResp<ElectricInfo> energyResp = new EnergyResp<>();
        List<ElectricInfo> electricInfos = electricInfoMapper.getElectricList(infoReq);
        energyResp.ok(null);
        if(electricInfos.size()>0){
            energyResp.ok(electricInfos.get(0));
        }
        return energyResp;
    }
}
