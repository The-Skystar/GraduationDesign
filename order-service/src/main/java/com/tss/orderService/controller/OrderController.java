package com.tss.orderService.controller;

import com.alibaba.fastjson.JSONObject;
import com.tss.orderService.service.OrderService;
import com.tss.orderService.vo.OrderVO;
import com.tss.orderService.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/5 13:10
 * @description：订单控制层
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/create",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public ResultVO createOrder(@RequestBody JSONObject orderVO) throws Exception{
        return orderService.createOrder(orderVO);
    }
}
