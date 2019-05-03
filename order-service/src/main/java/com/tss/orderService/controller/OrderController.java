package com.tss.orderService.controller;

import com.alibaba.fastjson.JSONObject;
import com.tss.orderService.annotations.LoginRequired;
import com.tss.orderService.service.OrderService;
import com.tss.orderService.util.RedisUtil;
import com.tss.orderService.vo.OrderVO;
import com.tss.orderService.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.GET;
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
        ResultVO<OrderVO> resultVO = orderService.createOrder(orderVO);
//        if (resultVO.getCode()== ReturnStatusEnums.ORDER_SUCCERR.getCode())
//            return orderService.orderDetail(resultVO.getData().getOrderCode());
        return resultVO;
    }

    @GetMapping("/getOrder")
    public ResultVO getNotReceive(String status) throws Exception{
        return orderService.getOrder(status);
    }

    @GetMapping("/searchByTime")
    public ResultVO searchByTime(String time,String status) throws Exception{
        return orderService.serachByTime(time,status);
    }

    @GetMapping("/searchAllColumn")
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

    @GetMapping("/test")
    public ResultVO websocket() throws IOException {
        webSocket.sendOneMessage("999","webSocket测试");
        return null;
    }

    @GetMapping("/batchReceive")
    public ResultVO batchReceiveOrder(String orderId) throws Exception{
        String[] orderIds = orderId.split(",");
        return orderService.batchReceiveOrder(orderIds);
    }

    @GetMapping("/take")
    public ResultVO takeOrder(String orderId,double cost,double weight) throws Exception{
        return orderService.takeOrder(orderId,cost,weight);
    }
}
