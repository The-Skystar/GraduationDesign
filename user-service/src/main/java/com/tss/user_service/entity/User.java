package com.tss.user_service.entity;

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
@TableName("tss.tb_user")
public class User extends Model<User> implements Serializable {

    @TableId("user_id")
    private String userId;

    @TableField("user_nick")
    private String userNick;

    @TableField("realname")
    private String realname;

    @TableField("pwd")
    private String pwd;

    @TableField("email")
    private String email;

    @TableField("phone")
    private String phone;

    @TableField("tel")
    private String tel;

    @TableField("regist_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registTime;

    @TableField("default_address")
    private String defaultAddress;

    @TableField("permis")
    private String permis;

    @TableField("id")
    private String id;


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
