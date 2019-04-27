package com.tss.ribbonService.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/18 12:54
 * @description：返回结果类
 */
@Data
@Component
public class ResultVO<T> implements Serializable {

    private int code;

    private String msg;

    private T data;

}
