package com.tss.user_service.service;

import com.baomidou.mybatisplus.service.IService;
import com.tss.user_service.entity.Address;
import com.tss.user_service.vo.ResultVO;
import org.springframework.transaction.annotation.Transactional;


public interface AddressService extends IService<Address> {

    @Transactional
    public ResultVO createAddress(Address address) throws Exception;
}
