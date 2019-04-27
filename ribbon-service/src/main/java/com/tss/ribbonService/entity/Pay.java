package com.tss.ribbonService.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/2 16:24
 * @description：支付记录
 */
@Data
@Component
public class Pay implements Serializable {

    private String payId;

    private String way;

    private float total;

    private String outTradeNo;

    private Date payTime;

}
