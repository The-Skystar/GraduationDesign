package com.tss.ribbonService.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tss.ribbonService.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/28 20:37
 * @description：
 */
@Component
public class DistrictService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ResultVO resultVO;

    @HystrixCommand(fallbackMethod = "getFallback")
    public ResultVO getPostcode(String districtCode) throws Exception{
        return restTemplate.getForObject("http://order-service/district/postcode?districtCode={1}",ResultVO.class,districtCode);
    }

    private ResultVO getFallback(String districtCode){
        resultVO.setCode(500);
        resultVO.setMsg("获取邮编失败");
        return resultVO;
    }
}
