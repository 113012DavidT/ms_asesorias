package com.uteq.auth.service;

import com.uteq.auth.dto.LoginRequest;
import com.uteq.auth.dto.AuthResponse;
import com.uteq.auth.dto.TokenValidationResponse;

public interface AuthService {
    AuthResponse autenticar(LoginRequest request);
    TokenValidationResponse validarToken(String token);
    AuthResponse refrescarToken(String refreshToken);
}
