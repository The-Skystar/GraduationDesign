package com.tss.orderService.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tss.orderService.Enum.ReturnStatusEnums;
import com.tss.orderService.service.KdService;
import com.tss.orderService.util.KdUtil;
import com.tss.orderService.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/11 13:58
 * @description：快递鸟业务服务层
 */
@Service
public class KdServiceImpl implements KdService {

    @Autowired
    private KdUtil kdUtil;

    @Autowired
    private ResultVO resultVO;

    //电商ID
    private String EBusinessID="1437550";
    //电商加密私钥，快递鸟提供，注意保管，不要泄漏
    private String AppKey="a4774b93-94cd-4315-a77c-82333cd375f5";
    //请求url, 正式环境地址：http://api.kdniao.com/api/Eorderservice    测试环境地址：http://testapi.kdniao.com:8081/api/EOrderService
    private String eOrderReqURL="http://testapi.kdniao.com:8081/api/Eorderservice";

    //测试请求url
    private String AppointmentReqURL = "http://testapi.kdniao.com:8081/api/oorderservice";
    //正式请求url
    //private string ReqURL = "http://api.kdniao.com/api/OOrderService";

    @Override
    public ResultVO eOrder(JSONObject requestDataJson) throws Exception {
        String requestData = requestDataJson.toJSONString();
        Map<String,String> params = new HashMap<>();
        params.put("RequestData", kdUtil.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBusinessID);
        params.put("RequestType", "1007");
        String dataSign = kdUtil.encrypt(requestData, AppKey, "UTF-8");
        params.put("DataSign", kdUtil.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");
        String result = kdUtil.sendPost(eOrderReqURL,params);
        resultVO.setCode(ReturnStatusEnums.ORDER_ONLINE_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.ORDER_ONLINE_SUCCESS.getMsg());
        resultVO.setData(JSONObject.parseObject(result));
        return resultVO;
    }

    @Override
    public ResultVO appointment(JSONObject requestDataJson) throws Exception {
        String requestData = requestDataJson.toJSONString();
        Map<String, String> params = new HashMap<>();
        params.put("RequestData", kdUtil.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBusinessID);
        params.put("RequestType", "1001");
        String dataSign = kdUtil.encrypt(requestData, AppKey, "UTF-8");
        params.put("DataSign", kdUtil.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");
        String result = kdUtil.sendPost(AppointmentReqURL, params);
        resultVO.setCode(ReturnStatusEnums.APPOINTMENT_SUCCESS.getCode());
        resultVO.setMsg(ReturnStatusEnums.APPOINTMENT_SUCCESS.getMsg());
        resultVO.setData(JSONObject.parseObject(result));
        return resultVO;
    }

}
