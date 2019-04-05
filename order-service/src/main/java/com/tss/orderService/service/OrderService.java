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
    public ResultVO createOrder(JSONObject orderVO) throws Exception;
}
