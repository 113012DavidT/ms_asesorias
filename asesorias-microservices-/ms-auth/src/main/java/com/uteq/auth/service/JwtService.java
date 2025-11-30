package com.uteq.auth.service;

import io.jsonwebtoken.Claims;

public interface JwtService {
    String generarToken(Long usuarioId, String correoMatricula, String rolNombre);
    String generarRefreshToken(Long usuarioId);
    String obtenerIdDelToken(String token);
    String obtenerCorreoDelToken(String token);
    String obtenerRolDelToken(String token);
    Claims obtenerClaimsDelToken(String token);
    Boolean validarToken(String token);
}
