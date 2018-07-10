package com.enn.service.user;


import com.enn.vo.energy.app.login.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 登录
 */
@FeignClient(value = "ENNBOSS-PROVIDER-COMMON")
public interface IloginService {
    /**
     * 登录接口
     * @return
     */
    @RequestMapping(value = "/app/login", method = RequestMethod.POST)
    AppLoginResp appLogin(AppLoginParam req);


    /**
     * 登出接口
     */
    @RequestMapping(value = "/app/logout", method = RequestMethod.POST)
    LogoutResp logout(@RequestParam("token") String token);


    /**
     * 验证token的有效性
     */
    @RequestMapping(value = "/app/checkToken", method = RequestMethod.POST)
    TokenCheckResp checkToken(@RequestParam("token") String token);

    /**
     *
     * 根据token获取openid
     */
    @RequestMapping(value = "/app/getOpenidByToken", method = RequestMethod.POST)
    OpenIdResp getOpenidByToken(@RequestParam("token") String token);
}