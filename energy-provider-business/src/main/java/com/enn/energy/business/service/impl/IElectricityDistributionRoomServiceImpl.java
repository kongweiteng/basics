package com.enn.energy.business.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.enn.energy.business.dao.ElectricityDistributionRoomMapper;
import com.enn.energy.business.service.IElectricityDistributionRoomService;
import com.enn.vo.energy.business.resp.ElectricityDistributionRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class IElectricityDistributionRoomServiceImpl extends ServiceImpl<ElectricityDistributionRoomMapper, ElectricityDistributionRoom> implements IElectricityDistributionRoomService {

	@Resource
	private ElectricityDistributionRoomMapper electricityDistributionRoomMapper;

	@Override
	public List<ElectricityDistributionRoom> getElectricityDistributionRoomByCustID(String custID) {
		EntityWrapper<ElectricityDistributionRoom> wrapper=new EntityWrapper<>();
		wrapper.eq("cust_id",custID);
		wrapper.eq("parent_id",0);
		wrapper.eq("del_flag",0);
		List<ElectricityDistributionRoom> electricityDistributionRooms = electricityDistributionRoomMapper.selectList(wrapper);
		return electricityDistributionRooms;
	}


	public List<ElectricityDistributionRoom> getDistributionRoomByCustID(String custID){
		EntityWrapper<ElectricityDistributionRoom> wrapper=new EntityWrapper<>();
		wrapper.eq("cust_id",custID);
		wrapper.eq("del_flag",0);
		List<ElectricityDistributionRoom> electricityDistributionRooms = electricityDistributionRoomMapper.selectList(wrapper);
		return electricityDistributionRooms;
	}
}
