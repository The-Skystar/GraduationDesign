package com.tss.user_service.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
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
public class IdCardVO implements Serializable {

    private String cerId;

    private String userId;

    private String name;

    private String famous;

    private String birthday;

    private String id;

    private String inst;

    private String issued;

    private String validity;

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
