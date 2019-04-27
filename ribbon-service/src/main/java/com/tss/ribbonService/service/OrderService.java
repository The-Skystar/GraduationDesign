package com.tss.ribbonService.service;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tss.ribbonService.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/24 9:32
 * @description：
 */
@Component
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ResultVO resultVO;

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO createOrder(JSONObject order) throws Exception{
        return restTemplate.postForObject("http://order-service/create",order,ResultVO.class);
    }

//    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO eOrder(JSONObject requestData) throws Exception{
        return restTemplate.postForObject("http://order-service/eOrder",requestData,ResultVO.class);
    }

    private ResultVO getFallback(JSONObject order){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        return resultVO;
    }
}
