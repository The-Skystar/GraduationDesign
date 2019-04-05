package com.tss.user_service.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tss.user_service.Enum.ReturnStatusEnums;
import com.tss.user_service.entity.Address;
import com.tss.user_service.entity.User;
import com.tss.user_service.mapper.AddressMapper;
import com.tss.user_service.mapper.UserMapper;
import com.tss.user_service.service.AddressService;
import com.tss.user_service.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public ResultVO<List<Address>> createAddress(Address address) throws Exception {
        addressMapper.insert(address);
        if ("1".equals(address.getIsDefault()))
            userMapper.setDefaultAddress(address.getAddressId(),address.getUserId());
        resultVO.setCode(ReturnStatusEnums.ADD_ADDRESS_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.ADD_ADDRESS_SUCCESS.getMsg());
        resultVO.setData(this.selectAll(address.getUserId()));
        return resultVO;
    }

    @Override
    public ResultVO<List<Address>> delAddress(String addressId,String userId) throws Exception {
        EntityWrapper<Address> wrapper = new EntityWrapper<>();
        wrapper.eq("address_id",addressId);
        if (addressMapper.delete(wrapper)==1){
            resultVO.setCode(ReturnStatusEnums.DEL_ADDRESS_SUCCESS.getCode());
            resultVO.setMsg(ReturnStatusEnums.DEL_ADDRESS_SUCCESS.getMsg());
            resultVO.setData(this.selectAll(userId));
        }
        return resultVO;
    }

    @Override
    public ResultVO<List<Address>> updAddress(Address address) throws Exception {
        if (addressMapper.updateById(address)==1){
            resultVO.setCode(ReturnStatusEnums.UPD_ADDRESS_SUCCESS.getCode());
            resultVO.setMsg(ReturnStatusEnums.UPD_ADDRESS_SUCCESS.getMsg());
            resultVO.setData(this.selectAll(address.getUserId()));
        }
        return resultVO;
    }

    @Override
    public ResultVO<List<Address>> selectAll(String userId) throws Exception {
        EntityWrapper<Address> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        resultVO.setCode(ReturnStatusEnums.SELECT_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.SELECT_SUCCESS.getMsg());
        resultVO.setData(addressMapper.selectList(wrapper));
        return resultVO;
    }

    @Override
    public ResultVO<Address> selectByAddressId(String addressId) throws Exception {
        resultVO.setCode(ReturnStatusEnums.SELECT_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.SELECT_SUCCESS.getMsg());
        resultVO.setData(addressMapper.selectById(addressId));
        return null;
    }
}
