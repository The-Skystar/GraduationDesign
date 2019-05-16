package com.tss.orderService.Enum;

public enum ReturnStatusEnums {
    LOGIN_SUCCESS(100,"登陆成功"),
    EXIT_FAIL(101,"退出登录失败"),
    EXIT_SUCCESS(102,"退出登录成功"),
    LOGGED(103,"用户已登录"),
    ACCOUNT_ERROR(104,"账号不存在"),
    PWD_ERROR(105,"密码错误"),
    NOT_LOGGED(106,"用户未登录"),
    FROZEN(107,"账号被冻结"),
    CANALED(108,"账号已注销"),
    REGIST_SUCCESS(109,"注册成功"),
    REGIST_FAIL(110,"注册失败"),
    OCCUPIED(111,"被占用"),
    VALIDATE(112,"验证通过"),
    SEND_CODE(113,"验证码已发送"),
    CODE_EXPIRED(114,"验证码已过期"),
    CODE_ERROR(115,"验证码错误"),
    MAIL_NOT_MATCH(116,"邮箱与注册邮箱不匹配"),
    UPD_USER_FAIL(117,"修改用户信息失败"),
    UPD_USER_SUCCESS(118,"修改用户信息成功"),
    UPD_PWD_FAIL(119,"修改密码失败"),
    UPD_PWD_SUCCESS(120,"修改密码成功"),
    OLD_PWD_ERROR(121,"原密码错误"),
    RECO_SUCCESS(201,"已识别身份证"),
    RECO_NOT_COMPLETE(202,"身份证识别信息不完整"),
    RECO_NOT_LEGAL(203,"身份证不合法"),
    NAME_NOT_MATCH(204,"真实姓名与身份证不匹配"),
    AUTHEN_SUCCESS(205,"实名认证通过"),
    SYS_ERROR(206,"系统错误导致认证失败"),
    ADD_ADDRESS_SUCCESS(300,"添加地址成功"),
    DEL_ADDRESS_SUCCESS(301,"删除地址成功"),
    UPD_ADDRESS_SUCCESS(302,"修改地址成功"),
    SELECT_SUCCESS(303,"查询到记录"),
    ORDER_ONLINE_SUCCESS(400,"电子面单生成成功"),
    ORDER_ONLINE_FAIL(401,"电子面单生成失败"),
    APPOINTMENT_SUCCESS(402,"预约取件成功"),
    APPOINTMENT_FAIL(403,"预约取件失败"),
    ORDER_EXPIRED(407,"订单过期，不能接单"),
    ORDER_RECEIVED(408,"已接单"),
    ORDER_RECEIVED_FAIL(409,"接单失败"),
    ORDER_TAKED(410,"取件"),

    ORDER_SUCCERR(999,"创建订单成功"),
    ;
    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ReturnStatusEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
