package com.tss.user_service.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidubce.BceClientConfiguration;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.dumap.DuMapClient;
import com.baidubce.services.dumap.model.ReverseGeocoderParam;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tss.user_service.Enum.ReturnStatusEnums;
import com.tss.user_service.entity.Address;
import com.tss.user_service.entity.User;
import com.tss.user_service.mapper.AddressMapper;
import com.tss.user_service.mapper.UserMapper;
import com.tss.user_service.service.AddressService;
import com.tss.user_service.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/24 14:01
 * @description：用户地址服务层
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    private static Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private User user;

    @Autowired
    private ResultVO resultVO;

    private static final String ACCESS_KEY_ID = "33f93943865a4daea36359397f8bcd63";             // 请替换为您的Access Key ID
    private static final String SECRET_ACCESS_KEY = "858fb53675314c5eb1ced1ec1a75f493";     // 请替换为您的Secret Access Key
    private static final String APP_ID = "0ea746ec-9ffd-459a-aab8-59b25d1a54e4";                           // 请替换为您的App Id


    @Override
    public ResultVO<List<Address>> createAddress(Address address) throws Exception {
        logger.info("创建一条地址信息，如果是默认地址则修改tb_user表");
        addressMapper.insert(address);
        if ("1".equals(address.getIsDefault()))
            userMapper.setDefaultAddress(address.getAddressId(),address.getUserId());
        resultVO.setCode(ReturnStatusEnums.ADD_ADDRESS_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.ADD_ADDRESS_SUCCESS.getMsg());
        resultVO.setData(this.getAll(address.getUserId()));
        return resultVO;
    }

    @Override
    public ResultVO<List<Address>> delAddress(String addressId,String userId) throws Exception {
        EntityWrapper<Address> wrapper = new EntityWrapper<>();
        wrapper.eq("address_id",addressId);
        EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
        userEntityWrapper.eq("user_id",userId);
        userEntityWrapper.eq("default_address",addressId);
        List<User> list = userMapper.selectList(userEntityWrapper);
        if (list!=null){
            user.setUserId(userId);
            user.setDefaultAddress("");
            userMapper.updateById(user);
            logger.info("该地址是默认地址，删除时修改user表");
        }
        if (addressMapper.delete(wrapper)==1){
            resultVO.setCode(ReturnStatusEnums.DEL_ADDRESS_SUCCESS.getCode());
            resultVO.setMsg(ReturnStatusEnums.DEL_ADDRESS_SUCCESS.getMsg());
            resultVO.setData(this.getAll(userId));
            logger.info("删除一条地址信息");
        }
        return resultVO;
    }

    @Override
    public ResultVO<List<Address>> updAddress(Address address) throws Exception {
        address.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (addressMapper.updateById(address)==1){
            logger.info("修改地址信息");
            if ("1".equals(address.getIsDefault())){
                user.setUserId(address.getUserId());
                user.setDefaultAddress(address.getAddressId());
                userMapper.updateById(user);
                logger.info("是默认地址，修改user表");
            }
            resultVO.setCode(ReturnStatusEnums.UPD_ADDRESS_SUCCESS.getCode());
            resultVO.setMsg(ReturnStatusEnums.UPD_ADDRESS_SUCCESS.getMsg());
            resultVO.setData(this.getAll(address.getUserId()));
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
        logger.info("查询用户所有地址信息");
        return resultVO;
    }

    public List<Address> getAll(String userId) throws Exception{
        EntityWrapper<Address> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id",userId);
        logger.info("查询用户所有地址信息");
        return addressMapper.selectList(wrapper);
    }

    @Override
    public ResultVO<Address> selectByAddressId(String addressId) throws Exception {
        resultVO.setCode(ReturnStatusEnums.SELECT_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.SELECT_SUCCESS.getMsg());
        resultVO.setData(addressMapper.selectById(addressId));
        logger.info("查询指定地址信息");
        return resultVO;
    }

    @Override
    public ResultVO geocoder(String longitude, String latitude) throws Exception {
        resultVO.setCode(600);
        resultVO.setMsg("逆地址编码");
        resultVO.setData(JSONObject.parseObject(this.createDuMapClientAndQuery(latitude,longitude)));
        return resultVO;
    }



    private DuMapClient client;

    private void createDuMapClient() {
        BceClientConfiguration config = new BceClientConfiguration()
                .withCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));

        client = new DuMapClient(config);
    }

    private String callDuMapService(String latitude,String longitude) {
        ReverseGeocoderParam params = ReverseGeocoderParam.builder()
                .location(latitude+","+longitude)
                .coordtype("bd09ll")
                .retCoordtype("bd09ll")
                .pois(0)
                .radius(1000)
                .output("json")
                .build();
        return client.reverseGeocoder(APP_ID, params);
    }

    public String createDuMapClientAndQuery(String latitude,String longitude) {
        createDuMapClient();
        return callDuMapService(latitude,longitude);
    }
}
