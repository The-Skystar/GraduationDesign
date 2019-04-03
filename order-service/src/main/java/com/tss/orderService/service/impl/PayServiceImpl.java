package com.tss.orderService.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tss.orderService.entity.Pay;
import com.tss.orderService.mapper.PayMapper;
import com.tss.orderService.service.PayService;
import org.springframework.stereotype.Service;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/2 16:53
 * @description：支付记录服务层
 */
@Service
public class PayServiceImpl extends ServiceImpl<PayMapper, Pay> implements PayService {
}
