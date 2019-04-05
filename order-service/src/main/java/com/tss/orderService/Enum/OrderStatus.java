package com.tss.orderService.Enum;

public enum OrderStatus {
    DROP_ORDER("1","已下单"),
    PAY("2","已支付"),
    TRANSPORT("3","运送中"),
    RECEIPT("4","已收货"),
    ABANNDONED("15","废弃")
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

    OrderStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
