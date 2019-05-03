package com.tss.user_service.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/24 13:51
 * @description：地址信息类
 */
@Data
@TableName("tss.tb_address")
public class Address extends Model<Address> implements Serializable {

    @TableId("address_id")
    private String addressId;

    @TableField("user_id")
    private String userId;

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

    @TableField("contact")
    private String contact;

    @TableField("phone")
    private String phone;

    @TableField("create_time")
    private Timestamp createTime;

    @TableField("update_time")
    private Timestamp updateTime;

    @TableField("area")
    private String area;

    @TableField("postcode")
    private String postcode;

    private transient String isDefault;

    @Override
    protected Serializable pkVal() {
        return this.addressId;
    }
}
