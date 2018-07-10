package com.enn.energy.system.service.impl;

import com.enn.energy.system.common.util.PagedList;
import com.enn.energy.system.dao.SteamInfoMapper;
import com.enn.energy.system.service.SteamInfoService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.SteamInfo;
import com.enn.vo.energy.system.SteamLadderPrice;
import com.enn.vo.energy.system.req.ElectricInfoReq;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SteamInfoServiceImpl implements SteamInfoService {

    @Resource
    private SteamInfoMapper steamInfoMapper;

    /**
     * 新增或修改蒸汽基础信息
     * @param steamInfo
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public EnergyResp<Integer> addSteam(SteamInfo steamInfo) {
        EnergyResp<Integer> energyResp = new EnergyResp<>();
        List<SteamLadderPrice> steamLadderPrices = steamInfo.getSteamLadderPrices();
        if(steamInfo.getId() == 0){
            steamInfoMapper.addSteamInfo(steamInfo);
            if(steamLadderPrices != null){
                for(SteamLadderPrice steamLadderPrice:steamLadderPrices){
                    steamLadderPrice.setSteamId(steamInfo.getId());
                    steamInfoMapper.addSteamLadderPrice(steamLadderPrice);
                }
            }
        }else{
            steamInfoMapper.updateSteamInfo(steamInfo);
            if(steamLadderPrices != null){
                for(SteamLadderPrice steamLadderPrice:steamLadderPrices){
                    steamInfoMapper.updateSteamLadderPrice(steamLadderPrice);
                }
            }
        }
        energyResp.ok(1);
        return energyResp;
    }

    /**
     * 删除蒸汽基础信息
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public EnergyResp<Integer> delSteam(String id) {
        EnergyResp<Integer> energyResp = new EnergyResp<>();
        steamInfoMapper.delSteamInfo(id);
        steamInfoMapper.delSteamLadderPrice(id);
        energyResp.ok(1);
        return energyResp;
    }

    /**
     * 查询一条蒸汽基础信息
     * @param id
     * @return
     */
    @Override
    public EnergyResp<SteamInfo> getSteamOne(String id) {
        EnergyResp<SteamInfo> energyResp = new EnergyResp<>();
        SteamInfo info = steamInfoMapper.getSteamInfoOne(id);
        if(info != null){
            info.setSteamLadderPrices(steamInfoMapper.getSteamLadderPrices(id));
        }
        energyResp.ok(info);
        return energyResp;
    }

    /**
     * 查询多条蒸汽基础信息
     * @param infoReq
     * @return
     */
    @Override
    public EnergyResp<PagedList<SteamInfo>> getSteamInfoList(ElectricInfoReq infoReq) {
        EnergyResp<PagedList<SteamInfo>> energyResp = new EnergyResp<>();

        PageHelper.startPage(infoReq.getPageNum(), infoReq.getPageSize());
        List<SteamInfo> steamInfos = steamInfoMapper.getSteamInfoList(infoReq);
        for(SteamInfo info:steamInfos){
            info.setSteamLadderPrices(steamInfoMapper.getSteamLadderPrices(info.getId()+""));
        }
        PageInfo<SteamInfo> pageInfo = new PageInfo<>(steamInfos);
        PagedList<SteamInfo> poPagedList = PagedList.newMe(pageInfo.getPageNum(), pageInfo.getPageSize(),
                pageInfo.getTotal(), steamInfos);

        energyResp.ok(poPagedList);
        return energyResp;
    }
}
