package com.tss.ribbonService.entity;

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
public class District implements Serializable {

    private String cityId;

    private String cityName;

    private String parentId;

    private String shortName;

    private String levelType;

    private String zipcode;

    private String ing;

    private String lat;

    private String pinyin;

}
