package com.enn.energy.passage.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.enn.vo.energy.system.ElectricInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectricInfoMapper extends BaseMapper<ElectricInfo>{

    ElectricInfo findElectricByCustId(@Param("id") Long id);
}
