package com.tss.orderService.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tss.orderService.Enum.ErrorEnums;
import com.tss.orderService.Enum.OrderStatus;
import com.tss.orderService.Enum.ReturnStatusEnums;
import com.tss.orderService.Enum.inter.ErrorCode;
import com.tss.orderService.config.DateConverterConfig;
import com.tss.orderService.entity.Orders;
import com.tss.orderService.exception.MyException;
import com.tss.orderService.mapper.GoodsMapper;
import com.tss.orderService.mapper.OrderMapper;
import com.tss.orderService.mapper.RecipientMapper;
import com.tss.orderService.service.OrderService;
import com.tss.orderService.util.FastJsonUtils;
import com.tss.orderService.util.IDUtils;
import com.tss.orderService.util.KdUtil;
import com.tss.orderService.vo.OrderVO;
import com.tss.orderService.vo.ResultVO;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/5 12:58
 * @description：订单基本信息服务层
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private ResultVO resultVO;

    @Autowired
    private Orders orders;

    @Autowired
    private RecipientMapper recipientMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private KdUtil kdUtil;

    //电商ID
    private String EBusinessID = "1437550";
    //电商加密私钥，快递鸟提供，注意保管，不要泄漏
    private String AppKey = "a4774b93-94cd-4315-a77c-82333cd375f5";
    //请求url, 正式环境地址：http://api.kdniao.com/api/Eorderservice    测试环境地址：http://testapi.kdniao.com:8081/api/EOrderService
    private String eOrderReqURL = "http://testapi.kdniao.com:8081/api/Eorderservice";

    //测试请求url
    private String AppointmentReqURL = "http://testapi.kdniao.com:8081/api/oorderservice";
    //正式请求url
    //private string ReqURL = "http://api.kdniao.com/api/OOrderService";

    @Override
    public ResultVO createOrder(JSONObject orderJson) throws Exception {
        OrderVO orderVO = FastJsonUtils.getJsonToBean(orderJson.toJSONString(), OrderVO.class);
        int sender = recipientMapper.insert(orderVO.getSender());
        int receiver = recipientMapper.insert(orderVO.getReceiver());
        int goods = goodsMapper.insert(orderVO.getGoods());
        orders.setOrderCode(IDUtils.getOrderId());
        orders.setSenderId(orderVO.getSender().getRecId());
        orders.setReceiverId(orderVO.getReceiver().getRecId());
        orders.setGoodsId(orderVO.getGoods().getGoodsId());
        orders.setUserId(orderVO.getUserId());
        orders.setStatus(OrderStatus.DROP_ORDER.getCode());
        orders.setOrderTime(new Timestamp(System.currentTimeMillis()));
        orders.setCost(orderVO.getCost());
        orders.setIsNotice(orderVO.getIsNotice());
        orders.setStartTime(orderVO.getStartTime());
        orders.setEndTime(orderVO.getEndTime());
        orders.setRemark(orderVO.getRemark());
        int order = orderMapper.insert(orders);
        if (sender != 1 || receiver != 1 || goods != 1 || order != 1) {
            throw new MyException(ErrorEnums.SYS_ERROR);
        }
        resultVO.setData(orderVO);
        return resultVO;
    }

    @Override
    public ResultVO eOrder(JSONObject requestDataJson) throws Exception {
        String requestData = requestDataJson.toJSONString();
        Map<String, String> params = new HashMap<>();
        params.put("RequestData", kdUtil.urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBusinessID);
        params.put("RequestType", "1007");
        String dataSign = kdUtil.encrypt(requestData, AppKey, "UTF-8");
        params.put("DataSign", kdUtil.urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");
        String result = kdUtil.sendPost(eOrderReqURL, params);
        System.out.println(result);
        JSONObject resultJson = JSONObject.parseObject(result);
        if (resultJson.getBoolean("Success")) {

            resultVO.setCode(ReturnStatusEnums.ORDER_ONLINE_SUCCESS.getCode());
            resultVO.setMsg(ReturnStatusEnums.ORDER_ONLINE_SUCCESS.getMsg());
            resultVO.setData(resultJson);
            return resultVO;
        }
        resultVO.setCode(ReturnStatusEnums.ORDER_ONLINE_FAIL.getCode());
        resultVO.setMsg(ReturnStatusEnums.ORDER_ONLINE_FAIL.getMsg());
        resultVO.setData(null);
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

    private void nullConverNullString(Object obj) throws Exception {
        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
        for (PropertyDescriptor descriptor : descriptors) {
            System.out.println(descriptor.getPropertyType());
            if (descriptor.getPropertyType() != null && "class java.lang.String".equals(descriptor.getPropertyType().toString())) {
                if (propertyUtilsBean.getNestedProperty(obj, descriptor.getName()) == null) {
                    propertyUtilsBean.setNestedProperty(obj, descriptor.getName(), "");
                }
            }
        }
    }

}
