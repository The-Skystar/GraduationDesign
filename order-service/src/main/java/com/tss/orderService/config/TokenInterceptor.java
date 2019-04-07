package com.tss.orderService.config;

import com.tss.orderService.annotations.LoginRequired;
import com.tss.orderService.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/4/7 14:12
 * @description：登录验证token拦截器
 */
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        System.out.println("进入token验证拦截器");
        if (!(handler instanceof HandlerMethod))
            return true;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
        if (loginRequired == null)
            return true;
        String token = request.getHeader("token");
        String currentUserId = request.getHeader("userId");
        if (currentUserId==null || token==null){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            return false;
        }
        String redisUserId = redisUtil.get(token);
        System.out.println(redisUserId);
        String redisToken = redisUtil.get(currentUserId);
        if (currentUserId.equals(redisUserId)){
            if (token.equals(redisToken)){
                return true;
            }
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }
}
