package com.tss.user_service.service;

import com.baomidou.mybatisplus.service.IService;
import com.tss.user_service.entity.User;
import com.tss.user_service.vo.ResultVO;

public interface UserService extends IService<User> {

    /**
     *注册用户
     * @param user
     * @return
     */
    public ResultVO regist(User user) throws Exception;

    /**
     * 注册用户前对字段进行验证
     * @param column 要验证的数据库字段
     * @param value 要验证的值
     * @return 验证结果 0-已被使用 1-验证通过
     */
    public ResultVO validate(String column, String value) throws Exception;

    /**
     * 用户密码登录
     * @param str 用户名、手机号或邮箱
     * @param pwd 密码
     * @return 登录结果 0-用户不存在 1-登录成功，返回token 2-密码错误
     */
    public ResultVO pwdLogin(String str,String pwd) throws Exception;

    /**
     * 发送短信验证码
     * @param templateid 短信模板
     * @param phone 手机号（可以是多个）
     * @param param 模板参数
     * @return 发送结果
     */
    public String sendMsg(String templateid,String param,String phone) throws Exception;

    /**
     * 发送邮箱验证码
     * @param from 发件人
     * @param to 收件人
     * @param content 邮件内容
     * @param title 邮件主题
     * @return
     * @throws Exception
     */
    public ResultVO sendEmial(String from,String to,String content,String title) throws Exception;

    /**
     * 邮箱验证码登录
     * @param email 手机号
     * @param ver 验证码
     * @return 登陆结果 0-用户不存在 1-登录成功，返回token 2-密码错误
     */
    public ResultVO emailLogin(String email, String ver) throws Exception;

    /**
     * 修改用户个人信息
     * @param user
     * @return
     * @throws Exception
     */
    public ResultVO updUser(User user) throws Exception;

    /**
     * 修改用户密码
     * @param id
     * @param oldPwd
     * @param newPwd
     * @param email
     * @param ver
     * @return
     * @throws Exception
     */
    public ResultVO updPwd(String id,String oldPwd,String newPwd,String email,String ver) throws Exception;

    /**
     * 退出登录
     * @param userId
     * @return
     * @throws Exception
     */
    public ResultVO exit(String userId) throws Exception;
}
