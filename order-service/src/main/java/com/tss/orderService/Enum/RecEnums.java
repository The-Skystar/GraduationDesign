package com.tss.orderService.Enum;

public enum RecEnums {
    SENDER("1","寄件人"),
    RECEIVER("2","收件人"),
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

    RecEnums(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
