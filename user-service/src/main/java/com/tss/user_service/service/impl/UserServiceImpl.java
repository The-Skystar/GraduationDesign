package com.tss.user_service.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tss.user_service.Enum.LoginStatusEnums;
import com.tss.user_service.Enum.ReturnStatusEnums;
import com.tss.user_service.Enum.SexEnums;
import com.tss.user_service.entity.User;
import com.tss.user_service.mapper.UserMapper;
import com.tss.user_service.service.UserService;
import com.tss.user_service.util.EncryptUtil;
import com.tss.user_service.util.JwtToken;
import com.tss.user_service.util.RedisUtil;
import com.tss.user_service.util.SendMsgUtil;
import com.tss.user_service.vo.ResultVO;
import com.tss.user_service.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ResultVO resultVO;

    @Autowired
    private UserVO userVO;

    @Autowired
    private EncryptUtil encryptUtil;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SendMsgUtil sendMsgUtil;

    @Autowired
    private JavaMailSender javaMailSender;

    public static final int validity = 60*60;

    @Override
    public ResultVO regist(User user) throws Exception{
        user.setPwd(encryptUtil.MD5(user.getPwd()));
        user.setId(encryptUtil.MD5(user.getId()));
        user.setStatus(LoginStatusEnums.REGIST.getCode());
        if ("男".equals(user.getSex()) || "1".equals(user.getSex()))
            user.setSex(SexEnums.MAN.getCode());
        if ("女".equals(user.getSex()) || "0".equals(user.getSex()))
            user.setSex(SexEnums.WOMAN.getCode());
        if (userMapper.insert(user)==1){
            resultVO.setCode(1);
            resultVO.setMsg("注册成功");
            return resultVO;
        }
        resultVO.setCode(0);
        resultVO.setMsg("注册失败");
        return resultVO;
    }

    @Override
    public ResultVO validate(String column, String value)  throws Exception{
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.eq(column,value);
        Integer count = userMapper.selectCount(wrapper);
        if (count > 0){
            resultVO.setCode(0);
            resultVO.setMsg("已被占用");
            return resultVO;
        }
        resultVO.setCode(1);
        resultVO.setMsg("验证通过");
        return resultVO;
    }

    @Override
    public ResultVO pwdLogin(String str, String pwd) throws Exception{
        List<User> userList = new ArrayList<>();
        userList.add(userMapper.getUserByPhone(str));
        userList.add(userMapper.getUserByEmail(str));
        userList.add(userMapper.getUserByNick(str));
        userList.removeAll(Collections.singleton(null));
        if (userList.size()==0){
            resultVO.setCode(ReturnStatusEnums.ACCOUNT_ERROR.getCode());
            resultVO.setMsg(ReturnStatusEnums.ACCOUNT_ERROR.getMsg());
        } else if (userList.size()==1&&userList.get(0).getStatus()!=LoginStatusEnums.REGIST.getCode()){
            User user = userList.get(0);
            if (LoginStatusEnums.LOGIN.getCode().equals(user.getStatus())){
                resultVO.setCode(ReturnStatusEnums.LOGGED.getCode());
                resultVO.setMsg(ReturnStatusEnums.LOGGED.getMsg());
            }
            if (LoginStatusEnums.CANCLE.getCode().equals(user.getStatus())){
                resultVO.setCode(ReturnStatusEnums.CANALED.getCode());
                resultVO.setMsg(ReturnStatusEnums.CANALED.getMsg());
            }
            if (LoginStatusEnums.LOCK.getCode().equals(user.getStatus())){
                resultVO.setCode(ReturnStatusEnums.FROZEN.getCode());
                resultVO.setMsg(ReturnStatusEnums.FROZEN.getMsg());
            }
            return resultVO;
        } else if (userList.size()==1&&encryptUtil.MD5(pwd).equals(userList.get(0).getPwd())){
            User user = userList.get(0);
            String token = jwtToken.createToken(user.getUserNick());
            if (redisUtil.get(user.getUserNick())==null || redisUtil.get(user.getUserNick()).length()==0)
                redisUtil.set(user.getUserNick(),token);
            redisUtil.expire(user.getUserNick(),validity);
            user.setStatus(LoginStatusEnums.LOGIN.getCode());
            userMapper.updateById(user);
            resultVO.setCode(ReturnStatusEnums.LOGIN_SUCCESS.getCode());
            resultVO.setMsg(token);
            resultVO.setData(user);
        }else{
            resultVO.setCode(ReturnStatusEnums.PWD_ERROR.getCode());
            resultVO.setMsg(ReturnStatusEnums.PWD_ERROR.getMsg());
        }
        return resultVO;
    }

    @Override
    public String sendMsg(String templateid,String param, String phone) throws Exception{
        String url = "https://api.miaodiyun.com/20150822/industrySMS/sendSMS";
        String sid = "72ad97c202e84cd2af3ca4c29ee5c356";
        String token = "dce4ac4498974fd48613cc2669dff895";
        return sendMsgUtil.sendMsg(url,sid,token,templateid,param,phone);
    }

    @Override
    public ResultVO sendEmial(String from, String to, String title, String content) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        javaMailSender.send(message);
        resultVO.setCode(ReturnStatusEnums.SEND_CODE.getCode());
        resultVO.setMsg(ReturnStatusEnums.SEND_CODE.getMsg());
        return resultVO;
    }

    @Override
    public ResultVO emailLogin(String email, String ver) throws Exception{
        User user = userMapper.getUserByEmail(email);
        if (user==null){
            resultVO.setCode(ReturnStatusEnums.MAIL_NOT_MATCH.getCode());
            resultVO.setMsg(ReturnStatusEnums.MAIL_NOT_MATCH.getMsg());
        } else if (LoginStatusEnums.LOGIN.getCode().equals(user.getStatus())){
            resultVO.setCode(ReturnStatusEnums.LOGGED.getCode());
            resultVO.setMsg(ReturnStatusEnums.LOGGED.getMsg());
        } else if (LoginStatusEnums.CANCLE.getCode().equals(user.getStatus())){
            resultVO.setCode(ReturnStatusEnums.CANALED.getCode());
            resultVO.setMsg(ReturnStatusEnums.CANALED.getMsg());
        } else if (LoginStatusEnums.LOCK.getCode().equals(user.getStatus())){
            resultVO.setCode(ReturnStatusEnums.FROZEN.getCode());
            resultVO.setMsg(ReturnStatusEnums.FROZEN.getMsg());
        } else if (redisUtil.get(email)==null || redisUtil.get(email).length()==0){
            resultVO.setCode(ReturnStatusEnums.CODE_EXPIRED.getCode());
            resultVO.setMsg(ReturnStatusEnums.CODE_EXPIRED.getMsg());
        }else if (ver.equals(redisUtil.get(email))){
            String token = jwtToken.createToken(email);
            if (redisUtil.get(email)==null || redisUtil.get(email).length()==0)
                redisUtil.set(email,token);
            redisUtil.expire(email,validity);
            user.setStatus(LoginStatusEnums.LOGIN.getCode());
            userMapper.updateById(user);
            resultVO.setCode(ReturnStatusEnums.LOGIN_SUCCESS.getCode());
            resultVO.setMsg(token);
            resultVO.setData(user);
        }else {
            resultVO.setCode(ReturnStatusEnums.CODE_ERROR.getCode());
            resultVO.setMsg(ReturnStatusEnums.CODE_ERROR.getMsg());
        }
        return resultVO;
    }

    @Override
    public ResultVO updUser(User user) throws Exception {
        int i = userMapper.updateById(user);
        if (i==1){
            user = userMapper.selectById(user.getId());
            resultVO.setCode(ReturnStatusEnums.UPD_USER_SUCCESS.getCode());
            resultVO.setMsg(ReturnStatusEnums.UPD_USER_SUCCESS.getMsg());
            resultVO.setData(user);
        }else {
            resultVO.setCode(ReturnStatusEnums.UPD_USER_FAIL.getCode());
            resultVO.setMsg(ReturnStatusEnums.UPD_USER_FAIL.getMsg());
        }
        return resultVO;
    }

    @Override
    public ResultVO updPwd(String id, String oldPwd, String newPwd,String email,String ver) throws Exception {
        User user = userMapper.selectById(id);
        if (oldPwd!=null){
            if (encryptUtil.MD5(oldPwd).equals(user.getPwd())){
                user.setPwd(encryptUtil.MD5(newPwd));
                if (userMapper.updateById(user)==1){
                    resultVO.setCode(ReturnStatusEnums.UPD_PWD_SUCCESS.getCode());
                    resultVO.setMsg(ReturnStatusEnums.UPD_PWD_SUCCESS.getMsg());
                    resultVO.setData(user);
                }else {
                    resultVO.setCode(ReturnStatusEnums.UPD_PWD_FAIL.getCode());
                    resultVO.setMsg(ReturnStatusEnums.UPD_PWD_FAIL.getMsg());
                }
            }else {
                resultVO.setCode(ReturnStatusEnums.OLD_PWD_ERROR.getCode());
                resultVO.setMsg(ReturnStatusEnums.OLD_PWD_ERROR.getMsg());
            }
        }else {
            String redisVer = redisUtil.get(email);
            if (!email.equals(user.getEmail())){
                //邮箱不匹配
                resultVO.setCode(ReturnStatusEnums.MAIL_NOT_MATCH.getCode());
                resultVO.setMsg(ReturnStatusEnums.MAIL_NOT_MATCH.getMsg());
            }else if(redisVer==null){
                //验证码过期
                resultVO.setCode(ReturnStatusEnums.CODE_EXPIRED.getCode());
                resultVO.setMsg(ReturnStatusEnums.CODE_EXPIRED.getMsg());
            }else if (!ver.equals(redisVer)){
                //验证码不匹配
                resultVO.setCode(ReturnStatusEnums.CODE_ERROR.getCode());
                resultVO.setMsg(ReturnStatusEnums.CODE_ERROR.getMsg());
            }else {
                user.setPwd(encryptUtil.MD5(newPwd));
                if (userMapper.updateById(user)==1){
                    resultVO.setCode(ReturnStatusEnums.UPD_PWD_SUCCESS.getCode());
                    resultVO.setMsg(ReturnStatusEnums.UPD_PWD_SUCCESS.getMsg());
                    resultVO.setData(user);
                }else {
                    resultVO.setCode(ReturnStatusEnums.UPD_PWD_FAIL.getCode());
                    resultVO.setMsg(ReturnStatusEnums.UPD_PWD_FAIL.getMsg());
                }
            }
        }
        return resultVO;
    }

}
