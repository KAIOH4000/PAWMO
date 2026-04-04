package com.example.springboot.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.IUserService;
import com.example.springboot.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * JWT认证拦截器
 * 作用：拦截请求并验证JWT令牌的有效性，实现基于令牌的身份认证
 * 仅允许携带有效令牌的请求访问受保护接口，或标记了@AuthAccess注解的接口
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;

    /**
     * 请求处理前执行的拦截方法
     * 验证请求中的JWT令牌，通过则放行，否则抛出认证异常
     *
     * @param request  HTTP请求对象，用于获取请求头或参数中的token
     * @param response HTTP响应对象
     * @param handler  拦截到的处理器（方法或资源）
     * @return boolean 认证通过返回true（放行），否则返回false（拦截）
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 1. 从请求头或 URL 参数中获取 token
        String token = request.getHeader("token");  // 优先从请求头获取 token
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");  // 若请求头无 token，则从 URL 参数获取
        }

        // 2. 处理无需认证的接口（标记了@AuthAccess注解的方法）
        // 判断当前处理器是否为控制器方法
        if (handler instanceof HandlerMethod) {
            // 获取方法上的@AuthAccess注解
            AuthAccess annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
            if (annotation != null) {
                // 存在该注解，说明接口无需认证，直接放行
                return true;
            }
        }

        // 3. 验证token是否存在
        if (StrUtil.isBlank(token)) {
            throw new ServiceException("401", "token验证失败，请重新登录");
        }

        // 4. 验证 token 的有效性（使用全局密钥）
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(TokenUtils.SIGN_KEY)).build();
            jwtVerifier.verify(token); // 验证 token
        } catch (JWTVerificationException e) {
            throw new ServiceException("401", "token验证失败，请重新登录");
        }

        // 7. 所有验证通过，放行请求
    return true;
}

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        System.out.println("=== JWT 拦截器 afterCompletion ===");
        System.out.println("请求 URL: " + request.getRequestURI());
        System.out.println("响应状态码：" + response.getStatus());
        if (ex != null) {
            System.out.println("发生异常：" + ex.getMessage());
            ex.printStackTrace();
        }
    }
}