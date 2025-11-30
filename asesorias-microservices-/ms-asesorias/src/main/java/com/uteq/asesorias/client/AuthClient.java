package com.uteq.asesorias.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-auth", url = "http://localhost:8088")
public interface AuthClient {
    
    @PostMapping("/api/auth/validate")
    TokenValidationResponse validarToken(@RequestBody TokenValidationRequest request);

    class TokenValidationRequest {
        public String token;
        
        public TokenValidationRequest(String token) {
            this.token = token;
        }
    }

    class TokenValidationResponse {
        public boolean valid;
        public Long usuarioId;
        public String email;
        public String rol;
    }
}
