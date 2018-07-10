package com.enn.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.service.business.ICustService;
import com.enn.service.user.IEntService;
import com.enn.service.user.IloginService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.app.login.*;
import com.enn.vo.energy.user.EnnBossCrm;
import com.enn.vo.energy.user.EntInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(value = "登录", tags = {"登录登出"})
@RestController
public class LoginController {

    @Value("${index.mobile}")
    private String mobile;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IloginService loginService;
    @Autowired
    private IEntService entService;
    @Autowired
    private ICustService custService;

    @ApiOperation(value = "登录", notes = "")
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public EnergyResp<LoginResp> login(@RequestBody @Valid LoginReq loginReq, BindingResult result) {
        EnergyResp<LoginResp> energyResp = new EnergyResp();
        if(result.hasErrors()){
            energyResp.faile(StatusCode.C.getCode(),StatusCode.C.getMsg(),result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        AppLoginParam appLoginParam = new AppLoginParam();
        appLoginParam.setLoginName(loginReq.getUserName());
        appLoginParam.setPwd(loginReq.getPassword());
        appLoginParam.setAppType(3);
        appLoginParam.setChannel(loginReq.getEquipType());
        appLoginParam.setUsToken(loginReq.getEquipment());
        AppLoginResp appLoginResp = null;
        try {
            appLoginResp = loginService.appLogin(appLoginParam);
        } catch (Exception e) {
            //e.printStackTrace();
            logger.info("请求登录服务出现异常！！");
            throw new EnergyException(StatusCode.E_C.getCode(),StatusCode.E_C.getMsg(),"请求登录服务出现异常!!"+e.getMessage());
        }
        if(appLoginResp.getRetCode()!=0){
            energyResp.faile(appLoginResp.getRetCode(),appLoginResp.getMsg(),appLoginResp.getMsg());
            return energyResp;
        }
        logger.info("请求登录接口返回信息："+appLoginResp.toString());
        LoginResp loginResp = new LoginResp();
        loginResp.setToken(appLoginResp.getToken());
        loginResp.setContact(mobile);
        //根据登录信息获取用户企业信息
        EntParam entParam = new EntParam();
        entParam.setOpenid(appLoginResp.getOpenid());
        EnnBossCrm entInfo = null;
        try {
            entInfo = entService.entInfo(appLoginResp.getOpenid());
        } catch (Exception e) {
            logger.info("请求企业信息服务出现异常！！");
            throw new EnergyException(StatusCode.E_C.getCode(),StatusCode.E_C.getMsg(),"请求企业信息服务出现异常!!"+e.getMessage());
        }
        logger.info("请求企业信息返回："+ JSONObject.toJSONString(entInfo));
        //判断有没有企业信息
        if (!entInfo.isEntExit()) {//没有企业信息
            energyResp.faile(StatusCode.E_D.getCode(), StatusCode.E_D.getMsg());
            return energyResp;
        }
        //判断是主账号还是子账号
        if(!entInfo.isMainAccount()){
            energyResp.faile(StatusCode.E_E.getCode(), StatusCode.E_E.getMsg());
            return energyResp;
        }
        //判断是否是售电公司的用户，如果不是则不能登录进来
        if(!checkEtsp(entInfo.getRoleList())){
            energyResp.faile(StatusCode.E_H.getCode(), StatusCode.E_H.getMsg());
            return energyResp;
        }
        EnergyResp<String> custIdResp = null;
        EntInfo ent = entInfo.getEnt();
        try{
            custIdResp = custService.getCustId(ent.getEntId());
        }catch(Exception e){
            logger.info("business服务出现异常！！");
            throw new EnergyException(StatusCode.E_C.getCode(),StatusCode.E_C.getMsg(),"business服务出现异常!!"+e.getMessage());
        }
        //判断请求售电客户信息是否为空
        if(custIdResp.getData()==null){
            //没有客户档案
            energyResp.faile(StatusCode.E_I.getCode(), "请联系用能平台管理员补充档案");
            return energyResp;
        }
        if (StatusCode.SUCCESS.getCode().equals(custIdResp.getCode())) {
            String cusId = custIdResp.getData();
            logger.info("请求用电后台企业业务id：" + cusId);
            //封装企业信息
            CusInfo cusInfo = new CusInfo();
            cusInfo.setCusName(ent.getEntName());
            cusInfo.setCusId(cusId);
            loginResp.setCusInfo(cusInfo);
            energyResp.ok(loginResp,"登录成功！");
        } else {
            energyResp.faile(StatusCode.E_F.getCode(), StatusCode.E_F.getMsg());
            return energyResp;
        }
        return energyResp;
    }

    /**
     * 登出接口
     */
    @ApiOperation(value = "登出", notes = "")
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public EnergyResp get(@RequestBody @Valid Logout logout, BindingResult result){
        EnergyResp energyResp = new EnergyResp();
        if(result.hasErrors()){
            energyResp.faile(StatusCode.C.getCode(),StatusCode.C.getMsg(),result.getFieldError().getDefaultMessage());
            return energyResp;
        }
        LogoutResp log = null;
        try {
            log = loginService.logout(logout.getToken());
        } catch (Exception e) {
            throw new EnergyException(StatusCode.E_C.getCode(),StatusCode.E_C.getMsg(),"注销接口服务出现异常!!"+e.getMessage());
        }
        if(log.getRetCode()==0){
            energyResp.ok("注销成功！！");
        }else {
            energyResp.faile(log.getRetCode(),log.getMsg());
        }
        return energyResp;
    }

    /**
     * 判断是否是用电用户
     */
    public  boolean  checkEtsp(List<AccRole> accRoleList){
        for (AccRole role:accRoleList){
            if("10".equals(role.getRoleCode())){
                return true;
            }
        }
        return false;
    }
}
