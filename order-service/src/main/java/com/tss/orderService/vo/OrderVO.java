package com.tss.orderService.vo;

import com.tss.orderService.entity.Goods;
import com.tss.orderService.entity.Orders;
import com.tss.orderService.entity.Recipient;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/5 13:37
 * @description：订单视图
 */
@Data
@Component
public class OrderVO implements Serializable {

    private String orderId;

    private String userId;

    private String status;

    private Timestamp orderTime;

    private String payId;

    private float cost;

    private String isNotice;

    private Timestamp startTime;

    private Timestamp endTime;

    private String remark;

    private String senderId;

    private Recipient sender;

    private String receiverId;

    private Recipient receiver;

    private String goodsId;

    private Goods goods;
}
