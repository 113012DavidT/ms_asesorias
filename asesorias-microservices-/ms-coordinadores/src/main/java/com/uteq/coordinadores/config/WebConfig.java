package com.uteq.coordinadores.config;

import com.uteq.coordinadores.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.allowed.roles.coordinadores:ADMIN,COORDINADOR}")
    private String allowedRolesCsv;

    private static class RoleInterceptor implements org.springframework.web.servlet.HandlerInterceptor {
        private final java.util.Set<String> allowed;
        RoleInterceptor(String csv) {
            allowed = java.util.Arrays.stream(csv.split(","))
                    .map(String::trim).filter(s -> !s.isEmpty())
                    .map(String::toUpperCase).collect(java.util.stream.Collectors.toSet());
        }
        private boolean esPublica(String path){
            return path.contains("/health") || path.contains("/actuator") || path.contains("/eureka");
        }
        @Override
        public boolean preHandle(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, Object handler) throws Exception {
            String path = request.getRequestURI();
            if (esPublica(path)) return true;
            String role = request.getHeader("X-User-Role");
            if (role == null || role.isBlank()) {response.sendError(401, "Falta X-User-Role"); return false;}
            if (!allowed.contains(role.toUpperCase())) {response.sendError(403, "Rol no autorizado"); return false;}
            return true;
        }
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor());
        registry.addInterceptor(new RoleInterceptor(allowedRolesCsv));
    }
}
