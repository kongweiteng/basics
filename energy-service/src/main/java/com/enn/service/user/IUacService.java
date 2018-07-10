package com.enn.service.user;

import com.enn.vo.energy.user.CheckTicketResp;
import com.enn.vo.energy.user.EnnBosstResp;
import com.enn.vo.energy.user.UserInfoResp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "ENNBOSS-PROVIDER-UAC")
public interface IUacService {


    /**
     * 验证ticket有效性
     * @param ticket
     * @return
     */
    @RequestMapping(value = "/uac/checkTicket/{ticket}", method = RequestMethod.POST)
    EnnBosstResp<CheckTicketResp> checkTicket(@PathVariable("ticket") String ticket);

    /**
     * 通过ticket获取用户信息
     * @param ticket
     * @return
     */
    @RequestMapping(value = "/uac/userInfo/{ticket}", method = RequestMethod.POST)
    EnnBosstResp<UserInfoResp> userInfo(@PathVariable("ticket") String ticket);
}
