package com.tss.ribbonService.controller;

import com.tss.ribbonService.service.InquireService;
import com.tss.ribbonService.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/25 10:43
 * @description：
 */
@RestController
public class InquireController {

    @Autowired
    private InquireService inquireService;

    @GetMapping("/query")
    public ResultVO expressInquire(String com, String nu,String phone) throws Exception{
        return inquireService.expressInquire(com,nu,phone);
    }

    @GetMapping("/estimate")
    public ResultVO priceEstimate() throws Exception{
        return inquireService.priceEstimate("","","","");
    }

    @GetMapping("/com")
    public ResultVO companyReco(String nu) throws Exception{
        return inquireService.companyReco(nu);
    }
}
