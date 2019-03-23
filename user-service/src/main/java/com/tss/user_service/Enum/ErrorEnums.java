package com.tss.user_service.Enum;

import com.tss.user_service.Enum.inter.ErrorCode;

public enum ErrorEnums implements ErrorCode {
    SYS_ERROR("1001","系统错误，存在不合法用户")
    ;

    private String errorCode;
    private String errorMessage;

    private ErrorEnums(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
