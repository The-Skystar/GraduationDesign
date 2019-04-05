package com.tss.orderService.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/5 13:28
 * @description：货物信息
 */
@Data
@Component
@TableName("tss.tb_goods")
public class Goods extends Model<Goods> implements Serializable {

    @TableId("goods_id")
    private String goodsId;

    @TableField("goods_type")
    private String goodsType;

    @TableField("numb")
    private int numb;

    @TableField("weight")
    private float weight;

    @TableField("goodsvol")
    private float goodsVol;

    @TableField("goodsdesp")
    private String goodsDesp;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
