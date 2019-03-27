package com.tss.user_service.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tss.user_service.entity.Address;
import com.tss.user_service.entity.User;
import com.tss.user_service.mapper.AddressMapper;
import com.tss.user_service.mapper.UserMapper;
import com.tss.user_service.service.AddressService;
import com.tss.user_service.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/24 14:01
 * @description：用户地址服务层
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private User user;

    @Autowired
    private ResultVO resultVO;
    @Override
    public ResultVO createAddress(Address address) throws Exception {
        addressMapper.insert(address);
        if ("1".equals(address.getIsDefault()))
            userMapper.setDefaultAddress(address.getAddressId(),address.getUserId());
        resultVO.setCode(1);
        resultVO.setMsg("插入成功");
        resultVO.setData(address);
        return resultVO;
    }
}
