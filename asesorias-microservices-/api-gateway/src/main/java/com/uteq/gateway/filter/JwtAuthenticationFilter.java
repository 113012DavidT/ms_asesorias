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
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import reactor.util.retry.Retry;

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
                // log reducido para evitar ruido en consola
                // log.debug("[Gateway] Ruta pública: {}", requestPath);
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            
            if (authHeader == null || authHeader.isEmpty()) {
                // Evitar logs ruidosos en producción
                // log.warn("Token no proporcionado para ruta: {}", requestPath);
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
                .onStatus(
                        status -> status.isError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> {
                                    String msg = String.format("ms-auth /validate status=%s body=%s", clientResponse.statusCode(), body);
                                    return Mono.error(new RuntimeException(msg));
                                })
                )
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(3))
                .retryWhen(Retry.max(2).filter(this::esErrorTransitorio))
                .flatMap(response -> {
                    if (response != null && (Boolean) response.getOrDefault("valid", false)) {
                        String uid = String.valueOf(response.get("usuarioId"));
                        String email = String.valueOf(response.get("correoMatricula"));
                        String role = String.valueOf(response.get("rolNombre"));
                        if (uid == null) uid = "";
                        if (email == null) email = "";
                        if (role == null) role = ""; else role = role.trim();
                        if (role.isEmpty()) {
                            log.warn("[Gateway] Token válido pero sin rol para path={} (posible refresh token)", exchange.getRequest().getURI().getPath());
                            return unauthorized(exchange, "Token inválido (sin rol)");
                        }
                        // Reducir logs a debug para trazas puntuales
                        // log.debug("[Gateway] JWT OK → path={}, uid={}, role={}", exchange.getRequest().getURI().getPath(), uid, role);
                        org.springframework.http.server.reactive.ServerHttpRequest mutatedRequest =
                                exchange.getRequest().mutate()
                                        .header("X-User-Id", uid)
                                        .header("X-User-Email", email)
                                        .header("X-User-Role", role)
                                        .build();
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    } else {
                        // log.warn("[Gateway] JWT inválido para path={} -> response={}", exchange.getRequest().getURI().getPath(), response);
                        return unauthorized(exchange, "Token inválido o expirado");
                    }
                })
                .onErrorResume(e -> {
                    // log.error("[Gateway] Error validando token para path={}: {}", exchange.getRequest().getURI().getPath(), e.getMessage());
                    return unauthorized(exchange, "Error validando token");
                });
    }

    private boolean esErrorTransitorio(Throwable e) {
        if (e instanceof java.net.ConnectException) return true;
        if (e instanceof java.util.concurrent.TimeoutException) return true;
        if (e instanceof WebClientResponseException we) {
            return we.getStatusCode().is5xxServerError();
        }
        return false;
    }

    private boolean esRutaPublica(String path) {
        return path.startsWith("/api/auth/")
            || path.startsWith("/api/admin/usuarios/login")
                || path.startsWith("/api/admin/usuarios/bootstrap")
                || path.startsWith("/eureka")
            || path.startsWith("/actuator")
            ;
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
