package com.tss.user_service.controller;

import com.tss.user_service.entity.Address;
import com.tss.user_service.entity.User;
import com.tss.user_service.service.AddressService;
import com.tss.user_service.service.UserService;
import com.tss.user_service.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/del")
    public ResultVO delAddress(String addressId,String userId) throws Exception{
        return addressService.delAddress(addressId,userId);
    }

    @PostMapping("/upd")
    public ResultVO updAddress(Address address) throws Exception{
        return addressService.updAddress(address);
    }

    @GetMapping("/selectAll")
    public ResultVO selectAll(String userId) throws Exception{
        return addressService.selectAll(userId);
    }

    @GetMapping("/getAddress")
    public ResultVO selectById(String addressId) throws Exception{
        return addressService.selectByAddressId(addressId);
    }
}
