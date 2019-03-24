package com.tss.user_service.Enum;

public enum LoginStatusEnums {
    REGIST("1","注册"),
    LOGIN("2","已登陆"),
    CANCLE("3","注销"),
    LOCK("4","冻结")
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
