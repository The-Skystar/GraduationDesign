package com.tss.ribbonService.controller;

import com.tss.ribbonService.service.DistrictService;
import com.tss.ribbonService.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/28 20:42
 * @description：
 */
@RestController
public class DistrictController {
    @Autowired
    private DistrictService districtService;

    @GetMapping("/postcode")
    public ResultVO getPostcode(String districtCode) throws Exception{
        return districtService.getPostcode(districtCode);
    }
}
