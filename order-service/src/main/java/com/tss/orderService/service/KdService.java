package com.tss.orderService.service;

import com.alibaba.fastjson.JSONObject;
import com.tss.orderService.vo.ResultVO;

public interface KdService {
    /**
     * Json方式 电子面单
     * @return
     * @throws Exception
     */
    public ResultVO eOrder(JSONObject requestData) throws Exception;

    /**
     * Json方式 在线预约下单
     * @param requestData
     * @return
     * @throws Exception
     */
    public ResultVO appointment(JSONObject requestData) throws Exception;
}
