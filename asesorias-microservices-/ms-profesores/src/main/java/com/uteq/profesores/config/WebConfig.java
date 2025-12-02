package com.uteq.profesores.config;

import com.uteq.profesores.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.allowed.roles.profesores:ADMIN,PROFESOR,COORDINADOR}")
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
            // Permitir listado público controlado para diagnóstico/UI inicial
            if ("GET".equalsIgnoreCase(request.getMethod()) && "/api/profesores".equals(path)) {
                return true;
            }
            // Reglas específicas para /api/grupos: restringir a PROFESOR y ADMIN
            if (path.startsWith("/api/grupos")) {
                String method = request.getMethod();
                String roleForGrupos = request.getHeader("X-User-Role");
                if (roleForGrupos != null) roleForGrupos = roleForGrupos.trim().toUpperCase();
                // Fallback: permitir GET cuando falta rol pero sí llega X-User-Id (gateway a veces no inyecta rol)
                if ((roleForGrupos == null || roleForGrupos.isBlank())) {
                    String uid = request.getHeader("X-User-Id");
                    if ("GET".equalsIgnoreCase(method) && uid != null && !uid.isBlank()) {
                        log.warn("[ms-profesores] GET /api/grupos permitido por fallback: falta X-User-Role pero X-User-Id={} presente", uid);
                        return true;
                    }
                    response.sendError(401, "Falta X-User-Role");
                    return false;
                }
                java.util.Set<String> allowedGrupos = java.util.Set.of("PROFESOR", "ADMIN");
                if (!allowedGrupos.contains(roleForGrupos)) { response.sendError(403, "Rol no autorizado para grupos"); return false; }
                return true;
            }
            String role = request.getHeader("X-User-Role");
            if (role != null) { role = role.trim(); }
            // Reducir logs para evitar ruido en producción
            // log.debug("[ms-profesores] path={}, X-User-Role={}, allowed={}", path, role, allowed);
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
