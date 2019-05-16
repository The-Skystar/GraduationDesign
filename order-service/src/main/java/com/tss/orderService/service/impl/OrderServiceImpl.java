package com.tss.orderService.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tss.orderService.Enum.ErrorEnums;
import com.tss.orderService.Enum.OrderStatus;
import com.tss.orderService.Enum.ReturnStatusEnums;
import com.tss.orderService.Enum.inter.ErrorCode;
import com.tss.orderService.config.DateConverterConfig;
import com.tss.orderService.controller.WebSocket;
import com.tss.orderService.entity.Goods;
import com.tss.orderService.entity.Orders;
import com.tss.orderService.entity.Pay;
import com.tss.orderService.entity.Recipient;
import com.tss.orderService.exception.MyException;
import com.tss.orderService.mapper.GoodsMapper;
import com.tss.orderService.mapper.OrderMapper;
import com.tss.orderService.mapper.PayMapper;
import com.tss.orderService.mapper.RecipientMapper;
import com.tss.orderService.service.OrderService;
import com.tss.orderService.util.FastJsonUtils;
import com.tss.orderService.util.IDUtils;
import com.tss.orderService.util.KdUtil;
import com.tss.orderService.vo.OrderVO;
import com.tss.orderService.vo.PayVO;
import com.tss.orderService.vo.ResultVO;
import org.apache.catalina.User;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/5 12:58
 * @description：订单基本信息服务层
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private ResultVO resultVO;

    @Autowired
    private Orders orders;

    @Autowired
    private RecipientMapper recipientMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PayMapper payMapper;

    @Autowired
    private KdUtil kdUtil;

    @Autowired
    private OrderVO orderVo;

    @Autowired
    private PayVO payVO;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    private JavaMailSender javaMailSender;

    //电商ID
    private String EBusinessID = "1437550";
    //电商加密私钥，快递鸟提供，注意保管，不要泄漏
    private String AppKey = "a4774b93-94cd-4315-a77c-82333cd375f5";
    //请求url, 正式环境地址：http://api.kdniao.com/api/Eorderservice    测试环境地址：http://testapi.kdniao.com:8081/api/EOrderService
    private String eOrderReqURL = "http://testapi.kdniao.com:8081/api/Eorderservice";

    //测试请求url
    private String AppointmentReqURL = "http://testapi.kdniao.com:8081/api/oorderservice";
    //正式请求url
    //private string ReqURL = "http://api.kdniao.com/api/OOrderService";

    @Override
    public ResultVO createOrder(JSONObject orderJson) throws Exception {
        OrderVO orderVO = FastJsonUtils.getJsonToBean(orderJson.toJSONString(), OrderVO.class);
        orderVO.setOrderCode(String.valueOf(IDUtils.getOrderId()));
        Recipient sender = orderVO.getSender();
        Recipient receiver = orderVO.getReceiver();
        Goods goods = orderVO.getGoods();
        int senderNum = recipientMapper.insert(sender);
        int receiverNum = recipientMapper.insert(receiver);
        int goodsNum = goodsMapper.insert(goods);
        orders.setOrderId(IDUtils.getUUID());
        orders.setOrderCode(String.valueOf(orderVO.getOrderCode()));
        orders.setSenderId(orderVO.getSender().getRecId());
        orders.setReceiverId(orderVO.getReceiver().getRecId());
        orders.setGoodsId(orderVO.getGoods().getGoodsId());
        orders.setUserId(orderVO.getUserId());
        orders.setStatus(OrderStatus.DROP_ORDER.getCode());
        orders.setOrderTime(new Timestamp(System.currentTimeMillis()));
        orders.setCost(orderVO.getCost());
        orders.setIsNotice(orderVO.getIsNotice());
        orders.setStartTime(orderVO.getStartTime());
        orders.setEndTime(orderVO.getEndTime());
        orders.setRemark(orderVO.getRemark());
        int order = orderMapper.insert(orders);
        if (senderNum != 1 || receiverNum != 1 || goodsNum != 1 || order != 1) {
            throw new MyException(ErrorEnums.SYS_ERROR);
        }
        BeanUtils.copyProperties(orders,orderVO);
        orderVO.setSender(sender);
        orderVO.setReceiver(receiver);
        orderVO.setGoods(goods);
        webSocket.sendOneMessage("888",JSONObject.toJSONString(orderVO));
        resultVO.setCode(ReturnStatusEnums.ORDER_SUCCERR.getCode());
        resultVO.setMsg(ReturnStatusEnums.ORDER_SUCCERR.getMsg());
        resultVO.setData(orderVO);
        return resultVO;
    }

    @Override
    public ResultVO eOrder(JSONObject requestDataJson) throws Exception {
        String requestData = requestDataJson.toJSONString();
        Map<String, String> params = new HashMap<>();
        params.put("RequestData", kdUtil.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBusinessID);
        params.put("RequestType", "1007");
        String dataSign = kdUtil.encrypt(requestData, AppKey, "UTF-8");
        params.put("DataSign", kdUtil.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");
        String result = kdUtil.sendPost(eOrderReqURL, params);
        System.out.println(result);
        JSONObject resultJson = JSONObject.parseObject(result);
        if (resultJson.getBoolean("Success")) {

            resultVO.setCode(ReturnStatusEnums.ORDER_ONLINE_SUCCESS.getCode());
            resultVO.setMsg(ReturnStatusEnums.ORDER_ONLINE_SUCCESS.getMsg());
            resultVO.setData(resultJson);
            return resultVO;
        }
        resultVO.setCode(ReturnStatusEnums.ORDER_ONLINE_FAIL.getCode());
        resultVO.setMsg(ReturnStatusEnums.ORDER_ONLINE_FAIL.getMsg());
        resultVO.setData(null);
        return resultVO;
    }

    @Override
    public ResultVO appointment(JSONObject requestDataJson) throws Exception {
        String requestData = requestDataJson.toJSONString();
        Map<String, String> params = new HashMap<>();
        params.put("RequestData", kdUtil.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBusinessID);
        params.put("RequestType", "1001");
        String dataSign = kdUtil.encrypt(requestData, AppKey, "UTF-8");
        params.put("DataSign", kdUtil.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");
        String result = kdUtil.sendPost(AppointmentReqURL, params);
        resultVO.setCode(ReturnStatusEnums.APPOINTMENT_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.APPOINTMENT_SUCCESS.getMsg());
        resultVO.setData(JSONObject.parseObject(result));
        return resultVO;
    }

    @Override
    public ResultVO orderDetail(String orderCode) throws Exception {
        Recipient sender = null;
        Recipient receiver = null;
        Goods goods = null;
        List<Orders> ordersList = orderMapper.selectList(new EntityWrapper<Orders>().eq("order_code",orderCode));
        if (ordersList!=null)
            orders = ordersList.get(0);
        if (orders.getSenderId()!=null)
            sender = recipientMapper.selectById(orders.getSenderId());
        if (orders.getReceiverId()!=null)
            receiver = recipientMapper.selectById(orders.getReceiverId());
        if (orders.getGoodsId()!=null)
            goods = goodsMapper.selectById(orders.getGoodsId());
        BeanUtils.copyProperties(orders,orderVo);
        orderVo.setSender(sender);
        orderVo.setReceiver(receiver);
        orderVo.setGoods(goods);
        System.err.println(ordersList);
        resultVO.setCode(ReturnStatusEnums.ORDER_SUCCERR.getCode());
        resultVO.setMsg(ReturnStatusEnums.ORDER_SUCCERR.getMsg());
        resultVO.setData(orderVo);
        return resultVO;
    }

    @Override
    public ResultVO getOrder(String status) throws Exception {
        Recipient sender = null;
        Recipient receiver = null;
        Goods goods = null;
        Pay pay = null;
        List<Orders> ordersList = null;
        if (status!=null&&status.length()!=0) {
             ordersList = orderMapper.selectList(new EntityWrapper<Orders>().eq("status",status).orderBy("start_time"));
        }else {
            ordersList = orderMapper.selectList(new EntityWrapper<Orders>().orderBy("order_time",false));
        }
        List<OrderVO> orderVOList = new ArrayList<>();
        if (ordersList!=null){
            for (int i = 0;i<ordersList.size();i++){
                OrderVO orderVO = new OrderVO();
                Orders order = ordersList.get(i);
                if (order.getSenderId()!=null)
                    sender = recipientMapper.selectById(order.getSenderId());
                if (order.getReceiverId()!=null)
                    receiver = recipientMapper.selectById(order.getReceiverId());
                if (order.getGoodsId()!=null)
                    goods = goodsMapper.selectById(order.getGoodsId());
                if (order.getPayId()!=null)
                    pay = payMapper.selectById(order.getPayId());
                orderVO.setOrderId(order.getOrderId());
                orderVO.setUserId(order.getUserId());
                orderVO.setStatus(order.getStatus());
                orderVO.setOrderTime(order.getOrderTime());
                orderVO.setOrderCode(order.getOrderCode());
                orderVO.setPay(pay);
                orderVO.setCost(order.getCost());
                orderVO.setIsNotice(order.getIsNotice());
                orderVO.setStartTime(order.getStartTime());
                orderVO.setEndTime(order.getEndTime());
                orderVO.setRemark(order.getRemark());
                orderVO.setSender(sender);
                orderVO.setReceiver(receiver);
                orderVO.setGoods(goods);
                orderVOList.add(orderVO);
            }
        }
        resultVO.setCode(ReturnStatusEnums.SELECT_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.SELECT_SUCCESS.getMsg());
        resultVO.setData(orderVOList);
        return resultVO;
    }

    @Override
    public ResultVO getOrderCount(String status) throws Exception {
        int count = orderMapper.selectCount(new EntityWrapper<Orders>().eq("status",status));
        resultVO.setCode(190);
        resultVO.setMsg("查询记录数");
        resultVO.setData(count);
        return resultVO;
    }

    @Override
    public ResultVO serachByTime(String time,String status) throws Exception {
        Recipient sender = null;
        Recipient receiver = null;
        Goods goods = null;
        Pay pay = null;
        List<Orders> ordersList = orderMapper.searchByTime(time,status);
        List<OrderVO> orderVOList = new ArrayList<>();
        if (ordersList!=null){
            for (Orders order:ordersList){
                OrderVO orderVO = new OrderVO();
                if (order.getSenderId()!=null)
                    sender = recipientMapper.selectById(order.getSenderId());
                if (order.getReceiverId()!=null)
                    receiver = recipientMapper.selectById(order.getReceiverId());
                if (order.getGoodsId()!=null)
                    goods = goodsMapper.selectById(order.getGoodsId());
                if (order.getPayId()!=null)
                    pay = payMapper.selectById(order.getPayId());
                orderVO.setOrderId(order.getOrderId());
                orderVO.setUserId(order.getUserId());
                orderVO.setStatus(order.getStatus());
                orderVO.setOrderTime(order.getOrderTime());
                orderVO.setOrderCode(order.getOrderCode());
                orderVO.setPay(pay);
                orderVO.setCost(order.getCost());
                orderVO.setIsNotice(order.getIsNotice());
                orderVO.setStartTime(order.getStartTime());
                orderVO.setEndTime(order.getEndTime());
                orderVO.setRemark(order.getRemark());
                orderVO.setSender(sender);
                orderVO.setReceiver(receiver);
                orderVO.setGoods(goods);
                orderVOList.add(orderVO);
            }
        }
        resultVO.setCode(ReturnStatusEnums.SELECT_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.SELECT_SUCCESS.getMsg());
        resultVO.setData(orderVOList);
        return resultVO;
    }

    @Override
    public ResultVO serachAllColumn(String status, String conditions) throws Exception {
        List<OrderVO> list = (List<OrderVO>) this.getOrder(status).getData();
        List<OrderVO> resultList = list.stream()
                .filter(orderVO -> orderVO.getOrderCode().contains(conditions)
                        ||conditions.equals(orderVO.getSender().getName())
                        ||conditions.equals(orderVO.getSender().getPhone())
                        ||(orderVO.getSender().getProvince()+orderVO.getSender().getCity()+orderVO.getSender().getCounty()+orderVO.getSender().getStreet()+orderVO.getSender().getAddress()).contains(conditions)
                ).collect(Collectors.toList());
        resultVO.setCode(ReturnStatusEnums.SELECT_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.SELECT_SUCCESS.getMsg());
        resultVO.setData(resultList);
        return resultVO;
    }

    @Override
    public ResultVO receiveOrder(String orderId) throws Exception {
        orders = orderMapper.selectById(orderId);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (new Date().before(orders.getStartTime())){
            orders.setReceiveTime(now);
            orders.setStatus(OrderStatus.RECEIVE.getCode());
            if (orderMapper.updateById(orders)==1){
                webSocket.sendOneMessage(orders.getUserId(),JSONObject.toJSONString(orders));
                this.sendEmial("1054632915@qq.com","yxj201503316@163.com","取件通知","您有快递已接单，等待取件");
                resultVO.setCode(ReturnStatusEnums.ORDER_RECEIVED.getCode());
                resultVO.setMsg(ReturnStatusEnums.ORDER_RECEIVED.getMsg());
                resultVO.setData(orders);
                return resultVO;
            }else {
                resultVO.setCode(ReturnStatusEnums.ORDER_RECEIVED_FAIL.getCode());
                resultVO.setMsg(ReturnStatusEnums.ORDER_RECEIVED_FAIL.getMsg());
                resultVO.setData(null);
                return resultVO;
            }
        }else {
            resultVO.setCode(ReturnStatusEnums.ORDER_EXPIRED.getCode());
            resultVO.setMsg(ReturnStatusEnums.ORDER_EXPIRED.getMsg());
            resultVO.setData(null);
            return resultVO;
        }
    }

    @Override
    public ResultVO batchReceiveOrder(String[] orderIds) throws Exception {
        for (String orderId:orderIds){
            if (this.receiveOrder(orderId).getCode()!=408){
                throw new MyException(ErrorEnums.SYS_ERROR);
            }
        }
        resultVO.setCode(ReturnStatusEnums.ORDER_RECEIVED.getCode());
        resultVO.setMsg(ReturnStatusEnums.ORDER_RECEIVED.getMsg());
        resultVO.setData(null);
        return resultVO;
    }

    @Override
    public ResultVO search(String status, String time, String conditions) throws Exception {
        List<OrderVO> list = (List<OrderVO>)this.serachByTime(time,status).getData();
        List<OrderVO> resultList = list.stream()
                .filter(orderVO -> orderVO.getOrderCode().contains(conditions)
                        ||conditions.equals(orderVO.getSender().getName())
                        ||conditions.equals(orderVO.getSender().getPhone())
                        ||(orderVO.getSender().getProvince()+orderVO.getSender().getCity()+orderVO.getSender().getCounty()+orderVO.getSender().getStreet()+orderVO.getSender().getAddress()).contains(conditions)
                ).collect(Collectors.toList());
        resultVO.setCode(ReturnStatusEnums.SELECT_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.SELECT_SUCCESS.getMsg());
        resultVO.setData(resultList);
        return resultVO;
    }

    @Override
    public ResultVO takeOrder(String orderId,double cost,double weight) throws Exception {
        orders.setOrderId(orderId);
        orders.setCost(cost);
        orders.setStatus(OrderStatus.TAKE.getCode());
        orders.setTakeTime(new Timestamp(System.currentTimeMillis()));
        int i = orderMapper.updateById(orders);
        orders = orderMapper.selectById(orderId);
        Goods goods = new Goods();
        goods.setGoodsId(orders.getGoodsId());
        goods.setWeight(weight);
        int j = goodsMapper.updateById(goods);
        if (i==1&&j==1){
            this.noticePay(orders.getOrderId());
            resultVO.setCode(ReturnStatusEnums.ORDER_TAKED.getCode());
            resultVO.setMsg(ReturnStatusEnums.ORDER_TAKED.getMsg());
            resultVO.setData(null);
            return resultVO;
        }else {
            throw new MyException(ErrorEnums.SYS_ERROR);
        }
    }

    @Override
    public ResultVO searchAll(String time, String conditions, String status) throws Exception {
        List<OrderVO> list = new ArrayList<>();
        if (!(time!=null && time.length()!=0))
            list = (List<OrderVO>)this.getOrder(status).getData();
        if ((time!=null && time.length()!=0)&&(status!=null&&status.length()!=0))
            list = (List<OrderVO>)this.serachByTime(time,status).getData();
        if (time!=null && time.length()!=0&&!(status!=null&&status.length()!=0))
            list = this.searchAllByTime(time);
        List<OrderVO> resultList = list.stream()
                .filter(orderVO -> orderVO.getOrderCode().contains(conditions)
                        ||conditions.equals(orderVO.getSender().getName())
                        ||conditions.equals(orderVO.getSender().getPhone())
                        ||(orderVO.getSender().getProvince()+orderVO.getSender().getCity()+orderVO.getSender().getCounty()+orderVO.getSender().getStreet()+orderVO.getSender().getAddress()).contains(conditions)
                ).collect(Collectors.toList());
        resultVO.setCode(ReturnStatusEnums.SELECT_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.SELECT_SUCCESS.getMsg());
        resultVO.setData(resultList);
        return resultVO;
    }

    @Override
    public ResultVO costTotal(String time) throws Exception {
        double total = orderMapper.costTotal(time);
        double collection = orderMapper.collection(time);
        payVO.setTime(time);
        payVO.setCostTotal(total);
        payVO.setCollection(collection);
        payVO.setRefund(0);
        List<PayVO> list = new ArrayList<>();
        list.add(payVO);
        resultVO.setCode(ReturnStatusEnums.SELECT_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.SELECT_SUCCESS.getMsg());
        resultVO.setData(list);
        return resultVO;
    }

    @Override
    public ResultVO costTotal(String startTime, String endTime) throws Exception {
        List<PayVO> list = orderMapper.costTotalBet(startTime,endTime);
        List<PayVO> list1 = orderMapper.collectionBet(startTime,endTime);
        for (PayVO payVO : list){
            for (PayVO payVO1 : list1){
                if (payVO.getTime().equals(payVO1.getTime())){
                    payVO.setCollection(payVO1.getCollection());
                    list1.remove(payVO1);
                }
            }
        }
        for (PayVO payVO1 : list1){
            PayVO payVO = new PayVO();
            payVO.setTime(payVO1.getTime());
            payVO.setCollection(payVO1.getCollection());
            list.add(payVO);
        }
        resultVO.setCode(ReturnStatusEnums.SELECT_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.SELECT_SUCCESS.getMsg());
        resultVO.setData(list);
        return resultVO;
    }

    @Override
    public ResultVO noticePay(String... orderIds) throws Exception {
        for (String orderId:orderIds){
            orders = orderMapper.selectById(orderId);
            webSocket.sendOneMessage(orders.getUserId(),JSONObject.toJSONString(orders));
            this.sendEmial("1054632915@qq.com","yxj201503316@163.com","付款通知","您有"+orders.getCost()+"元消费待付款");
        }
        resultVO.setCode(999);
        resultVO.setMsg("通知成功");
        resultVO.setData(null);
        return resultVO;
    }

    @Override
    public ResultVO sendOrder(String phone) throws Exception {
        String[] status = {"1","2","3","4","5"};
        List<Orders> ordersList = orderMapper.selectList(new EntityWrapper<Orders>().in("status",status));
        Recipient sender = null;
        Recipient receiver = null;
        Goods goods = null;
        Pay pay = null;
        List<OrderVO> orderVOList = new ArrayList<>();
        if (ordersList!=null){
            for (Orders order:ordersList){
                OrderVO orderVO = new OrderVO();
                if (order.getSenderId()!=null)
                    sender = recipientMapper.selectById(order.getSenderId());
                if (order.getReceiverId()!=null)
                    receiver = recipientMapper.selectById(order.getReceiverId());
                if (order.getGoodsId()!=null)
                    goods = goodsMapper.selectById(order.getGoodsId());
                if (order.getPayId()!=null)
                    pay = payMapper.selectById(order.getPayId());
                orderVO.setOrderId(order.getOrderId());
                orderVO.setUserId(order.getUserId());
                orderVO.setStatus(order.getStatus());
                orderVO.setOrderTime(order.getOrderTime());
                orderVO.setOrderCode(order.getOrderCode());
                orderVO.setPay(pay);
                orderVO.setCost(order.getCost());
                orderVO.setIsNotice(order.getIsNotice());
                orderVO.setStartTime(order.getStartTime());
                orderVO.setEndTime(order.getEndTime());
                orderVO.setRemark(order.getRemark());
                orderVO.setSender(sender);
                orderVO.setReceiver(receiver);
                orderVO.setGoods(goods);
                orderVOList.add(orderVO);
            }
        }
        List<OrderVO> resultList = orderVOList.stream().filter(orderVO -> phone.equals(orderVO.getSender().getPhone())).collect(Collectors.toList());
        resultVO.setCode(ReturnStatusEnums.SELECT_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.SELECT_SUCCESS.getMsg());
        resultVO.setData(resultList);
        return resultVO;
    }

    @Override
    public ResultVO recOrder(String phone) throws Exception {
        String[] status = {"1","2","3","4","5"};
        List<Orders> ordersList = orderMapper.selectList(new EntityWrapper<Orders>().in("status",status));
        Recipient sender = null;
        Recipient receiver = null;
        Goods goods = null;
        Pay pay = null;
        List<OrderVO> orderVOList = new ArrayList<>();
        if (ordersList!=null){
            for (Orders order:ordersList){
                OrderVO orderVO = new OrderVO();
                if (order.getSenderId()!=null)
                    sender = recipientMapper.selectById(order.getSenderId());
                if (order.getReceiverId()!=null)
                    receiver = recipientMapper.selectById(order.getReceiverId());
                if (order.getGoodsId()!=null)
                    goods = goodsMapper.selectById(order.getGoodsId());
                if (order.getPayId()!=null)
                    pay = payMapper.selectById(order.getPayId());
                orderVO.setOrderId(order.getOrderId());
                orderVO.setUserId(order.getUserId());
                orderVO.setStatus(order.getStatus());
                orderVO.setOrderTime(order.getOrderTime());
                orderVO.setOrderCode(order.getOrderCode());
                orderVO.setPay(pay);
                orderVO.setCost(order.getCost());
                orderVO.setIsNotice(order.getIsNotice());
                orderVO.setStartTime(order.getStartTime());
                orderVO.setEndTime(order.getEndTime());
                orderVO.setRemark(order.getRemark());
                orderVO.setSender(sender);
                orderVO.setReceiver(receiver);
                orderVO.setGoods(goods);
                orderVOList.add(orderVO);
            }
        }
        List<OrderVO> resultList = orderVOList.stream().filter(orderVO -> phone.equals(orderVO.getReceiver().getPhone())).collect(Collectors.toList());
        resultVO.setCode(ReturnStatusEnums.SELECT_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.SELECT_SUCCESS.getMsg());
        resultVO.setData(resultList);
        return resultVO;
    }

    @Override
    public ResultVO completedOrder(String phone) throws Exception {
        List<Orders> ordersList = orderMapper.selectList(new EntityWrapper<Orders>().eq("status","6"));
        Recipient sender = null;
        Recipient receiver = null;
        Goods goods = null;
        Pay pay = null;
        List<OrderVO> orderVOList = new ArrayList<>();
        if (ordersList!=null){
            for (Orders order:ordersList){
                OrderVO orderVO = new OrderVO();
                if (order.getSenderId()!=null)
                    sender = recipientMapper.selectById(order.getSenderId());
                if (order.getReceiverId()!=null)
                    receiver = recipientMapper.selectById(order.getReceiverId());
                if (order.getGoodsId()!=null)
                    goods = goodsMapper.selectById(order.getGoodsId());
                if (order.getPayId()!=null)
                    pay = payMapper.selectById(order.getPayId());
                orderVO.setOrderId(order.getOrderId());
                orderVO.setUserId(order.getUserId());
                orderVO.setStatus(order.getStatus());
                orderVO.setOrderTime(order.getOrderTime());
                orderVO.setOrderCode(order.getOrderCode());
                orderVO.setPay(pay);
                orderVO.setCost(order.getCost());
                orderVO.setIsNotice(order.getIsNotice());
                orderVO.setStartTime(order.getStartTime());
                orderVO.setEndTime(order.getEndTime());
                orderVO.setRemark(order.getRemark());
                orderVO.setSender(sender);
                orderVO.setReceiver(receiver);
                orderVO.setGoods(goods);
                orderVOList.add(orderVO);
            }
        }
        List<OrderVO> resultList = orderVOList.stream()
                .filter(orderVO -> phone.equals(orderVO.getSender().getPhone())
                ||phone.equals(orderVO.getReceiver().getPhone())).collect(Collectors.toList());
        resultVO.setCode(ReturnStatusEnums.SELECT_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.SELECT_SUCCESS.getMsg());
        resultVO.setData(resultList);
        return resultVO;
    }

    public ResultVO sendEmial(String from, String to, String title, String content) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        javaMailSender.send(message);
        resultVO.setCode(ReturnStatusEnums.SEND_CODE.getCode());
        resultVO.setMsg(ReturnStatusEnums.SEND_CODE.getMsg());
        return resultVO;
    }

    public List<OrderVO> searchAllByTime(String time) throws Exception{
        Recipient sender = null;
        Recipient receiver = null;
        Goods goods = null;
        Pay pay = null;
        List<Orders> ordersList = orderMapper.searchAllByTime(time);
        List<OrderVO> orderVOList = new ArrayList<>();
        if (ordersList!=null){
            for (Orders order:ordersList){
                OrderVO orderVO = new OrderVO();
                if (order.getSenderId()!=null)
                    sender = recipientMapper.selectById(order.getSenderId());
                if (order.getReceiverId()!=null)
                    receiver = recipientMapper.selectById(order.getReceiverId());
                if (order.getGoodsId()!=null)
                    goods = goodsMapper.selectById(order.getGoodsId());
                if (order.getPayId()!=null)
                    pay = payMapper.selectById(order.getPayId());
                orderVO.setOrderId(order.getOrderId());
                orderVO.setUserId(order.getUserId());
                orderVO.setStatus(order.getStatus());
                orderVO.setOrderTime(order.getOrderTime());
                orderVO.setOrderCode(order.getOrderCode());
                orderVO.setPay(pay);
                orderVO.setCost(order.getCost());
                orderVO.setIsNotice(order.getIsNotice());
                orderVO.setStartTime(order.getStartTime());
                orderVO.setEndTime(order.getEndTime());
                orderVO.setRemark(order.getRemark());
                orderVO.setSender(sender);
                orderVO.setReceiver(receiver);
                orderVO.setGoods(goods);
                orderVOList.add(orderVO);
            }
        }
        return orderVOList;
    }

    private void nullConverNullString(Object obj) throws Exception {
        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
        for (PropertyDescriptor descriptor : descriptors) {
            System.out.println(descriptor.getPropertyType());
            if (descriptor.getPropertyType() != null && "class java.lang.String".equals(descriptor.getPropertyType().toString())) {
                if (propertyUtilsBean.getNestedProperty(obj, descriptor.getName()) == null) {
                    propertyUtilsBean.setNestedProperty(obj, descriptor.getName(), "");
                }
            }
        }
    }


}
