package com.enn.energy.passage.service;

import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.po.CustMeter;
import com.enn.vo.energy.system.*;

public interface IForceChannelService {

    CustMeter findByCustMeter(String meterNo);

    AccountingUnit findByAccountingId(Long accountingId);

    CompanyCust findByCustId(Long custId);

    ElectricInfo findElectricByCustId(Long id);

    ElectricPriceTime findElectricPriceTimeByElectricInfoId(long id,String date);

    SteamInfo findSteamByCustId(Long id);

    SteamLadderPrice findSteamLadderPriceBySteamInfoId(long id, String dosage);
}
