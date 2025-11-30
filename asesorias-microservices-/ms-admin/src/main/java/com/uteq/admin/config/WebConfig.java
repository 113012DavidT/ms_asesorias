package com.uteq.admin.config;

import com.uteq.admin.interceptor.RoleEnforcementInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RoleEnforcementInterceptor roleEnforcementInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleEnforcementInterceptor)
            .addPathPatterns("/api/admin/**")
            .excludePathPatterns(
                "/api/admin/usuarios/login",
                "/api/admin/usuarios/bootstrap",
                "/actuator/**"
            );
    }
}
