package com.enn.energy.system.rest;


import com.enn.vo.energy.system.CusCust;
import com.enn.energy.system.service.ICusCustService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.ListResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 客户基本信息表 tag：CustomerInfo 前端控制器
 * </p>
 *
 * @author kongweiteng
 * @since 2018-05-29
 */
@ApiIgnore
@RestController
@RequestMapping("/cusCust")
@Api(tags = {"查询demo"})
public class CusCustController {
    @Autowired
    private ICusCustService cusCustService;

    @ApiOperation(value = "单条", notes = "one")
    @RequestMapping(value = "/one", method = RequestMethod.POST)
    public EnergyResp<CusCust> get() {

        return cusCustService.selectOne("134");
    }

    @ApiOperation(value = "列表", notes = "list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public EnergyResp<ListResp<CusCust>> list() {
        EnergyResp<ListResp<CusCust>> resp = cusCustService.selectAll();
        return resp;
    }


}
