package com.echat.easychat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: TODO
 * @date 2024/10/28 19:15
 */
@Configuration
public class WebCorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许请求所有路径
                .allowedOriginPatterns("*") // 使用 allowedOriginPatterns 代替 allowedOrigins
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
