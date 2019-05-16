package com.tss.orderService.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.tss.orderService.entity.Orders;
import com.tss.orderService.vo.ResultVO;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService extends IService<Orders> {

    /**
     * 生成订单
     * @param orderVO
     * @return
     * @throws Exception
     */
    @Transactional
    ResultVO createOrder(JSONObject orderVO) throws Exception;

    /**
     * Json方式 电子面单
     * @return
     * @throws Exception
     */
    @Transactional
    ResultVO eOrder(JSONObject requestData) throws Exception;

    /**
     * Json方式 在线预约下单
     * @param requestData
     * @return
     * @throws Exception
     */
    @Transactional
    ResultVO appointment(JSONObject requestData) throws Exception;

    ResultVO orderDetail(String orderCode) throws Exception;

    ResultVO getOrder(String status) throws Exception;

    ResultVO getOrderCount(String status) throws Exception;

    ResultVO serachByTime(String time,String status) throws Exception;

    ResultVO serachAllColumn(String status,String conditions) throws Exception;

    ResultVO receiveOrder(String orderId) throws Exception;

    @Transactional
    ResultVO batchReceiveOrder(String[] orderIds) throws Exception;

    ResultVO search(String status, String time, String conditions) throws Exception;

    @Transactional
    ResultVO takeOrder(String orderId,double cost,double weight) throws Exception;

    ResultVO searchAll(String time, String conditions, String status) throws Exception;

    ResultVO costTotal(String time) throws Exception;

    ResultVO costTotal(String startTime,String endTime) throws Exception;

    ResultVO noticePay(String...orderIds) throws Exception;

    ResultVO sendOrder(String phone) throws Exception;

    ResultVO recOrder(String phone) throws Exception;

    ResultVO completedOrder(String phone) throws Exception;
}
