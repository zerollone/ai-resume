package com.ai.resume.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ws
 * @date 2026/4/18 18:15
 */
@Configuration
public class SaTokenConfig {

    // 配置拦截器
    @Configuration
    public class SaTokenConfigure implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            // 注册Sa-Token拦截器，定义需要拦截的路径
            registry.addInterceptor(new SaInterceptor())
                    .addPathPatterns("/**")
                    .excludePathPatterns("/user/login", "/user/register",
                            "/doc.html",
                            "/swagger-ui/**",
                            "/swagger-resources/**",
                            "/webjars/**",
                            "/v3/api-docs/**",
                            "/favicon.ico");
        }
    }
}
