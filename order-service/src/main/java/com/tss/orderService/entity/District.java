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
 * @date ：Created in 2019/3/25 10:40
 * @description：行政区划类
 */
@Data
@Component
@TableName("tss.tb_district")
public class District extends Model<District> implements Serializable {

    @TableId("city_id")
    private String cityId;

    @TableField("city_name")
    private String cityName;

    @TableField("parentid")
    private String parentId;

    @TableField("shortname")
    private String shortName;

    @TableField("level_type")
    private String levelType;

    @TableField("zipcode")
    private String zipcode;

    @TableField("ing")
    private String ing;

    @TableField("lat")
    private String lat;

    @TableField("pinyin")
    private String pinyin;

    @Override
    protected Serializable pkVal() {
        return this.cityId;
    }
}
