package com.example.springboot.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addInterceptors(@NonNull InterceptorRegistry registry) {
        // 配置jwt的拦截器规则
        registry.addInterceptor(jwtInterceptor())
                // 拦截所有的请求路径
                .addPathPatterns("/**")
                // 放行以下接口
                .excludePathPatterns("/login", "/register", "/password", "/type/**", "/goods/**");
        super.addInterceptors(registry);
    }

    @Bean
    public @NonNull JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

}
