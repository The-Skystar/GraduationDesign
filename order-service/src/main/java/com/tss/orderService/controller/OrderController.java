package com.tss.orderService.controller;

import com.alibaba.fastjson.JSONObject;
import com.tss.orderService.annotations.LoginRequired;
import com.tss.orderService.service.OrderService;
import com.tss.orderService.util.RedisUtil;
import com.tss.orderService.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/5 13:10
 * @description：订单控制层
 */
@RestController
@Api(value = "订单控制器")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WebSocket webSocket;

    /**
     * 生成订单，前台以json对象的格式传递订单参数，生成订单后通知PC端接收订单
     * @param orderVO
     * @return
     * @throws Exception
     */
    @LoginRequired
    @RequestMapping(value = "/create",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "生成订单并通知PC端接收订单",notes = "成功返回订单信息")
    @ApiImplicitParam(paramType = "body",name = "orderVO",value = "json格式订单信息",required = true,dataType = "JSONObject")
    public ResultVO createOrder(@RequestBody JSONObject orderVO) throws Exception{
        return orderService.createOrder(orderVO);
    }

    @GetMapping("/test")
    public ResultVO websocket() throws IOException {
        webSocket.sendOneMessage("999","webSocket测试");
        return null;
    }

}
