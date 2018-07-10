package com.enn.web.redis;

import com.alibaba.fastjson.JSON;
import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Aspect
@Order(value = 1)
@Component("redisLogServiceInterceptor")
@Slf4j
public class RedisLogServiceInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLogServiceInterceptor.class);


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @param proceedingJoinPoint
     * @return
     * @Title: execute
     * @Description: 切入点业务逻辑
     */
    @Around("@annotation(RedisLogService)")
    public Object execute(ProceedingJoinPoint proceedingJoinPoint) throws EnergyException{
        Object result = null;

        try {
            Method method = getMethod(proceedingJoinPoint);

            // 获取注解对象
            RedisLogService redisLogService = method.getAnnotation(RedisLogService.class);

            // 判断是否使用缓存
            boolean useRedis = redisLogService.use();

            if (useRedis) {

                // 使用redis
                ValueOperations<String, Object> operations = redisTemplate.opsForValue();

                // 判断当前操作
                switch (redisLogService.cacheOperation()) {

                    case FIND:

                        result = executeDefault(redisLogService, redisTemplate.opsForValue(), proceedingJoinPoint, method);

                        break;
                    case UPDATE:

                        //result = executeUpdate(redisLogService, operations, proceedingJoinPoint);
                        throw new EnergyException(StatusCode.ERROR.getCode(),StatusCode.ERROR.getMsg(),"没有定义 UPDATE 操作的缓存！！");

                    case INSERT:
                        throw new EnergyException(StatusCode.ERROR.getCode(),StatusCode.ERROR.getMsg(),"没有定义 INSERT 操作的缓存！！");
                        //result = executeInsert(redisLogService, operations, proceedingJoinPoint);
                    default:

                        result = proceedingJoinPoint.proceed();

                        break;
                }
            } else {

                result = proceedingJoinPoint.proceed();
            }

        } catch (EnergyException e) {
            throw e;
        } catch (Throwable e) {
            throw new EnergyException(StatusCode.ERROR.getCode(),StatusCode.ERROR.getMsg(),e.getMessage());
        }
        return result;
    }

    /**
     * @param joinPoint
     * @return
     * @Title: getMethod
     * @Description: 获取被拦截方法对象
     */
    protected Method getMethod(JoinPoint joinPoint) throws Exception {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        Method method = methodSignature.getMethod();

        return method;
    }


    /**
     *
     * @Title: executeDefault
     * @Description: 默认操作的执行
     * @param redisLogService
     * @param operations
     * @param proceedingJoinPoint
     * @param method
     * @throws Throwable
     */
    @SuppressWarnings("unchecked")
    private Object executeDefault(RedisLogService redisLogService, ValueOperations<String, Object> operations,
                                  ProceedingJoinPoint proceedingJoinPoint, Method method) throws Throwable {

        Object result = null;

        Object[] args = proceedingJoinPoint.getArgs();

        // 获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();

        String[] paraNameArr = u.getParameterNames(method);

        // 获取key的后缀的参数名
        String key = redisLogService.key();

        if (StringUtils.isNotBlank(key)) {
            // 使用SPEL进行key的解析
            ExpressionParser parser = new SpelExpressionParser();

            // SPEL上下文
            StandardEvaluationContext context = new StandardEvaluationContext();

            // 把方法参数放入SPEL上下文中
            for (int i = 0; i < paraNameArr.length; i++) {

                context.setVariable(paraNameArr[i], args[i]);
            }

            Object object = parser.parseExpression(key).getValue(context);

            if (null != object) {

                if (object instanceof Map<?, ?>) {

                    //key = GzdtlStringUtil.transMapToString((Map<String, Object>) object);
                    key=JSON.toJSONString(object);
                } else if (object instanceof Collection<?>) {

                    Collection<Object> collection = (Collection<Object>) object;

                    StringBuffer stringBuffer = new StringBuffer();

                    for (Object o : collection) {

                        stringBuffer.append(o.toString());
                    }

                    key = stringBuffer.toString();
                } else {

                    key = object.toString();
                }
            }
        }

        String className = proceedingJoinPoint.getTarget().getClass().getName();

        if (className.indexOf(".") >= 0) {

            className = className.substring(className.lastIndexOf(".") + 1, className.length());
        }

        String methodName = method.getName();

        String[] group = redisLogService.group();

        if (null != group && group.length > 0) {

            if (StringUtils.isNotBlank(key)) {

                key = group[0] + ":" + className + ":" + methodName + ":" + key;
            } else {

                key = group[0] + ":" + className + ":" + methodName;
            }
        } else {

            if (StringUtils.isNotBlank(key)) {

                key = "group" + ":" + className + ":" + methodName + ":" + key;
            } else {

                key = "group" + ":" + className + ":" + methodName;
            }
        }
        Date date1= new Date();
        result = operations.get(key);
        Date date2= new Date();
        if(result!=null){
            log.info("本次请求redis耗时："+(date2.getTime()-date1.getTime()));
            //log.info("从缓存中取出数据,key:"+key+",value:"+result);
            log.info("从缓存中取出数据,key:"+key);
        }
        // 如果缓存没有数据则更新缓存
        if (result == null) {
            log.info("缓存中不存在该key的数据，key:"+key+"，继续执行方法！！！");
            result = proceedingJoinPoint.proceed();
            /*
             * 将查询结果转为json字符串
             */
            //result=JSON.toJSONString(result);
            int expire = redisLogService.expire();

            // 更新缓存
            if (expire > 0) {

                operations.set(key, result, expire, TimeUnit.SECONDS);
            } else {

                operations.set(key, result);
            }
        }

        /**
         * 返回数据之前，将数据转换为特定类型返回
        //获取返回类型class
        Method met = getMethod(proceedingJoinPoint);
        Class<?> returnType = met.getReturnType();
        Object object = JSON.parseObject((String) result, returnType);*/

        return result;
    }



}