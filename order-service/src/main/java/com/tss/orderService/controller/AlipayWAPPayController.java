package com.tss.orderService.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.tss.orderService.config.DateConverterConfig;
import com.tss.orderService.entity.AlipayProperties;
import com.tss.orderService.entity.Orders;
import com.tss.orderService.entity.Pay;
import com.tss.orderService.mapper.OrderMapper;
import com.tss.orderService.service.PayService;
import com.tss.orderService.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/2 12:55
 * @description：支付宝-手机网站支付控制层
 */
@Slf4j
@Controller
@RequestMapping("/alipay/wap")
public class AlipayWAPPayController {

    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private Pay pay;

    @Autowired
    private DateConverterConfig dateConverterConfig;

    @Autowired
    private PayService payService;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping("/toPayPage")
    public String toPayPage(HttpServletRequest request,HttpServletResponse response){
        String userId = request.getParameter("userId");
        String cost = request.getParameter("cost");
        String subject = request.getParameter("subject");
        request.getSession().setAttribute("userId",userId);
        request.getSession().setAttribute("cost",cost);
        request.getSession().setAttribute("subject",subject);
        return "toPay";
    }
    /**
     * 去支付
     *
     * 支付宝返回一个form表单，并自动提交，跳转到支付宝页面
     *
     * @param response
     * @throws Exception
     */
    @GetMapping("/alipage")
    public void gotoPayPage(HttpServletRequest request,HttpServletResponse response) throws Exception {
        // 订单模型
        String productCode="QUICK_WAP_WAY";
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        String outTradeNo = UUID.randomUUID().toString();
        model.setOutTradeNo(outTradeNo);
        redisUtil.set(outTradeNo,request.getParameter("orderId"));
        model.setSubject(request.getParameter("subject"));
        model.setTotalAmount(request.getParameter("cost"));
        model.setBody("支付测试，共0.01元");
        model.setTimeoutExpress("2m");
        model.setProductCode(productCode);

        AlipayTradeWapPayRequest wapPayRequest =new AlipayTradeWapPayRequest();
        wapPayRequest.setReturnUrl("http://127.0.0.1:8082/alipay/wap/returnUrl");
        wapPayRequest.setNotifyUrl(alipayProperties.getNotifyUrl());
        wapPayRequest.setBizModel(model);

        // 调用SDK生成表单, 并直接将完整的表单html输出到页面
        String form = alipayClient.pageExecute(wapPayRequest).getBody();
        System.out.println(form);
        response.setContentType("text/html;charset=" + alipayProperties.getCharset());
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
    }


    /**
     * 支付宝页面跳转同步通知页面
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws AlipayApiException
     */
    @RequestMapping("/returnUrl")
    public String returnUrl(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, AlipayApiException,Exception {
        response.setContentType("text/html;charset=" + alipayProperties.getCharset());

        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            System.out.println(valueStr);
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean verifyResult = AlipaySignature.rsaCheckV1(params, alipayProperties.getAlipayPublicKey(), alipayProperties.getCharset(), "RSA2");
        if(verifyResult){
            //验证成功
            //请在这里加上商户的业务逻辑程序代码，如保存支付宝交易号
            pay.setOutTradeNo(request.getParameter("out_trade_no"));
            pay.setTotal(Float.parseFloat(request.getParameter("total_amount")));
            pay.setPayTime(dateConverterConfig.convert(request.getParameter("timestamp")));
            pay.setWay("支付宝");
            payService.insert(pay);
            Orders orders = new Orders();
            orders.setOrderId(redisUtil.get(request.getParameter("out_trade_no")));
            orders.setPayId(pay.getPayId());
            orderMapper.updateById(orders);
            webSocket.sendOneMessage("888", JSONObject.toJSONString(orders));

            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            return "wapPaySuccess";

        }else{
            return "wapPayFail";

        }
    }

    /**
     * 退款
     * @param orderNo 商户订单号
     * @return
     */
    @PostMapping("/refund")
    @ResponseBody
    public String refund(String orderNo) throws AlipayApiException {
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();

        AlipayTradeRefundModel model=new AlipayTradeRefundModel();
        // 商户订单号
        model.setOutTradeNo(orderNo);
        // 退款金额
        model.setRefundAmount("0.01");
        // 退款原因
        model.setRefundReason("无理由退货");
        // 退款订单号(同一个订单可以分多次部分退款，当分多次时必传)
//        model.setOutRequestNo(UUID.randomUUID().toString());
        alipayRequest.setBizModel(model);

        AlipayTradeRefundResponse alipayResponse = alipayClient.execute(alipayRequest);
        System.out.println(alipayResponse.getBody());

        return alipayResponse.getBody();
    }

    /**
     * 退款查询
     * @param orderNo 商户订单号
     * @param refundOrderNo 请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部订单号
     * @return
     * @throws AlipayApiException
     */
    @GetMapping("/refundQuery")
    @ResponseBody
    public String refundQuery(String orderNo, String refundOrderNo) throws AlipayApiException {
        AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();

        AlipayTradeFastpayRefundQueryModel model=new AlipayTradeFastpayRefundQueryModel();
        model.setOutTradeNo(orderNo);
        model.setOutRequestNo(refundOrderNo);
        alipayRequest.setBizModel(model);

        AlipayTradeFastpayRefundQueryResponse alipayResponse = alipayClient.execute(alipayRequest);
        System.out.println(alipayResponse.getBody());

        return alipayResponse.getBody();
    }

    /**
     * 关闭交易
     * @param orderNo
     * @return
     * @throws AlipayApiException
     */
    @PostMapping("/close")
    @ResponseBody
    public String close(String orderNo) throws AlipayApiException {
        AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
        AlipayTradeCloseModel model =new AlipayTradeCloseModel();
        model.setOutTradeNo(orderNo);
        alipayRequest.setBizModel(model);

        AlipayTradeCloseResponse alipayResponse= alipayClient.execute(alipayRequest);
        System.out.println(alipayResponse.getBody());

        return alipayResponse.getBody();
    }
}


