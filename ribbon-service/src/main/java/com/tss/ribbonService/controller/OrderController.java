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

    @GetMapping("/getOrder")
    public ResultVO getNotReceive(String status) throws Exception{
        return orderService.getNotReceive(status);
    }

    @GetMapping("/searchByTime")
    public ResultVO searchByTime(String time,String status) throws Exception{
        return orderService.searchByTime(time,status);
    }

    @GetMapping("/serachAllColumn")
    public ResultVO serachAllColumn(String status, String conditions) throws Exception{
        return orderService.serachAllColumn(status,conditions);
    }

    @GetMapping("/receiveOrder")
    public ResultVO receiveOrder(String orderId) throws Exception{
        return orderService.receiveOrder(orderId);
    }

    @GetMapping("/count")
    public ResultVO getOrderCount(String status) throws Exception{
        return orderService.getOrderCount(status);
    }

    @GetMapping("/search")
    public ResultVO serach(String status, String time, String conditions) throws Exception{
        return orderService.serach(status,time,conditions);
    }

    @GetMapping("/batchReceive")
    public ResultVO batchReceiveOrder(String orderId) throws Exception{
        return orderService.batchReceiveOrder(orderId);
    }

    @GetMapping("/take")
    public ResultVO takeOrder(String orderId,double cost,double weight) throws Exception{
        return orderService.takeOrder(orderId,cost,weight);
    }
}
