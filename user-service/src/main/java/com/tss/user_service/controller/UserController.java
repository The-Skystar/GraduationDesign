package com.tss.user_service.controller;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tss.user_service.Enum.ErrorEnums;
import com.tss.user_service.entity.User;
import com.tss.user_service.exception.MyException;
import com.tss.user_service.service.UserService;
import com.tss.user_service.util.*;
import com.tss.user_service.vo.ResultVO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private User user;

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${spring.mail.username}")
    private String from;

    @PostMapping("/user")
    public List<User> getUser() throws Exception {
        EntityWrapper<User> wrapper = new EntityWrapper<User>();
        wrapper.eq("user_nick","tss");
        return userService.selectList(wrapper);
    }

    /**
     * 注册用户
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/regist")
    public ResultVO regist(User user) throws Exception{
        return userService.regist(user);
    }

    /**
     * 注册用户前对字段进行验证
     * @param column 要验证的数据库字段
     * @param value 要验证的值
     * @return 验证结果 0-已被使用 1-验证通过
     */
    @PostMapping(value = "/validate/{column}/{value}")
    public ResultVO validate(@PathVariable String column, @PathVariable String value) throws Exception{
        return userService.validate(column,value);
    }

    /**
     * 用户密码登录
     * @param str 用户名、手机号或邮箱
     * @param pwd 密码
     * @return 登录结果
     */
    @PostMapping("/pwdLogin")
    public ResultVO pwdLogin(String str,String pwd) throws Exception{
        return userService.pwdLogin(str,pwd);
    }

    /**
     * 向指定手机号发送验证码
     * @param phone
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/sendMsg/{phone}")
    public String getMsg(@PathVariable String phone) throws Exception{
        String ver = sendMsgUtil.getRandNum();
        int time = 1;
        String param = ver + "," + time;
        redisUtil.set(phone,ver);
        redisUtil.expire(phone,time*60);
        return userService.sendMsg("1485146617",param,phone);
    }

    /**
     * 向指定邮箱发送验证码，过期时间为5分钟
     * @param email
     * @return
     * @throws Exception
     */
    @PostMapping("/sendEmail/{email}")
    public ResultVO sendEmail(@PathVariable String email) throws Exception{
        if (redisUtil.get(email)!=null)
            redisUtil.del(email);
        String ver = sendMsgUtil.getRandNum();
        int time = 5;
        redisUtil.set(email,ver);
        redisUtil.expire(email,time*60);
        String content = "【木易】您的验证码为" + ver + "，请于" + time + "分钟内正确输入，如非本人操作，请忽略此邮件。";
//        String content = "我写的发送邮件的代码成功了，与你分享一下，开心，哈哈$$$";
        return userService.sendEmial(from,email,content,"验证码");
    }

    /**
     * 邮箱验证码登录,手机验证码登录要收取服务费，目前不支持
     * @param email
     * @param ver
     * @return
     * @throws Exception
     */
    @PostMapping("/emailLogin")
    public ResultVO emailLogin(String email,String ver) throws Exception{
        return userService.emailLogin(email,ver);
    }

    /**
     * 修改用户个人信息
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/updUser")
    public ResultVO updUser(User user) throws Exception{
        return userService.updUser(user);
    }

    /**
     * 修改用户密码，使用原密码修改时，传参为userId，oldPwd，newPwd
     * 使用邮箱验证码修改时，传参为userId，newPwd，email，ver
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param email
     * @param ver
     * @return
     * @throws Exception
     */
    @PostMapping("/updPwd")
    public ResultVO updPwd(String userId,String oldPwd,String newPwd,String email,String ver) throws Exception{
        return userService.updPwd(userId,oldPwd,newPwd,email,ver);
    }

}
