package com.tss.user_service.Enum;

public enum LoginStatusEnums {
    ;

    private String code;

    private String desp;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    LoginStatusEnums(String code, String desp) {
        this.code = code;
        this.desp = desp;
    }

}
