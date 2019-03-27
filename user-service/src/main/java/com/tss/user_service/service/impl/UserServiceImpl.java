package com.tss.user_service.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tss.user_service.Enum.LoginStatusEnums;
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
            resultVO.setCode(0);
            resultVO.setMsg("用户不存在");
        } else if (userList.size()==1&&userList.get(0).getStatus()!=LoginStatusEnums.REGIST.getCode()){
            User user = userList.get(0);
            resultVO.setCode(3);
            if (LoginStatusEnums.LOGIN.getCode().equals(user.getStatus())){
                resultVO.setMsg("账户已在其他地方登录");
            }
            if (LoginStatusEnums.CANCLE.getCode().equals(user.getStatus())){
                resultVO.setMsg("该账户已注销");
            }
            if (LoginStatusEnums.LOCK.getCode().equals(user.getStatus())){
                resultVO.setMsg("该账户已冻结");
            }
            return resultVO;
        } else if (userList.size()==1&&encryptUtil.MD5(pwd).equals(userList.get(0).getPwd())){
            User user = userList.get(0);
            String token = jwtToken.createToken(user.getUserNick());
            if (redisUtil.get(user.getUserNick())==null)
                redisUtil.set(user.getUserNick(),token);
            redisUtil.expire(user.getUserNick(),validity);
            user.setStatus(LoginStatusEnums.LOGIN.getCode());
            userMapper.updateById(user);
            resultVO.setCode(1);
            resultVO.setMsg(token);
            resultVO.setData(user);
        }else{
            resultVO.setCode(2);
            resultVO.setMsg("密码错误");
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
        resultVO.setCode(1);
        resultVO.setMsg("验证码已发送");
        return resultVO;
    }

    @Override
    public ResultVO emailLogin(String email, String ver) throws Exception{
        User user = userMapper.getUserByEmail(email);
        if (user==null){
            resultVO.setCode(-2);
            resultVO.setMsg("邮箱未注册");
        } else if (LoginStatusEnums.LOGIN.getCode().equals(user.getStatus())){
            resultVO.setCode(-1);
            resultVO.setMsg("账户已在其他地方登录");
        } else if (LoginStatusEnums.CANCLE.getCode().equals(user.getStatus())){
            resultVO.setCode(-1);
            resultVO.setMsg("该账户已注销");
        } else if (LoginStatusEnums.LOCK.getCode().equals(user.getStatus())){
            resultVO.setCode(-1);
            resultVO.setMsg("该账户已冻结");
        } else if (redisUtil.get(email)==null){
            resultVO.setCode(-1);
            resultVO.setMsg("验证码已过期");
        }else if (ver.equals(redisUtil.get(email))){
            String token = jwtToken.createToken(email);
            if (redisUtil.get(email)==null)
                redisUtil.set(email,token);
            redisUtil.expire(email,validity);
            user.setStatus(LoginStatusEnums.LOGIN.getCode());
            userMapper.updateById(user);
            resultVO.setCode(1);
            resultVO.setMsg("登录成功");
            resultVO.setData(user);
        }else {
            resultVO.setCode(-3);
            resultVO.setMsg("验证码错误");
        }
        return resultVO;
    }

//    private UserVO userToUserVO(User user){
//
//    }
}
