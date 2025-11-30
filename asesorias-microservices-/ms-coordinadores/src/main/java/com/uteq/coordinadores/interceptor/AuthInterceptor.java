package com.uteq.coordinadores.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String userId = request.getHeader("X-User-Id");
        String userEmail = request.getHeader("X-User-Email");
        String userRole = request.getHeader("X-User-Role");
        
        if (userId != null) {
            request.setAttribute("userId", userId);
            request.setAttribute("userEmail", userEmail);
            request.setAttribute("userRole", userRole);
        }
        
        return true;
    }
}
