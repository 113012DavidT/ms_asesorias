package com.uteq.asesorias.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor para capturar headers de autenticación propagados por el API Gateway
 */
public class AuthInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String userId = request.getHeader("X-User-Id");
        String userEmail = request.getHeader("X-User-Email");
        String userRole = request.getHeader("X-User-Role");
        
        // Los headers están disponibles en el request para ser usados en los services
        if (userId != null) {
            request.setAttribute("userId", userId);
            request.setAttribute("userEmail", userEmail);
            request.setAttribute("userRole", userRole);
        }
        
        return true;
    }
}
