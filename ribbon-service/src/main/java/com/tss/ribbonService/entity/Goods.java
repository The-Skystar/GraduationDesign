package com.tss.ribbonService.entity;

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
public class Goods implements Serializable {

    private String goodsId;

    private String goodsType;

    private int numb;

    private float weight;

    private float goodsVol;

    private String goodsDesp;

}
