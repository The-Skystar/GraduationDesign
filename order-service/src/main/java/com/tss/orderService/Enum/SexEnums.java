package com.tss.orderService.Enum;

public enum SexEnums {
    MAN("1","男"),
    WOMAN("0","女")
    ;
    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    SexEnums(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
