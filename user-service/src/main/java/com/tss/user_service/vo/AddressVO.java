package com.tss.user_service.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/25 15:30
 * @description：用户地址信息视图类
 */
@Data
@Component
public class AddressVO implements Serializable {

    private String userId;

    private String address;

    private String contact;

    private String phone;

    private String createTime;

    private String updateTime;

}
