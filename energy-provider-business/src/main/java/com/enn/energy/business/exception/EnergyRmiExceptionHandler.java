package com.enn.energy.business.exception;

import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.energy.system.common.util.Servlets;
import com.enn.service.system.ISysLogErrorService;
import com.enn.vo.energy.EnergyResp;
import com.enn.vo.energy.system.SysLogError;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;


@ControllerAdvice
@Slf4j
public class EnergyRmiExceptionHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISysLogErrorService sysLogErrorService;
    /**
     * 自定义异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = EnergyException.class)
    @ResponseBody
    public EnergyResp<String> etspExceptionHandler(EnergyException e) {
        HttpServletRequest request = Servlets.getRequest();
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e1) {
            log.info("获取本机ip失败！！");
        }
        Date time = new Date();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        String exception = baos.toString();
        //定义异常信息
        Integer code = null;
        String msg = null;
        String error = null;
        //code
        if (e.getCode() != null) {
            code = e.getCode();
        } else {
            code = StatusCode.ERROR.getCode();
        }
        //msg
        if (e.getMsg() != null) {
            msg = e.getMsg();
        } else {
            msg = StatusCode.ERROR.getMsg();
        }
        //error
        if (e.getError() != null) {
            error = e.getError() + ": " + exception;
        } else {
            error = StatusCode.ERROR.getMsg();
        }
        logger.error(error);
        logger.error(exception);
        EnergyResp<String> etspResp = new EnergyResp<>();
        if (e.getStackTrace().length > 0) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// 得到异常棧的首个元素
            error += "Class：" + stackTraceElement.getFileName() + ", Method：" + stackTraceElement.getMethodName() + ", Line：" + stackTraceElement.getLineNumber() + ", error：" + e.getMessage() + ", time：" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        }
        etspResp.faile(code, msg, error);

        //将日志存到数据库中
        SysLogError sysLogError = new SysLogError();
        sysLogError.setCode(code);
        sysLogError.setError(error);
        sysLogError.setMsg(msg);
        sysLogError.setProject("energy-provider-business");
        sysLogError.setTime(time);
        sysLogError.setRequest(request.getRequestURI());
        sysLogError.setIp(localHost.getHostAddress());
        try {
            EnergyResp<Integer> integerEnergyResp = sysLogErrorService.insertOne(sysLogError);
        } catch (Exception e1) {
            log.info("将错误信息插入数据库发生错误！！！！");
        }
        return etspResp;
    }


    /**
     * 统一异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public EnergyResp<String> runtimeExceptionHandler(RuntimeException e) {
        HttpServletRequest request = Servlets.getRequest();
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e1) {
            log.info("获取本机ip失败！！");
        }
        EnergyResp<String> etspResp = new EnergyResp<>();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        String exception = baos.toString();
        logger.error(exception);
        String error = null;
        if (e.getStackTrace().length > 0) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];// 得到异常棧的首个元素
            error = "Class：" + stackTraceElement.getFileName() + ", Method：" + stackTraceElement.getMethodName() + ", Line：" + stackTraceElement.getLineNumber() + ", error：" + e.getMessage() + ", time：" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        }
        etspResp.faile(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMsg(), error);

        //将日志存到数据库中
        SysLogError sysLogError = new SysLogError();
        sysLogError.setCode(StatusCode.ERROR.getCode());
        sysLogError.setError(exception);
        sysLogError.setMsg(StatusCode.ERROR.getMsg());
        sysLogError.setProject("energy-provider-business");
        sysLogError.setTime(new Date());
        sysLogError.setRequest(request.getRequestURI());
        sysLogError.setIp(localHost.getHostAddress());
        try {
            EnergyResp<Integer> integerEnergyResp = sysLogErrorService.insertOne(sysLogError);
        } catch (Exception e1) {
            log.info("将错误信息插入数据库发生错误！！！！");
        }


        return etspResp;
    }

}