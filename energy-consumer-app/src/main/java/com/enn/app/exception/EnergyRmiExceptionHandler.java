package com.enn.app.exception;

import com.enn.constant.StatusCode;
import com.enn.energy.system.common.exception.EnergyException;
import com.enn.energy.system.common.util.DateUtil;
import com.enn.vo.energy.EnergyResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;


@ControllerAdvice
public class EnergyRmiExceptionHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 自定义异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = EnergyException.class)
    @ResponseBody
    public EnergyResp<String> etspExceptionHandler(EnergyException e) {
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
        return etspResp;
    }

}