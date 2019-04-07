package com.tss.orderService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/7 16:57
 * @description：
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 拦截器的执行在spring容器的bean初始化之前，需要将拦截器bean化，否则@Autowired注入bean将是null
    @Bean
    public TokenInterceptor tokenInterceptor(){
        return new TokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // 注入拦截器时，不再是new一个拦截器对象，直接使用tokenInterceptor方法
        //返回的拦截器对象
        registry.addInterceptor(tokenInterceptor()).addPathPatterns("/**");
    }

}

