package com.uteq.auth.controller;

import com.uteq.auth.dto.AuthResponse;
import com.uteq.auth.dto.LoginRequest;
import com.uteq.auth.dto.TokenValidationRequest;
import com.uteq.auth.dto.TokenValidationResponse;
import com.uteq.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.autenticar(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validarToken(@RequestBody TokenValidationRequest request) {
        TokenValidationResponse response = authService.validarToken(request.getToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refrescarToken(@RequestHeader("Authorization") String refreshToken) {
        String token = refreshToken.replace("Bearer ", "");
        AuthResponse response = authService.refrescarToken(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("MS-Auth está funcionando correctamente");
    }
}
