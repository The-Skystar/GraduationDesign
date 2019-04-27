package com.tss.ribbonService.entity;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/21 13:34
 * @description：身份证认证信息类
 */
@Data
@Component
public class IdCard implements Serializable {

    private String cerId;

    private String userId;

    private String name;

    private String famous;

    private Date birthday;

    private String id;

    private String inst;

    private Date issued;

    private Date validity;

    private String sex;

    private String address;

    @Override
    public String toString() {
        return "IdCard{" +
                "cerId='" + cerId + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", famous='" + famous + '\'' +
                ", birthday=" + birthday +
                ", id='" + id + '\'' +
                ", inst='" + inst + '\'' +
                ", issued=" + issued +
                ", validity=" + validity +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
