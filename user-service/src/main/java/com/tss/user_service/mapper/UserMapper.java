package com.tss.user_service.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tss.user_service.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface UserMapper extends BaseMapper<User> {

    User getUserByNick(String nick);

    User getUserByPhone(String phone);

    User getUserByEmail(String email);

    int setDefaultAddress(@Param("addressId")String addressId,@Param("userId")String userId) throws Exception;
}
