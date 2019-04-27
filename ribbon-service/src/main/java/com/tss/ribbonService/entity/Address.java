package com.tss.ribbonService.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/24 13:51
 * @description：地址信息类
 */
@Data
public class Address implements Serializable {

    private String addressId;

    private String userId;

    private String province;

    private String city;

    private String county;

    private String street;

    private String address;

    private String contact;

    private String phone;

    private Date createTime;

    private Date updateTime;

    private String area;

    private transient String isDefault;

    @Override
    public String toString() {
        return "Address{" +
                "addressId='" + addressId + '\'' +
                ", userId='" + userId + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", street='" + street + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", area='" + area + '\'' +
                ", isDefault='" + isDefault + '\'' +
                '}';
    }
}
