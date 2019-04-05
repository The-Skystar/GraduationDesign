package com.tss.orderService.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tss.orderService.Enum.ErrorEnums;
import com.tss.orderService.Enum.OrderStatus;
import com.tss.orderService.Enum.inter.ErrorCode;
import com.tss.orderService.config.DateConverterConfig;
import com.tss.orderService.entity.Orders;
import com.tss.orderService.exception.MyException;
import com.tss.orderService.mapper.GoodsMapper;
import com.tss.orderService.mapper.OrderMapper;
import com.tss.orderService.mapper.RecipientMapper;
import com.tss.orderService.service.OrderService;
import com.tss.orderService.util.FastJsonUtils;
import com.tss.orderService.vo.OrderVO;
import com.tss.orderService.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

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

    @Override
    public ResultVO createOrder(JSONObject orderJson) throws Exception {
        OrderVO orderVO = FastJsonUtils.getJsonToBean(orderJson.toJSONString(),OrderVO.class);
        int sender = recipientMapper.insert(orderVO.getSender());
        int receiver = recipientMapper.insert(orderVO.getReceiver());
        int goods = goodsMapper.insert(orderVO.getGoods());
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
        if (sender!=1 || receiver!=1 || goods!=1 || order!=1){
            throw new MyException(ErrorEnums.SYS_ERROR);
        }
        resultVO.setData(orderVO);
        return resultVO;
    }
}
