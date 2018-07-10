package com.enn.energy.business.rest;

import com.enn.constant.StatusCode;
import com.enn.energy.business.service.ITeamInfoService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.business.po.TeamInfoPo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 班组信息Controller
 *
 * @Author: 张洪源
 * @Date: 2018-06-07 13:42
 */
@Api(tags = {"班组信息管理"})
@RestController
@RequestMapping("/teamInfo")
public class TeamInfoController {

    @Autowired
    private ITeamInfoService teamInfoService;

    @ApiOperation(value = "查询班组信息")
    @RequestMapping(value = "/getListByLineId", method = RequestMethod.POST)
    public EnergyResp<List<TeamInfoPo>> getListByLineId(@RequestBody Long lineId) {
        EnergyResp<List<TeamInfoPo>> resp = new EnergyResp<List<TeamInfoPo>>();
        if (null == lineId) {
            resp.faile(StatusCode.C.getCode(), StatusCode.C.getMsg());
            return resp;
        }
        resp.ok(teamInfoService.getListByLineId(lineId));
        return resp;
    }
}
