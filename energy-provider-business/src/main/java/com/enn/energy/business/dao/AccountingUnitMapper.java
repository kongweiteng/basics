package com.enn.energy.business.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.enn.vo.energy.business.po.AccountingUnit;
import com.enn.vo.energy.business.vo.SteamUnitVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author kongweiteng
 * @since 2018-06-06
 */
@Repository
public interface AccountingUnitMapper extends BaseMapper<AccountingUnit> {

    List<SteamUnitVo> workShops(@Param("custID") Integer custID);

    List<AccountingUnit> getUnitByMete (String meter);


    List<AccountingUnit> findLineNameByLineId(@Param("lineIdList") List<Long> lineIdList);
}