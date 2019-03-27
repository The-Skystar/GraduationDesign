package com.tss.user_service.controller;

import com.tss.user_service.entity.Address;
import com.tss.user_service.entity.User;
import com.tss.user_service.service.AddressService;
import com.tss.user_service.service.UserService;
import com.tss.user_service.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/24 14:05
 * @description：用户地址信息控制层
 */
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/createAddress")
    public ResultVO createAddress(Address address) throws Exception{
        return addressService.createAddress(address);
    }


}
