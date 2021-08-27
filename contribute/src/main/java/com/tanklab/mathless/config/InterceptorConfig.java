package com.tanklab.mathless.config;

import com.tanklab.mathless.interceptor.DeployInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public DeployInterceptor deployInterceptor() {
        return new DeployInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(deployInterceptor()).addPathPatterns("/api/contribute/deploy");
    }
}
