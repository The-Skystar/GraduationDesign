package com.tss.ribbonService.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tss.ribbonService.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/25 10:39
 * @description：
 */
@Component
public class InquireService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ResultVO resultVO;

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO expressInquire(String com,String nu,String phone) throws Exception{
        Map<String,String> params = new HashMap<>();
        params.put("com",com);
        params.put("nu",nu);
        params.put("phone",phone);
        return restTemplate.getForObject("http://order-service/query/?com={com}&nu={nu}&phone={phone}",ResultVO.class,params);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO priceEstimate(String from, String to,String weight, String time) throws Exception{
        Map<String,String> params = new HashMap<>();
        params.put("from",from);
        params.put("to",to);
        params.put("weight",weight);
        params.put("time",time);
        return restTemplate.getForObject("http://order-service/estimate?from={from}&to={to}&weight={weight}&time={time}",ResultVO.class,params);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO companyReco(String nu) throws Exception{
        return restTemplate.getForObject("http://order-service/com?nu={1}",ResultVO.class,nu);
    }

    private ResultVO getFallback(String com,String nu,String phone){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        return resultVO;
    }

    private ResultVO getFallback(String from, String to,String weight, String time){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        return resultVO;
    }

    private ResultVO getFallback(String nu){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        return resultVO;
    }
}
