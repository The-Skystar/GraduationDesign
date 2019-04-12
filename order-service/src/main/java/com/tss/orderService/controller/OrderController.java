package com.tss.orderService.controller;

import com.alibaba.fastjson.JSONObject;
import com.tss.orderService.annotations.LoginRequired;
import com.tss.orderService.service.KdService;
import com.tss.orderService.service.OrderService;
import com.tss.orderService.util.IDUtils;
import com.tss.orderService.util.RedisUtil;
import com.tss.orderService.util.SnowFlake;
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

    @Autowired
    private KdService kdService;


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

    @PostMapping("/eOrder")
    public ResultVO eOrder(@RequestBody JSONObject requestData) throws Exception{
        String requestDatas= "{'OrderCode': '012657700387'," +
                "'ShipperCode':'SF'," +
                "'PayType':1," +
                "'ExpType':1," +
                "'Cost':1.0," +
                "'OtherCost':1.0," +
                "'Sender':" +
                "{" +
                "'Company':'LV','Name':'Taylor','Mobile':'15018442396','ProvinceName':'上海','CityName':'上海','ExpAreaName':'青浦区','Address':'明珠路73号'}," +
                "'Receiver':" +
                "{" +
                "'Company':'GCCUI','Name':'Yann','Mobile':'15018442396','ProvinceName':'北京','CityName':'北京','ExpAreaName':'朝阳区','Address':'三里屯街道雅秀大厦'}," +
                "'Commodity':" +
                "[{" +
                "'GoodsName':'鞋子','Goodsquantity':1,'GoodsWeight':1.0}]," +
                "'Weight':1.0," +
                "'Quantity':1," +
                "'Volume':0.0," +
                "'Remark':'小心轻放'," +
                "'IsReturnPrintTemplate':1}";

        return kdService.eOrder(JSONObject.parseObject(requestDatas));
    }

    @PostMapping("/appointment")
    public ResultVO appointment(@RequestBody JSONObject requestData) throws Exception{
//        String requestDatas= "{'OrderCode': '012657700312'," +
//                "'ShipperCode':'YTO'," +
//                "'PayType':1," +
//                "'ExpType':1," +
//                "'Cost':1.0," +
//                "'OtherCost':1.0," +
//                "'Sender':" +
//                "{" +
//                "'Company':'LV','Name':'Taylor','Mobile':'15018442396','ProvinceName':'上海','CityName':'上海','ExpAreaName':'青浦区','Address':'明珠路73号'}," +
//                "'Receiver':" +
//                "{" +
//                "'Company':'GCCUI','Name':'Yann','Mobile':'15018442396','ProvinceName':'北京','CityName':'北京','ExpAreaName':'朝阳区','Address':'三里屯街道雅秀大厦'}," +
//                "'Commodity':" +
//                "[{" +
//                "'GoodsName':'鞋子','Goodsquantity':1,'GoodsWeight':1.0}]," +
//                "'AddService':" +
//                "[{" +
//                "'Name':'COD','Value':'1020'}]," +
//                "'Weight':1.0," +
//                "'Quantity':1," +
//                "'Volume':0.0," +
//                "'Remark':'小心轻放'," +
//                "'Commodity':" +
//                "[{" +
//                "'GoodsName':'鞋子'," +
//                "'Goodsquantity':1," +
//                "'GoodsWeight':1.0}]" +
//                "}";

        return kdService.appointment(requestData);
    }



}
