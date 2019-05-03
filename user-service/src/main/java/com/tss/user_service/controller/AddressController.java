package com.tss.user_service.controller;

import com.tss.user_service.entity.Address;
import com.tss.user_service.service.AddressService;
import com.tss.user_service.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/24 14:05
 * @description：用户地址信息控制层
 */
@RestController
@Api(value = "用户地址控制器")
public class AddressController {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private AddressService addressService;

    @PostMapping("/createAddress")
    @ApiOperation(value = "创建用户地址信息",notes = "成功返回该用户所有地址")
    @ApiImplicitParam(paramType = "body",name = "address",value = "地址信息",required = true,dataType = "Address")
    public ResultVO createAddress(@RequestBody Address address) throws Exception{
        return addressService.createAddress(address);
    }

    @GetMapping("/delAddress")
    @ApiOperation(value = "删除用户地址信息",notes = "成功返回该用户其他地址信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "addressId",value = "地址编号",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "userId",value = "用户编号",required = true,dataType = "String")
    })
    public ResultVO delAddress(String addressId,String userId) throws Exception{
        return addressService.delAddress(addressId,userId);
    }

    @PostMapping("/updAddress")
    @ApiOperation(value = "修改用户地址信息",notes = "地址编号必传")
    @ApiImplicitParam(paramType = "body",name = "address",value = "修改的地址信息",required = true,dataType = "Address")
    public ResultVO updAddress(@RequestBody Address address) throws Exception{
        return addressService.updAddress(address);
    }

    @GetMapping("/selectAll")
    @ApiOperation(value = "查询用户的所有地址信息")
    @ApiImplicitParam(paramType = "query",name = "userId",value = "用户编号",required = true,dataType = "String")
    public ResultVO selectAll(String userId) throws Exception{
        return addressService.selectAll(userId);
    }

    @GetMapping("/selectById")
    @ApiOperation(value = "根据地址编号查询地址信息")
    @ApiImplicitParam(paramType = "query",name = "addressId",value = "地址编号",required = true,dataType = "String")
    public ResultVO selectById(String addressId) throws Exception{
        return addressService.selectByAddressId(addressId);
    }

    @GetMapping("/geocoder")
    public ResultVO geocoder(String longitude,String latitude) throws Exception{
        return addressService.geocoder(longitude,latitude);
    }
}
