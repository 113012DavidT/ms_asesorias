package com.uteq.auth.service.impl;

import com.uteq.auth.client.UsuarioClient;
import com.uteq.auth.dto.AuthResponse;
import com.uteq.auth.dto.LoginRequest;
import com.uteq.auth.dto.TokenValidationResponse;
import com.uteq.auth.dto.UsuarioDTO;
import com.uteq.auth.service.AuthService;
import com.uteq.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UsuarioClient usuarioClient;

    @Override
    public AuthResponse autenticar(LoginRequest request) {
        try {
            log.info("Autenticando usuario: {}", request.getCorreoMatricula());
            
            // Llamar a ms-admin para validar credenciales y obtener datos del usuario
            UsuarioDTO usuario = usuarioClient.login(request.getCorreoMatricula(), request.getPassword());
            
            if (usuario == null) {
                throw new RuntimeException("Credenciales inválidas");
            }

            // Generar tokens con el rol real del usuario
            String token = jwtService.generarToken(usuario.getId(), usuario.getCorreoMatricula(), usuario.getRolNombre());
            String refreshToken = jwtService.generarRefreshToken(usuario.getId());

            log.info("Token generado exitosamente para: {} con rol: {}", usuario.getCorreoMatricula(), usuario.getRolNombre());

                return new AuthResponse(
                    token,
                    refreshToken,
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getRolNombre(),
                    usuario.getCorreoMatricula()
                );
        } catch (Exception e) {
            log.error("Error en autenticación: {}", e.getMessage());
            throw new RuntimeException("Error en autenticación: " + e.getMessage());
        }
    }

    @Override
    public TokenValidationResponse validarToken(String token) {
        try {
            if (jwtService.validarToken(token)) {
                Claims claims = jwtService.obtenerClaimsDelToken(token);
                Long usuarioId = Long.parseLong(claims.getSubject());
                String correoMatricula = (String) claims.get("correoMatricula");
                String rolNombre = (String) claims.get("rol");

                return TokenValidationResponse.builder()
                        .valid(true)
                        .usuarioId(usuarioId)
                        .correoMatricula(correoMatricula)
                        .rolNombre(rolNombre)
                        .mensaje("Token válido")
                        .build();
            } else {
                return TokenValidationResponse.builder()
                        .valid(false)
                        .mensaje("Token inválido o expirado")
                        .build();
            }
        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
            return TokenValidationResponse.builder()
                    .valid(false)
                    .mensaje("Error validando token: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public AuthResponse refrescarToken(String refreshToken) {
        try {
            if (!jwtService.validarToken(refreshToken)) {
                throw new RuntimeException("Refresh token inválido");
            }

            String usuarioId = jwtService.obtenerIdDelToken(refreshToken);
            String correoMatricula = jwtService.obtenerCorreoDelToken(refreshToken);
            String rolNombre = jwtService.obtenerRolDelToken(refreshToken);

            String nuevoToken = jwtService.generarToken(Long.parseLong(usuarioId), correoMatricula, rolNombre);
            String nuevoRefreshToken = jwtService.generarRefreshToken(Long.parseLong(usuarioId));

                return AuthResponse.builder()
                    .token(nuevoToken)
                    .refreshToken(nuevoRefreshToken)
                    .type("Bearer")
                    .usuarioId(Long.parseLong(usuarioId))
                    .nombre(correoMatricula)
                    .apellido(null)
                    .rolNombre(rolNombre)
                    .correoMatricula(correoMatricula)
                    .expiresIn(86400L)
                    .build();
        } catch (Exception e) {
            log.error("Error refrescando token: {}", e.getMessage());
            throw new RuntimeException("Error refrescando token: " + e.getMessage());
        }
    }
}
