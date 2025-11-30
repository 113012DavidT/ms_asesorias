package com.uteq.gateway.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PreflightCorsFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            String origin = exchange.getRequest().getHeaders().getFirst("Origin");
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            if (origin != null && isAllowedOrigin(origin)) {
                headers.add("Access-Control-Allow-Origin", origin);
            }
            headers.add("Vary", "Origin");
            headers.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            headers.add("Access-Control-Allow-Headers", "*");
            headers.add("Access-Control-Expose-Headers", "Authorization,Content-Type");
            headers.add("Access-Control-Allow-Credentials", "true");
            response.setStatusCode(HttpStatus.OK);
            return response.setComplete();
        }
        return chain.filter(exchange);
    }

    private boolean isAllowedOrigin(String origin) {
        return origin.startsWith("http://localhost:5173")
            || origin.startsWith("http://localhost:5174")
            || origin.startsWith("http://localhost:5175")
            || origin.startsWith("http://127.0.0.1:5173")
            || origin.startsWith("http://127.0.0.1:5174")
            || origin.startsWith("http://127.0.0.1:5175");
    }
}
