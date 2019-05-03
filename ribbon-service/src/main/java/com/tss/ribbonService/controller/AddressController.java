package com.tss.ribbonService.controller;

import com.tss.ribbonService.entity.Address;
import com.tss.ribbonService.service.AddressService;
import com.tss.ribbonService.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/22 11:22
 * @description：
 */
@RestController
@Api(value = "用户地址信息控制器")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/createAddress")
    @ApiOperation(value = "创建用户地址信息")
    public ResultVO createAddress(Address address) throws Exception{
        return addressService.createAddress(address);
    }

    @GetMapping("/del")
    @ApiOperation(value = "删除用户地址信息")
    public ResultVO delAddress(String addressId,String userId) throws Exception{
        return addressService.delAddress(addressId,userId);
    }

    @PostMapping("/upd")
    @ApiOperation(value = "修改用户地址信息")
    public ResultVO updAddress(Address address) throws Exception{
        return addressService.updAddress(address);
    }

    @GetMapping("/all")
    @ApiOperation(value = "查询用户所有地址信息")
    public ResultVO selectAll(String userId) throws Exception{
        return addressService.selectAll(userId);
    }

    @GetMapping("/selectById")
    @ApiOperation(value = "查询指定地址信息")
    public ResultVO selectById(String addressId) throws Exception{
        return addressService.selectById(addressId);
    }

    @GetMapping("/geocoder")
    @ApiOperation(value = "逆地址编码")
    public ResultVO geocoder(String longitude,String latitude) throws Exception{
        return addressService.geocoder(longitude,latitude);
    }
}
