package com.tss.ribbonService.controller;

import com.alibaba.fastjson.JSONObject;
import com.tss.ribbonService.service.OrderService;
import com.tss.ribbonService.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/24 9:41
 * @description：
 */
@RestController
@Api(value = "订单控制器")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/create",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "生成订单并通知PC端接收订单",notes = "成功返回订单信息")
    @ApiImplicitParam(paramType = "body",name = "orderVO",value = "json格式订单信息",required = true,dataType = "JSONObject")
    public ResultVO createOrder(@RequestBody JSONObject order)throws Exception{
        return orderService.createOrder(order);
    }

    @PostMapping("/eOrder")
    public ResultVO eOrder(@RequestBody JSONObject requestData) throws Exception{
        return orderService.eOrder(requestData);
    }
}
