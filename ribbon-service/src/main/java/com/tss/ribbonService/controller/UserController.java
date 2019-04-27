package com.tss.ribbonService.controller;

import com.tss.ribbonService.entity.User;
import com.tss.ribbonService.service.UserService;
import com.tss.ribbonService.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/23 13:34
 * @description：
 */
@RestController
@Api(value = "用户服务控制器")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/reg")
    @ApiOperation(value = "注册用户")
    @ApiImplicitParam(paramType = "body",name = "user",value = "用户信息",required = true,dataType = "User")
    public ResultVO regist(User user) throws Exception{
        return userService.regist(user);
    }

    @PostMapping(value = "/validate")
    @ApiOperation(value = "验证字段是否合法",notes = "参数为数据库对应字段名及其字段的值")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "column",value = "数据库字段名",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "value",value = "字段的值",required = true,dataType = "String")
    })
    public ResultVO validate(String column, String value) throws Exception{
        return userService.validate(column,value);
    }

    @PostMapping("/pwdLogin")
    @ApiOperation(value = "账号密码登录",notes = "账号可以是用户名/手机号（暂不支持）/邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "str",value = "账号",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "pwd",value = "密码",required = true,dataType = "String")
    })
    public ResultVO pwdLogin(String str,String pwd) throws Exception{
        return userService.pwdLogin(str,pwd);
    }

    @PostMapping("/sendEmail")
    @ApiOperation(value = "发送邮箱验证码",notes = "邮箱格式需正确规范")
    @ApiImplicitParam(paramType = "path",name = "email",value = "邮箱地址",required = true,dataType = "String")
    public ResultVO sendEmail(String email) throws Exception{
        return userService.sendEmail(email);
    }

    @PostMapping("/emailLogin")
    @ApiOperation(value = "邮箱验证码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "email",value = "邮箱地址",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "ver",value = "验证码",required = true,dataType = "String")
    })
    public ResultVO emailLogin(String email,String ver) throws Exception{
        return userService.emailLogin(email,ver);
    }

    @PostMapping("/updUser")
    @ApiOperation(value = "修改用户基本信息")
    @ApiImplicitParam(paramType = "body",name = "user",value = "修改后的用户信息",required = true,dataType = "User")
    public ResultVO updUser(User user) throws Exception{
        return userService.updUser(user);
    }

    @PostMapping("/updPwd")
    @ApiOperation(value = "修改用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "id",value = "用户编号",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "oldPwd",value = "旧密码",required = false,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "newPwd",value = "新密码",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "email",value = "邮箱",required = false,dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "ver",value = "验证码",required = false,dataType = "String")
    })
    public ResultVO updPwd(String id, String oldPwd, String newPwd,String email,String ver) throws Exception{
        return userService.updPwd(id,oldPwd,newPwd,email,ver);
    }

    @GetMapping("/exit")
    @ApiOperation(value = "退出登录")
    @ApiImplicitParam(paramType = "query",name = "userId",value = "用户编号",required = true,dataType = "String")
    public ResultVO exit(String userId) throws Exception{
        return userService.exit(userId);
    }

    @PostMapping("/autoLogin")
    @ApiOperation(value = "自动登录")
    public ResultVO autoLogin(String token) throws Exception{
        return userService.autoLogin(token);
    }

    @PostMapping("/bindEmail")
    @ApiOperation(value = "绑定邮箱")
    public ResultVO bindEmail(User user) throws Exception{
        return userService.bindEmail(user);
    }
}
