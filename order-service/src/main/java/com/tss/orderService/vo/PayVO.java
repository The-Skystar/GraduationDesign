package com.tss.orderService.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/5/5 15:54
 * @description：
 */
@Data
@Component
public class PayVO {

    private String time;

    private double costTotal;

    private double collection;

    private double refund;

}
