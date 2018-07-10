package com.enn.energy.passage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.enn.vo.energy.system.ElectricPriceTime;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectricPriceTimeMapper extends BaseMapper<ElectricPriceTime>{

    @Select("SELECT e.id,e.electric_info_id,e.type,e.start_date,e.end_date,e.del_flag from electric_price_time e where electric_info_id=#{id} and start_date <= #{date} and end_date >= #{date}")
    ElectricPriceTime findElectricPriceTimeByElectricInfoId(@Param("id") long id, @Param("date") String date);
}
