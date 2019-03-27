package com.tss.user_service.vo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@Data
@Component
public class UserVO extends Model<UserVO> implements Serializable {

    private String userId;

    private String userNick;

    private String realname;

    private String pwd;

    private String email;

    private String phone;

    private String tel;

    private Date registTime;

    private String defaultAddress;

    private String permis;

    private String id;

    private String statusKey;

    private String statusValue;

    private String sexKey;

    private String sexValue;

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

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
                '}';
    }
}
