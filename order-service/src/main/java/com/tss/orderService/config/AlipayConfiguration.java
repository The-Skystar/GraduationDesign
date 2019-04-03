package com.tss.orderService.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.tss.orderService.entity.AlipayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/2 12:53
 * @description：支付宝支付配置类
 */
@Configuration
@EnableConfigurationProperties(AlipayProperties.class)
public class AlipayConfiguration {

    @Autowired
    private AlipayProperties properties;

    @Bean
    public AlipayTradeService alipayTradeService() {
        return new AlipayTradeServiceImpl.ClientBuilder()
                .setGatewayUrl(properties.getGatewayUrl())
                .setAppid(properties.getAppid())
                .setPrivateKey(properties.getAppPrivateKey())
                .setAlipayPublicKey(properties.getAlipayPublicKey())
                .setSignType(properties.getSignType())
                .build();
    }

    @Bean
    public AlipayClient alipayClient(){
        return new DefaultAlipayClient(properties.getGatewayUrl(),
                properties.getAppid(),
                properties.getAppPrivateKey(),
                properties.getFormate(),
                properties.getCharset(),
                properties.getAlipayPublicKey(),
                properties.getSignType());
    }
}


