package com.enn.energy.business.service;


import com.baomidou.mybatisplus.service.IService;
import com.enn.vo.energy.business.resp.ElectricityDistributionRoom;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IElectricityDistributionRoomService extends IService<ElectricityDistributionRoom> {

	List<ElectricityDistributionRoom> getElectricityDistributionRoomByCustID(String custID);

	List<ElectricityDistributionRoom> getDistributionRoomByCustID(String custID);

}
