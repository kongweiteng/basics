package com.enn.app.interceptor;


import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.Reflections;
import com.enn.service.user.IloginService;
import com.enn.vo.energy.app.login.TokenCheckResp;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


/**
 * 验证请求权限拦截器
 * @author kongweiteng
 */
@Slf4j
@Aspect
@Component
public class ControllerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ControllerInterceptor.class);

    @Resource
    private IloginService loginService;
    /**
     * 定义拦截规则：拦截com.xjj.web.controller包下面的所有类中，有@RequestMapping注解的方法。
     */
    @Pointcut("execution(* com.enn.app..*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void controllerMethodPointcut(){}

    /**
     * 拦截器具体实现
     * @param pjp
     * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。）
     */
    @Around("controllerMethodPointcut()") //指定拦截器规则；也可以直接把“execution(* com.xjj.........)”写进这里
    public Object Interceptor(ProceedingJoinPoint pjp)throws Throwable{
        Object result  = null;
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod(); //获取被拦截的方法
        String methodName = method.getName(); //获取被拦截的方法名
        logger.info("请求开始，方法：{}", method);
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        boolean login = isLogin(request);
        if(!login){
            //
            Class onwClass = signature.getReturnType();
            //System.err.println(onwClass.getSimpleName());
            Object o =null;
            try {
                o = onwClass.newInstance();
                //Reflections.invokeMethod(o,"faile",new Class[] {Object.class,String.class,Integer.class},new Object[] {"890-64","用户未登录！",1011});
                Reflections.invokeMethod(o,"faile",new Class[] {Integer.class,String.class},new Object[] {1011,"用户未登录！！"});
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            /*DefaultResp defaultResp = new DefaultResp();
            defaultResp.msg="认证失败";
            defaultResp.retCode=1;*/
           /* JSONObject jobj=new JSONObject();
            jobj.put(RequestKey.code.name(), StatusCode.AUTHENTICATION.getCode());
            jobj.put(RequestKey.msg.name(), StatusCode.AUTHENTICATION.getMsg());*/
            return o;
        }
        /*try {
            if(result == null){*/
                // 一切正常的情况下，继续执行被拦截的方法
                result = pjp.proceed();
            /*}
        } catch (Throwable e) {
            logger.info("exception: ", e);
        }*/
        return result;
    }


    //判断是否已经登录
    private boolean isLogin(HttpServletRequest request) {
        //String propertyValue = ConfigUtil.getPropertyValue("ENNXZ-TOKEN");
        String servletPath = request.getServletPath();
        if(servletPath.contains("login")){
            logger.info("放行登录接口 ！！");
            return true;
        }
        String token = request.getHeader("token").trim();
        if(!"123456".equals(token)){
            if(token==null || token==""){
                return false;
            }
            //验证token的有效性
            TokenCheckResp tokenCheckResp = null;
            try {
                tokenCheckResp = loginService.checkToken(token);
            } catch (Exception e) {
                //e.printStackTrace();
                logger.info("请求登录服务出现异常！！--验证token接口");
                throw new EnergyException(StatusCode.E_C.getCode(), StatusCode.E_C.getMsg(),"请求登录服务出现异常!!--验证token接口"+e.getMessage());
            }
            logger.info("验证token接口返回："+tokenCheckResp.toString());
            if(tokenCheckResp.getRetCode()==0){
                if(tokenCheckResp.getExist()){//存在
                    return true;
                }else{
                    return false;
                }
            }else {
                logger.info("请求验证token接口请求异常："+tokenCheckResp.getMsg());
            }
            logger.info("用户没有登录！");
            return false;
        }else{
            return true;
        }
    }
}