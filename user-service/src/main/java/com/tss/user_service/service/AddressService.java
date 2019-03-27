package com.tss.user_service.service;

import com.baomidou.mybatisplus.service.IService;
import com.tss.user_service.entity.Address;
import com.tss.user_service.vo.ResultVO;


public interface AddressService extends IService<Address> {

    public ResultVO createAddress(Address address) throws Exception;
}
