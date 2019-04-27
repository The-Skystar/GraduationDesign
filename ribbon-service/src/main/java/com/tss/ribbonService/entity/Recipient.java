package com.tss.ribbonService.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/5 13:15
 * @description：收寄人地址信息
 */
@Data
@Component
public class Recipient implements Serializable {

    private String recId;

    private String type;

    private String name;

    private String phone;

    private String postCode;

    private String province;

    private String city;

    private String county;

    private String street;

    private String address;

}
