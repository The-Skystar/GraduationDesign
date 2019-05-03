package com.tss.ribbonService.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tss.ribbonService.entity.Address;
import com.tss.ribbonService.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/22 11:08
 * @description：
 */
@Component
public class AddressService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ResultVO resultVO;

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO createAddress(Address address) throws Exception{
        return restTemplate.postForObject("http://user-service/createAddress",address,ResultVO.class);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO delAddress(String addressId, String userId) throws Exception{
        Map<String,String> params = new HashMap<>();
        params.put("addressId",addressId);
        params.put("userId",userId);
        return restTemplate.getForObject("http://user-service/delAddress?addressId={addressId}&userId={userId}",ResultVO.class,params);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO updAddress(Address address) throws Exception{
        return restTemplate.postForObject("http://user-service/updAddress",address,ResultVO.class);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO selectAll(String userId) throws Exception{
        return restTemplate.getForObject("http://user-service/selectAll?userId={1}",ResultVO.class,userId);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO selectById(String addressId) throws Exception{
        return restTemplate.getForObject("http://user-service/selectById?addressId={1}",ResultVO.class,addressId);
    }

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO geocoder(String longitude,String latitude) throws Exception{
        Map params = new HashMap();
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        return restTemplate.getForObject("http://user-service/geocoder?longitude={longitude}&latitude={latitude}",ResultVO.class,params);
    }

    public ResultVO getFallback(String addressId){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        resultVO.setData(null);
        return resultVO;
    }

    public ResultVO getFallback(Address address){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        resultVO.setData(null);
        return resultVO;
    }

    public ResultVO getFallback(String addressId,String userId){
        resultVO.setCode(500);
        resultVO.setMsg("系统错误");
        resultVO.setData(null);
        return resultVO;
    }

}
