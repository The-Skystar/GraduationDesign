package com.tss.orderService.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tss.orderService.entity.Goods;
import com.tss.orderService.mapper.GoodsMapper;
import com.tss.orderService.service.GoodsService;
import org.springframework.stereotype.Service;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/5 13:35
 * @description：货物信息服务层
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
}
