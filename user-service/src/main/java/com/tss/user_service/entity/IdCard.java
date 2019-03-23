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

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/21 13:34
 * @description：身份证认证信息类
 */
@Data
@Component
@TableName("tss.tb_certification")
public class IdCard extends Model<IdCard> implements Serializable {

    @TableId("cer_id")
    private String cerId;

    @TableField("user_id")
    private String userId;

    @TableField("name")
    private String name;

    @TableField("famous")
    private String famous;

    @TableField("birthday")
    private Date birthday;

    @TableField("id")
    private String id;

    @TableField("inst")
    private String inst;

    @TableField("issued")
    private Date issued;

    @TableField("validity")
    private Date validity;

    @TableField("sex")
    private String sex;

    @TableField("address")
    private String address;

    @Override
    protected Serializable pkVal() {
        return this.cerId;
    }

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
