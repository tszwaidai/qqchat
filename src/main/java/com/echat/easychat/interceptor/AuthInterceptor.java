package com.echat.easychat.interceptor;

import com.echat.easychat.utils.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.echat.easychat.utils.UserConstants.SECRET_KEY;

/**
 * @author tszwaidai
 * @version 1.0
 * @description: 创建拦截器来解析 JWT token
 * @date 2024/11/9 17:49
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String userId = claims.getSubject();
            String nickName = (String) claims.get("nickName");
            UserContext.setUserId(userId);
            UserContext.setNickName(nickName);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear(); // 清理 ThreadLocal
    }
}
