package com.tss.ribbonService.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Data
@Component
public class User implements Serializable {

    private String userId;

    private String userNick;

    private String realname;

    private String pwd;

    private String email;

    private String phone;

    private String tel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registTime;

    private String defaultAddress;

    private String permis;

    private String id;

    private String status;

    private String sex;

    private String ver;

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userNick='" + userNick + '\'' +
                ", realname='" + realname + '\'' +
                ", pwd='" + pwd + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", tel='" + tel + '\'' +
                ", registTime=" + registTime +
                ", defaultAddress='" + defaultAddress + '\'' +
                ", permis='" + permis + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
