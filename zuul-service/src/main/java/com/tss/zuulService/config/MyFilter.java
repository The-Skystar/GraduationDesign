package com.tss.zuulService.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.tss.zuulService.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/23 14:06
 * @description：
 */
public class MyFilter extends ZuulFilter {
    private static Logger logger = LoggerFactory.getLogger(MyFilter.class);

    List<String> urlList = new ArrayList<>();

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run(){
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        logger.info("send {} request to {}",request.getMethod(),request.getRequestURL().toString());
        urlList.add("http://10.156.85.170:8088/tss/pwdLogin");
        urlList.add("http://10.156.85.170:8088/tss/exit");
        urlList.add("http://10.156.85.170:8088/tss/sendEmail");
        urlList.add("http://10.156.85.170:8088/tss/reg");
        urlList.add("http://10.156.85.170:8088/tss/validate");
        urlList.add("http://10.156.85.170:8088/tss/autoLogin");
        urlList.add("http://10.156.85.170:8088/tss/emailLogin");
        urlList.add("http://10.156.85.170:8088/tss/geocoder");
        urlList.add("http://10.156.85.170:8088/tss/query");
        urlList.add("http://10.156.85.170:8088/tss/estimate");
        urlList.add("http://10.156.85.170:8088/tss/com");
        urlList.add("http://10.156.85.170:8088/tss/postcode");
        if (urlList.contains(request.getRequestURL().toString())){
            logger.info("不需要验证，放行");
            return true;
        }
        logger.info("进入token验证拦截器");
        String token = request.getHeader("token");
        String currentUserId = request.getHeader("userId");
        if (currentUserId==null || token==null){
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.setSendZuulResponse(false);
            ctx.setResponseBody(MediaType.APPLICATION_JSON_VALUE);
            logger.info("客户号或者token未传递");
            return false;
        }
        String redisUserId = null;
        String redisToken = null;
        try {
            redisUserId = redisUtil.get(token);
            redisToken = redisUtil.get(currentUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (currentUserId.equals(redisUserId)){
            if (token.equals(redisToken)){
                logger.info("token验证通过，放行");
                return true;
            }
        }
        ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        ctx.setSendZuulResponse(false);
        logger.info("token验证未通过");
        return false;
    }
}
