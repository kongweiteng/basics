package com.enn.energy.business.service;

import com.enn.vo.energy.business.po.TeamInfoPo;

import java.util.List;

/**
 * 班组信息Service
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 13:33
 */
public interface ITeamInfoService {

    /**
     * 根据生产线ID查询班组信息
     *
     * @param lineId
     * @return
     */
    List<TeamInfoPo> getListByLineId(Long lineId);
}
