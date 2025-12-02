package com.uteq.alumnos.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignHeadersConfig {

    @Bean
    public RequestInterceptor forwardingHeadersInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
                if (attrs instanceof ServletRequestAttributes) {
                    HttpServletRequest req = ((ServletRequestAttributes) attrs).getRequest();
                    copyHeader(req, template, "Authorization");
                    copyHeader(req, template, "X-User-Id");
                    copyHeader(req, template, "X-User-Email");
                    copyHeader(req, template, "X-User-Role");
                }
            }

            private void copyHeader(HttpServletRequest req, RequestTemplate template, String name) {
                String value = req.getHeader(name);
                if (value != null && !value.isBlank()) {
                    template.header(name, value);
                }
            }
        };
    }
}
