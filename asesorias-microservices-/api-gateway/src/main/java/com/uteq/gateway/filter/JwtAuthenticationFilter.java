package com.uteq.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public JwtAuthenticationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Ignorar preflight CORS (OPTIONS) para que pase antes de validar JWT
            if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                return chain.filter(exchange);
            }
            String requestPath = exchange.getRequest().getURI().getPath();
            
            // Rutas públicas que no requieren autenticación
            if (esRutaPublica(requestPath)) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            
            if (authHeader == null || authHeader.isEmpty()) {
                log.warn("Token no proporcionado para ruta: {}", requestPath);
                return unauthorized(exchange, "Token no proporcionado");
            }

            String token = authHeader.replace("Bearer ", "");
            
            return validarTokenConMsAuth(token, exchange, chain);
        };
    }

    private Mono<Void> validarTokenConMsAuth(String token, ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        return webClientBuilder.build()
                .post()
                .uri("http://ms-auth:8088/api/auth/validate")
                .bodyValue(Map.of("token", token))
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    if (response != null && (Boolean) response.getOrDefault("valid", false)) {
                        exchange.getRequest().mutate()
                                .header("X-User-Id", String.valueOf(response.get("usuarioId")))
                                .header("X-User-Email", String.valueOf(response.get("correoMatricula")))
                                .header("X-User-Role", String.valueOf(response.get("rolNombre")))
                                .build();
                        return chain.filter(exchange);
                    } else {
                        return unauthorized(exchange, "Token inválido o expirado");
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error validando token: {}", e.getMessage());
                    return unauthorized(exchange, "Error validando token");
                });
    }

    private boolean esRutaPublica(String path) {
        return path.startsWith("/api/auth/")
                || path.startsWith("/api/admin/usuarios/login")
                || path.startsWith("/api/admin/usuarios/bootstrap")
                || path.startsWith("/eureka")
                || path.startsWith("/actuator");
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String mensaje) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory()
                        .wrap(("{\"error\": \"" + mensaje + "\"}").getBytes()))
        );
    }

    public static class Config {
    }
}
