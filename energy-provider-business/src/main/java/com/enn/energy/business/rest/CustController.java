package com.enn.energy.business.rest;

import com.enn.energy.business.service.ICustService;
import com.enn.vo.energy.EnergyResp;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cust")
@Api(value = "企业用户接口",tags = {"企业用户"})
public class CustController {

    private static final Logger logger = LoggerFactory.getLogger(CustController.class);

    @Autowired
    private ICustService custService;

    /**
     * 根据 企业关联id（关联平台），查询业务id
     * @param relationId
     * @return
     */
    @PostMapping("/getCustId")
    public EnergyResp<String> getCustId(@RequestBody String relationId){
        logger.info("企业关联id（关联平台），查询业务id："+relationId.trim());

        EnergyResp<String> energyResp = new EnergyResp<>();
        energyResp.ok(custService.getCustId(relationId.replace("\"","")));
        return energyResp;
    }
}
