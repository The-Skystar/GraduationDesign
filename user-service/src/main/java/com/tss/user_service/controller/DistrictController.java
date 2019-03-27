package com.tss.user_service.controller;

import com.tss.user_service.service.DistrictService;
import com.tss.user_service.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/25 11:00
 * @description：行政区划控制层
 */
@RestController
public class DistrictController {

    @Autowired
    private DistrictService districtService;

    @GetMapping("/province")
    public ResultVO getProvince() throws Exception{
        return districtService.getProvince();
    }

    @GetMapping("/city")
    public ResultVO getCity(String parent) throws Exception{
        return districtService.getCity(parent);
    }

}
