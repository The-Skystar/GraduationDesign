package com.tss.orderService.Enum;

public enum DistrictEnums {
    PROCINCE("1","省"),
    CITY("2","市"),
    COUNTY("3","区县"),
    STREET("4","街道，镇")
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

    DistrictEnums(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
