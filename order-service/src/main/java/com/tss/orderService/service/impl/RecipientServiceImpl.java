package com.tss.orderService.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tss.orderService.entity.Recipient;
import com.tss.orderService.mapper.RecipientMapper;
import com.tss.orderService.service.RecipientService;
import org.springframework.stereotype.Service;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/5 13:26
 * @description：收寄人地址信息服务层
 */
@Service
public class RecipientServiceImpl extends ServiceImpl<RecipientMapper, Recipient> implements RecipientService {
}
