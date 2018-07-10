package com.enn.energy.business.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.enn.energy.business.dao.TeamInfoMapper;
import com.enn.energy.business.service.ITeamInfoService;
import com.enn.vo.energy.business.po.TeamInfoPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 班组信息ServiceImpl
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 13:34
 */
@Service
public class TeamInfoServiceImpl implements ITeamInfoService {

    @Autowired
    private TeamInfoMapper teamInfoMapper;

    /**
     * 根据生产线ID查询班组信息
     *
     * @param lineId
     * @return
     */
    @Override
    public List<TeamInfoPo> getListByLineId(Long lineId) {
        if (null == lineId) {
            return null;
        }
        EntityWrapper<TeamInfoPo> wrapper = new EntityWrapper<>();
        wrapper.eq("line_id", lineId);
        return teamInfoMapper.selectList(wrapper);
    }
}
