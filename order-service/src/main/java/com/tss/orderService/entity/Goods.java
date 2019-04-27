package com.tss.orderService.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
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

    @TableField(value = "goods_type")
    private String goodsType;

    @TableField(value = "numb")
    private int numb;

    @TableField(value = "weight")
    private double weight;

    @TableField(value = "goodsvol")
    private double goodsVol;

    @TableField(value = "GOODSDESC")
    private String goodsDesp;

    @Override
    protected Serializable pkVal() {
        return this.goodsId;
    }
}
