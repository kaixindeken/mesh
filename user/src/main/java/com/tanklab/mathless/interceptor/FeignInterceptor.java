package com.tanklab.mathless.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class FeignInterceptor implements RequestInterceptor {

    @Resource
    private HttpServletRequest request;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = request.getHeader("Authentication-Token");
        requestTemplate.header("Authentication-Token", token);
    }
}
