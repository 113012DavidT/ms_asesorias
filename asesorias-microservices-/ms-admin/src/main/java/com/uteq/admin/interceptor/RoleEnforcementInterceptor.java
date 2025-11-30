package com.uteq.admin.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor simple para forzar que solo el rol ADMIN pueda acceder a ms-admin.
 * Si el gateway no propagó el header X-User-Role o el rol es distinto, responde 403.
 */
@Component
public class RoleEnforcementInterceptor implements HandlerInterceptor {

    private boolean esPublica(String path) {
        // Endpoints que deben ser accesibles sin autenticación previa
        return path.startsWith("/api/auth/") // cualquier auth público que se enrute por error
                || path.startsWith("/api/admin/usuarios") // permitir bootstrap de usuarios
                || path.startsWith("/actuator")
                || path.startsWith("/eureka");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if (esPublica(path)) {
            return true; // saltar validación de rol
        }
        String role = request.getHeader("X-User-Role");
        if (role == null || role.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Falta header X-User-Role");
            return false;
        }
        if (!"ADMIN".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso restringido a rol ADMIN");
            return false;
        }
        return true;
    }
}
