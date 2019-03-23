package com.tss.user_service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalErrorHandler {
    private final static String DEFAULT_ERROR_VIEW = "error/error";//错误信息页

    @Autowired
    private ErrorInfoBuilder errorInfoBuilder;//错误信息的构建工具

    /**
     * 根据业务规则,统一处理异常。
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object exceptionHandler(HttpServletRequest request, Throwable error) {

        return errorInfoBuilder.getErrorInfo(request, error);

    }

    private boolean isAjaxRequest(HttpServletRequest request) {

        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
