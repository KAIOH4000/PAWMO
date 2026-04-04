package com.example.springboot.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置jwt的拦截器规则
        registry.addInterceptor(jwtInterceptor)
                // 拦截所有的请求路径
                .addPathPatterns("/**")
                // 放行以下接口
                .excludePathPatterns("/login", "/register", "/password", "/type/**", "/goods/**", "/file/**");
    }

}