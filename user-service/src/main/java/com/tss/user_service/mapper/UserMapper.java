package com.tss.user_service.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.tss.user_service.entity.User;

public interface UserMapper extends BaseMapper<User> {

    User getUserByNick(String nick);

    User getUserByPhone(String phone);

    User getUserByEmail(String email);
}
