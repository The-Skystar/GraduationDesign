package com.tss.orderService.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/5 13:15
 * @description：收寄人地址信息
 */
@Data
@Component
@TableName("tss.tb_recipient")
public class Recipient extends Model<Recipient> implements Serializable {

    @TableId("send_rec_id")
    private String recId;

    @TableField("type")
    private String type;

    @TableField("name")
    private String name;

    @TableField("phone")
    private String phone;

    @TableField("postcode")
    private String postCode;

    @TableField("province")
    private String province;

    @TableField("city")
    private String city;

    @TableField("county")
    private String county;

    @TableField("street")
    private String street;

    @TableField("address")
    private String address;

    @Override
    protected Serializable pkVal() {
        return this.recId;
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "recId='" + recId + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", postCode='" + postCode + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", street='" + street + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
