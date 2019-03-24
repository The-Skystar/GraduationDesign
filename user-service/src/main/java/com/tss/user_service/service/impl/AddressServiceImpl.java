package com.tss.user_service.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tss.user_service.entity.Address;
import com.tss.user_service.mapper.AddressMapper;
import com.tss.user_service.service.AddressService;
import org.springframework.stereotype.Service;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/24 14:01
 * @description：用户地址服务层
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {
}
