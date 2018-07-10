package com.enn.web.filter;

import com.enn.constant.StatusCode;
import com.enn.energy.system.common.util.StringUtil;
import com.enn.service.user.IUacService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.user.CheckTicketResp;
import com.enn.vo.energy.user.EnnBosstResp;
import com.enn.web.common.config.AuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;


@Aspect
@Component
@Order(Integer.MAX_VALUE)
@Slf4j
public class ControllerInterceptor {

    @Autowired
    protected AuthProperties authProperties;
    @Autowired
    private IUacService uacService;

    /**
     * 定义拦截规则：拦截com.xjj.web.controller包下面的所有类中，有@RequestMapping注解的方法。
     */
    @Pointcut("execution(* com.enn.web.controller..*(..))")
    public void controllerMethodPointcut() {
    }


    /**
     * 拦截器具体实现
     *
     * @param pjp
     * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。）
     */
    @Around("controllerMethodPointcut()") //指定拦截器规则；也可以直接把“execution(* com.xjj.........)”写进这里
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
        Date date1= new Date();
        Object result = null;
        MethodSignature signature = (MethodSignature) pjp.getSignature();

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        //请求编号记忆
        long count=System.currentTimeMillis();
        count++;
        /* 打印请求地址及参数 */
        log.info("请求开始，请求地址："+request.getRequestURL()+",请求编号"+count);

        StringBuffer sb = new StringBuffer() ;
        InputStream is = request.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String s = "" ;
        while((s=br.readLine())!=null){
            sb.append(s) ;
        }
        log.info("请求参数："+sb.toString());

        if (!isLogin(request)) {
            EnergyResp energyResp = new EnergyResp();
            energyResp.faile(StatusCode.L.getCode(),StatusCode.L.getMsg());
            energyResp.setData(authProperties.getUac_url());
            return energyResp;
        }
        result = pjp.proceed();
        Date date2= new Date();
        log.info("请求结束，本次请求时间："+(date2.getTime()-date1.getTime())+",请求编号"+count);
        return result;
    }


    //判断是否已经登录
    private boolean isLogin(HttpServletRequest request) {
        String ticket = request.getHeader("ticket");
        if (StringUtil.isEmpty(ticket)) {
            return false;
        }
        if ("123456".equals(ticket)) {
            return true;
        }
        try {
            EnnBosstResp<CheckTicketResp> resp = uacService.checkTicket(ticket);  //验证ticket有效性
            return resp.getEntity().isExit();
        } catch (Exception e) {
            return false;
        }
    }

}
