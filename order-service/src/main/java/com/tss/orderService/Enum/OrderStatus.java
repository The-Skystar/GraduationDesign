package com.tss.orderService.Enum;

public enum OrderStatus {
    DROP_ORDER("1","已下单"),
    RECEIVE("2","已接单"),
    TAKE("3","已取件"),
    PAY("4","已支付"),
    TRANSPORT("5","运送中"),
    RECEIPT("6","已收货"),
    DIFFICULT("7","疑难件"),
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
