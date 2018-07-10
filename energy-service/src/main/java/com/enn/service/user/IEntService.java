package com.enn.service.user;

import com.enn.vo.energy.app.login.EntParam;
import com.enn.vo.energy.app.login.EntResp;
import com.enn.vo.energy.user.EnnBossCrm;
import com.enn.vo.energy.user.EnnBosstResp;
import com.enn.vo.energy.user.EntInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "ENNBOSS-PROVIDER-CRM")
public interface IEntService {

    /**
     * 	登录check企业注册信息(获取用户信息)
     * @param openid
     * @return
     */
    @RequestMapping(value = "/index/userAndEnt", method = RequestMethod.POST)
    EnnBossCrm userAndEnt(@RequestParam("openid") String openid);


    @RequestMapping(value = "/index/entInfo", method = RequestMethod.POST)
    EnnBossCrm entInfo(@RequestParam("openid") String openid);

    /**
     * 登录后获取企业信息
     */
    @RequestMapping(value = "/index/checkEnt", method = RequestMethod.POST)
    EntResp getEnt(EntParam entParam);
}
