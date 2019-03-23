package com.tss.user_service.exception;

import com.tss.user_service.Enum.inter.ErrorCode;

import java.util.Map;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/23 15:03
 * @description：自定义异常类
 */
public class MyException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private ErrorCode iErrorCode;

    private String errorCode;
    private String errorMessage;
    private Map<String, Object> errorData;

    public MyException(ErrorCode iErrorCode) {
        super();
        this.iErrorCode = iErrorCode;
        this.errorCode = iErrorCode.getErrorCode();
        this.errorMessage = iErrorCode.getErrorMessage();
    }

    public ErrorCode getiErrorCode() {
        return iErrorCode;
    }

    public void setiErrorCode(ErrorCode iErrorCode) {
        this.iErrorCode = iErrorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, Object> getErrorData() {
        return errorData;
    }

    public void setErrorData(Map<String, Object> errorData) {
        this.errorData = errorData;
    }
}
