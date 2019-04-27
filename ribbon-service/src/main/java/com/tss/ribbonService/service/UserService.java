package com.tss.ribbonService.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.tss.ribbonService.entity.User;
import com.tss.ribbonService.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/23 11:13
 * @description：
 */
@Component
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ResultVO resultVO;

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO regist(User user) throws Exception{
        return restTemplate.postForObject("http://user-service/regist",user,ResultVO.class);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO validate(String column,String value) throws Exception{
        Map<String,String> params = new HashMap<>();
        params.put("column",column);
        params.put("value",value);
        return restTemplate.getForObject("http://user-service/validate?column={column}&value={value}",ResultVO.class,params);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO pwdLogin(String str, String pwd) throws Exception{
        Map<String,String> params = new HashMap<>();
        params.put("str",str);
        params.put("pwd",pwd);
        return restTemplate.postForObject("http://user-service/pwdLogin?str={str}&pwd={pwd}",null,ResultVO.class,params);
    }

    @HystrixCommand(fallbackMethod = "getFallback",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")
    })
    public ResultVO sendEmail(String email) throws Exception{
        return restTemplate.getForObject("http://user-service/sendEmail?email={email}",ResultVO.class,email);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO emailLogin(String email,String ver) throws Exception{
        Map<String,String> params = new HashMap<>();
        params.put("email",email);
        params.put("ver",ver);
        return restTemplate.postForObject("http://user-service/emailLogin?email={email}&ver={ver}",null,ResultVO.class,params);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO updUser(User user) throws Exception{
        return restTemplate.postForObject("http://user-service/updUser",user,ResultVO.class);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO updPwd(String id, String oldPwd, String newPwd,String email,String ver) throws Exception{
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        params.put("oldPwd",oldPwd);
        params.put("newPwd",newPwd);
        params.put("email",email);
        params.put("ver",ver);
        return restTemplate.postForObject("http://user-service/updPwd?" +
                "userId={id}&" +
                "oldPwd={oldPwd}&" +
                "newPwd={newPwd}&" +
                "email={email}&" +
                "ver={ver}",null,ResultVO.class,params);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO exit(String userId) throws Exception{
        return restTemplate.getForObject("http://user-service/exit?userId={1}",ResultVO.class,userId);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO autoLogin(String token) throws Exception{
        return restTemplate.postForObject("http://user-service/autoLogin?token={1}",null,ResultVO.class,token);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO bindEmail(User user) throws Exception{
        return restTemplate.postForObject("http://user-service/bindEmail",user,ResultVO.class);
    }

    private ResultVO getFallback(User user){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        resultVO.setData(null);
        return resultVO;
    }

    private ResultVO getFallback(String column, String value){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        resultVO.setData(null);
        return resultVO;
    }

    private ResultVO getFallback(String email){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        resultVO.setData(null);
        return resultVO;
    }

    private ResultVO getFallback(String id, String oldPwd, String newPwd,String email,String ver){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        resultVO.setData(123);
        return resultVO;
    }
}
