package com.tss.user_service.service;

import com.baomidou.mybatisplus.service.IService;
import com.tss.user_service.entity.Address;
import com.tss.user_service.vo.ResultVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface AddressService extends IService<Address> {

    /**
     * 用户新增地址信息,并返回该用户所有地址信息
     * @param address
     * @return
     * @throws Exception
     */
    @Transactional
    public ResultVO<List<Address>> createAddress(Address address) throws Exception;

    /**
     * 删除用户地址信息，并返回该用户所有地址信息
     * @param addressId
     * @param userId
     * @return
     * @throws Exception
     */
    @Transactional
    public ResultVO<List<Address>> delAddress(String addressId,String userId) throws Exception;

    /**
     * 修改用户地址信息，并返回该用户所有地址信息
     * @param address
     * @return
     * @throws Exception
     */
    @Transactional
    public ResultVO<List<Address>> updAddress(Address address) throws Exception;

    /**
     * 根据用户编号获取用户所有地址信息
     * @param userId
     * @return
     * @throws Exception
     */
    public ResultVO<List<Address>> selectAll(String userId) throws Exception;

    /**
     * 根据地址信息编号获取用户地址信息
     * @param addressId
     * @return
     * @throws Exception
     */
    public ResultVO<Address> selectByAddressId(String addressId) throws Exception;
}
