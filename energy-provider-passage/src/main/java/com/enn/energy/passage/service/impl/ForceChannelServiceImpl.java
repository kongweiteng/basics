package com.enn.energy.passage.service.impl;

import com.enn.energy.passage.dao.*;
import com.enn.energy.passage.service.IForceChannelService;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.CustMeter;
import com.enn.vo.energy.system.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ForceChannelServiceImpl implements IForceChannelService{

    @Autowired
    private CustMeterMapper custMeterMapper;
    @Autowired
    private AccountingUnitMapper accountingUnitMapper;
    @Autowired
    private CompanyCustMapper companyCustMapper;
    @Autowired
    private ElectricInfoMapper electricInfoMapper;
    @Autowired
    private ElectricPriceTimeMapper electricPriceTimeMapper;
    @Autowired
    private SteamInfoMapper steamInfoMapper;
    @Autowired
    private SteamLadderPriceMapper steamLadderPriceMapper;

    @Override
    public CustMeter findByCustMeter(String meterNo) {
         return custMeterMapper.selectById(meterNo);
    }

    @Override
    public AccountingUnit findByAccountingId(Long accountingId) {
        return accountingUnitMapper.selectById(accountingId);
    }

    @Override
    public CompanyCust findByCustId(Long custId) {
        return companyCustMapper.selectById(custId);
    }

    @Override
    public ElectricInfo findElectricByCustId(Long id) {
        return electricInfoMapper.findElectricByCustId(id);
    }

    @Override
    public ElectricPriceTime findElectricPriceTimeByElectricInfoId(long id, String date) {
        return electricPriceTimeMapper.findElectricPriceTimeByElectricInfoId(id,date);
    }

    @Override
    public SteamInfo findSteamByCustId(Long id) {
        return steamInfoMapper.findSteamByCustId(id);
    }

    @Override
    public SteamLadderPrice findSteamLadderPriceBySteamInfoId(long id, String dosage) {
        return steamLadderPriceMapper.findSteamLadderPriceBySteamInfoId(id,dosage);
    }
}
