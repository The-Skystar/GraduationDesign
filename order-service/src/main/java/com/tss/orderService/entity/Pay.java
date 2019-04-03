package com.tss.orderService.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/2 16:24
 * @description：支付记录
 */
@Data
@TableName("tss.tb_pay")
public class Pay extends Model<Pay> implements Serializable {

    @TableId("pay_id")
    private String payId;

    @TableField("pay_way")
    private String way;

    @TableField("pay_total")
    private float total;

    @TableField("OUTTRADENO")
    private String outTradeNo;

    @TableField("pay_time")
    private Date payTime;

    @Override
    protected Serializable pkVal() {
        return this.payId;
    }
}
