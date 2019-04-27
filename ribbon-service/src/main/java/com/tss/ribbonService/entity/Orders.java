package com.tss.ribbonService.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/5 12:40
 * @description：订单基本信息
 */
@Data
@Component
public class Orders implements Serializable {

    private String orderId;

    private long orderCode;

    private String userId;

    private String senderId;

    private String receiverId;

    private String goodsId;

    private String status;

    private Timestamp orderTime;

    private String payId;

    private float cost;

    private String isNotice;

    private Timestamp startTime;

    private Timestamp endTime;

    private String remark;

}
