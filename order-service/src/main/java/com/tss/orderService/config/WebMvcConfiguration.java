package com.tss.orderService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/2 13:08
 * @description：路由配置类，添加路径和页面的映射关系
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/toPay").setViewName("toPay");
        super.addViewControllers(registry);
    }
}
