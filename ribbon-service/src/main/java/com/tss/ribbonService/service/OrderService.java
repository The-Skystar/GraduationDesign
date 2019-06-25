package com.tss.ribbonService.service;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sun.org.apache.regexp.internal.REUtil;
import com.tss.ribbonService.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO getNotReceive(String status) throws Exception{
        return restTemplate.getForObject("http://order-service/getOrder?status={1}",ResultVO.class,status);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO searchByTime(String time,String status) throws Exception{
        Map<String,String> params = new HashMap<>();
        params.put("time",time);
        params.put("status",status);
        return restTemplate.getForObject("http://order-service/searchByTime?time={time}&status={status}",ResultVO.class,params);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO serachAllColumn(String status, String conditions) throws Exception{
        Map<String,String> params = new HashMap<>();
        params.put("conditions",conditions);
        params.put("status",status);
        return restTemplate.getForObject("http://order-service/searchAllColumn?status={status}&conditions={conditions}",ResultVO.class,params);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO receiveOrder(String orderId) throws Exception{
        return restTemplate.getForObject("http://order-service/receiveOrder?orderId={1}",ResultVO.class,orderId);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO getOrderCount(String status) throws Exception{
        return restTemplate.getForObject("http://order-service/count?status={1}",ResultVO.class,status);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO serach(String status, String time, String conditions) throws Exception{
        Map<String,String> params = new HashMap<>();
        params.put("conditions",conditions);
        params.put("status",status);
        params.put("time",time);
        return restTemplate.getForObject("http://order-service/search?status={status}&time={time}&conditions={conditions}",ResultVO.class,params);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO batchReceiveOrder(String orderId) throws Exception{
        return restTemplate.getForObject("http://order-service/batchReceive?orderId={1}",ResultVO.class,orderId);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO takeOrder(String orderId,double cost,double weight) throws Exception{
        Map<String,Object> params = new HashMap<>();
        params.put("orderId",orderId);
        params.put("cost",cost);
        params.put("weight",weight);
        return restTemplate.getForObject("http://order-service/take?orderId={orderId}&cost={cost}&weight={weight}",ResultVO.class,params);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO searchAll(String time, String conditions, String status) throws Exception{
        Map<String,String> params = new HashMap<>();
        params.put("conditions",conditions);
        params.put("status",status);
        params.put("time",time);
        return restTemplate.getForObject("http://order-service/searchAll?status={status}&time={time}&conditions={conditions}",ResultVO.class,params);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO costTotal(String time) throws Exception{
        return restTemplate.getForObject("http://order-service/costTotal?time={1}",ResultVO.class,time);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO costTotal(String startTime, String endTime) throws Exception{
        Map<String,String> params = new HashMap<>();
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        return restTemplate.getForObject("http://order-service/costTotalBet?startTime={startTime}&endTime={endTime}",ResultVO.class,params);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO noticePay(String orderId) throws Exception{
        return restTemplate.getForObject("http://order-service/notice?orderId={1}",ResultVO.class,orderId);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO sendOrder(String phone) throws Exception{
        return restTemplate.getForObject("http://order-service/sendOrder?phone={1}",ResultVO.class,phone);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO recOrder(String phone) throws Exception{
        return restTemplate.getForObject("http://order-service/recOrder?phone={1}",ResultVO.class,phone);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO comOrder(String phone) throws Exception{
        return restTemplate.getForObject("http://order-service/comOrder?phone={1}",ResultVO.class,phone);
    }

    private ResultVO getFallback(String orderId,double cost,double weight){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        return resultVO;
    }

    private ResultVO getFallback(JSONObject order){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        return resultVO;
    }

    private ResultVO getFallback(String status,String time,String conditions){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        return resultVO;
    }

    private ResultVO getFallback(String status,String time){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        return resultVO;
    }

    private ResultVO getFallback(String status){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        return resultVO;
    }
}
