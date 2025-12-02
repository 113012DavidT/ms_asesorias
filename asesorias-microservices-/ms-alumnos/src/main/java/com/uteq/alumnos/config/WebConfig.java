package com.uteq.alumnos.config;

import com.uteq.alumnos.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.allowed.roles.alumnos:ADMIN,ALUMNO,COORDINADOR}")
    private String allowedRolesCsv;

    private static class RoleInterceptor implements org.springframework.web.servlet.HandlerInterceptor {
        private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RoleInterceptor.class);
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
            String method = request.getMethod();
            log.info("[ms-alumnos] path={}, method={}, X-User-Role={}, allowed={}", path, method, role, allowed);
            if (role == null || role.isBlank()) {
                // Fallback: permitir GET cuando llega X-User-Id aunque falte el rol (para evitar romper UI)
                String uid = request.getHeader("X-User-Id");
                if ("GET".equalsIgnoreCase(method) && uid != null && !uid.isBlank()) {
                    log.warn("[ms-alumnos] {} permitido por fallback: falta X-User-Role pero X-User-Id={} presente (path={})", method, uid, path);
                    return true;
                }
                response.sendError(401, "Falta X-User-Role");
                return false;
            }
            String roleUp = role.toUpperCase();
            // Regla específica: permitir a PROFESOR hacer GET del listado básico de alumnos
            if ("GET".equalsIgnoreCase(method) && "/api/alumnos".equals(path)) {
                if ("PROFESOR".equals(roleUp) || allowed.contains(roleUp)) {
                    log.info("[ms-alumnos] GET {} permitido para rol {} (regla específica listado)", path, roleUp);
                    return true;
                } else {
                    response.sendError(403, "Rol no autorizado para listado de alumnos");
                    return false;
                }
            }
            if (!allowed.contains(roleUp)) {response.sendError(403, "Rol no autorizado"); return false;}
            return true;
        }
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor());
        registry.addInterceptor(new RoleInterceptor(allowedRolesCsv));
    }
}
