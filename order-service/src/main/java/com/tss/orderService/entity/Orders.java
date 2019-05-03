package com.tss.orderService.entity;

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
 * @date ：Created in 2019/4/5 12:40
 * @description：订单基本信息
 */
@Data
@Component
@TableName("tss.tb_order")
public class Orders extends Model<Orders> implements Serializable {

    @TableId("order_id")
    private String orderId;

    @TableField("order_code")
    private String orderCode;

    @TableField("user_id")
    private String userId;

    @TableField("sender")
    private String senderId;

    @TableField("receiver")
    private String receiverId;

    @TableField("goods_id")
    private String goodsId;

    @TableField("status")
    private String status;

    @TableField("order_time")
    private Timestamp orderTime;

    @TableField("pay_id")
    private String payId;

    @TableField("cost")
    private double cost;

    @TableField("isnotice")
    private String isNotice;

    @TableField("start_time")
    private Date startTime;

    @TableField("end_time")
    private Date endTime;

    @TableField("remark")
    private String remark;

    @TableField("receive_time")
    private Timestamp receiveTime;

    @TableField("take_time")
    private Timestamp takeTime;

    @TableField("send_time")
    private Timestamp sendTime;

    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }
}
